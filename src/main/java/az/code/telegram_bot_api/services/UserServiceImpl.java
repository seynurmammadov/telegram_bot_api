package az.code.telegram_bot_api.services;

import az.code.telegram_bot_api.exceptions.UserNotFoundException;
import az.code.telegram_bot_api.models.Request;
import az.code.telegram_bot_api.models.User;
import az.code.telegram_bot_api.models.UserRequest;
import az.code.telegram_bot_api.models.enums.RequestStatus;
import az.code.telegram_bot_api.repositories.UserRepository;
import az.code.telegram_bot_api.services.interfaces.UserService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
public class UserServiceImpl implements UserService {
    final
    UserRepository userRepo;

    public UserServiceImpl(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public User findUserByEmail(String email) {
        Optional<User> user = userRepo.findByEmail(email);
        if (user.isPresent()) {
            return user.get();
        }
        throw new UserNotFoundException();
    }

    @Override
    public void save(User user) {
        userRepo.save(user);
    }

    @Override
    public void activeAndSave(User user) {
        user.setActive(true);
        save(user);
    }
    @Override
    public void addRequestToUsers(Request request) {
        List<User> users = userRepo.getAllActive();
        users.forEach(u -> u.addRequest(
                UserRequest.builder()
                        .user(u)
                        .request(request)
                        .requestStatus(RequestStatus.NEW_REQUEST)
                        .build()
                )
        );
        userRepo.saveAll(users);
    }
}
