package com.spring.ecom.services;

import com.spring.ecom.entities.Address;
import com.spring.ecom.entities.Product;
import com.spring.ecom.entities.User;
import com.spring.ecom.repositories.CategoryRepository;
import com.spring.ecom.repositories.ProductRepository;
import com.spring.ecom.repositories.ProfileRepository;
import com.spring.ecom.repositories.UserRepository;
import com.spring.ecom.repositories.specifications.ProductSpecification;
import jakarta.persistence.EntityManager;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final EntityManager entityManager;
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProfileRepository profileRepository;
    @Transactional
    public void showEntityStates(){
        var user = User.builder()
                .name("Doe")
                .email("doe@example.com")
                .password("password")
                .build();

        if(entityManager.contains(user)){
            System.out.println("Persitent");
        } else{
            System.out.println("Transient / Detached");
        }

        userRepository.save(user);

        if(entityManager.contains(user)){
            System.out.println("Persitent");
        } else{
            System.out.println("Transient / Detached");
        }
    }

    public void persistRelated(){
        var user = User.builder()
                .name("J")
                .email("j@J.j")
                .password("password")
                .build();
        var address = Address.builder()
                .zip("sads")
                .state("sad")
                .city("sa")
                .street("32")
                .build();
        user.addAddress(address);
        userRepository.save(user);
    }

    @Transactional
    public void deleteRelated(){
        var user = userRepository.findById(3L).orElseThrow();
        var address = user.getAddresses().get(0);
        user.removeAddress(address);
        userRepository.save(user);
    }

    @Transactional
    public void manageProduct(){
        productRepository.deleteById(4L);
    }

    @Transactional
    public void updateProductPrices(){
        productRepository.updatePriceByCategory(BigDecimal.valueOf(10) , (byte) 1);
    }

    @Transactional
    public void fetchProducts(){
        var product = new Product();
        product.setName("product");
        var matcher = ExampleMatcher.matching()
                .withIncludeNullValues()
                .withIgnorePaths("id" , "description")
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
        var example = Example.of(product , matcher);
        var products = productRepository.findAll(example);
        products.forEach(System.out::println);
    }

    public void fetchProductsByCriteria(){
        var products = productRepository.findProductsByCriteria(null , BigDecimal.valueOf(1) , BigDecimal.valueOf(11));
        products.forEach(System.out::println);
    }

    public void fetchProductsBySpecification(String name , BigDecimal minPrice , BigDecimal maxPrice){
        Specification<Product> spec = ProductSpecification.fetchCategory();
        if(name != null){
            spec.and(ProductSpecification.hasName(name));
        }if(minPrice != null){
            spec.and(ProductSpecification.hasPriceGreaterThanEqualTo(minPrice));
        }if(maxPrice != null){
            spec.and(ProductSpecification.hasPriceLessThanEqualTo(maxPrice));
        }

        productRepository.findAll(spec).forEach(System.out::println);
    }

    public void fetchSortedProducts(){
        var sort = Sort.by("name").and(
                Sort.by("price").descending()
        );

        productRepository.findAll(sort).forEach(System.out::println);
    }

    public void fetchPaginatedProducts(int page , int size){
        PageRequest pageRequest = PageRequest.of(page , size);
        productRepository.findAll(pageRequest).forEach(System.out::println);
    }

    @Transactional
    public void fetchUsers(){
        var users = userRepository.findAllWithAddresses();
        users.forEach(u ->{
            System.out.println(u);
            u.getAddresses().forEach(System.out::println);
        });
    }

    @Transactional
    public void fetchProfile(Integer minValue){
        var profiles = userRepository.findLoyaltyUsers(minValue);
        profiles.forEach(p -> {
            System.out.println("id = " + p.getId() + " email = " + p.getEmail());
        });
    }
}
