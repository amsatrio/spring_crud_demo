package io.github.amsatrio.spring_crud_demo.modules.hospital.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import io.github.amsatrio.spring_crud_demo.modules.hospital.entity.MCourierType;

import java.util.Optional;

public interface MCourierTypeRepository extends JpaRepository<MCourierType, Long>, JpaSpecificationExecutor<MCourierType> {
    Page<MCourierType> findAllByIsDeleteTrue(Pageable pageable);

    Optional<MCourierType> findByIdAndIsDeleteFalse(Long id);
}
