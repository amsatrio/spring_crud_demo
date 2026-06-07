package io.github.amsatrio.spring_crud_demo.modules.hospital.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import io.github.amsatrio.spring_crud_demo.modules.hospital.entity.MLocationLevel;

import java.util.Optional;

public interface MLocationLevelRepository extends JpaRepository<MLocationLevel, Long>, JpaSpecificationExecutor<MLocationLevel> {
    Page<MLocationLevel> findAllByIsDeleteTrue(Pageable pageable);

    Optional<MLocationLevel> findByIdAndIsDeleteFalse(Long id);
}
