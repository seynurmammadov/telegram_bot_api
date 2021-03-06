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

class OfferDTOTest {

    private Validator validator;
    String symbols = "thsimbolsthsimbolsthsimbolsthsimbolsthsimbolsthsimbolsthsimbolsthsimbolsthsimbolsthsimbolsthsimbolsthsimbolsthsimbolsthsimbolsthsimbolsthsimbolsthsimbolsthsimbolsthsimbolsthsimbolsthsimbolsthsimbolsthsimbolsthsimbolsthsimbolsthsimbolsthsimbolsthsimbolsthsimbolsthsimbolsthsimbolsthsimbolsthsimbolsthsimbolsthsimbolsthsimbolsthsimbolsthsimbolsthsimbolsthsimbolsthsimbolsthsimbolsthsimbolsthsimbolsthsimbolsthsimbolsthsimbolsthsimbolsthsimbolsthsimbolsthsimbolsthsimbolsthsimbolsthsimbolsthsimbolsthsimbolsthsimbolsthsimbolsthsimbolsthsimbolsthsimbolsthsimbolsthsimbolsthsimbolsthsimbolsthsimbols" +
            "thsimbolsthsimbolsthsimbolsthsimbolsthsimbolsthsimbolsthsimbolsthsimbolsthsimbolsthsimbolsthsimbolsthsimbolsthsimbolsthsimbolsthsimbolsthsimbolsthsimbolsthsimbolsthsimbolsthsimbolsthsimbolsthsimbolsthsimbolsthsimbolsthsimbolsthsimbolsthsimbolsthsimbolsthsimbolsthsimbolsthsimbolsthsimbolsthsimbolsthsimbolsthsimbolsthsimbolsthsimbolsthsimbolsthsimbolsthsimbolsthsimbolsthsimbolsthsimbolsthsimbolsthsimbolsthsimbolsthsimbolsthsimbolsthsimbolsthsimbolsthsimbolsthsimbolsthsimbolsthsimbolsthsimbolsthsimbolsthsimbolsthsimbolsthsimbolsthsimbolsthsimbolsthsimbolsthsimbolsthsimbolsthsimbolsthsimbols";

    @BeforeEach
    void setData() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    @DisplayName("Offer DTO - validation Valid")
    public void testOfferDTOSuccess() {
        OfferDTO offerDTO = OfferDTO.builder()
                .dateInterim("20.12.2012-20.12.2012")
                .description("some desc")
                .notes("notes")
                .price(10L)
                .build();
        Set<ConstraintViolation<OfferDTO>> violations = validator.validate(offerDTO);
        assertTrue(violations.isEmpty());
    }

    @Test
    @DisplayName("Offer DTO - validation Invalid")
    public void testOfferDTOError() {
        OfferDTO offerDTO = OfferDTO.builder()
                .dateInterim("null")
                .description(symbols)
                .notes(null)
                .price(2139213903129012L)
                .build();
        Set<ConstraintViolation<OfferDTO>> violations = validator.validate(offerDTO);
        assertThat(violations)
                .hasSize(4)
                .map(ConstraintViolation::getMessage)
                .contains("Price should be smaller than 9999999999",
                        "Description should be less than 800 characters",
                        "Notes must not be null or empty",
                        "Invalid date interim!(Date should be like: 'xx.xx.xxxx-xx.xx.xxxx')");
    }

}