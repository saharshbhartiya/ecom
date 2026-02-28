package com.spring.ecom.repositories;

import com.spring.ecom.entities.Profile;
import org.springframework.data.repository.CrudRepository;

public interface ProfileRepository extends CrudRepository<Profile , Long> {
}
