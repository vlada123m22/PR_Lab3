package com.vlada.lab3.repository.rabbitMQ;

import com.vlada.lab3.entities.RabbitMQ.Product;
import org.springframework.data.repository.CrudRepository;

public interface ProductRepository extends CrudRepository<Product, Long> {
}
