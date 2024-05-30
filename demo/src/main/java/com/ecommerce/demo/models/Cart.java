package com.ecommerce.demo.models;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.List;

@Entity
@Data
public class Cart implements Serializable {

    @Id
    private String id;
    @ElementCollection
    private List<Long> products;
    private int qty;
    private double price;

}
