package com.ecommerce.demo.models;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.UUID;

@Entity
@Data
@Table(name = "product")
public class Product implements Serializable {
    @Id
    private long id;
    private String name;
    @Column(name = "`desc`")
    private String desc;
    private String category;
    private double price;
    private int stock;

    @PrePersist
    private void prePersist(){
        UUID uuid = UUID.randomUUID();
        this.id = uuid.getMostSignificantBits();
    }


}
