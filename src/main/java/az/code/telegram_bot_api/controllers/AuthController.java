package az.code.telegram_bot_api.controllers;

import az.code.telegram_bot_api.models.DTOs.LoginDTO;
import az.code.telegram_bot_api.models.DTOs.RegistrationDTO;
import az.code.telegram_bot_api.services.interfaces.AuthService;
import az.code.telegram_bot_api.services.interfaces.VerificationService;
import az.code.telegram_bot_api.utils.MessageUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.representations.AccessTokenResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping("api/auth")
@Slf4j
public class AuthController {
    final
    AuthService authService;
    final
    VerificationService verificationService;
    final
    MessageUtil messageUtil;

    public AuthController(AuthService authService, VerificationService verificationService,
                          MessageUtil messageUtil) {
        this.authService = authService;
        this.verificationService = verificationService;
        this.messageUtil = messageUtil;
    }

    @PostMapping("/register")
    public ResponseEntity<RegistrationDTO> register(@Valid @RequestBody RegistrationDTO registrationDTO,
                                                    HttpServletRequest request) {
        log.info("User {} is registering", registrationDTO.getAgent_name());
        return new ResponseEntity<>(authService.registration(
                registrationDTO,
                messageUtil.getUrl(request)),
                HttpStatus.OK
        );
    }

    @PostMapping(path = "/login")
    public ResponseEntity<AccessTokenResponse> login(@RequestBody LoginDTO loginDTO) throws JsonProcessingException {
        log.info("User with email {} is joining", loginDTO.getEmail());
        return ResponseEntity.ok(authService.login(loginDTO));
    }


}
