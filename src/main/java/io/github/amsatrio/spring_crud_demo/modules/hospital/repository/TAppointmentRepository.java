package io.github.amsatrio.spring_crud_demo.modules.hospital.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import io.github.amsatrio.spring_crud_demo.modules.hospital.entity.TAppointment;

import java.util.Optional;

public interface TAppointmentRepository extends JpaRepository<TAppointment, Long>, JpaSpecificationExecutor<TAppointment> {
    Page<TAppointment> findAllByIsDeleteTrue(Pageable pageable);

    Optional<TAppointment> findByIdAndIsDeleteFalse(Long id);
}
