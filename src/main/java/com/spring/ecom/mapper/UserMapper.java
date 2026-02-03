package com.spring.ecom.mapper;

import com.spring.ecom.dtos.AddressDTO;
import com.spring.ecom.dtos.RegisterUserRequest;
import com.spring.ecom.dtos.UserDTO;
import com.spring.ecom.dtos.UserUpdateDTO;
import com.spring.ecom.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toEntity(RegisterUserRequest request);
    UserDTO toDto(User user);
    void update(UserUpdateDTO request , @MappingTarget User user);
}
