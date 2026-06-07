package io.github.amsatrio.spring_crud_demo.modules.hospital.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import io.github.amsatrio.spring_crud_demo.modules.hospital.entity.MMedicalItem;

import java.util.Optional;

public interface MMedicalItemRepository extends JpaRepository<MMedicalItem, Long>, JpaSpecificationExecutor<MMedicalItem> {
    Page<MMedicalItem> findAllByIsDeleteTrue(Pageable pageable);

    Optional<MMedicalItem> findByIdAndIsDeleteFalse(Long id);
}
