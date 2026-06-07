package io.github.amsatrio.spring_crud_demo.modules.hospital.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import io.github.amsatrio.spring_crud_demo.modules.hospital.entity.MBank;

import java.util.Optional;

public interface MBankRepository extends JpaRepository<MBank, Long>, JpaSpecificationExecutor<MBank> {
    Page<MBank> findAllByIsDeleteTrue(Pageable pageable);

    Optional<MBank> findByIdAndIsDeleteFalse(Long id);
}
