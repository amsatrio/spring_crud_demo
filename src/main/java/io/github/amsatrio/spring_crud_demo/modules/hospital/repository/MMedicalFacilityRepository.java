package io.github.amsatrio.spring_crud_demo.modules.hospital.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import io.github.amsatrio.spring_crud_demo.modules.hospital.entity.MMedicalFacility;

import java.util.Optional;

public interface MMedicalFacilityRepository extends JpaRepository<MMedicalFacility, Long>, JpaSpecificationExecutor<MMedicalFacility> {
    Page<MMedicalFacility> findAllByIsDeleteTrue(Pageable pageable);

    Optional<MMedicalFacility> findByIdAndIsDeleteFalse(Long id);
}
