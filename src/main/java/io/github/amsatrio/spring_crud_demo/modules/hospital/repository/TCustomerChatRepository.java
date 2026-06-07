package io.github.amsatrio.spring_crud_demo.modules.hospital.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import io.github.amsatrio.spring_crud_demo.modules.hospital.entity.TCustomerChat;

import java.util.Optional;

public interface TCustomerChatRepository extends JpaRepository<TCustomerChat, Long>, JpaSpecificationExecutor<TCustomerChat> {
    Page<TCustomerChat> findAllByIsDeleteTrue(Pageable pageable);

    Optional<TCustomerChat> findByIdAndIsDeleteFalse(Long id);
}
