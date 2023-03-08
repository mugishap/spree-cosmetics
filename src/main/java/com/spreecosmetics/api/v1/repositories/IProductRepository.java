package com.spreecosmetics.api.v1.repositories;

import com.spreecosmetics.api.v1.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface IProductRepository extends JpaRepository<Product, UUID> {

    @Query("SELECT p FROM Product p"+
            " WHERE p.name LIKE ('%'|| lower(:query)||'%')" +
            "OR p.manufacturer  LIKE ('%'|| lower(:query)||'%')"
    )
    public Product searchProductByName(String query);

}
