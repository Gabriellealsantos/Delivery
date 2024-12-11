package com.portproject.delivery.repositories;

import com.portproject.delivery.entities.Order;
import com.portproject.delivery.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    
}
