package az.code.telegram_bot_api.repositories;

import az.code.telegram_bot_api.models.Request;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RequestRepository extends JpaRepository<Request, Long> {
}
