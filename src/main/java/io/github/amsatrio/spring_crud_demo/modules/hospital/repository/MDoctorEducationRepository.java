package io.github.amsatrio.spring_crud_demo.modules.hospital.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import io.github.amsatrio.spring_crud_demo.modules.hospital.entity.MDoctorEducation;

import java.util.Optional;

public interface MDoctorEducationRepository extends JpaRepository<MDoctorEducation, Long>, JpaSpecificationExecutor<MDoctorEducation> {
    Page<MDoctorEducation> findAllByIsDeleteTrue(Pageable pageable);

    Optional<MDoctorEducation> findByIdAndIsDeleteFalse(Long id);
}
