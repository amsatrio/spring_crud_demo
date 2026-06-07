package io.github.amsatrio.spring_crud_demo.modules.hospital.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import io.github.amsatrio.spring_crud_demo.modules.hospital.entity.TDoctorOfficeSchedule;

import java.util.Optional;

public interface TDoctorOfficeScheduleRepository extends JpaRepository<TDoctorOfficeSchedule, Long>, JpaSpecificationExecutor<TDoctorOfficeSchedule> {
    Page<TDoctorOfficeSchedule> findAllByIsDeleteTrue(Pageable pageable);

    Optional<TDoctorOfficeSchedule> findByIdAndIsDeleteFalse(Long id);
}
