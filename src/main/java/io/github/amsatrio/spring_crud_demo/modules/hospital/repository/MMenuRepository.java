package io.github.amsatrio.spring_crud_demo.modules.hospital.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import io.github.amsatrio.spring_crud_demo.modules.hospital.entity.MMenu;

import java.util.Optional;

public interface MMenuRepository extends JpaRepository<MMenu, Long>, JpaSpecificationExecutor<MMenu> {
    Page<MMenu> findAllByIsDeleteTrue(Pageable pageable);

    Optional<MMenu> findByIdAndIsDeleteFalse(Long id);
}
