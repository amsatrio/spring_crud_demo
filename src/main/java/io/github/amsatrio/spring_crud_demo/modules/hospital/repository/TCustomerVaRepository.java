package io.github.amsatrio.spring_crud_demo.modules.hospital.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import io.github.amsatrio.spring_crud_demo.modules.hospital.entity.TCustomerVa;

import java.util.Optional;

public interface TCustomerVaRepository extends JpaRepository<TCustomerVa, Long>, JpaSpecificationExecutor<TCustomerVa> {
    Page<TCustomerVa> findAllByIsDeleteTrue(Pageable pageable);

    Optional<TCustomerVa> findByIdAndIsDeleteFalse(Long id);
}
