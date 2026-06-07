package io.github.amsatrio.spring_crud_demo.modules.hospital.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import io.github.amsatrio.spring_crud_demo.modules.hospital.entity.MCourier;

import java.util.Optional;

public interface MCourierRepository extends JpaRepository<MCourier, Long>, JpaSpecificationExecutor<MCourier> {
    Page<MCourier> findAllByIsDeleteTrue(Pageable pageable);

    Optional<MCourier> findByIdAndIsDeleteFalse(Long id);
}
