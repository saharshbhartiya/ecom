package com.spring.ecom.repositories;

import com.spring.ecom.entities.Address;
import org.springframework.data.repository.CrudRepository;

public interface AddressRepository extends CrudRepository<Address, Long> {
}
