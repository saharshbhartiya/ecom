package com.spring.ecom.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
@Entity
public class Profile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(nullable = false, name = "bio")
    private String bio;
    @Column(nullable = false, name = "phone_number")
    private String phoneNumber;
    @Column(nullable = false, name = "date_of_birth")
    private LocalDate dateOfBirth;
    @Column(nullable = false, name = "loyalty_point")
    private Integer loyaltyPoint;

    @OneToOne
    @JoinColumn(name = "id")
    @MapsId
    @ToString.Exclude
    private User user;
}
