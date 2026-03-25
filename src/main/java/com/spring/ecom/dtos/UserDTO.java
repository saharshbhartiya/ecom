package com.spring.ecom.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class UserDTO {
    private Long id;
    private String name;
    private String email;

    //private List<AddressDTO> addresses;
}
