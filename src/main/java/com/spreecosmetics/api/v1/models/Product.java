package com.spreecosmetics.api.v1.models;

import com.spreecosmetics.api.v1.fileHandling.File;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column
    private String name;

    @Column
    private String currency;

    @Column
    private int price;

    @ManyToOne
    private User addedby;

    @Column
    private String manufacturer;

    @Column
    private String manufacturedAt;

    @Column
    private String expiresAt;

    @OneToOne
    private File coverImage;

    @OneToMany
    private List<File> images;


    public Product(String name, String currency, int price, String manufacturer, String manufacturedAt, String expiresAt) {
        this.name = name;
        this.currency = currency;
        this.price = price;
        this.manufacturer = manufacturer;
        this.manufacturedAt = manufacturedAt;
        this.expiresAt = expiresAt;
    }
}
