package com.application.knitting.service;

import com.application.knitting.dto.PatternDto;
import com.application.knitting.exception.PatternNotFoundException;
import com.application.knitting.model.Pattern;
import com.application.knitting.repository.PatternRepository;
import com.itextpdf.text.DocumentException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

import static com.application.knitting.service.PdfService.PDF_DIRECTORY;

@Service
@Slf4j
@AllArgsConstructor
public class PatternService {
    private final PatternRepository patternRepository;
    private final PdfService pdfService;
    private final PhotoService photoService;
    private final MaterialService materialService;

    public void createPattern(PatternDto patternDto) {
        patternRepository.save(toEntity(patternDto));
    }

    public PatternDto getPattern(long id) {
        Pattern pattern = patternRepository.findById(id)
                .orElseThrow(() -> new PatternNotFoundException("Le patron "+ id + "est introuvable."));
        return toDto(pattern);
    }

    public List<PatternDto> getPatternList()  {
        return  patternRepository.findAll()
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public Map<String, Boolean> deletePattern(Long id) {
        Pattern pattern = getPatternEntity(id);
        patternRepository.delete(pattern);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return response;
    }

    public void createPDF(long id) throws DocumentException, FileNotFoundException {
        Pattern pattern = getPatternEntity(id);
        pdfService.makeDocument(pattern.getName() + ".pdf",
                pattern.getName(),
                pattern.getDescription(),
                pattern.getNumberOfStitches(),
                pattern.getNumberOfRows(),
                pattern.getInstructions(),
                pattern.getMaterialList(),
                pattern.getYarn()
        );
    }

    public ResponseEntity<InputStreamResource> getPDF(long id) throws FileNotFoundException, PatternNotFoundException {
        Pattern pattern = getPatternEntity(id);
        String filename = PDF_DIRECTORY + pattern.getName() + ".pdf";
        File file = new File(filename);
        InputStreamResource resource = new InputStreamResource(new FileInputStream(file));

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", String.format("attachment; filename=\"%s\"", file.getName()));
        headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
        headers.add("Expires", "0");
        return ResponseEntity.ok().headers(headers).headers(headers)
                .contentLength(file.length())
                .contentType(MediaType.parseMediaType("application/pdf")).body(resource);
    }

    private Pattern toEntity(PatternDto patternDto) {
        return Pattern.builder()
                .name(patternDto.getName())
                .numberOfRows(patternDto.getNumberOfRows())
                .numberOfStitches(patternDto.getNumberOfStitches())
                .yarn(patternDto.getYarn())
                .description(patternDto.getDescription())
                .instructions(patternDto.getInstructions())
                .photoList(photoService.mapListDtoToList(patternDto.getPhotoDtoList()))
                .materialList(materialService.mapListDtoToList(patternDto.getMaterialDtoList()))
                .created(Instant.now())
                .build();
    }

    private PatternDto toDto(Pattern pattern) {
        return PatternDto.builder()
                .name(pattern.getName())
                .numberOfRows(pattern.getNumberOfRows())
                .numberOfStitches(pattern.getNumberOfStitches())
                .yarn(pattern.getYarn())
                .description(pattern.getDescription())
                .instructions(pattern.getInstructions())
                .photoDtoList(photoService.mapListToListDto(pattern.getPhotoList()))
                .materialDtoList(materialService.mapListToListDto(pattern.getMaterialList()))
                .created(pattern.getCreated())
                .build();
    }

    private Pattern getPatternEntity(long id) {
        return patternRepository.findById(id).orElseThrow(
                ()-> new PatternNotFoundException("Pattern not found with id : " + id)
        );
    }
}
