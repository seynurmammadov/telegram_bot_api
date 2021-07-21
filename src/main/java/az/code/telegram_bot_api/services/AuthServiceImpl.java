package az.code.telegram_bot_api.services;

import az.code.telegram_bot_api.exceptions.UserNotFoundException;
import az.code.telegram_bot_api.exceptions.VerifyEmailException;
import az.code.telegram_bot_api.models.LoginDTO;
import az.code.telegram_bot_api.models.RegistrationDTO;
import az.code.telegram_bot_api.models.User;
import az.code.telegram_bot_api.models.VerificationToken;
import az.code.telegram_bot_api.models.mapper.MapperModel;
import az.code.telegram_bot_api.repositories.UserRepository;
import az.code.telegram_bot_api.services.interfaces.AuthService;
import az.code.telegram_bot_api.services.interfaces.VerificationService;
import az.code.telegram_bot_api.utils.TokenUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.CreatedResponseUtil;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.authorization.client.AuthzClient;
import org.keycloak.authorization.client.Configuration;
import org.keycloak.representations.AccessTokenResponse;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.ws.rs.core.Response;
import java.time.LocalDateTime;
import java.util.*;

@Service
@Slf4j
public class AuthServiceImpl implements AuthService {
    @Value("${app.keycloak.admin.resource}")
    private String adminClientId;
    @Value("${keycloak.resource}")
    private String clientId;
    @Value("${app.keycloak.admin.realm}")
    private String adminRealm;
    @Value("${keycloak.realm}")
    private String realm;
    @Value("${keycloak.auth-server-url}")
    private String authServerUrl;
    @Value("${app.keycloak.role}")
    private String role;
    @Value("${keycloak.credentials.secret}")
    private String clientSecret;
    @Value("${app.keycloak.username}")
    private String username;
    @Value("${app.keycloak.password}")
    private String password;

    final
    MapperModel mapperModel;

    final
    UserRepository userRepo;

    final
    TokenUtil tokenUtil;

    final
    VerificationService verificationService;

    public AuthServiceImpl(MapperModel mapperModel, UserRepository userRepo, TokenUtil tokenUtil, VerificationService verificationService) {
        this.mapperModel = mapperModel;
        this.userRepo = userRepo;
        this.tokenUtil = tokenUtil;
        this.verificationService = verificationService;
    }

    @Override
    public RegistrationDTO registration(RegistrationDTO registrationDTO) {
        RealmResource realmResource = getKeycloakRealm();
        UsersResource usersResource = realmResource.users();
        UserRepresentation userRP =createKeycloakUser(registrationDTO);
        Response response = usersResource.create(userRP);
        registrationDTO.setStatusCode(response.getStatus());
        registrationDTO.setStatus(response.getStatusInfo().toString());
        registrationDTO.setUsername(userRP.getUsername());
        createUser(registrationDTO, realmResource, usersResource, response);
        return registrationDTO;
    }

    private void createUser(RegistrationDTO registrationDTO, RealmResource realmResource, UsersResource usersResource, Response response) {
        if (response.getStatus() == 201) {
            setPasswordAndRole(registrationDTO, realmResource, usersResource, response);
            User user = createUser(registrationDTO);
            verificationService.sendVerifyToken(user);
        }
    }

    private RealmResource getKeycloakRealm() {
        Keycloak keycloak = KeycloakBuilder.builder()
                .serverUrl(authServerUrl)
                .grantType(OAuth2Constants.PASSWORD)
                .realm(adminRealm)
                .clientId(adminClientId)
                .username(username).password(password)
                .resteasyClient(new ResteasyClientBuilder().connectionPoolSize(10).build())
                .build();
        keycloak.tokenManager().getAccessToken();
        return keycloak.realm(realm);
    }

    private UserRepresentation createKeycloakUser(RegistrationDTO userDTO) {
        UserRepresentation user = new UserRepresentation();
        user.setEnabled(true);
        user.setUsername(userDTO.getCompany_name() + "_" + UUID.randomUUID());
        user.setEmail(userDTO.getEmail());
        user.setFirstName(userDTO.getAgent_name());
        user.setLastName(userDTO.getAgent_surname());
        return user;
    }

    private void setPasswordAndRole(RegistrationDTO userDTO,
                                    RealmResource realmResource,
                                    UsersResource usersResource,
                                    Response response) {
        String userId = CreatedResponseUtil.getCreatedId(response);
        log.info("Created userId {}", userId);
        UserResource userResource = setPassword(userDTO, usersResource, userId);
        setRole(realmResource, userResource);
    }

    private void setRole(RealmResource realmResource, UserResource userResource) {
        RoleRepresentation realmRoleUser = realmResource.roles().get(role).toRepresentation();
        userResource.roles().realmLevel().add(Collections.singletonList(realmRoleUser));
    }

    private UserResource setPassword(RegistrationDTO userDTO, UsersResource usersResource, String userId) {
        CredentialRepresentation passwordCred = new CredentialRepresentation();
        passwordCred.setTemporary(false);
        passwordCred.setType(CredentialRepresentation.PASSWORD);
        passwordCred.setValue(userDTO.getPassword());

        UserResource userResource = usersResource.get(userId);
        userResource.resetPassword(passwordCred);
        return userResource;
    }

    private User createUser(RegistrationDTO userDTO) {
        User user = mapperModel.entityToDTO(userDTO, User.class);
        user.setCreated_at(LocalDateTime.now());
        return userRepo.save(userRepo.save(user));
    }

    @Override
    public AccessTokenResponse login(LoginDTO loginDTO) throws JsonProcessingException {
        Map<String, Object> clientCredentials = new HashMap<>();
        clientCredentials.put("secret", clientSecret);
        clientCredentials.put("grant_type", "password");
        Configuration configuration = new Configuration(authServerUrl, realm, clientId, clientCredentials, null);
        AuthzClient authzClient = AuthzClient.create(configuration);
        AccessTokenResponse response = authzClient.obtainAccessToken(loginDTO.getEmail(), loginDTO.getPassword());
        JsonNode user = tokenUtil.getUser(response.getToken());
        if (!user.get("email_verified").asBoolean()) {
            throw new VerifyEmailException();
        }
        return response;
    }

    @Override
    public HttpStatus verify(String token) {
        VerificationToken dbToken = verificationService.findByToken(token);
        User user = userRepo.findUserByUsername(dbToken.getUser().getUsername());
        UsersResource usersResource = getKeycloakRealm().users();
        List<UserRepresentation> users = usersResource.search(user.getUsername());
        if (users.size() == 0)
            throw new UserNotFoundException();
        verifyUser(usersResource, users);
        verificationService.delete(dbToken);
        return HttpStatus.OK;
    }

    private void verifyUser(UsersResource usersResource, List<UserRepresentation> users) {
        UserRepresentation search = users.get(0);
        UserResource userRS = usersResource.get(search.getId());
        search.setEmailVerified(true);
        userRS.update(search);
    }
}
