package com.financas.backend.dto.mapper;

import com.financas.backend.dto.request.TransactionRequestDTO;
import com.financas.backend.dto.response.TransactionResponseDTO;
import com.financas.backend.entity.Transaction;

public class TransactionMapper {

    public static TransactionResponseDTO mapTransactionToTransactionResponseDTO(Transaction transaction) {
        return new TransactionResponseDTO(
                transaction.getId(),
                transaction.getDescription(),
                transaction.getAmount(),
                transaction.getDate(),
                transaction.getTransactionType(),
                transaction.getAccount().getId(),
                transaction.getCategory() != null ? transaction.getCategory().getId() : null
        );
    }

    public static Transaction mapTransactionRequestDTOToTransaction(TransactionRequestDTO transactionRequestDTO) {
        Transaction transaction = new Transaction();

        transaction.setDescription(transactionRequestDTO.getDescription());
        transaction.setAmount(transactionRequestDTO.getAmount());
        transaction.setDate(transactionRequestDTO.getDate());
        transaction.setTransactionType(transactionRequestDTO.getTransactionType());

        return transaction;
    }
}
