package az.code.telegram_bot_api;

import az.code.telegram_bot_api.models.*;
import az.code.telegram_bot_api.models.DTOs.*;
import az.code.telegram_bot_api.models.enums.AddressType;
import az.code.telegram_bot_api.models.enums.RequestStatus;
import az.code.telegram_bot_api.models.enums.TokenType;
import az.code.telegram_bot_api.models.enums.TourType;
import org.keycloak.representations.idm.UserRepresentation;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

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

    public ResetPasswordDTO generateResetPasswordDTO() {
        return ResetPasswordDTO.builder()
                .oldPassword("password")
                .password_repeat("password")
                .password("password")
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

    public LoginDTO generateLoginDTO(String email) {
        return LoginDTO.builder()
                .email(email)
                .password("password")
                .build();
    }

    public Offer generateOffer() {
        return Offer.builder()
                .price(1010L)
                .description("description")
                .notes("notes")
                .dateInterim("20.12.2021-21.12.2012")
                .build();
    }

    public AcceptedOffer generateAcceptedOffer(User user) {
        return AcceptedOffer.builder()
                .createdAt(LocalDateTime.now())
                .firstName("name")
                .UUID(UUID.randomUUID().toString())
                .username("userName")
                .lastName("lastname")
                .phoneNumber("number")
                .agentUsername(user.getUsername())
                .userId(1L)
                .build();

    }

    public RequestDTO generateRequestDTO() {
        return RequestDTO.builder()
                .language("AZ")
                .UUID(UUID.randomUUID().toString())
                .travelStartDate("2021-12-22")
                .travelEndDate("2021-12-22")
                .tourType(TourType.EXCURSION.getVal())
                .addressToUser("addressToUser")
                .addressFrom("addressFrom")
                .addressToType(AddressType.YOURSELF.getVal())
                .travellerCount("about 5")
                .budget(100)
                .build();
    }

    public UserData generateUserData() {
        Map<String, String> ans = new HashMap<>();
        ans.put("travelEndDate", "2021-07-31");
        ans.put("travelStartDate", "2021-07-31");
        ans.put("tourType", "EXTREME");
        ans.put("addressToUser", "adsa");
        ans.put("language", "AZ");
        ans.put("addressToType", "YOURSELF");
        ans.put("travellerCount", "10");
        ans.put("addressFrom", "Во");
        ans.put("budget", "10");
        return UserData.builder()
                .langId(1L)
                .UUID(UUID.randomUUID().toString())
                .answers(ans)
                .build();
    }

    public Language generateLanguage() {
        return Language.builder()
                .id(1L)
                .keyword("AZ")
                .langName("azerbaijan")
                .build();
    }

    public Request toRequest(RequestDTO request) {
        return Request.builder()
                .language(generateLanguage())
                .isActive(true)
                .travelStartDate(LocalDate.parse(request.getTravelStartDate()))
                .travelEndDate(LocalDate.parse(request.getTravelEndDate()))
                .UUID(request.getUUID())
                .tourType(TourType.valueOfTour(request.getTourType()))
                .addressFrom(request.getAddressFrom())
                .addressToUser(request.getAddressToUser())
                .addressToType(AddressType.valueOfAddress(request.getAddressToType()))
                .travellerCount(request.getTravellerCount())
                .budget(request.getBudget())
                .createdAt(LocalDateTime.now())
                .experationDate(LocalDateTime.now())
                .build();
    }


    public RegistrationDTO generateRegistrationDTO() {
        return RegistrationDTO.builder()
                .agent_name("name")
                .agent_surname("surname")
                .company_name("companyName")
                .email("seynursm@cods.az")
                .password_repeat("serynu246273")
                .password("serynu246273")
                .tin(21312L)
                .build();
    }

    public User toUser(RegistrationDTO registrationDTO) {
        return User.builder()
                .tin(registrationDTO.getTin())
                .agent_name(registrationDTO.getAgent_name())
                .agent_surname(registrationDTO.getAgent_surname())
                .company_name(registrationDTO.getCompany_name())
                .email(registrationDTO.getEmail())
                .verificationToken(Collections.emptyList())
                .userRequests(Collections.emptySet())
                .build();
    }

    public UserRepresentation generateUserRepresentation(RegistrationDTO userDTO) {
        UserRepresentation user = new UserRepresentation();
        user.setEnabled(true);
        user.setUsername(userDTO.getCompany_name().toLowerCase(Locale.ROOT).replace(" ", "_") + "_" + UUID.randomUUID());
        user.setEmail(userDTO.getEmail());
        user.setFirstName(userDTO.getAgent_name());
        user.setLastName(userDTO.getAgent_surname());
        return user;
    }

    public OfferDTO generateOfferDTO() {
        return OfferDTO.builder().price(1010L)
                .description("description")
                .notes("notes")
                .dateInterim("20.12.2021-21.12.2012").build();
    }

    public RequestDTO toRequestDTO(Map<String, String> ans) {
        RequestDTO requestDTO = new RequestDTO();
        requestDTO.setTravelEndDate(ans.get("travelEndDate"));
        requestDTO.setTravelStartDate(ans.get("travelStartDate"));
        requestDTO.setTourType(ans.get("tourType"));
        requestDTO.setAddressToType(ans.get("addressToType"));
        requestDTO.setAddressToUser(ans.get("addressToUser"));
        requestDTO.setLanguage(ans.get("language"));
        requestDTO.setTravellerCount(ans.get("travellerCount"));
        requestDTO.setAddressFrom(ans.get("addressFrom"));
        requestDTO.setBudget(Integer.parseInt(ans.get("budget")));
        return requestDTO;
    }

}

