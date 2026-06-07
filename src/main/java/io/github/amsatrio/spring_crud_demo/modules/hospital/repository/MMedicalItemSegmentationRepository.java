package io.github.amsatrio.spring_crud_demo.modules.hospital.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import io.github.amsatrio.spring_crud_demo.modules.hospital.entity.MMedicalItemSegmentation;

import java.util.Optional;

public interface MMedicalItemSegmentationRepository extends JpaRepository<MMedicalItemSegmentation, Long>, JpaSpecificationExecutor<MMedicalItemSegmentation> {
    Page<MMedicalItemSegmentation> findAllByIsDeleteTrue(Pageable pageable);

    Optional<MMedicalItemSegmentation> findByIdAndIsDeleteFalse(Long id);
}
