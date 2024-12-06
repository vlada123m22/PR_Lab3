package com.vlada.lab3.services.rabbitMQ;

import com.vlada.lab3.configuration.RabbitMQConfig;
import com.vlada.lab3.dto.ProductDTO;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ConsumerService {

    private final RestTemplate restTemplate = new RestTemplate();

    @RabbitListener(queues = RabbitMQConfig.QUEUE)
    public void consumeProduct(ProductDTO productDTO) {
        System.out.println("Consumed: " + productDTO);

        // Send the data to the web server
        String webServerUrl = "http://localhost:8080/receive-data";
        ResponseEntity<String> response = restTemplate.postForEntity(webServerUrl, productDTO, String.class);

        System.out.println("Response from Web Server: " + response.getBody());
    }
}
