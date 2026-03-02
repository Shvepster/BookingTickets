package com.example.bookingtickets.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "events")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String category;
    private Double price;

    public Event(String title, String category, Double price) {
        this.title = title;
        this.category = category;
        this.price = price;
    }
}