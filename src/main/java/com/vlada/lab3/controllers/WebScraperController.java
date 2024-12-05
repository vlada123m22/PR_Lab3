package com.vlada.lab3.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
public class WebScraperController {

    @Autowired
    private com.example.webscraper.WebScraperService webScraperService;

    @GetMapping("/scrape-titles")
    public List<String> getScrapedTitles() {
        try {
            return webScraperService.scrapeTitles();
        } catch (IOException e) {
            e.printStackTrace();
            return List.of("Error scraping the website");
        }
    }
}
