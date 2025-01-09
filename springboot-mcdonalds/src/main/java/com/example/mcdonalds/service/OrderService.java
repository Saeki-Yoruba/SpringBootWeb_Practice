package com.example.mcdonalds.service;
/*
 * 功能:
 * 尋找該用戶的所有訂單
 * 加入/儲存訂單
 */

import java.util.List;
import java.util.Optional;

import com.example.mcdonalds.model.dto.OrderDTO;

import com.example.mcdonalds.model.dto.OrderItemDTO;

public interface OrderService {
	// 尋找該使用者的所有訂單
	// 根據使用者 id 取得該使用者的訂購資料
	public List<OrderDTO> findOrdersByUserId(Long userId);
	
	// 加入/儲存訂單
	public Optional<OrderDTO> saveOrder(Long userId, List<OrderItemDTO> orderItems);
}
