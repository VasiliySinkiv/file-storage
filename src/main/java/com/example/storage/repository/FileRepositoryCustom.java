package com.example.storage.repository;

import com.example.storage.entity.File;

import java.time.LocalDateTime;
import java.util.List;

public interface FileRepositoryCustom {

    List<File> findFiles(String name, LocalDateTime startDate, LocalDateTime endDate, List<String> types);
}
