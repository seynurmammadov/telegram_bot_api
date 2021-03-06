package az.code.telegram_bot_api.controllers;

import az.code.telegram_bot_api.models.DTOs.UserTokenDTO;
import az.code.telegram_bot_api.models.UserRequest;
import az.code.telegram_bot_api.services.interfaces.RequestService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static az.code.telegram_bot_api.utils.BaseUtils.getPageable;

@RestController
@RequestMapping("api/request")
@Slf4j
public class RequestController {

    RequestService requestService;

    public RequestController(RequestService requestService) {
        this.requestService = requestService;
    }

    @GetMapping("")
    public ResponseEntity<List<UserRequest>> getAll(@RequestAttribute UserTokenDTO user,
                                                    @RequestParam(required = false, defaultValue = "0")
                                                            Integer pageNo,
                                                    @RequestParam(required = false, defaultValue = "10")
                                                            Integer itemsCount,
                                                    @RequestParam(required = false, defaultValue = "requestStatus")
                                                            String sortBy,
                                                    @RequestParam(required = false)
                                                            String status) {
        List<UserRequest> requests;
        if (status != null) {
            log.info("User with username '{}' calls get requests by status", user.getUsername());
            requests = requestService.getAll(user, getPageable(pageNo, itemsCount, sortBy), status);
        } else {
            log.info("User with username '{}' calls get all requests", user.getUsername());
            requests = requestService.getAll(user, getPageable(pageNo, itemsCount, sortBy));
        }
        return new ResponseEntity<>(requests, HttpStatus.OK);
    }

    @GetMapping("/archived")
    public ResponseEntity<List<UserRequest>> getAllArchived(@RequestAttribute UserTokenDTO user,
                                                            @RequestParam(required = false, defaultValue = "0")
                                                                    Integer pageNo,
                                                            @RequestParam(required = false, defaultValue = "10")
                                                                    Integer itemsCount,
                                                            @RequestParam(required = false, defaultValue = "requestStatus")
                                                                    String sortBy) {
        log.info("User with username '{}' calls get all archived requests", user.getUsername());
        List<UserRequest> requests = requestService.getAllArchived(user, getPageable(pageNo, itemsCount, sortBy));
        return new ResponseEntity<>(requests, HttpStatus.OK);
    }

    @PutMapping("/archive/{userRequestId}")
    public ResponseEntity<Void> archiveRequest(@RequestAttribute UserTokenDTO user,
                                               @PathVariable Long userRequestId) {
        log.info("User with username '{}' calls archive requests method request id {}",
                user.getUsername(),
                userRequestId);
        return new ResponseEntity<>(requestService.archiveRequest(user, userRequestId));
    }

    @PutMapping("/unarchive/{userRequestId}")
    public ResponseEntity<Void> unarchiveRequest(@RequestAttribute UserTokenDTO user,
                                                 @PathVariable Long userRequestId) {
        log.info("User with username '{}' calls unarchive requests method request id {}",
                user.getUsername(),
                userRequestId);
        return new ResponseEntity<>(requestService.unarchiveRequest(user, userRequestId));
    }

    @DeleteMapping("/archived/{userRequestId}")
    public ResponseEntity<Void> deleteArchived(@RequestAttribute UserTokenDTO user,
                                               @PathVariable Long userRequestId) {
        log.info("User with username '{}' calls delete archived requests method request id {}",
                user.getUsername(),
                userRequestId);
        return new ResponseEntity<>(requestService.deleteArchived(user, userRequestId));
    }

}
