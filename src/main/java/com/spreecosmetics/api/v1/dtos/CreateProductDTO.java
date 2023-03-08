package com.spreecosmetics.api.v1.dtos;


import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class CreateProductDTO {

    private String name;
    private String currency;
    private int price;
    private String manufacturer;
    private String manufacturedAt;
    private String expiresAt;

}
