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

class ResetPasswordDTOTest {

    private Validator validator;

    @BeforeEach
    void setData() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    @DisplayName("Reset Password DTO - validation success- Valid")
    public void testResetPasswordDTOSuccess() {
        ResetPasswordDTO resetPasswordDTO = ResetPasswordDTO.builder()
                .password_repeat("seynur247")
                .password("seynur247").build();
        Set<ConstraintViolation<ResetPasswordDTO>> violations = validator.validate(resetPasswordDTO);
        assertTrue(violations.isEmpty());
    }

    @Test
    @DisplayName("Reset Password DTO - validation error- Valid")
    public void testResetPasswordDTOError() {
        ResetPasswordDTO resetPasswordDTO = ResetPasswordDTO.builder()
                .password_repeat(null)
                .password("21334").build();
        Set<ConstraintViolation<ResetPasswordDTO>> violations = validator.validate(resetPasswordDTO);
        assertThat(violations)
                .hasSize(3)
                .map(ConstraintViolation::getMessage)
                .contains("The password fields must match",
                        "Password should be less than 15 characters and bigger than 6 charters",
                        "Password repeat must not be null or empty");
    }

}