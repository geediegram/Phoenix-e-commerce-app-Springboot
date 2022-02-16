package com.phoenix.data.models;


import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne()
    private Product product;

    private Integer quantityAddedToCart;

    public Item(Product product, int quantityAddedToCart){
        if (quantityAddedToCart <=product.getQuantity()){
            this.quantityAddedToCart = quantityAddedToCart;
        } else {
            this.quantityAddedToCart = 0;
        }
        this.product = product;
    }

    public void setQuantityAddedToCart(int quantityAddedToCart){
        if (quantityAddedToCart <= product.getQuantity()){
            this.quantityAddedToCart = quantityAddedToCart;
        }
    }
}
