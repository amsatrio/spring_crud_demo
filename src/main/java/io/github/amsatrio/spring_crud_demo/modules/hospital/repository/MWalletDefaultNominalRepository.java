package io.github.amsatrio.spring_crud_demo.modules.hospital.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import io.github.amsatrio.spring_crud_demo.modules.hospital.entity.MWalletDefaultNominal;

import java.util.Optional;

public interface MWalletDefaultNominalRepository extends JpaRepository<MWalletDefaultNominal, Long>, JpaSpecificationExecutor<MWalletDefaultNominal> {
    Page<MWalletDefaultNominal> findAllByIsDeleteTrue(Pageable pageable);

    Optional<MWalletDefaultNominal> findByIdAndIsDeleteFalse(Long id);
}
