package io.github.amsatrio.spring_crud_demo.modules.hospital.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import io.github.amsatrio.spring_crud_demo.modules.hospital.entity.TCustomerCustomNominal;

import java.util.Optional;

public interface TCustomerCustomNominalRepository extends JpaRepository<TCustomerCustomNominal, Long>, JpaSpecificationExecutor<TCustomerCustomNominal> {
    Page<TCustomerCustomNominal> findAllByIsDeleteTrue(Pageable pageable);

    Optional<TCustomerCustomNominal> findByIdAndIsDeleteFalse(Long id);
}
