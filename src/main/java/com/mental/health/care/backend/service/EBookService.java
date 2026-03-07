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

    // Mengambil key dari application-secret.properties
    @Value("${google.books.api-key}")
    private String apiKey;

    private final String GOOGLE_BOOKS_URL = "https://www.googleapis.com/books/v1/volumes";

    public List<EBookDTO> getMentalHealthBooks(String query) {
        // q=subject:self-help+psychology adalah filter bagus untuk mental health
        // maxResults=12 untuk membatasi jumlah buku yang muncul
        String formattedQuery = query.replace(" ", "+");
        String url = String.format("%s?q=%s&maxResults=20&orderBy=relevance&key=%s", 
                GOOGLE_BOOKS_URL, formattedQuery, apiKey);

        try {
            // Menembak API Google dan menampung respon ke GoogleBooksResponseDTO
            GoogleBooksResponseDTO response = restTemplate.getForObject(url, GoogleBooksResponseDTO.class);

            if (response != null && response.getItems() != null) {
                // Mengubah List<Item> menjadi List<EBookDTO> menggunakan Mapper
                return response.getItems().stream()
                        .map(eBookMapper::toDto)
                        .collect(Collectors.toList());
            }
        } catch (Exception e) {
            // Log error jika terjadi masalah koneksi atau parsing
            System.err.println("Error fetching books: " + e.getMessage());
        }

        return Collections.emptyList();
    }
}