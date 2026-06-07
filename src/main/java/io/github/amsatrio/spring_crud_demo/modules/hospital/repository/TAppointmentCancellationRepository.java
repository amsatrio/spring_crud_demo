package io.github.amsatrio.spring_crud_demo.modules.hospital.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import io.github.amsatrio.spring_crud_demo.modules.hospital.entity.TAppointmentCancellation;

import java.util.Optional;

public interface TAppointmentCancellationRepository extends JpaRepository<TAppointmentCancellation, Long>, JpaSpecificationExecutor<TAppointmentCancellation> {
    Page<TAppointmentCancellation> findAllByIsDeleteTrue(Pageable pageable);

    Optional<TAppointmentCancellation> findByIdAndIsDeleteFalse(Long id);
}
