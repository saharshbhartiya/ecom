package com.spring.ecom.dtos;

import com.spring.ecom.entities.User;

import java.util.List;
import java.util.stream.Collectors;

public class UserMapper {
    public static UserDTO mapToDTO(User user){
        List<AddressDTO> addressDTOs = user.getAddresses()
                .stream()
                .map(address -> {
                    return AddressDTO.builder()
                            .id(address.getId())
                            .street(address.getStreet())
                            .city(address.getCity())
                            .state(address.getState())
                            .zip(address.getZip())
                            .build();
                })
                .toList();


        return UserDTO.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .addresses(addressDTOs)
                .build();
    }
}
