package az.code.telegram_bot_api.controllers;

import az.code.telegram_bot_api.models.DTOs.UserTokenDTO;
import az.code.telegram_bot_api.models.Request;
import az.code.telegram_bot_api.models.UserRequest;
import az.code.telegram_bot_api.services.interfaces.RequestService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/request")
@Slf4j
public class RequestController {
    final
    RequestService requestService;

    public RequestController(RequestService requestService) {
        this.requestService = requestService;
    }

    @GetMapping("")
    public ResponseEntity<List<UserRequest>> getRequests(@RequestAttribute UserTokenDTO user,
                                                         @RequestParam(required = false, defaultValue = "0")
                                                                 Integer pageNo,
                                                         @RequestParam(required = false, defaultValue = "10")
                                                                 Integer itemsCount,
                                                         @RequestParam(required = false, defaultValue = "requestStatus")
                                                                 String sortBy) {
        log.info("User with username '{}' calls get all requests method", user.getUsername());
        return new ResponseEntity<>(requestService.getAll(user, PageRequest.of(pageNo, itemsCount, Sort.by(sortBy))), HttpStatus.OK);
    }

    @GetMapping("/archive/{userRequestId}")
    public ResponseEntity<Void> archiveRequest(@RequestAttribute UserTokenDTO user, @PathVariable Long userRequestId) {
        log.info("User with username '{}' calls get archive requests method request id {}", user.getUsername(), userRequestId);
        return new ResponseEntity<>(requestService.archiveRequest(user, userRequestId));
    }
}
