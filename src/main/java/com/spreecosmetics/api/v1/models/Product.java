package com.spreecosmetics.api.v1.models;

import com.spreecosmetics.api.v1.audits.TimestampAudit;
import com.spreecosmetics.api.v1.enums.ECurrency;
import com.spreecosmetics.api.v1.fileHandling.File;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "products")
public class Product extends TimestampAudit {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(unique = true)
    private String name;

    @Column
    @Enumerated(EnumType.STRING)
    private ECurrency currency;

    @Column()
    private int price;

    @ManyToOne
    private User addedBy;

    @Column
    private String manufacturer;

    @Column(columnDefinition = "1")
    private int quantity;

    @Column
    private String manufacturedAt;

    @Column
    private String expiresAt;

    @JoinColumn(name = "product_image_id")
    @OneToOne(cascade = CascadeType.ALL)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private File coverImage;

    @OneToMany
    private List<File> images;

    public Product(String name, ECurrency currency, int price, String manufacturer, String manufacturedAt, String expiresAt) {
        this.name = name;
        this.currency = currency;
        this.price = price;
        this.manufacturer = manufacturer;
        this.manufacturedAt = manufacturedAt;
        this.expiresAt = expiresAt;
    }
}
