package com.spring.ecom.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class AddressDTO {
    private Long id;
    private String street;
    private String city;
    private String zip;
    private String state;
}

