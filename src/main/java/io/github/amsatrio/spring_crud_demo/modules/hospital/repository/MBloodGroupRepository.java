package io.github.amsatrio.spring_crud_demo.modules.hospital.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import io.github.amsatrio.spring_crud_demo.modules.hospital.entity.MBloodGroup;

import java.util.Optional;

public interface MBloodGroupRepository extends JpaRepository<MBloodGroup, Long>, JpaSpecificationExecutor<MBloodGroup> {
    Page<MBloodGroup> findAllByIsDeleteTrue(Pageable pageable);

    Optional<MBloodGroup> findByIdAndIsDeleteFalse(Long id);
}
