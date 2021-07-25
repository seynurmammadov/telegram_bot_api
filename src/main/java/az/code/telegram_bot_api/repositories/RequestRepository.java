package az.code.telegram_bot_api.repositories;

import az.code.telegram_bot_api.models.Request;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface RequestRepository extends JpaRepository<Request, Long> {
    @Query("select r from Request r where r.isActive=true")
    List<Request> getAllActive();

    @Query("select  r from Request r where r.isActive=true and r.UUID=:UUID")
    Optional<Request> findByUUID(String UUID);
}
