package io.github.amsatrio.spring_crud_demo.modules.hospital.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import io.github.amsatrio.spring_crud_demo.modules.hospital.entity.MEducationLevel;

import java.util.Optional;

public interface MEducationLevelRepository extends JpaRepository<MEducationLevel, Long>, JpaSpecificationExecutor<MEducationLevel> {
    Page<MEducationLevel> findAllByIsDeleteTrue(Pageable pageable);

    Optional<MEducationLevel> findByIdAndIsDeleteFalse(Long id);
}
