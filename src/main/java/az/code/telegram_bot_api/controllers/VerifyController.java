package az.code.telegram_bot_api.controllers;

import az.code.telegram_bot_api.models.DTOs.LoginDTO;
import az.code.telegram_bot_api.models.DTOs.ResetPasswordDTO;
import az.code.telegram_bot_api.models.DTOs.UserTokenDTO;
import az.code.telegram_bot_api.services.interfaces.AuthService;
import az.code.telegram_bot_api.services.interfaces.VerificationService;
import az.code.telegram_bot_api.utils.MessageUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping("api/verify")
@Slf4j
public class VerifyController {
    final
    AuthService authService;
    final
    VerificationService verificationService;
    final
    MessageUtil messageUtil;

    public VerifyController(AuthService authService, VerificationService verificationService, MessageUtil messageUtil) {
        this.authService = authService;
        this.verificationService = verificationService;
        this.messageUtil = messageUtil;
    }

    @GetMapping(path = "/confirm")
    public ResponseEntity<Void> verify(@RequestParam("token") String token) {
        return new ResponseEntity<>(verificationService.verify(token));
    }

    @PostMapping(path = "/forgot-password")
    public ResponseEntity<Void> forgot(@RequestBody LoginDTO loginDTO, HttpServletRequest request) {
        return new ResponseEntity<>(verificationService.passwordForgot(loginDTO, messageUtil.getUrl(request)));
    }

    @PostMapping(path = "/reset-password/{token}")
    public ResponseEntity<Void> resetWithToken(@PathVariable String token,
                                               @Valid @RequestBody ResetPasswordDTO resetPasswordDTO) {
        return new ResponseEntity<>(verificationService.resetWithToken(token, resetPasswordDTO));
    }

    @PostMapping(path = "/reset-password")
    public ResponseEntity<Void> resetWithOldPassword(@RequestAttribute UserTokenDTO user,
                                                     @Valid @RequestBody ResetPasswordDTO resetPasswordDTO) {
        log.info("User with username '{}' calls resetWithOldPassword", user.getUsername());
        return new ResponseEntity<>(verificationService.resetWithOldPassword(user, resetPasswordDTO));
    }
}
