package az.code.telegram_bot_api.services;

import az.code.telegram_bot_api.exceptions.InvalidPasswordException;
import az.code.telegram_bot_api.exceptions.TokenInvalidException;
import az.code.telegram_bot_api.exceptions.UserNotFoundException;
import az.code.telegram_bot_api.models.*;
import az.code.telegram_bot_api.models.DTOs.LoginDTO;
import az.code.telegram_bot_api.models.DTOs.ResetPasswordDTO;
import az.code.telegram_bot_api.models.DTOs.UserTokenDTO;
import az.code.telegram_bot_api.models.enums.TokenType;
import az.code.telegram_bot_api.repositories.UserRepository;
import az.code.telegram_bot_api.repositories.VerificationRepo;
import az.code.telegram_bot_api.services.interfaces.VerificationService;
import az.code.telegram_bot_api.utils.KeycloakUtil;
import az.code.telegram_bot_api.utils.MessageUtil;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.authorization.client.AuthzClient;
import org.keycloak.authorization.client.Configuration;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class VerificationServiceImpl implements VerificationService {

    final
    VerificationRepo verificationRepo;
    final
    MessageUtil messageUtil;
    final
    UserRepository userRepo;
    final
    KeycloakUtil keycloakUtil;

    public VerificationServiceImpl(VerificationRepo verificationRepo, MessageUtil messageUtil, UserRepository userRepo, KeycloakUtil keycloakUtil) {
        this.verificationRepo = verificationRepo;
        this.messageUtil = messageUtil;
        this.userRepo = userRepo;
        this.keycloakUtil = keycloakUtil;
    }

    @Override
    public void sendVerifyToken(User user) {
        clearTokens(user, TokenType.EMAIL_VERIFY);
        String token = verificationRepo
                .save(VerificationToken.builder()
                        .createdDate(LocalDateTime.now())
                        .user(user)
                        .tokenType(TokenType.EMAIL_VERIFY)
                        .token(UUID.randomUUID().toString())
                        .build())
                .getToken();
        messageUtil.regVerifyNotification(user.getEmail(), token);
    }

    @Override
    public HttpStatus passwordForgot(LoginDTO loginDTO) {
        User user = findUserByEmail(loginDTO.getEmail());
        clearTokens(user, TokenType.PASSWORD_RESET);
        String token = verificationRepo
                .save(VerificationToken.builder()
                        .createdDate(LocalDateTime.now())
                        .user(user)
                        .tokenType(TokenType.PASSWORD_RESET)
                        .token(UUID.randomUUID().toString())
                        .build())
                .getToken();
        messageUtil.forgot(user.getEmail(), token);
        return HttpStatus.OK;
    }

    @Override
    public VerificationToken findByToken(String token, TokenType tokenType) {
        Optional<VerificationToken> verifyToken = verificationRepo.findByToken(token, tokenType);
        if (verifyToken.isPresent()) {
            return verifyToken.get();
        } else
            throw new TokenInvalidException();
    }

    @Override
    public HttpStatus resetWithToken(String token, ResetPasswordDTO resetPasswordDTO) {
        VerificationToken dbToken = findByToken(token, TokenType.PASSWORD_RESET);
        keycloakUtil.setPassword(dbToken.getUser().getUsername(), resetPasswordDTO);
        verificationRepo.delete(dbToken);
        return HttpStatus.OK;
    }

    @Override
    public HttpStatus resetWithOldPassword(UserTokenDTO user, ResetPasswordDTO resetPasswordDTO) {
        Configuration configuration = keycloakUtil.getConfiguration();
        AuthzClient authzClient = AuthzClient.create(configuration);
        try{
            authzClient.obtainAccessToken(user.getEmail(), resetPasswordDTO.getOldPassword());
        }
        catch (Exception e){
            throw new InvalidPasswordException();
        }
        keycloakUtil.setPassword(user.getUsername(), resetPasswordDTO);
        return HttpStatus.OK;
    }

    @Override
    public HttpStatus verify(String token) {
        VerificationToken dbToken = findByToken(token, TokenType.EMAIL_VERIFY);
        User user = findUserByEmail(dbToken.getUser().getEmail());
        UsersResource usersResource = keycloakUtil.getKeycloakRealm().users();
        List<UserRepresentation> users = usersResource.search(user.getUsername());
        if (users.size() == 0)
            throw new UserNotFoundException();
        verifyUser(usersResource, users);
        user.setActive(true);
        userRepo.save(user);
        verificationRepo.delete(dbToken);
        return HttpStatus.OK;
    }

    private void clearTokens(User user, TokenType emailVerify) {
        if(user.getVerificationToken().size()!=0){
            user.setVerificationToken(user.getVerificationToken()
                    .stream().filter(t -> t.getTokenType() != emailVerify)
                    .collect(Collectors.toList()));
        }
        userRepo.save(user);
    }

    private User findUserByEmail(String email) {
        Optional<User> user = userRepo.findByEmail(email);
        if (user.isPresent()) {
            return user.get();
        }
        throw new UserNotFoundException();
    }

    private void verifyUser(UsersResource usersResource, List<UserRepresentation> users) {
        UserRepresentation search = users.get(0);
        UserResource userRS = usersResource.get(search.getId());
        search.setEmailVerified(true);
        userRS.update(search);
    }
}
