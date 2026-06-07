package io.github.amsatrio.spring_crud_demo.modules.hospital.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import io.github.amsatrio.spring_crud_demo.modules.hospital.entity.TCustomerWalletWithdraw;

import java.util.Optional;

public interface TCustomerWalletWithdrawRepository extends JpaRepository<TCustomerWalletWithdraw, Long>, JpaSpecificationExecutor<TCustomerWalletWithdraw> {
    Page<TCustomerWalletWithdraw> findAllByIsDeleteTrue(Pageable pageable);

    Optional<TCustomerWalletWithdraw> findByIdAndIsDeleteFalse(Long id);
}
