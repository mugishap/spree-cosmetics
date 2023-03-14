package com.spreecosmetics.api.v1.models;

import com.spreecosmetics.api.v1.audits.TimestampAudit;
import com.spreecosmetics.api.v1.enums.ECurrency;
import com.spreecosmetics.api.v1.enums.EPaymentMethod;
import com.spreecosmetics.api.v1.enums.EPurchaseLocation;
import lombok.*;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Invoice extends TimestampAudit {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToMany()
    @JoinColumn(name = "product_ids")
    private List<Product> products;

    @Column()
    private int totalPrice;

    @Column()
    @Enumerated(EnumType.STRING)
    private ECurrency currency;

    @ManyToOne()
    private User buyer;

    @Column()
    private String buyerName;

    @Column()
    @Enumerated(EnumType.STRING)
    private EPaymentMethod paymentMethod;

    @Column()
    @Enumerated(EnumType.STRING)
    private EPurchaseLocation purchaseLocation;
}
