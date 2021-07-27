package az.code.telegram_bot_api.services;

import az.code.telegram_bot_api.models.AcceptedOffer;
import az.code.telegram_bot_api.models.Request;
import az.code.telegram_bot_api.models.DTOs.RequestDTO;
import az.code.telegram_bot_api.models.DTOs.UserData;
import az.code.telegram_bot_api.models.UserRequest;
import az.code.telegram_bot_api.models.enums.RequestStatus;
import az.code.telegram_bot_api.models.mapper.MapperModel;
import az.code.telegram_bot_api.repositories.AcceptedOfferRepository;
import az.code.telegram_bot_api.repositories.LanguageRepository;
import az.code.telegram_bot_api.repositories.RequestRepository;
import az.code.telegram_bot_api.repositories.UserRequestRepository;
import az.code.telegram_bot_api.services.interfaces.ListenerService;
import az.code.telegram_bot_api.services.interfaces.RequestService;
import az.code.telegram_bot_api.services.interfaces.UserService;
import az.code.telegram_bot_api.utils.TimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class ListenerServiceImpl implements ListenerService {
    final
    RequestRepository requestRepository;
    final
    MapperModel mapperModel;
    final
    LanguageRepository languageRepository;
    final
    TimeUtil timeUtil;
    final
    UserService userService;
    final
    RequestService requestService;
    final
    AcceptedOfferRepository acceptedRepo;

    public ListenerServiceImpl(RequestRepository requestRepository, MapperModel mapperModel,
                               LanguageRepository languageRepository, TimeUtil timeUtil, UserService userService, RequestService requestService, AcceptedOfferRepository acceptedRepo) {
        this.requestRepository = requestRepository;
        this.mapperModel = mapperModel;
        this.languageRepository = languageRepository;
        this.timeUtil = timeUtil;
        this.userService = userService;
        this.requestService = requestService;
        this.acceptedRepo = acceptedRepo;
    }

    public void saveRequest(UserData userData) {
        RequestDTO requestDTO = mapperModel.defaultMap(userData.getAnswers(), RequestDTO.class);
        requestDTO.setUUID(userData.getUUID());
        Request request = mapperModel.requestDTOtoRequest(
                requestDTO,
                languageRepository.getByKeyword(requestDTO.getLanguage()),
                timeUtil.getExpireTime()
        );
        userService.addRequestToUsers(requestRepository.save(request));
    }

    @Override
    public void cancelRequest(String UUID) {
        Optional<Request> request = requestRepository.findByUUID(UUID);
        if(request.isPresent()){
            request.get().setActive(false);
            requestRepository.save(request.get());
        }
    }

    @Override
    public void acceptedOffer(AcceptedOffer offer) {
        UserRequest request = requestService.getForAccepted(offer.getUsername(),offer.getUUID());
        request.setRequestStatus(RequestStatus.ACCEPTED);
        request.getRequest().setActive(false);
        offer.setCreatedAt(LocalDateTime.now());
        offer.setUserRequest(request);
        acceptedRepo.save(offer);
        requestService.save(request);
    }

}
