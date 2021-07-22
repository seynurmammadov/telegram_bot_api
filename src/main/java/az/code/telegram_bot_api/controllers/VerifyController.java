package az.code.telegram_bot_api.controllers;

import az.code.telegram_bot_api.models.DTOs.LoginDTO;
import az.code.telegram_bot_api.models.DTOs.ResetPasswordDTO;
import az.code.telegram_bot_api.models.DTOs.UserTokenDTO;
import az.code.telegram_bot_api.services.interfaces.AuthService;
import az.code.telegram_bot_api.services.interfaces.VerificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/verify")
@Slf4j
public class VerifyController {
    final
    AuthService authService;

    final
    VerificationService verificationService;

    public VerifyController(AuthService authService, VerificationService verificationService) {
        this.authService = authService;
        this.verificationService = verificationService;
    }

    @PostMapping(path = "/confirm")
    public ResponseEntity<Void> verify(@RequestParam("token") String token) {
        return new ResponseEntity<>(verificationService.verify(token));
    }

    @PostMapping(path = "/forgot-password")
    public ResponseEntity<Void> forgot(@RequestBody LoginDTO loginDTO) {
        return new ResponseEntity<>(verificationService.passwordForgot(loginDTO));
    }

    @PostMapping(path = "/reset-password/{token}")
    public ResponseEntity<Void> resetWithToken(@PathVariable String token,
                                               @RequestBody ResetPasswordDTO resetPasswordDTO) {
        return new ResponseEntity<>(verificationService.resetWithToken(token,resetPasswordDTO));
    }
    @PostMapping(path = "/reset-password")
    public ResponseEntity<Void> resetWithOldPassword(@RequestAttribute UserTokenDTO user,
                                                     @RequestBody ResetPasswordDTO resetPasswordDTO) {
        return new ResponseEntity<>(verificationService.resetWithOldPassword(user,resetPasswordDTO));
    }
}
