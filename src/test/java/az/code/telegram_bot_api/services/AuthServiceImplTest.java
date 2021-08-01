package az.code.telegram_bot_api.services;

import az.code.telegram_bot_api.CustomMockResponse;
import az.code.telegram_bot_api.SPRING_TEST_DATA;
import az.code.telegram_bot_api.models.DTOs.RegistrationDTO;
import az.code.telegram_bot_api.models.User;
import az.code.telegram_bot_api.models.mapper.MapperModel;
import az.code.telegram_bot_api.repositories.UserRepository;
import az.code.telegram_bot_api.utils.KeycloakUtil;
import org.junit.jupiter.api.*;
import org.keycloak.representations.idm.UserRepresentation;
import org.mockito.MockedStatic;
import org.mockito.Mockito;


import javax.mail.MessagingException;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;

class AuthServiceImplTest {

    SPRING_TEST_DATA data = new SPRING_TEST_DATA();
    public static MockedStatic<Clock> clockMockedStatic;
    public static MockedStatic<UUID> UUIDMockedStatic;

    @BeforeAll
    static void mockStaticVar() {
        clockMockedStatic = Mockito.mockStatic(Clock.class, CALLS_REAL_METHODS);
        UUIDMockedStatic = Mockito.mockStatic(UUID.class, CALLS_REAL_METHODS);
    }

    @AfterAll
    public static void close() {
        clockMockedStatic.close();
        UUIDMockedStatic.close();
    }

    AuthServiceImpl as;

    @BeforeEach
    void setData() {
        as = getAuthService();
    }

    @Test
    @DisplayName("Auth Service - create new User Valid")
    void createUser() throws CloneNotSupportedException, MessagingException, IOException {
        Response response = new CustomMockResponse();
        User user = data.generateUser();
        doNothing().when(as).setPasswordAndRole(any(), any(), any());
        when(as.mapperModel.entityToDTO(any(), any())).thenReturn(user);
        as.createUser(data.generateRegistrationDTO(), null, response, "");
        User expected = (User) user.clone();
        verify(as.verificationService, times(0)).sendVerifyToken(expected, "");
    }

    @Test
    @DisplayName("Auth Service - create Keycloak User Valid")
    void createKeycloakUser() {
        RegistrationDTO registrationDTO = data.generateRegistrationDTO();
        UserRepresentation expected = data.generateUserRepresentation(registrationDTO);
        assertThat(as.createKeycloakUser(registrationDTO)).isEqualToComparingFieldByField(expected);
    }

    @Test
    @DisplayName("Auth Service - Create User Valid")
    void testCreateUser() throws CloneNotSupportedException {
        RegistrationDTO registrationDTO = data.generateRegistrationDTO();
        User user = data.toUser(registrationDTO);
        when(as.mapperModel.entityToDTO(any(), any())).thenReturn(user);
        User expected = (User) user.clone();
        expected.setCreated_at(LocalDateTime.now());
        as.createUser(registrationDTO);
        verify(as.userRepo).save(expected);
    }

    private AuthServiceImpl getAuthService() {
        AuthServiceImpl as = mock(AuthServiceImpl.class, CALLS_REAL_METHODS);
        as.mapperModel = mock(MapperModel.class, CALLS_REAL_METHODS);
        as.userRepo = mock(UserRepository.class);
        as.verificationService = mock(VerificationServiceImpl.class);
        as.keycloakUtil = mock(KeycloakUtil.class);
        mockClockNUUID();
        return as;
    }

    private void mockClockNUUID() {
        Clock clock = Clock.fixed(Instant.parse("2021-12-22T05:15:30.00Z"), ZoneId.of("UTC"));
        clockMockedStatic.when(Clock::systemDefaultZone).thenReturn(clock);
        UUIDMockedStatic.when(UUID::randomUUID).thenReturn(new UUID(10, 10));
    }

}