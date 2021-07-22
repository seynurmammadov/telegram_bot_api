package az.code.telegram_bot_api.services;

import az.code.telegram_bot_api.exceptions.UserNotFoundException;
import az.code.telegram_bot_api.models.User;
import az.code.telegram_bot_api.repositories.UserRepository;
import az.code.telegram_bot_api.services.interfaces.UserService;
import org.springframework.stereotype.Service;

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
}
