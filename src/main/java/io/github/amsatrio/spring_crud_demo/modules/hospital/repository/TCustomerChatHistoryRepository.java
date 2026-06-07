package io.github.amsatrio.spring_crud_demo.modules.hospital.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import io.github.amsatrio.spring_crud_demo.modules.hospital.entity.TCustomerChatHistory;

import java.util.Optional;

public interface TCustomerChatHistoryRepository extends JpaRepository<TCustomerChatHistory, Long>, JpaSpecificationExecutor<TCustomerChatHistory> {
    Page<TCustomerChatHistory> findAllByIsDeleteTrue(Pageable pageable);

    Optional<TCustomerChatHistory> findByIdAndIsDeleteFalse(Long id);
}
