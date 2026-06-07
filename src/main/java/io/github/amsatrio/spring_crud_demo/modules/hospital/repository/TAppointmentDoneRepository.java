package io.github.amsatrio.spring_crud_demo.modules.hospital.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import io.github.amsatrio.spring_crud_demo.modules.hospital.entity.TAppointmentDone;

import java.util.Optional;

public interface TAppointmentDoneRepository extends JpaRepository<TAppointmentDone, Long>, JpaSpecificationExecutor<TAppointmentDone> {
    Page<TAppointmentDone> findAllByIsDeleteTrue(Pageable pageable);

    Optional<TAppointmentDone> findByIdAndIsDeleteFalse(Long id);
}
