package com.mental.health.care.backend.service;

import com.mental.health.care.backend.dto.EBookDTO;
import com.mental.health.care.backend.dto.GoogleBooksResponseDTO;
import com.mental.health.care.backend.mapper.EBookMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EBookService {

    private final RestTemplate restTemplate;
    private final EBookMapper eBookMapper;

    @Value("${google.books.api-key}")
    private String apiKey;

    private final String GOOGLE_BOOKS_URL = "https://www.googleapis.com/books/v1/volumes";

    public List<EBookDTO> getMentalHealthBooks(String query) {
        String formattedQuery = query.replace(" ", "+");
        String url = String.format("%s?q=%s&maxResults=20&filter=partial&langRestrict=id&printType=books&key=%s", 
                GOOGLE_BOOKS_URL, formattedQuery, apiKey);

        try {
            GoogleBooksResponseDTO response = restTemplate.getForObject(url, GoogleBooksResponseDTO.class);

            if (response != null && response.getItems() != null) {
                return response.getItems().stream()
                        .map(item -> {
                            EBookDTO dto = eBookMapper.toDto(item);
                        
                            if (dto.getThumbnail() != null) {
                                String highResImg = dto.getThumbnail()
                                    .replace("http://", "https://")
                                    .replace("zoom=1", "zoom=2");
                                dto.setThumbnail(highResImg);
                            }
                            
                            return dto;
                        })
                        .collect(Collectors.toList());
            }
        } catch (Exception e) {
            System.err.println("Error fetching books: " + e.getMessage());
        }

        return Collections.emptyList();
    }
}