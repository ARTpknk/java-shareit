package ru.practicum.shareit.request;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.booking.model.Booking;

import java.util.List;

@Repository
public interface RequestRepository extends JpaRepository<Request, Integer> {

    List<Request> findAllByRequestorIdOrderByCreatedDesc(Integer requestorId);
    @Query(value = "SELECT * FROM Requests " +
            "WHERE NOT requestor_id = ?1 ORDER BY created DESC LIMIT ?2 OFFSET ?3", nativeQuery = true)
    List<Request> findUsersRequests(Integer requestorId, Integer size, Integer from);

}
