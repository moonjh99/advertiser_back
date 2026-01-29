package com.example.advertiser.product.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Getter;

@Data
@Getter
public class ProductUpsertRequest {

    @NotBlank
    @Size(max=120)
    private String name;

    @Min(0)
    private int price;

    @NotBlank
    @Size(max=500)
    private String description;

}
