package com.vlada.lab3.services.rabbitMQ;


import com.vlada.lab3.dto.ProductDTO;
import com.vlada.lab3.entities.RabbitMQ.Product;
import com.vlada.lab3.repository.rabbitMQ.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional
    public void saveProduct(ProductDTO productDTO){
        Product product = new Product();
        product.setName(productDTO.getName());
        product.setPrice(productDTO.getPrice());
        productRepository.save(product);
    }

}
