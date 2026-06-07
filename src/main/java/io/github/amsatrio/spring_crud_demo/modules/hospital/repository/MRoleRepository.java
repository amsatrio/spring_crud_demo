package io.github.amsatrio.spring_crud_demo.modules.hospital.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import io.github.amsatrio.spring_crud_demo.modules.hospital.entity.MRole;

import java.util.Optional;

public interface MRoleRepository extends JpaRepository<MRole, Long>, JpaSpecificationExecutor<MRole> {
    Page<MRole> findAllByIsDeleteTrue(Pageable pageable);

    Optional<MRole> findByIdAndIsDeleteFalse(Long id);
    
    Optional<MRole> findByCodeAndIsDeleteFalse(String code);
}
