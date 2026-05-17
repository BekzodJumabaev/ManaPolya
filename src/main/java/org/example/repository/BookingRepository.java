package org.example.repository;

import org.example.entity.Booking;
import org.example.enums.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {


    @Query("""
          select count(b) > 0 from Booking b 
                    where b.sportField.id = :fieldId
                              and b.status = 'CONFIRMED'
                                        and (:start < b.endTime and :end > b.startTime)
          """)
    boolean existsOverLappingBooking(
            @Param("fieldId") Long fieldId,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
            );

     List<Booking> findBySportFieldIdAndStatusAndEndTimeAfterOrderByStartTimeAsc(Long sportFieldId, BookingStatus status, LocalDateTime now);

    @Query("select b from Booking b where b.sportField.owner.username = :username ORDER BY b.startTime DESC")
    List<Booking> findAllBookingsByOwnerUsername(@Param("username") String username);
}
