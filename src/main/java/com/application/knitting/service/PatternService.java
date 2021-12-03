package com.application.knitting.service;

import com.application.knitting.dto.PatternDto;
import com.application.knitting.exception.PatternNotFoundException;
import com.application.knitting.model.Pattern;
import com.application.knitting.repository.PatternRepository;
import com.itextpdf.text.DocumentException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
public class PatternService {
    private final PatternRepository patternRepository;
    private final PdfService pdfService;

    public void createPattern(PatternDto patternDto) {
        Pattern pattern = toEntity(patternDto);
        patternRepository.save(pattern);
    }

    public PatternDto getPattern(Long id) {
        Pattern pattern = patternRepository.findById(id)
                .orElseThrow(() -> new PatternNotFoundException(id.toString()));
        return toDto(pattern);
    }

    public ArrayList<PatternDto> getPatternList()  {
        return (ArrayList<PatternDto>) patternRepository.findAll()
                .stream()
                .map(PatternService::toDto)
                .collect(Collectors.toList());
    }

    public Map<String, Boolean> deletePattern(Long id) {
        Pattern pattern = patternRepository.findById(id).orElseThrow(
                () -> new PatternNotFoundException("Pattern with id: " + id + " was not found.")
        );
        patternRepository.delete(pattern);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return response;
    }

    public void createPDF(long id) throws DocumentException, FileNotFoundException {
        Pattern pattern = patternRepository.findById(id).orElseThrow(
                ()-> new PatternNotFoundException("Pattern not found with id : " + id)
        );
        pdfService.makeDocument(pattern.getName() + "_pdf_pattern",
                pattern.getName(),
                pattern.getDescription(),
                pattern.getNumberOfStitches(),
                pattern.getNumberOfRows(),
                pattern.getInstructions(),
                pattern.getMaterialList(),
                pattern.getYarn()
        );
    }

    private static Pattern toEntity(PatternDto patternDto) {
        return Pattern.builder()
                .name(patternDto.getName())
                .numberOfRows(patternDto.getNumberOfRows())
                .numberOfStitches(patternDto.getNumberOfStitches())
                .yarn(patternDto.getYarn())
                .description(patternDto.getDescription())
                .instructions(patternDto.getInstructions())
                .created(Instant.now())
                .build();
    }

    private static PatternDto toDto(Pattern pattern) {
        return PatternDto.builder()
                .name(pattern.getName())
                .numberOfRows(pattern.getNumberOfRows())
                .numberOfStitches(pattern.getNumberOfStitches())
                .yarn(pattern.getYarn())
                .description(pattern.getDescription())
                .instructions(pattern.getInstructions())
                .created(pattern.getCreated())
                .build();
    }
}
