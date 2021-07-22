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
import org.springframework.stereotype.Service;

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


    public ListenerServiceImpl(RequestRepository requestRepository, MapperModel mapperModel,
                               LanguageRepository languageRepository, UserRepository userRepo) {
        this.requestRepository = requestRepository;
        this.mapperModel = mapperModel;
        this.languageRepository = languageRepository;
        this.userRepo = userRepo;
    }

    public void saveRequest(UserData userData) {
        RequestDTO requestDTO = mapperModel.defaultMap(userData.getAnswers(), RequestDTO.class);
        requestDTO.setUUID(userData.getUUID());
        Request request = mapperModel.requestDTOtoRequest(
                requestDTO,
                languageRepository.getByKeyword(requestDTO.getLanguage()));
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
