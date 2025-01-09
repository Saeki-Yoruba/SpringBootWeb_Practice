package com.example.karaoke.service.impl;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.karaoke.exception.BookingNotFoundException;
import com.example.karaoke.exception.RoomNotFoundException;
import com.example.karaoke.exception.UserNotFoundException;
import com.example.karaoke.model.dto.BookingDTO;
import com.example.karaoke.model.entity.*;
import com.example.karaoke.repository.BookingRepository;
import com.example.karaoke.repository.RoomRepository;
import com.example.karaoke.repository.UserRepository;
import com.example.karaoke.service.BookingService;

@Service
public class BookingServiceImpl implements BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;

    private boolean isEndTimeAfterStartTime(LocalDateTime startTime, LocalDateTime endTime) {
        return endTime != null && startTime != null && endTime.isAfter(startTime);
    }

    private User findUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("找不到用戶，ID: " + userId));
    }

    private Room findRoomById(Long roomId) {
        return roomRepository.findById(roomId)
                .orElseThrow(() -> new RoomNotFoundException("找不到房間，ID: " + roomId));
    }

    @Override
    public List<BookingDTO> findAllBookings() {
        return bookingRepository.findAll()
                .stream()
                .map(booking -> modelMapper.map(booking, BookingDTO.class))
                .toList();
    }

    @Override
    public List<BookingDTO> findBookingByUserId(Long userId) {
        return bookingRepository.findByUserId(userId)
                .stream()
                .map(booking -> modelMapper.map(booking, BookingDTO.class))
                .toList();
    }

    @Override
    public BookingDTO findBookingByIdAndUserId(Long bookingId, Long userId) throws BookingNotFoundException {
        Booking booking = bookingRepository.findByIdAndUserId(bookingId, userId)
                .orElseThrow(() -> new BookingNotFoundException("找不到預約，ID: " + bookingId));
        return modelMapper.map(booking, BookingDTO.class);
    }

    @Override
    public Optional<BookingDTO> saveBooking(Long userId, BookingDTO bookingDTO) {
        // 驗證結束時間是否大於開始時間
        if (!isEndTimeAfterStartTime(bookingDTO.getStartTime(), bookingDTO.getEndTime())) {
            throw new IllegalArgumentException("結束時間必須大於開始時間");
        }

        if (bookingDTO.getStartTime().toLocalDate().isBefore(LocalDate.now().plusDays(1))) {
            throw new IllegalArgumentException("日期必須是明天或以後");
        }

        // 驗證開始時間是否在明天以後
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime tomorrow = now.plusDays(1).withHour(0).withMinute(0).withSecond(0).withNano(0);
        if (bookingDTO.getStartTime().isBefore(tomorrow)) {
            throw new IllegalArgumentException("只能預約明天以後的時間");
        }

        // 檢查預約時長是否超過6小時
        long durationHours = Duration.between(bookingDTO.getStartTime(), bookingDTO.getEndTime()).toHours();
        if (durationHours > 6) {
            throw new IllegalArgumentException("預約時長不能超過6小時");
        }
        
        // 驗證房間是否存在
        User user = findUserById(userId);
        Room room = findRoomById(bookingDTO.getRoomId());

        // 驗證房間是否有時間段衝突
        boolean isOverlapping = bookingRepository.existsByRoomAndTimeRange(
                room, bookingDTO.getStartTime(), bookingDTO.getEndTime(), null);
        if (isOverlapping) {
            throw new IllegalArgumentException("房間時間衝突");
        }

        // 創建並保存預約
        Booking booking = new Booking();
        booking.setUser(user);
        booking.setRoom(room);
        booking.setStartTime(bookingDTO.getStartTime());
        booking.setEndTime(bookingDTO.getEndTime());

        Booking savedBooking = bookingRepository.save(booking);
        return Optional.of(modelMapper.map(savedBooking, BookingDTO.class));
    }


    @Override
    public BookingDTO updateBooking(BookingDTO bookingDTO) throws BookingNotFoundException {
        // 檢查結束時間是否大於開始時間
        if (!isEndTimeAfterStartTime(bookingDTO.getStartTime(), bookingDTO.getEndTime())) {
            throw new IllegalArgumentException("結束時間必須大於開始時間");
        }

        if (bookingDTO.getStartTime().toLocalDate().isBefore(LocalDate.now().plusDays(1))) {
            throw new IllegalArgumentException("日期必須是明天或以後");
        }
        
        // 檢查預約時長是否超過6小時
        long durationHours = Duration.between(bookingDTO.getStartTime(), bookingDTO.getEndTime()).toHours();
        if (durationHours > 6) {
            throw new IllegalArgumentException("預約時長不能超過6小時");
        }

        // 檢查預約是否存在
        Booking booking = bookingRepository.findById(bookingDTO.getId())
                .orElseThrow(() -> new BookingNotFoundException("預約不存在，ID: " + bookingDTO.getId()));

        // 檢查新房間是否存在
        Room newRoom = findRoomById(bookingDTO.getRoomId());

        // 檢查時間段是否與其他預約衝突
        boolean isOverlapping = bookingRepository.existsByRoomAndTimeRange(
                newRoom, bookingDTO.getStartTime(), bookingDTO.getEndTime(), bookingDTO.getId());
        if (isOverlapping) {
            throw new IllegalArgumentException("時間段與其他預約衝突");
        }

        // 更新預約信息
        booking.setRoom(newRoom);
        booking.setStartTime(bookingDTO.getStartTime());
        booking.setEndTime(bookingDTO.getEndTime());

        Booking updatedBooking = bookingRepository.save(booking);
        return modelMapper.map(updatedBooking, BookingDTO.class);
    }

    @Override
    public void deleteBooking(Long bookingId) throws BookingNotFoundException {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new BookingNotFoundException("找不到預約，ID: " + bookingId));
        bookingRepository.delete(booking);
    }
}

