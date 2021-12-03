package com.application.knitting.controller;

import com.application.knitting.dto.PatternDto;
import com.application.knitting.service.PatternService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/v1/patterns/")
@AllArgsConstructor
public class PatternController {
    private final PatternService patternService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createPattern(@RequestBody PatternDto patternDto) {
        patternService.createPattern(patternDto);
    }

    @GetMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public PatternDto getPatternById(@PathVariable long id) {
        return patternService.getPattern(id);
    }

    @GetMapping
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public List<PatternDto> findAllPatterns() {
        return patternService.getPatternList();
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public Map<String, Boolean> deletePattern(@PathVariable(value = "id") long id) {
        return patternService.deletePattern(id);
    }
}
