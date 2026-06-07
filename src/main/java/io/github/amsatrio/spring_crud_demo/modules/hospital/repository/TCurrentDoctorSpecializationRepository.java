package io.github.amsatrio.spring_crud_demo.modules.hospital.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import io.github.amsatrio.spring_crud_demo.modules.hospital.entity.TCurrentDoctorSpecialization;

import java.util.Optional;

public interface TCurrentDoctorSpecializationRepository extends JpaRepository<TCurrentDoctorSpecialization, Long>, JpaSpecificationExecutor<TCurrentDoctorSpecialization> {
    Page<TCurrentDoctorSpecialization> findAllByIsDeleteTrue(Pageable pageable);

    Optional<TCurrentDoctorSpecialization> findByIdAndIsDeleteFalse(Long id);
}
