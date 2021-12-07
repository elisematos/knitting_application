package com.application.knitting.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PatternDto {
    private String name;
    private Integer numberOfRows;
    private Integer numberOfStitches;
    private String yarn;
    private String description;
    private String instructions;
    private Instant created;
    private List<MaterialDto> materialDtoList;
    private List<PhotoDto> photoDtoList;
}
