package io.github.amsatrio.spring_crud_demo.modules.hospital.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import io.github.amsatrio.spring_crud_demo.modules.hospital.entity.MMenuRole;

import java.util.Optional;

public interface MMenuRoleRepository extends JpaRepository<MMenuRole, Long>, JpaSpecificationExecutor<MMenuRole> {
    Page<MMenuRole> findAllByIsDeleteTrue(Pageable pageable);

    Optional<MMenuRole> findByIdAndIsDeleteFalse(Long id);
}
