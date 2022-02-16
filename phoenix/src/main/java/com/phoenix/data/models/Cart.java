package com.phoenix.data.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
//@Builder
//@NoArgsConstructor
//@AllArgsConstructor

public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @CreationTimestamp
    private LocalDateTime createdAt;


    @OneToMany(cascade = CascadeType.PERSIST)
    private List<Item> itemList;

    @Transient
    private Double totalPrice;

    public void addItem(Item item){
        if (itemList == null){
            itemList = new ArrayList<>();
        }
        itemList.add(item);
    }
}
