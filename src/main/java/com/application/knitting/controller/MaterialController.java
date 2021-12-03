package com.application.knitting.controller;

import com.application.knitting.dto.MaterialDto;
import com.application.knitting.service.MaterialService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/v1/materials/")
@AllArgsConstructor
public class MaterialController {
    private final MaterialService materialService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createMaterial(@RequestBody MaterialDto materialDto) {
        materialService.createMaterial(materialDto);
    }

    @GetMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public MaterialDto getMaterialById(@PathVariable Long id) { return materialService.getMaterial(id); }

    @GetMapping
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public List<MaterialDto> findAllMaterials() {
        return materialService.getMaterialList();
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public Map<String, Boolean> deleteMaterial(@PathVariable(value = "id") long id) {
        return materialService.deleteMaterial(id);
    }
}
