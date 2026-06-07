package io.github.amsatrio.spring_crud_demo.modules.hospital.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import io.github.amsatrio.spring_crud_demo.modules.hospital.entity.TMedicalItemPurchaseDetail;

import java.util.Optional;

public interface TMedicalItemPurchaseDetailRepository extends JpaRepository<TMedicalItemPurchaseDetail, Long>, JpaSpecificationExecutor<TMedicalItemPurchaseDetail> {
    Page<TMedicalItemPurchaseDetail> findAllByIsDeleteTrue(Pageable pageable);

    Optional<TMedicalItemPurchaseDetail> findByIdAndIsDeleteFalse(Long id);
}
