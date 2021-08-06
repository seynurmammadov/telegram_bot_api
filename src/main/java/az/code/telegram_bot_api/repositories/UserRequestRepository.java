package az.code.telegram_bot_api.repositories;


import az.code.telegram_bot_api.models.UserRequest;
import az.code.telegram_bot_api.models.enums.RequestStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRequestRepository extends JpaRepository<UserRequest, Long> {
    @Query("select ur from UserRequest ur " +
            "where ur.user.username=:username and ur.isArchived=false and ur.isDeleted=false")
    Page<UserRequest> getAll(String username, Pageable pageable);

    @Query("select ur from UserRequest ur " +
            "where ur.user.username=:username and ur.isArchived=true and ur.isDeleted=false")
    Page<UserRequest> getAllArchived(String username, Pageable pageable);

    @Query("select ur from UserRequest ur " +
            "where ur.user.username=:username and ur.isArchived=false and ur.requestStatus=:status " +
            "and ur.isDeleted=false")
    Page<UserRequest> getAllByStatus(String username, Pageable pageable, RequestStatus status);

    @Query("select ur from UserRequest ur " +
            "where ur.user.username=:username and " +
            "ur.id=:id and ur.isDeleted=false and ((ur.request.isActive=false and ur.requestStatus=:except ) or (ur.requestStatus<>:except ))")
    Optional<UserRequest> getById(String username, Long id, RequestStatus except);

    @Query("select ur from UserRequest ur " +
            "where ur.user.username=:username and ur.requestStatus<>:except and " +
            "ur.id=:id and ur.requestStatus<>:except2 and ur.isArchived=true" +
            " and ur.isDeleted=false and ur.request.isActive=true")
    Optional<UserRequest> getArchivedById(String username, Long id, RequestStatus except, RequestStatus except2);

    @Query("select ur from UserRequest ur " +
            "where ur.user.username=:username  and " +
            "ur.id=:id  and ur.isArchived=true" +
            " and ur.isDeleted=false")
    Optional<UserRequest> getArchivedById(String username, Long id);

    @Query("select ur from UserRequest ur " +
            "where ur.user.username=:username and " +
            "ur.request.UUID=:requestId and ur.isDeleted=false and ur.request.isActive=true and ur.requestStatus<>:except")
    Optional<UserRequest> getForOffer(String username, String requestId, RequestStatus except);

    @Query("select ur from UserRequest ur " +
            "where ur.user.username=:username and " +
            "ur.request.UUID=:UUID and ur.isDeleted=false")
    Optional<UserRequest> getForAccepted(String username, String UUID);



    @Query("select ur from UserRequest ur where ur.isDeleted=true")
    List<UserRequest> getDeleteItems();
}

