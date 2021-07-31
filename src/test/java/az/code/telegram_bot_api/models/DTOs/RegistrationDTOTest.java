package az.code.telegram_bot_api.models.DTOs;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class RegistrationDTOTest {

    private Validator validator;

    @BeforeEach
    void setData() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    @DisplayName("Registration DTO - validation success- Valid")
    public void testRegistrationDTOSuccess() {
        RegistrationDTO registrationDTO = RegistrationDTO.builder()
                .tin(23131L)
                .password("1234567")
                .password_repeat("1234567")
                .company_name("name")
                .agent_name("name")
                .agent_surname("name")
                .email("email@gmail.com")
                .build();
        Set<ConstraintViolation<RegistrationDTO>> violations = validator.validate(registrationDTO);
        assertTrue(violations.isEmpty());
    }

    @Test
    @DisplayName("Registration DTO - validation error- Valid")
    public void testRegistrationDTOError() {
        RegistrationDTO registrationDTO = RegistrationDTO.builder()
                .tin(-23131L)
                .password("12346")
                .password_repeat("9120u9123")
                .company_name(null)
                .agent_name("3000000000000000000000000000000000000000000000000000000")
                .agent_surname("          ")
                .email("esrdsf")
                .build();
        Set<ConstraintViolation<RegistrationDTO>> violations = validator.validate(registrationDTO);
        assertThat(violations)
                .hasSize(7)
                .map(ConstraintViolation::getMessage)
                .contains("Password should be less than 15 characters and bigger than 6 charters",
                        "Agent surname must not be null or empty",
                        "Company name must not be null or empty",
                        "Agent name should be less than 30 characters",
                        "TIN should be positive",
                        "The password fields must match!",
                        "Email is not valid");
    }

}