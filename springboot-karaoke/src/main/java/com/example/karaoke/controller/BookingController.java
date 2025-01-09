package com.example.karaoke.controller;

import com.example.karaoke.exception.BookingNotFoundException;
import com.example.karaoke.exception.RoomNotFoundException;
import com.example.karaoke.model.dto.BookingDTO;
import com.example.karaoke.model.dto.UserDTO;
import com.example.karaoke.response.ApiResponse;
import com.example.karaoke.service.BookingService;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/booking")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    private UserDTO getLoggedInUser(HttpSession session) {
        UserDTO userDTO = (UserDTO) session.getAttribute("userDTO");
        if (userDTO == null) {
            throw new RuntimeException("用戶未登錄");
        }
        return userDTO;
    }

    @GetMapping("/all")
    public ResponseEntity<ApiResponse<List<BookingDTO>>> getAllBookings() {
        try {
            List<BookingDTO> allBookings = bookingService.findAllBookings();
            return ResponseEntity.ok(ApiResponse.success("查詢成功", allBookings));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(ApiResponse.error(500, "系統錯誤：" + e.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<BookingDTO>>> findAllBookings(HttpSession session) {
        UserDTO userDTO = getLoggedInUser(session);
        List<BookingDTO> bookings = bookingService.findBookingByUserId(userDTO.getId());
        return ResponseEntity.ok(ApiResponse.success("查詢成功", bookings));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<BookingDTO>> findBooking(
            @PathVariable Long id, HttpSession session) throws BookingNotFoundException {
        UserDTO userDTO = getLoggedInUser(session);
        BookingDTO booking = bookingService.findBookingByIdAndUserId(id, userDTO.getId());
        return ResponseEntity.ok(ApiResponse.success("查詢成功", booking));
    }

    @PostMapping("/reservation")
    public ResponseEntity<ApiResponse<BookingDTO>> addBooking(
            @RequestBody BookingDTO bookingDTO, HttpSession session) {
        try {
            UserDTO userDTO = getLoggedInUser(session);
            Optional<BookingDTO> optBookingDTO = bookingService.saveBooking(userDTO.getId(), bookingDTO);
            return optBookingDTO.map(booking -> ResponseEntity.ok(ApiResponse.success("預約成功", booking)))
                    .orElse(ResponseEntity.status(400).body(ApiResponse.error(400, "預約失敗")));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(400).body(ApiResponse.error(400, e.getMessage()));
        } catch (RoomNotFoundException e) {
            return ResponseEntity.status(404).body(ApiResponse.error(404, e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(ApiResponse.error(500, "系統錯誤：" + e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<BookingDTO>> updateBooking(
            @PathVariable Long id, @RequestBody BookingDTO bookingDTO, HttpSession session) {
        try {
            bookingDTO.setId(id);
            BookingDTO updatedBookingDTO = bookingService.updateBooking(bookingDTO);
            return ResponseEntity.ok(ApiResponse.success("修改成功", updatedBookingDTO));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(400).body(ApiResponse.error(400, "時間衝突：" + e.getMessage()));
        } catch (RoomNotFoundException | BookingNotFoundException e) {
            return ResponseEntity.status(404).body(ApiResponse.error(404, e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(ApiResponse.error(500, "系統錯誤：" + e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteBooking(@PathVariable Long id) throws BookingNotFoundException {
        bookingService.deleteBooking(id);
        return ResponseEntity.ok(ApiResponse.success("刪除成功", null));
    }
}

