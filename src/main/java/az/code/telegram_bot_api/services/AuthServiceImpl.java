package az.code.telegram_bot_api.services;

import az.code.telegram_bot_api.exceptions.InvalidUserDataException;
import az.code.telegram_bot_api.exceptions.VerifyEmailException;
import az.code.telegram_bot_api.models.DTOs.LoginDTO;
import az.code.telegram_bot_api.models.DTOs.RegistrationDTO;
import az.code.telegram_bot_api.models.User;
import az.code.telegram_bot_api.models.mapper.MapperModel;
import az.code.telegram_bot_api.repositories.UserRepository;
import az.code.telegram_bot_api.services.interfaces.AuthService;
import az.code.telegram_bot_api.services.interfaces.VerificationService;
import az.code.telegram_bot_api.utils.KeycloakUtil;
import az.code.telegram_bot_api.utils.TokenUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.CreatedResponseUtil;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.authorization.client.AuthzClient;
import org.keycloak.authorization.client.Configuration;
import org.keycloak.representations.AccessTokenResponse;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.ws.rs.core.Response;
import java.time.LocalDateTime;
import java.util.*;

@Service
@Slf4j
public class AuthServiceImpl implements AuthService {

    @Value("${app.keycloak.role}")
    private String role;


    final
    MapperModel mapperModel;
    final
    UserRepository userRepo;
    final
    TokenUtil tokenUtil;
    final
    VerificationService verificationService;
    final
    KeycloakUtil keycloakUtil;

    public AuthServiceImpl(MapperModel mapperModel, UserRepository userRepo, TokenUtil tokenUtil, VerificationService verificationService, KeycloakUtil keycloakUtil) {
        this.mapperModel = mapperModel;
        this.userRepo = userRepo;
        this.tokenUtil = tokenUtil;
        this.verificationService = verificationService;
        this.keycloakUtil = keycloakUtil;
    }

    @Override
    public RegistrationDTO registration(RegistrationDTO registrationDTO, String url) {
        RealmResource realmResource = keycloakUtil.getKeycloakRealm();
        UserRepresentation userRP = createKeycloakUser(registrationDTO);
        Response response = realmResource.users().create(userRP);
        registrationDTO.setStatusCode(response.getStatus());
        registrationDTO.setStatus(response.getStatusInfo().toString());
        registrationDTO.setUsername(userRP.getUsername());
        createUser(registrationDTO, realmResource, response, url);
        return registrationDTO;
    }

    @Override
    public AccessTokenResponse login(LoginDTO loginDTO) throws JsonProcessingException {
        Configuration configuration = keycloakUtil.getConfiguration();
        AuthzClient authzClient = AuthzClient.create(configuration);
        AccessTokenResponse response;
        try {
            response = authzClient.obtainAccessToken(loginDTO.getEmail(), loginDTO.getPassword());
        } catch (Exception e) {
            throw new InvalidUserDataException();
        }
        JsonNode user = tokenUtil.getUser(response.getToken());
        if (!user.get("email_verified").asBoolean()) {
            throw new VerifyEmailException();
        }
        return response;
    }


    private void createUser(RegistrationDTO registrationDTO, RealmResource realmResource,
                            Response response, String url) {
        if (response.getStatus() == 201) {
            setPasswordAndRole(registrationDTO, realmResource, response);
            User user = createUser(registrationDTO);
            verificationService.sendVerifyToken(user, url);
        }
    }

    private UserRepresentation createKeycloakUser(RegistrationDTO userDTO) {
        UserRepresentation user = new UserRepresentation();
        user.setEnabled(true);
        user.setUsername(userDTO.getCompany_name().toLowerCase(Locale.ROOT).replace(" ", "_") + "_" + UUID.randomUUID());
        user.setEmail(userDTO.getEmail());
        user.setFirstName(userDTO.getAgent_name());
        user.setLastName(userDTO.getAgent_surname());
        return user;
    }

    private void setPasswordAndRole(RegistrationDTO userDTO,
                                    RealmResource realmResource,
                                    Response response) {
        String userId = CreatedResponseUtil.getCreatedId(response);
        log.info("Created userId {}", userId);
        UserResource userResource = keycloakUtil.setPassword(userDTO, userId);
        setRole(realmResource, userResource);
    }

    private void setRole(RealmResource realmResource, UserResource userResource) {
        RoleRepresentation realmRoleUser = realmResource.roles().get(role).toRepresentation();
        userResource.roles().realmLevel().add(Collections.singletonList(realmRoleUser));
    }

    private User createUser(RegistrationDTO userDTO) {
        User user = mapperModel.entityToDTO(userDTO, User.class);
        user.setCreated_at(LocalDateTime.now());
        return userRepo.save(userRepo.save(user));
    }
}
