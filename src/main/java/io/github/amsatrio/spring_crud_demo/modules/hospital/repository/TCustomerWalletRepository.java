package io.github.amsatrio.spring_crud_demo.modules.hospital.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import io.github.amsatrio.spring_crud_demo.modules.hospital.entity.TCustomerWallet;

import java.util.Optional;

public interface TCustomerWalletRepository extends JpaRepository<TCustomerWallet, Long>, JpaSpecificationExecutor<TCustomerWallet> {
    Page<TCustomerWallet> findAllByIsDeleteTrue(Pageable pageable);

    Optional<TCustomerWallet> findByIdAndIsDeleteFalse(Long id);
}
