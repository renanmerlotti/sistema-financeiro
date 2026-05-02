package com.financas.backend.service;

import com.financas.backend.dto.request.TransactionRequestDTO;
import com.financas.backend.dto.response.TransactionResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TransactionService {
    TransactionResponseDTO createTransaction(TransactionRequestDTO transactionRequestDTO, Long userId);

    Page<TransactionResponseDTO> listAllTransactions(Long userId, Pageable pageable);

    void deleteTransaction(Long transactionId);

}
