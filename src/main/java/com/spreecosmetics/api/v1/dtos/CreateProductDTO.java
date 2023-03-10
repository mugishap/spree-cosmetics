package com.spreecosmetics.api.v1.dtos;


import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Size;
import java.util.List;

@Getter
@Setter
@Data
public class CreateProductDTO {

    @NotNull
    @Size(min = 3, max = 70)
    private String name;

    @NotNull
    private String currency;

    @NotNull
    private int price;

    private String manufacturer;

    private String manufacturedAt;

    private String expiresAt;

}
