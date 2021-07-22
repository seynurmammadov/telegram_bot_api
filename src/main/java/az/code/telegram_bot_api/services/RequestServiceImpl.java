package az.code.telegram_bot_api.services;

import az.code.telegram_bot_api.exceptions.UserRequestNotFound;
import az.code.telegram_bot_api.models.DTOs.UserTokenDTO;
import az.code.telegram_bot_api.models.UserRequest;
import az.code.telegram_bot_api.models.enums.RequestStatus;
import az.code.telegram_bot_api.repositories.RequestRepository;
import az.code.telegram_bot_api.repositories.UserRequestRepository;
import az.code.telegram_bot_api.services.interfaces.RequestService;
import az.code.telegram_bot_api.services.interfaces.UserService;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static az.code.telegram_bot_api.utils.BaseUtils.paginationResult;

@Service
public class RequestServiceImpl implements RequestService {
    final
    RequestRepository requestRepository;
    final
    UserService userService;
    final
    UserRequestRepository userReqRepo;

    public RequestServiceImpl(RequestRepository requestRepository, UserService userService, UserRequestRepository userRequestRepository) {
        this.requestRepository = requestRepository;
        this.userService = userService;
        this.userReqRepo = userRequestRepository;
    }

    @Override
    public List<UserRequest> getAll(UserTokenDTO userTokenDTO, Pageable pageable) {
        return paginationResult(userReqRepo.getAllByUsername(userTokenDTO.getUsername(), pageable));
    }

    @Override
    public List<UserRequest> getAll(UserTokenDTO userTokenDTO, Pageable pageable, String status) {
        RequestStatus requestStatus = RequestStatus.valueOfStatus(status);
        if (requestStatus != null) {
            return paginationResult(
                    userReqRepo.getAllByStatus(
                            userTokenDTO.getUsername(),
                            pageable,
                            requestStatus
                    )
            );
        }
        return null;
    }

    @Override
    public HttpStatus archiveRequest(UserTokenDTO userTokenDTO, Long userRequestId) {
        Optional<UserRequest> userRequest = userReqRepo.getByUsernameAndId(
                userTokenDTO.getUsername(),
                userRequestId,
                RequestStatus.NEW_REQUEST);
        if (userRequest.isPresent()) {
            userRequest.get().setArchived(true);
            userReqRepo.save(userRequest.get());
            return HttpStatus.OK;
        }
        throw new UserRequestNotFound();
    }
}
