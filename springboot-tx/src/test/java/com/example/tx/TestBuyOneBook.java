package com.example.tx;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.tx.service.BookService;
import com.example.tx.service.BuyService;

@SpringBootTest
public class TestBuyOneBook {
	@Autowired
	private BuyService buyService;
	
	@Autowired
	private BookService bookService;
	@Test
	public void test() {
		String username = "John";
		Integer bookId = 1;
		// 取得交易前餘額		
		Integer walletBalance = bookService.getWalletBalance(username);
		System.out.println(username + " 交易前餘額 = " + walletBalance);
		// 取得交易前書本庫存
		Integer amount = bookService.getBookAmount(bookId);
		System.out.println("bookId = " + bookId + " 交易前庫存 = " + amount);
		System.out.println("---------------------------------------------------------------");
		try {
			buyService.buyOneBook(username, bookId);
			System.out.println("買書成功");
		} catch (Exception e) {
			System.err.println(e.getMessage());
			System.err.println("買書失敗");
		}
		System.out.println("---------------------------------------------------------------");
		// 取得交易後餘額
		walletBalance = bookService.getWalletBalance(username);
		System.out.println(username + " 交易後餘額 = " + walletBalance);
		// 取得交易後書本庫存
		amount = bookService.getBookAmount(bookId);
		System.out.println("bookId = " + bookId + " 交易後庫存 = " + amount);
				
	}
}