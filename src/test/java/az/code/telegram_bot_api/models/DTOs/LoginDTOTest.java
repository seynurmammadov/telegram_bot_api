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

class LoginDTOTest {

    private Validator validator;

    @BeforeEach
    void setData() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    @DisplayName("Login DTO - validation Valid ")
    public void testLoginDTOSuccess() {
        LoginDTO loginDTO = LoginDTO.builder()
                .email("seynur@fam.com")
                .password("seynur247").build();
        Set<ConstraintViolation<LoginDTO>> violations = validator.validate(loginDTO);
        assertTrue(violations.isEmpty());
    }

    @Test
    @DisplayName("Login DTO - validation Invalid")
    public void testLoginDTOError() {
        LoginDTO loginDTO = LoginDTO.builder()
                .email(" ")
                .password("         ").build();
        Set<ConstraintViolation<LoginDTO>> violations = validator.validate(loginDTO);
        assertThat(violations)
                .hasSize(3)
                .map(ConstraintViolation::getMessage)
                .contains("Email is not valid",
                        "Email must not be null or empty",
                        "Password must not be null or empty");
    }

}