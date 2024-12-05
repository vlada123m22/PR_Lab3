package com.example.webscraper;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class WebScraperService {

    public List<String> scrapeTitles() throws IOException {
        // Connect to the website and get the HTML document
        String url = "https://example.com";  // Replace with the actual URL
        Document document = Jsoup.connect(url).get();

        // Create a list to store the titles
        List<String> titles = new ArrayList<>();

        // Select elements using CSS selectors (modify according to the structure of the website)
        for (Element element : document.select("h2.title")) {  // Adjust the selector to match the titles
            titles.add(element.text());
        }

        return titles;
    }
}
