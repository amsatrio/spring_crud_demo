package io.github.amsatrio.spring_crud_demo.modules.hospital.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import io.github.amsatrio.spring_crud_demo.modules.hospital.entity.TCourierDiscount;

import java.util.Optional;

public interface TCourierDiscountRepository extends JpaRepository<TCourierDiscount, Long>, JpaSpecificationExecutor<TCourierDiscount> {
    Page<TCourierDiscount> findAllByIsDeleteTrue(Pageable pageable);

    Optional<TCourierDiscount> findByIdAndIsDeleteFalse(Long id);
}
