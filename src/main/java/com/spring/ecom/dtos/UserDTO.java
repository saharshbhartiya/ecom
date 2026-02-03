package com.spring.ecom.dtos;

import com.spring.ecom.entities.Address;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
@AllArgsConstructor
public class UserDTO {
    private Long id;
    private String name;
    private String email;

    //private List<AddressDTO> addresses;
}
