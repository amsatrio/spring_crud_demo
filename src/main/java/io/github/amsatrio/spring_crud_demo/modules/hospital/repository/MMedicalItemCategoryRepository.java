package io.github.amsatrio.spring_crud_demo.modules.hospital.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import io.github.amsatrio.spring_crud_demo.modules.hospital.entity.MMedicalItemCategory;

import java.util.Optional;

public interface MMedicalItemCategoryRepository extends JpaRepository<MMedicalItemCategory, Long>, JpaSpecificationExecutor<MMedicalItemCategory> {
    Page<MMedicalItemCategory> findAllByIsDeleteTrue(Pageable pageable);

    Optional<MMedicalItemCategory> findByIdAndIsDeleteFalse(Long id);
}
