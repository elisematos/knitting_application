package com.application.knitting.service;

import com.application.knitting.dto.PhotoDto;
import com.application.knitting.model.Photo;
import com.application.knitting.repository.PhotoRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class PhotoService {

    private final PhotoRepository photoRepository;

    public void save(PhotoDto photoDto) {
        photoRepository.save(toEntity(photoDto));
    }

    public List<Photo> mapListDtoToList(List<PhotoDto> photoDtos) {
        return photoDtos
                .stream()
                .map(PhotoService::toEntity)
                .collect(Collectors.toList());
    }

    public List<PhotoDto> mapListToListDto(List<Photo> photoList) {
        return photoList
                .stream()
                .map(PhotoService::toDto)
                .collect(Collectors.toList());
    }

    private static Photo toEntity(PhotoDto photoDto) {
        return Photo.builder()
                .name(photoDto.getName())
                .build();
    }

    private static PhotoDto toDto(Photo photo) {
        return PhotoDto.builder()
                .name(photo.getName())
                .build();
    }
}
