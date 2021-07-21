package az.code.telegram_bot_api.controllers;

import az.code.telegram_bot_api.models.LoginDTO;
import az.code.telegram_bot_api.models.RegistrationDTO;
import az.code.telegram_bot_api.services.interfaces.AuthService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.representations.AccessTokenResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("api/auth")
@Slf4j
public class AuthController {
    final
    AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<Object> registerUser(@Valid @RequestBody RegistrationDTO registrationDTO) {
        log.info("User {} is registering", registrationDTO.getAgent_name());
        return new ResponseEntity<>(authService.registration(registrationDTO), HttpStatus.OK);
    }

    @PostMapping(path = "/verify")
    public ResponseEntity<Void> confirmUserAccount(@RequestParam("token") String confirmationToken) {
        return new ResponseEntity<>(authService.verify(confirmationToken));
    }

    @PostMapping(path = "/login")
    public ResponseEntity<AccessTokenResponse> login(@RequestBody LoginDTO loginDTO) throws JsonProcessingException {
        return ResponseEntity.ok(authService.login(loginDTO));
    }
}
