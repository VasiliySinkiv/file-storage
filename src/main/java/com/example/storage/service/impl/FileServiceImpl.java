package com.example.storage.service.impl;

import com.example.storage.entity.File;
import com.example.storage.entity.MediaData;
import com.example.storage.repository.FileRepository;
import com.example.storage.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class FileServiceImpl implements FileService {

    private static final String PATH = "http://localhost:8080/storage/download/";
    private static final String ALL_BEFORE_POINT = "^.*\\.";

    @Autowired
    private FileRepository fileRepository;

    @Override
    @Transactional
    public void save(MultipartFile multipartFile) throws IOException {
        File sourceFile = new File().setName(multipartFile.getOriginalFilename())
                .setSize(multipartFile.getSize())
                .setType(Objects.requireNonNull(multipartFile.getOriginalFilename()).replaceFirst(ALL_BEFORE_POINT, ""))
                .setDateUploading(LocalDateTime.now())
                .setDateModification(LocalDateTime.now())
                .setMediaData(new MediaData().setContent(multipartFile.getBytes()));
        File targetFile = fileRepository.save(sourceFile);
        targetFile.setLink(PATH + targetFile.getId());
        fileRepository.save(targetFile);
    }

    @Override
    @Transactional
    public void update(MultipartFile multipartFile, int id) throws IOException {
        File file = fileRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(String.format("File with id '%s' not found.", id)));
        file.setName(multipartFile.getOriginalFilename())
                .setSize(multipartFile.getSize())
                .setType(Objects.requireNonNull(multipartFile.getOriginalFilename()).replaceFirst(ALL_BEFORE_POINT, ""))
                .setDateModification(LocalDateTime.now())
                .getMediaData().setContent(multipartFile.getBytes());
        fileRepository.save(file);
    }

    @Override
    public File download(int id) {
        return fileRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(String.format("File with id '%s' not found.", id)));
    }

    @Override
    public List<File> downloadSomeFiles(List<Integer> listId) {
        return fileRepository.findAllById(listId);
    }

    @Override
    public void delete(int id) {
        File file = fileRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(String.format("File with id '%s' not found.", id)));
        fileRepository.delete(file);
    }

    @Override
    public List<String> getAllFileNames() {
        return fileRepository.findAll().stream().map(File::getName).collect(Collectors.toList());
    }

    @Override
    public File getFile(int id) {
        return fileRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(String.format("File with id '%s' not found.", id)));
    }

    @Override
    public List<File> getAllFiles(String name, LocalDate startDate, LocalDate endDate, List<String> types) {
        LocalDateTime startLocalDate = startDate != null ? startDate.atTime(LocalTime.MIN) : LocalDateTime.MIN;
        LocalDateTime endLocalDate = endDate != null ? endDate.atTime(LocalTime.MIN) : LocalDateTime.now();
        return fileRepository.findFiles(name, startLocalDate, endLocalDate, types);
    }
}
