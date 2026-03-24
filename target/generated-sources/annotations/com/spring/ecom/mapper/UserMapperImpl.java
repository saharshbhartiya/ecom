package com.spring.ecom.mapper;

import com.spring.ecom.dtos.RegisterUserRequest;
import com.spring.ecom.dtos.UserDTO;
import com.spring.ecom.dtos.UserUpdateDTO;
import com.spring.ecom.entities.User;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-03-24T16:30:12+0530",
    comments = "version: 1.6.3, compiler: Eclipse JDT (IDE) 3.45.0.v20260224-0835, environment: Java 21.0.10 (Eclipse Adoptium)"
)
@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public User toEntity(RegisterUserRequest request) {
        if ( request == null ) {
            return null;
        }

        User.UserBuilder user = User.builder();

        user.email( request.getEmail() );
        user.name( request.getName() );
        user.password( request.getPassword() );

        return user.build();
    }

    @Override
    public UserDTO toDto(User user) {
        if ( user == null ) {
            return null;
        }

        UserDTO.UserDTOBuilder userDTO = UserDTO.builder();

        userDTO.email( user.getEmail() );
        userDTO.id( user.getId() );
        userDTO.name( user.getName() );

        return userDTO.build();
    }

    @Override
    public void update(UserUpdateDTO request, User user) {
        if ( request == null ) {
            return;
        }

        user.setEmail( request.getEmail() );
        user.setName( request.getName() );
    }
}
