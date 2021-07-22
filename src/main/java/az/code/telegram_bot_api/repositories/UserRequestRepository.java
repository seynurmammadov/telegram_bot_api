package az.code.telegram_bot_api.repositories;

import az.code.telegram_bot_api.models.UserRequest;
import az.code.telegram_bot_api.models.enums.RequestStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;


public interface UserRequestRepository extends JpaRepository<UserRequest, Long> {
    @Query("select ur from UserRequest ur " +
            "join Request r on ur.request.orderId = r.orderId " +
            "where ur.user.username=:username and ur.isArchived=false")
    Page<UserRequest> getAllByUsername(String username, Pageable pageable);

    @Query("select ur from UserRequest ur " +
            "join Request r on ur.request.orderId = r.orderId " +
            "where ur.user.username=:username and ur.isArchived=false and ur.requestStatus=:status")
    Page<UserRequest> getAllByStatus(String username, Pageable pageable, RequestStatus status);

    @Query("select ur from UserRequest ur " +
            "join Request r on ur.request.orderId = r.orderId " +
            "where ur.user.username=:username and ur.requestStatus<>:status and " +
            "ur.id=:id")
    Optional<UserRequest> getByUsernameAndId(String username, Long id, RequestStatus status);
}
