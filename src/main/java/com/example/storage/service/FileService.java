package com.example.storage.service;

import com.example.storage.entity.File;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public interface FileService {

    File download(int id);

    void save(MultipartFile multipartFile) throws IOException;

    void update(MultipartFile multipartFile, int id) throws IOException;

    void delete(int id);

    List<String> getAllFileNames();

    File getFile(int id);

    List<File> getAllFiles(String name, LocalDate startDate, LocalDate endDate, List<String> types);

    List<File> downloadSomeFiles(List<Integer> listId);

}
