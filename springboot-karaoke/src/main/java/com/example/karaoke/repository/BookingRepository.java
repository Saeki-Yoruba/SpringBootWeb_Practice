package com.example.karaoke.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.karaoke.model.entity.Booking;
import com.example.karaoke.model.entity.Room;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    Optional<Booking> findByIdAndUserId(Long id, Long userId);
    List<Booking> findByUserId(Long userId);

    @Query("SELECT CASE WHEN COUNT(b) > 0 THEN TRUE ELSE FALSE END " +
           "FROM Booking b " +
           "WHERE b.room = :room " +
           "AND b.id <> :excludeBookingId " +
           "AND b.startTime < :endTime " +
           "AND b.endTime > :startTime")
    boolean existsByRoomAndTimeRange(
            @Param("room") Room room,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime,
            @Param("excludeBookingId") Long excludeBookingId
    );
}

