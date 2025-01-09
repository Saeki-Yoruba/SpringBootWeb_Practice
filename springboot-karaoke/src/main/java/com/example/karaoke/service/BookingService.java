package com.example.karaoke.service;

import java.util.List;
import java.util.Optional;

import com.example.karaoke.exception.BookingNotFoundException;
import com.example.karaoke.exception.RoomNotFoundException;
import com.example.karaoke.model.dto.BookingDTO;

/*
 * 1.尋找該使用者的所有預約
 * 2.加入儲存/訂單
 * 3.尋找全員的預約
 * 4.更新預約
 * 5.刪除預約
 */
public interface BookingService {
    // 查詢所有預約
    List<BookingDTO> findAllBookings();

    // 查詢該用戶的所有預約
    List<BookingDTO> findBookingByUserId(Long userId);

    // 查詢用戶的特定預約
    BookingDTO findBookingByIdAndUserId(Long bookingId, Long userId) throws BookingNotFoundException;

    // 新增預約
    Optional<BookingDTO> saveBooking(Long userId, BookingDTO bookingDTO) throws RoomNotFoundException;

    // 更新預約
    BookingDTO updateBooking(BookingDTO bookingDTO) throws BookingNotFoundException;

    // 刪除預約
    void deleteBooking(Long id) throws BookingNotFoundException;
}
