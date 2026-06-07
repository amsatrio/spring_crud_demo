package io.github.amsatrio.spring_crud_demo.modules.hospital.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import io.github.amsatrio.spring_crud_demo.modules.hospital.entity.TAppointmentRescheduleHistory;

import java.util.Optional;

public interface TAppointmentRescheduleHistoryRepository extends JpaRepository<TAppointmentRescheduleHistory, Long>, JpaSpecificationExecutor<TAppointmentRescheduleHistory> {
    Page<TAppointmentRescheduleHistory> findAllByIsDeleteTrue(Pageable pageable);

    Optional<TAppointmentRescheduleHistory> findByIdAndIsDeleteFalse(Long id);
}
