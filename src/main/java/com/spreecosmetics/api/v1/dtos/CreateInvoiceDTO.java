package com.spreecosmetics.api.v1.dtos;

import com.spreecosmetics.api.v1.enums.ECurrency;
import com.spreecosmetics.api.v1.enums.EPaymentMethod;
import com.spreecosmetics.api.v1.enums.EPurchaseLocation;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class CreateInvoiceDTO {

    private List<UUID> productIds;
    private int totalPrice;
    private ECurrency currency;
    private boolean isPaid;
    private String buyerName;
    private EPaymentMethod paymentMethod;
    private EPurchaseLocation purchaseLocation;

}
