package com.production.backend.repository;

import com.production.backend.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("""
        SELECT DISTINCT p
        FROM Product p
        LEFT JOIN FETCH p.compositions c
        LEFT JOIN FETCH c.resource
    """)
    List<Product> findAllWithResources();

    @Query("""
        SELECT p
        FROM Product p
        LEFT JOIN FETCH p.compositions c
        LEFT JOIN FETCH c.resource
        WHERE p.code = :code
    """)
    Optional<Product> findByIdWithResources(Long code);
}
