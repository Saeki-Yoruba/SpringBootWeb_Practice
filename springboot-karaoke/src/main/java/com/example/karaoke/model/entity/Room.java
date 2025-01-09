package com.example.karaoke.model.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name="`rooms`")
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String roomNumber;

    @Column(nullable = false)
    private int capacity;

    @Column(nullable = false)
    private String machineType;

    @Column(nullable = false)
    private Double hourlyPrice;

}
