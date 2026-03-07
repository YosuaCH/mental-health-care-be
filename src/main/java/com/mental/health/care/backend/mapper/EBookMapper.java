package com.mental.health.care.backend.mapper;
import org.springframework.stereotype.Component;

import com.mental.health.care.backend.dto.EBookDTO;
import com.mental.health.care.backend.dto.GoogleBooksResponseDTO;

@Component
public class EBookMapper {

    public EBookDTO toDto(GoogleBooksResponseDTO.Item item) {
        var info = item.getVolumeInfo();
        
        return EBookDTO.builder()
                .id(item.getId())
                .title(info.getTitle())
                .authors(info.getAuthors())
                .description(info.getDescription())
                .thumbnail(info.getImageLinks() != null ? info.getImageLinks().getThumbnail() : null)
                .previewLink(info.getPreviewLink())
                .pageCount(info.getPageCount())
                .categories(info.getCategories() != null ? String.join(", ", info.getCategories()) : "")
                .build();
    }
}