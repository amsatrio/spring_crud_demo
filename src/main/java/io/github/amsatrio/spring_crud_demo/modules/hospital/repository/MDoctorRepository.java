package io.github.amsatrio.spring_crud_demo.modules.hospital.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import io.github.amsatrio.spring_crud_demo.modules.hospital.entity.MDoctor;

import java.util.Optional;

public interface MDoctorRepository extends JpaRepository<MDoctor, Long>, JpaSpecificationExecutor<MDoctor> {
    Page<MDoctor> findAllByIsDeleteTrue(Pageable pageable);

    Optional<MDoctor> findByIdAndIsDeleteFalse(Long id);
}
