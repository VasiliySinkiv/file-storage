package com.example.storage.controller;

import com.example.storage.entity.File;
import com.example.storage.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import javax.validation.constraints.Size;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.zip.*;

@RestController
@RequestMapping("/storage")
public class FileController {

    @Autowired
    private FileService fileService;

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ResponseEntity<Void> upload(@RequestParam("file") MultipartFile multipartFile) throws IOException {
        fileService.save(multipartFile);
        ResponseEntity.BodyBuilder responseEntity = ResponseEntity.status(201);
        return responseEntity.build();
    }

    @GetMapping("/download/{id}")
    ResponseEntity<Resource> download(@PathVariable int id) {
        File file = fileService.download(id);
        Resource resource = new ByteArrayResource(file.getMediaData().getContent());
        return ResponseEntity.status(200)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;fileName=" + file.getName())
                .body(resource);
    }

    @GetMapping("/download")
    ResponseEntity<StreamingResponseBody> download(@RequestParam("listId") List<Integer> listId) {

        List<File> fileList = fileService.downloadSomeFiles(listId);

        return ResponseEntity.status(200)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;fileName=\"archive.zip\"")
                .body(out -> {
                    ZipOutputStream zipOutputStream = new ZipOutputStream(out);
                    for (File file : fileList) {
                        ZipEntry entry1 = new ZipEntry(file.getName());
                        zipOutputStream.putNextEntry(entry1);
                        zipOutputStream.write(file.getMediaData().getContent());
                        zipOutputStream.closeEntry();
                    }
                    zipOutputStream.close();
                });

    }

    @PutMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ResponseEntity<Void> save(@RequestParam("multipartFile") MultipartFile multipartFile,
                              @RequestParam("id") int id) throws IOException {
        fileService.update(multipartFile, id);
        return ResponseEntity.status(200).build();
    }

    @DeleteMapping(value = "/delete/{id}")
    ResponseEntity<Void> delete(@PathVariable int id) {
        fileService.delete(id);
        return ResponseEntity.status(200).build();
    }

    @GetMapping(value = "/file/{id}")
    ResponseEntity<File> getFile(@PathVariable int id) {
        File file = fileService.getFile(id);
        return ResponseEntity.status(200).body(file);
    }

    @GetMapping(value = "/files/filter")
    ResponseEntity<List<File>> getAllFiles(@RequestParam(name = "name", required = false) String name,
                                            @RequestParam(name = "startDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                            @RequestParam(name = "endDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
                                            @RequestParam(name = "types", required = false) List<String> types) {
        List<File> allEntity = fileService.getAllFiles(name, startDate, endDate, types);
        return ResponseEntity.status(200).body(allEntity);
    }

    @GetMapping(value = "/names")
    ResponseEntity<List<String>> getAllFileNames() {
        List<String> names = fileService.getAllFileNames();
        return ResponseEntity.status(200).body(names);
    }
}
