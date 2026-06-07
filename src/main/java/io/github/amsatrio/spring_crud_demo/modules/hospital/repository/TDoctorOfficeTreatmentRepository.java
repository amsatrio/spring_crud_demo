package io.github.amsatrio.spring_crud_demo.modules.hospital.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import io.github.amsatrio.spring_crud_demo.modules.hospital.entity.TDoctorOfficeTreatment;

import java.util.Optional;

public interface TDoctorOfficeTreatmentRepository extends JpaRepository<TDoctorOfficeTreatment, Long>, JpaSpecificationExecutor<TDoctorOfficeTreatment> {
    Page<TDoctorOfficeTreatment> findAllByIsDeleteTrue(Pageable pageable);

    Optional<TDoctorOfficeTreatment> findByIdAndIsDeleteFalse(Long id);
}
