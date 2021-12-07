package com.application.knitting.controller;

import com.application.knitting.dto.PhotoDto;
import com.application.knitting.message.ResponseMessage;
import com.application.knitting.model.PhotoInfo;
import com.application.knitting.service.PhotoService;
import com.application.knitting.service.PhotoStorageService;
import lombok.AllArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/v1/patterns/")
@AllArgsConstructor
public class PhotoController {
    PhotoStorageService storageService;
    PhotoService photoService;

    @PostMapping("{idPattern}/photos")
    public ResponseEntity<ResponseMessage> uploadPhotos(@PathVariable("idPattern") long idPattern,
                                                        @RequestParam("files") MultipartFile[] files) {
        try {
            List<String> photoNames = new ArrayList<>();
            Arrays.stream(files).forEach(file -> {
                storageService.save(file);
                photoNames.add(file.getOriginalFilename());
                PhotoDto photoDto = PhotoDto.builder()
                        .name(file.getOriginalFilename())
                        .build();
                photoService.save(photoDto);
            });
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ResponseMessage("Uploaded the files successfully: " + photoNames));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage("Fail to upload files!"));
        }
    }

    @GetMapping("{idPattern}/photos")
    public ResponseEntity<List<PhotoInfo>> getPhotoList(@PathVariable("idPattern") long idPattern) {
        List<PhotoInfo> photos = storageService.loadAll().map(path -> {
            String filename = path.getFileName().toString();
            String url = MvcUriComponentsBuilder
                    .fromMethodName(PhotoController.class, "getFile", path.getFileName().toString()).build().toString();
            return new PhotoInfo(filename, url);
        }).collect(Collectors.toList());
        return photos.isEmpty()
                ? ResponseEntity.status(HttpStatus.NO_CONTENT).build()
                : ResponseEntity.ok(photos);
    }

    @GetMapping("{idPattern}/photos/{filename}")
    public ResponseEntity<Resource> getPhoto(@PathVariable("idPattern") long idPattern,
                                             @PathVariable String filename) {
        Resource file = storageService.load(filename);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }
}
