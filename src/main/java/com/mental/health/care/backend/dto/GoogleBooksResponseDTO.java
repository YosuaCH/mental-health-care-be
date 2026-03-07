package com.mental.health.care.backend.dto;

import lombok.Data;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class GoogleBooksResponseDTO {
    private List<Item> items;

    @Data
    public static class Item {
        private String id;
        private VolumeInfo volumeInfo;
    }

    @Data
    public static class VolumeInfo {
        private String title;
        private List<String> authors;
        private String description;
        private ImageLinks imageLinks;
        private String previewLink;
        private Integer pageCount;
        private List<String> categories;
    }

    @Data
    public static class ImageLinks {
        private String thumbnail;
    }
}