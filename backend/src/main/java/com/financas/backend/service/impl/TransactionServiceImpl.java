package com.financas.backend.service.impl;

import com.financas.backend.dto.mapper.TransactionMapper;
import com.financas.backend.dto.request.TransactionRequestDTO;
import com.financas.backend.dto.response.TransactionResponseDTO;
import com.financas.backend.entity.Account;
import com.financas.backend.entity.Category;
import com.financas.backend.entity.Transaction;
import com.financas.backend.exception.ResourceNotFoundException;
import com.financas.backend.repository.AccountRepository;
import com.financas.backend.repository.CategoryRepository;
import com.financas.backend.repository.TransactionRepository;
import com.financas.backend.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;
    private final CategoryRepository categoryRepository;


    @Override
    public TransactionResponseDTO createTransaction(TransactionRequestDTO transactionRequestDTO) {
        Transaction transaction = TransactionMapper.mapTransactionRequestDTOToTransaction(transactionRequestDTO);

        Account account = accountRepository.findById(transactionRequestDTO.getAccountId())
                .orElseThrow(() -> new ResourceNotFoundException("Account not found"));

        transaction.setAccount(account);

        if(transactionRequestDTO.getCategoryId() != null) {
            Category category = categoryRepository.findById(transactionRequestDTO.getCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Category not found"));

            transaction.setCategory(category);
        }

        Transaction savedTransaction = transactionRepository.save(transaction);

        return TransactionMapper.mapTransactionToTransactionResponseDTO(savedTransaction);
    }

    @Override
    public Page<TransactionResponseDTO> listAllTransactions(Pageable pageable) {
        return transactionRepository.findAll(pageable)
                .map((transaction) -> TransactionMapper.mapTransactionToTransactionResponseDTO(transaction));
    }
}
