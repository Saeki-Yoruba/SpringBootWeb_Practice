package com.example.karaoke.model.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name="`users`")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(unique = true,nullable = false)
	private String username;
	@Column(nullable = false)
	private String phone;
	@Column(nullable = false)
	private String email;
	@Column(nullable = false)
	private String password;
}