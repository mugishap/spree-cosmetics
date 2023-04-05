package com.spreecosmetics.api.v1.repositories;

import com.spreecosmetics.api.v1.models.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ICartRepository extends JpaRepository<Cart, UUID> {
}
