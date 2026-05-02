package com.financas.backend.repository;

import com.financas.backend.entity.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    Page<Transaction> findByAccountUserId(Long userId, Pageable pageable);

    boolean existsByAccountId(Long accountId);

    boolean existsByCategoryId(Long categoryId);

    Optional<Transaction> findByIdAndAccountUserId(Long id, Long userId);
}
