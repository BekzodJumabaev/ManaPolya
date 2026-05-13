package org.example.repository;

import org.example.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.time.LocalTime;

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
}
