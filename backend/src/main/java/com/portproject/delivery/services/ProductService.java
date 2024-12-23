package com.portproject.delivery.services;

import com.portproject.delivery.dtos.ProductDTO;
import com.portproject.delivery.entities.Product;
import com.portproject.delivery.repositories.ProductRepository;
import com.portproject.delivery.services.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Transactional(readOnly = true)
    public List<ProductDTO> findAllByOrderByNameAsc() {
        List<Product> list = productRepository.findAllByOrderByNameAsc();

        if (list.isEmpty()) {
            throw new ResourceNotFoundException("Nenhum produto encontrado");
        }

        return list.stream().map(x -> new ProductDTO(x)).collect(Collectors.toList());
    }
}
