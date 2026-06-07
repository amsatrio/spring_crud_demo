package io.github.amsatrio.spring_crud_demo.modules.hospital.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import io.github.amsatrio.spring_crud_demo.modules.hospital.entity.TMedicalItemPurchase;

import java.util.Optional;

public interface TMedicalItemPurchaseRepository extends JpaRepository<TMedicalItemPurchase, Long>, JpaSpecificationExecutor<TMedicalItemPurchase> {
    Page<TMedicalItemPurchase> findAllByIsDeleteTrue(Pageable pageable);

    Optional<TMedicalItemPurchase> findByIdAndIsDeleteFalse(Long id);
}
