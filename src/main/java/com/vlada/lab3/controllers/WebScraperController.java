package com.vlada.lab3.controllers;
import com.vlada.lab3.dto.ProductDTO;
import com.vlada.lab3.entities.RabbitMQ.Product;
import com.vlada.lab3.repository.rabbitMQ.ProductRepository;
import com.vlada.lab3.services.rabbitMQ.ProductService;
import com.vlada.lab3.services.rabbitMQ.WebScraperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
public class WebScraperController {
    private final ProductService productService;
    private final WebScraperService webScraperService;

    public WebScraperController(ProductService productService, WebScraperService webScraperService) {
        this.productService = productService;
        this.webScraperService = webScraperService;
    }

    @GetMapping("/scrape-titles")
    public void getScrapedTitles() throws IOException {
        webScraperService.scrapeProducts();
    }


    @PostMapping("/receive-data")
    public ResponseEntity<String> receiveData(@RequestBody ProductDTO productDTO) {
        productService.saveProduct(productDTO);
        System.out.println("Received Data: " + productDTO);
        return ResponseEntity.ok("Data received successfully.");
    }

}
