package com.vlada.lab3.services.rabbitMQ;

import com.vlada.lab3.configuration.RabbitMQConfig;
import com.vlada.lab3.dto.ProductDTO;
import com.vlada.lab3.entities.RabbitMQ.Product;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class WebScraperService {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void scrapeProducts() throws IOException {
        // Connect to the website and get the HTML document
        String url = "https://www.pandashop.md/ro/catalog/electronics/telephones/mobile/";

        rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());


        // Connect to the website and fetch the HTML
        Document document = Jsoup.connect(url).get();

        Elements productElements = document.select(".card.js-itemsList-item");

        for (Element product : productElements) {
            String name = product.select(".card-title.lnk-inner").text(); // Replace with actual CSS selector for name
            String price = product.select(".card-price_curr").text(); // Replace with actual CSS selector for price

            ProductDTO productDTO = new ProductDTO();
            productDTO.setName(name);
            productDTO.setPrice(price);

            // Publish the product to RabbitMQ
            rabbitTemplate.convertAndSend(RabbitMQConfig.QUEUE, productDTO);
            System.out.println("Published product: " + productDTO);
        }

    }
}
