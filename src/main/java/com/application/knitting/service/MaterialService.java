package com.application.knitting.service;

import com.application.knitting.dto.MaterialDto;
import com.application.knitting.dto.PhotoDto;
import com.application.knitting.exception.MaterialNotFoundException;
import com.application.knitting.model.Material;
import com.application.knitting.model.Photo;
import com.application.knitting.repository.MaterialRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class MaterialService {
    private final MaterialRepository materialRepository;

    public void createMaterial(MaterialDto materialDto) {
        Material material = toEntity(materialDto);
        materialRepository.save(material);
    }

    public MaterialDto getMaterial(Long id) {
        Material material = materialRepository.findById(id)
                .orElseThrow(() -> new MaterialNotFoundException(id.toString()));
        return toDto(material);
    }

    public ArrayList<MaterialDto> getMaterialList()  {
        return (ArrayList<MaterialDto>) materialRepository.findAll()
                .stream()
                .map(com.application.knitting.service.MaterialService::toDto)
                .collect(Collectors.toList());
    }

    public Map<String, Boolean> deleteMaterial(Long id) {
        Material material = materialRepository.findById(id).orElseThrow(
                () -> new MaterialNotFoundException("Material with id: " + id + " was not found.")
        );
        materialRepository.delete(material);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return response;
    }

    public List<MaterialDto> mapListToListDto(List<Material> materialList) {
        return materialList
                .stream()
                .map(MaterialService::toDto)
                .collect(Collectors.toList());
    }

    public List<Material> mapListDtoToList(List<MaterialDto> materialDtoList) {
        return materialDtoList
                .stream()
                .map(MaterialService::toEntity)
                .collect(Collectors.toList());
    }


    private static Material toEntity(MaterialDto materialDto) {
        return Material.builder()
                .name(materialDto.getName())
                .build();
    }

    private static MaterialDto toDto(Material material) {
        return MaterialDto.builder()
                .name(material.getName())
                .build();
    }
}
