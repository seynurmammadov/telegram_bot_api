package az.code.telegram_bot_api;

import az.code.telegram_bot_api.models.DTOs.UserTokenDTO;
import az.code.telegram_bot_api.models.Request;
import az.code.telegram_bot_api.models.User;
import az.code.telegram_bot_api.models.UserRequest;
import az.code.telegram_bot_api.models.VerificationToken;
import az.code.telegram_bot_api.models.enums.AddressType;
import az.code.telegram_bot_api.models.enums.RequestStatus;
import az.code.telegram_bot_api.models.enums.TokenType;
import az.code.telegram_bot_api.models.enums.TourType;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.UUID;

public class SPRING_TEST_DATA {
    public VerificationToken generatePasswordToken() {
        return VerificationToken.builder()
                .tokenType(TokenType.PASSWORD_RESET)
                .token(UUID.randomUUID().toString())
                .createdDate(LocalDateTime.now())
                .build();
    }

    public VerificationToken generateEmailToken() {
        return VerificationToken.builder()
                .tokenType(TokenType.EMAIL_VERIFY)
                .token(UUID.randomUUID().toString())
                .createdDate(LocalDateTime.now())
                .build();
    }

    public User generateUser() {
        return User.builder()
                .company_name("seynur_company")
                .email(UUID.randomUUID() + "@code.edu.az")
                .agent_surname("Mammadov")
                .agent_name("Seynur")
                .created_at(LocalDateTime.now())
                .isActive(true)
                .verificationToken(new ArrayList<>())
                .userRequests(new HashSet<>())
                .tin(21321312L)
                .username(UUID.randomUUID().toString())
                .build();
    }

    public UserRequest generateUserRequest(Request request, User user, RequestStatus status, boolean archived) {
        return UserRequest.builder()
                .id(1L)
                .user(user)
                .request(request)
                .requestStatus(status)
                .isArchived(archived)
                .build();
    }

    public UserTokenDTO generateUserDTO(User user) {
        return UserTokenDTO.builder()
                .username(user.getUsername())
                .build();
    }

    public Request generateRequest() {
        return Request.builder()
                .experationDate(LocalDateTime.now())
                .createdAt(LocalDateTime.now())
                .budget(200)
                .travellerCount("2301")
                .addressToType(AddressType.YOURSELF)
                .addressToUser("address")
                .addressFrom("baku")
                .tourType(TourType.EXCURSION)
                .UUID(UUID.randomUUID().toString())
                .travelEndDate(LocalDate.now())
                .travelStartDate(LocalDate.now())
                .isActive(true)
                .userRequests(Collections.emptyList())
                .build();
    }
}

