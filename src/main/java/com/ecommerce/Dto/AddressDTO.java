package com.ecommerce.Dto;


import lombok.Data;

@Data
public class AddressDTO {
    private Integer id;

    private Integer houseNumber;

    private Integer pincode;

    private String landmark;

    private String city;

    private Integer userid;
}
