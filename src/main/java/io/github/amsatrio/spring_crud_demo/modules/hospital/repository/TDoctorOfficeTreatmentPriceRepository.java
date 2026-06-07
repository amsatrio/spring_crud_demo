package io.github.amsatrio.spring_crud_demo.modules.hospital.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import io.github.amsatrio.spring_crud_demo.modules.hospital.entity.TDoctorOfficeTreatmentPrice;

import java.util.Optional;

public interface TDoctorOfficeTreatmentPriceRepository extends JpaRepository<TDoctorOfficeTreatmentPrice, Long>, JpaSpecificationExecutor<TDoctorOfficeTreatmentPrice> {
    Page<TDoctorOfficeTreatmentPrice> findAllByIsDeleteTrue(Pageable pageable);

    Optional<TDoctorOfficeTreatmentPrice> findByIdAndIsDeleteFalse(Long id);
}
