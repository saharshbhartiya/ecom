package com.spring.ecom.repositories;

import com.spring.ecom.dtos.UserSummary;
import com.spring.ecom.entities.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    @EntityGraph(attributePaths = {"tags" , "addresses"})
    Optional<User> findByEmail(String email);

    @EntityGraph(attributePaths = "addresses")
    @Query("select u from User u")
    List<User> findAllWithAddresses();

    @Query("select u.id as id, u.email as email from User u where u.profile.loyaltyPoint > :loyaltyPoint order by u.email desc")
    @EntityGraph(attributePaths = "email")
    List<UserSummary> findLoyaltyUsers(@Param("loyaltyPoint") Integer LoyaltyPoint);
}
