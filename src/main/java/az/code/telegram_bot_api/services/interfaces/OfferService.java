package az.code.telegram_bot_api.services.interfaces;

import az.code.telegram_bot_api.models.DTOs.OfferDTO;
import az.code.telegram_bot_api.models.DTOs.UserTokenDTO;
import az.code.telegram_bot_api.models.Offer;
import org.springframework.http.HttpStatus;

import java.io.IOException;

public interface OfferService {
    HttpStatus sendOffer(UserTokenDTO userTokenDTO, String  userRequestId, OfferDTO offer) throws IOException;
}
