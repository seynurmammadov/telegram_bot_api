package az.code.telegram_bot_api.repositories;

import az.code.telegram_bot_api.models.Request;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RequestRepository extends JpaRepository<Request, Long> {
    @Query("select r from Request r where r.isExpired=false")
    List<Request> getAllActive();
}
