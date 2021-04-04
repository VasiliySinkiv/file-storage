package com.example.storage.repository;

import com.example.storage.entity.File;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository extends JpaRepository<File, Integer>, FileRepositoryCustom {
}
