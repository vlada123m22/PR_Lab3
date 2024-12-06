package com.vlada.lab3.dto;

import jakarta.persistence.*;
import lombok.Data;

@Data
public class ProductDTO {

    private String name;

    private String price;

    @Override
    public String toString() {
        return "ProductDTO{" +
                "name='" + name + '\'' +
                ", price='" + price + '\'' +
                '}';
    }

}