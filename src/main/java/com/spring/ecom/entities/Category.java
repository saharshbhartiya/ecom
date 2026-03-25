package com.spring.ecom.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Byte id;

    @Column(nullable = false , name = "name")
    private String name;

    @OneToMany(mappedBy = "category")
    private Set<Product> products = new HashSet<>();

    public Category(String name){
        this.name  = name;
    }

    public Category(byte id) {
        this.id = id;
    }

    @Override
    public String toString(){
        return getClass().getSimpleName() + "(" +
                "id = " + id + " , " +
                "name = " +name;
    }
}
