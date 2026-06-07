package io.github.amsatrio.spring_crud_demo.modules.hospital.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import io.github.amsatrio.spring_crud_demo.modules.hospital.entity.MPaymentMethod;

import java.util.Optional;

public interface MPaymentMethodRepository extends JpaRepository<MPaymentMethod, Long>, JpaSpecificationExecutor<MPaymentMethod> {
    Page<MPaymentMethod> findAllByIsDeleteTrue(Pageable pageable);

    Optional<MPaymentMethod> findByIdAndIsDeleteFalse(Long id);
}
