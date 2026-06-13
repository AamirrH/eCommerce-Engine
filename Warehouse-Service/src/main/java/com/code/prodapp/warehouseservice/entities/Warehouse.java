package com.code.prodapp.warehouseservice.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.locationtech.jts.geom.Point;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
public class Warehouse {

    // UUID or Universally Unique ID is randomly generated ID made of numbers and characters guaranteed to be unique everytime
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String warehouseName;

    // Point -> org.locationtech.jts
    // GiST Index cannot be made using Hibernate, they have to be using Flyway
    @Column(nullable = false,columnDefinition = "geography(Point,4326)")
    private Point location;

    @Column(nullable = false)
    private String city;

    @Column(nullable = false)
    private Integer capacity;

    @Column(nullable = false)
    private boolean active;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;




}
