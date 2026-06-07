package io.github.amsatrio.spring_crud_demo.modules.hospital.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import io.github.amsatrio.spring_crud_demo.modules.hospital.entity.MAdmin;

import java.util.Optional;

public interface MAdminRepository extends JpaRepository<MAdmin, Long>, JpaSpecificationExecutor<MAdmin> {
    Page<MAdmin> findAllByIsDeleteTrue(Pageable pageable);

    Optional<MAdmin> findByIdAndIsDeleteFalse(Long id);
}
