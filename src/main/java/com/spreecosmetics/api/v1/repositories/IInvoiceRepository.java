package com.spreecosmetics.api.v1.repositories;

import com.spreecosmetics.api.v1.models.Invoice;
import com.spreecosmetics.api.v1.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface IInvoiceRepository extends JpaRepository<Invoice, UUID> {

    @Query("SELECT i FROM Invoice i where i.buyer=:user")
    public Optional<List<Invoice>> getInvoicesByUser(User user);

}
