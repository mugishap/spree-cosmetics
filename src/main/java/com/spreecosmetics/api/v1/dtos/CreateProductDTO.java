package com.spreecosmetics.api.v1.dtos;


import com.spreecosmetics.api.v1.enums.ECurrency;
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
    private ECurrency currency;

    @NotNull
    private int price;

    private String manufacturer;

    private String manufacturedAt;

    private String expiresAt;

}
