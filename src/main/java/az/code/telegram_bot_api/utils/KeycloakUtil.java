package az.code.telegram_bot_api.utils;

import az.code.telegram_bot_api.exceptions.UserNotFoundException;
import az.code.telegram_bot_api.models.DTOs.RegistrationDTO;
import az.code.telegram_bot_api.models.DTOs.ResetPasswordDTO;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.authorization.client.Configuration;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class KeycloakUtil {
    @Value("${app.keycloak.admin.resource}")
    private String adminClientId;
    @Value("${app.keycloak.admin.realm}")
    private String adminRealm;
    @Value("${keycloak.realm}")
    private String realm;
    @Value("${keycloak.auth-server-url}")
    private String authServerUrl;
    @Value("${app.keycloak.username}")
    private String username;
    @Value("${app.keycloak.password}")
    private String password;
    @Value("${keycloak.credentials.secret}")
    private String clientSecret;
    @Value("${keycloak.resource}")
    private String clientId;


    public RealmResource getKeycloakRealm() {
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

    public UserResource setPassword(RegistrationDTO userDTO, String userId) {
        return setPassword(
                ResetPasswordDTO.builder()
                        .password(userDTO.getPassword())
                        .password_repeat(userDTO.getPassword_repeat())
                        .build(),
                getKeycloakRealm().users(),
                userId);
    }

    public void setPassword(String username, ResetPasswordDTO userDTO) {
        UsersResource userResource = getKeycloakRealm().users();
        List<UserRepresentation> users = userResource.search(username);
        if (users.size() == 0)
            throw new UserNotFoundException();
        setPassword(
                ResetPasswordDTO.builder()
                        .password(userDTO.getPassword())
                        .password_repeat(userDTO.getPassword_repeat())
                        .build(),
                userResource,
                users.get(0).getId());
    }

    public UserResource setPassword(ResetPasswordDTO userDTO, UsersResource usersResource, String userId) {
        CredentialRepresentation passwordCred = new CredentialRepresentation();
        passwordCred.setTemporary(false);
        passwordCred.setType(CredentialRepresentation.PASSWORD);
        passwordCred.setValue(userDTO.getPassword());

        UserResource userResource = usersResource.get(userId);
        userResource.resetPassword(passwordCred);
        return userResource;
    }
    public Configuration getConfiguration() {
        Map<String, Object> clientCredentials = new HashMap<>();
        clientCredentials.put("secret", clientSecret);
        clientCredentials.put("grant_type", "password");
        return new Configuration(authServerUrl, realm, clientId, clientCredentials, null);
    }
}
