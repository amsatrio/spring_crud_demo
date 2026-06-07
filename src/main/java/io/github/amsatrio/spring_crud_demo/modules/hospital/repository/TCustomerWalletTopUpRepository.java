package io.github.amsatrio.spring_crud_demo.modules.hospital.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import io.github.amsatrio.spring_crud_demo.modules.hospital.entity.TCustomerWalletTopUp;

import java.util.Optional;

public interface TCustomerWalletTopUpRepository extends JpaRepository<TCustomerWalletTopUp, Long>, JpaSpecificationExecutor<TCustomerWalletTopUp> {
    Page<TCustomerWalletTopUp> findAllByIsDeleteTrue(Pageable pageable);

    Optional<TCustomerWalletTopUp> findByIdAndIsDeleteFalse(Long id);
}
