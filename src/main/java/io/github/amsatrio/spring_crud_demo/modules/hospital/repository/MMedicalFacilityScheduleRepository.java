package io.github.amsatrio.spring_crud_demo.modules.hospital.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import io.github.amsatrio.spring_crud_demo.modules.hospital.entity.MMedicalFacilitySchedule;

import java.util.Optional;

public interface MMedicalFacilityScheduleRepository extends JpaRepository<MMedicalFacilitySchedule, Long>, JpaSpecificationExecutor<MMedicalFacilitySchedule> {
    Page<MMedicalFacilitySchedule> findAllByIsDeleteTrue(Pageable pageable);

    Optional<MMedicalFacilitySchedule> findByIdAndIsDeleteFalse(Long id);
}
