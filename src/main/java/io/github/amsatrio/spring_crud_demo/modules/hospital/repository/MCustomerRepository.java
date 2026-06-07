package io.github.amsatrio.spring_crud_demo.modules.hospital.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import io.github.amsatrio.spring_crud_demo.modules.hospital.entity.MCustomer;

import java.util.Optional;

public interface MCustomerRepository extends JpaRepository<MCustomer, Long>, JpaSpecificationExecutor<MCustomer> {
    Page<MCustomer> findAllByIsDeleteTrue(Pageable pageable);

    Optional<MCustomer> findByIdAndIsDeleteFalse(Long id);
}
