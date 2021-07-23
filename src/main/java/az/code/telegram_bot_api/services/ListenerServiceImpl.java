package az.code.telegram_bot_api.services;

import az.code.telegram_bot_api.models.Request;
import az.code.telegram_bot_api.models.DTOs.RequestDTO;
import az.code.telegram_bot_api.models.User;
import az.code.telegram_bot_api.models.DTOs.UserData;
import az.code.telegram_bot_api.models.UserRequest;
import az.code.telegram_bot_api.models.enums.RequestStatus;
import az.code.telegram_bot_api.models.mapper.MapperModel;
import az.code.telegram_bot_api.repositories.LanguageRepository;
import az.code.telegram_bot_api.repositories.RequestRepository;
import az.code.telegram_bot_api.repositories.UserRepository;
import az.code.telegram_bot_api.services.interfaces.ListenerService;
import az.code.telegram_bot_api.utils.TimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class ListenerServiceImpl implements ListenerService {
    final
    RequestRepository requestRepository;
    final
    MapperModel mapperModel;
    final
    LanguageRepository languageRepository;
    final
    UserRepository userRepo;
    final
    TimeUtil timeUtil;

    public ListenerServiceImpl(RequestRepository requestRepository, MapperModel mapperModel,
                               LanguageRepository languageRepository, UserRepository userRepo, TimeUtil timeUtil) {
        this.requestRepository = requestRepository;
        this.mapperModel = mapperModel;
        this.languageRepository = languageRepository;
        this.userRepo = userRepo;
        this.timeUtil = timeUtil;
    }

    public void saveRequest(UserData userData) {
        RequestDTO requestDTO = mapperModel.defaultMap(userData.getAnswers(), RequestDTO.class);
        requestDTO.setUUID(userData.getUUID());
        Request request = mapperModel.requestDTOtoRequest(
                requestDTO,
                languageRepository.getByKeyword(requestDTO.getLanguage()),
                timeUtil.getExpireTime()
        );
        addRequestToUsers(request);
    }

    private void addRequestToUsers(Request request) {
        List<User> users = userRepo.getAllActive();
        final Request finalRequest = requestRepository.save(request);
        users.forEach(u -> u.addRequest(
                UserRequest.builder()
                        .user(u)
                        .request(finalRequest)
                        .requestStatus(RequestStatus.NEW_REQUEST)
                        .build()
                )
        );
        userRepo.saveAll(users);
    }
}
