package com.ecommerce.demo.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "`order`")
public class Order {
    @Id
    private String id;
    private double total;
    private String status;
    @DateTimeFormat(pattern = "MM/dd/yyyy HH:mm:ss")
    private Date orderCreation;
    @ElementCollection
    private List<Long> productList;

}
