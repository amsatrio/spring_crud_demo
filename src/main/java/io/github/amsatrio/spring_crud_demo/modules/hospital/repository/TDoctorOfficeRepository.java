package io.github.amsatrio.spring_crud_demo.modules.hospital.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import io.github.amsatrio.spring_crud_demo.modules.hospital.entity.TDoctorOffice;

import java.util.Optional;

public interface TDoctorOfficeRepository extends JpaRepository<TDoctorOffice, Long>, JpaSpecificationExecutor<TDoctorOffice> {
    Page<TDoctorOffice> findAllByIsDeleteTrue(Pageable pageable);

    Optional<TDoctorOffice> findByIdAndIsDeleteFalse(Long id);
}
