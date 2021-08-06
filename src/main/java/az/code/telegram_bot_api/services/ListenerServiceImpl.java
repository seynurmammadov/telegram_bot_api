package az.code.telegram_bot_api.services;

import az.code.telegram_bot_api.models.AcceptedOffer;
import az.code.telegram_bot_api.models.Request;
import az.code.telegram_bot_api.models.DTOs.UserData;
import az.code.telegram_bot_api.models.UserRequest;
import az.code.telegram_bot_api.models.enums.RequestStatus;
import az.code.telegram_bot_api.models.mapper.MapperModel;
import az.code.telegram_bot_api.repositories.AcceptedOfferRepository;
import az.code.telegram_bot_api.repositories.RequestRepository;
import az.code.telegram_bot_api.services.interfaces.ListenerService;
import az.code.telegram_bot_api.services.interfaces.RequestService;
import az.code.telegram_bot_api.services.interfaces.UserService;
import az.code.telegram_bot_api.utils.TimeUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class ListenerServiceImpl implements ListenerService {

    RequestRepository requestRepository;
    MapperModel mapperModel;
    TimeUtil timeUtil;
    UserService userService;
    RequestService requestService;
    AcceptedOfferRepository acceptedRepo;

    public ListenerServiceImpl(RequestRepository requestRepository, MapperModel mapperModel,
                               TimeUtil timeUtil, UserService userService,
                               RequestService requestService,
                               AcceptedOfferRepository acceptedRepo) {
        this.requestRepository = requestRepository;
        this.mapperModel = mapperModel;
        this.timeUtil = timeUtil;
        this.userService = userService;
        this.requestService = requestService;
        this.acceptedRepo = acceptedRepo;
    }

    public void saveRequest(UserData userData) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Request request = Request.builder()
                .isActive(true)
                .UUID(userData.getUUID())
                .answers(mapper.writeValueAsString(userData.getAnswers()))
                .createdAt(LocalDateTime.now())
                .experationDate(timeUtil.getExpireTime())
                .build();
        userService.addRequestToUsers(requestRepository.save(request));
    }

    @Override
    public void cancelRequest(String UUID) {
        Optional<Request> request = requestRepository.findByUUID(UUID);
        if (request.isPresent()) {
            request.get().setActive(false);
            requestRepository.save(request.get());
        }
    }

    @Override
    public void acceptedOffer(AcceptedOffer offer) {
        UserRequest request = requestService.getForAccepted(offer.getAgentUsername(), offer.getUUID());
        request.setRequestStatus(RequestStatus.ACCEPTED);
        offer.setCreatedAt(LocalDateTime.now());
        offer.setUserRequest(request);
        acceptedRepo.save(offer);
        requestService.save(request);
    }

}
