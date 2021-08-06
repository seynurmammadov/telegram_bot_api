package az.code.telegram_bot_api.controllers;

import az.code.telegram_bot_api.models.DTOs.OfferDTO;
import az.code.telegram_bot_api.models.DTOs.UserTokenDTO;
import az.code.telegram_bot_api.services.interfaces.OfferService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;

@RestController
@RequestMapping("api/request/offer")
@Slf4j
public class OfferController {

    OfferService offerService;

    public OfferController(OfferService offerService) {
        this.offerService = offerService;
    }

    @PostMapping("/{requestId}")
    public ResponseEntity<Void> sendOffer(@RequestAttribute UserTokenDTO user,
                                          @PathVariable String requestId,
                                          @Valid @RequestBody OfferDTO offer) throws IOException {
        log.info("User with username '{}' calls send offer method request id {}",
                user.getUsername(),
                requestId);
        return new ResponseEntity<>(offerService.sendOffer(user, requestId, offer));
    }

}
