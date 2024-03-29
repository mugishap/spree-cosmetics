package com.spreecosmetics.api.v1.repositories;

import com.spreecosmetics.api.v1.enums.EFileStatus;
import com.spreecosmetics.api.v1.fileHandling.File;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface IFileRepository extends JpaRepository<File, UUID> {
    Page<File> findAllByStatus(Pageable pageable, EFileStatus status);

}
