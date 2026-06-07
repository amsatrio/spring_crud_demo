package io.github.amsatrio.spring_crud_demo.modules.hospital.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import io.github.amsatrio.spring_crud_demo.modules.hospital.entity.TDoctorTreatment;

import java.util.Optional;

public interface TDoctorTreatmentRepository extends JpaRepository<TDoctorTreatment, Long>, JpaSpecificationExecutor<TDoctorTreatment> {
    Page<TDoctorTreatment> findAllByIsDeleteTrue(Pageable pageable);

    Optional<TDoctorTreatment> findByIdAndIsDeleteFalse(Long id);
}
