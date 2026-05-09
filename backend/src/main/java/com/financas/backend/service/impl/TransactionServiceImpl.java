package com.financas.backend.service.impl;

import com.financas.backend.dto.mapper.TransactionMapper;
import com.financas.backend.dto.request.TransactionRequestDTO;
import com.financas.backend.dto.response.TransactionResponseDTO;
import com.financas.backend.entity.Account;
import com.financas.backend.entity.Category;
import com.financas.backend.entity.Transaction;
import com.financas.backend.entity.TransactionType;
import com.financas.backend.exception.ResourceNotFoundException;
import com.financas.backend.repository.AccountRepository;
import com.financas.backend.repository.CategoryRepository;
import com.financas.backend.repository.TransactionRepository;
import com.financas.backend.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;
    private final CategoryRepository categoryRepository;

    @Override
    public TransactionResponseDTO createTransaction(TransactionRequestDTO transactionRequestDTO, Long userId) {
        Account account = accountRepository.findById(transactionRequestDTO.getAccountId())
                .orElseThrow(() -> new ResourceNotFoundException("Account not found"));

        if (!account.getUser().getId().equals(userId)) {
            throw new ResourceNotFoundException("Account not found");
        }

        Transaction transaction = TransactionMapper.mapTransactionRequestDTOToTransaction(transactionRequestDTO);
        transaction.setAccount(account);

        if (transactionRequestDTO.getCategoryId() != null) {
            Category category = categoryRepository.findById(transactionRequestDTO.getCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Category not found"));

            transaction.setCategory(category);
        }

        Transaction savedTransaction = transactionRepository.save(transaction);

        if (savedTransaction.getTransactionType() == TransactionType.INCOME) {
            account.setBalance(account.getBalance().add(savedTransaction.getAmount()));
        } else {
            account.setBalance(account.getBalance().subtract(savedTransaction.getAmount()));
        }
        accountRepository.save(account);

        return TransactionMapper.mapTransactionToTransactionResponseDTO(savedTransaction);
    }

    @Override
    public Page<TransactionResponseDTO> listAllTransactions(Long userId, LocalDate startDate, LocalDate endDate, Pageable pageable) {
        if (startDate != null && endDate != null) {
            return transactionRepository.findByAccountUserIdAndDateBetween(userId, startDate, endDate, pageable)
                    .map(transaction -> TransactionMapper.mapTransactionToTransactionResponseDTO(transaction));
        }
        return transactionRepository.findByAccountUserId(userId, pageable)
                .map(transaction -> TransactionMapper.mapTransactionToTransactionResponseDTO(transaction));
    }

    @Override
    public TransactionResponseDTO updateTransaction(Long transactionId, TransactionRequestDTO dto, Long userId) {
        Transaction transaction = transactionRepository.findByIdAndAccountUserId(transactionId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Transaction with id " + transactionId + " not found"));

        Account account = transaction.getAccount();
        if (transaction.getTransactionType() == TransactionType.INCOME) {
            account.setBalance(account.getBalance().subtract(transaction.getAmount()));
        } else {
            account.setBalance(account.getBalance().add(transaction.getAmount()));
        }

        transaction.setDescription(dto.getDescription());
        transaction.setAmount(dto.getAmount());
        transaction.setDate(dto.getDate());
        transaction.setTransactionType(dto.getTransactionType());

        if (dto.getTransactionType() == TransactionType.INCOME) {
            account.setBalance(account.getBalance().add(dto.getAmount()));
        } else {
            account.setBalance(account.getBalance().subtract(dto.getAmount()));
        }
        accountRepository.save(account);

        Transaction updated = transactionRepository.save(transaction);

        return TransactionMapper.mapTransactionToTransactionResponseDTO(updated);

    }

    @Override
    public void deleteTransaction(Long transactionId, Long userId) {
        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new ResourceNotFoundException("Transaction with id " +
                        transactionId + " not found"));

        if (!transaction.getAccount().getUser().getId().equals(userId)) {
            throw new ResourceNotFoundException("Transaction with id " + transactionId + " not found");
        }

        Account account = transaction.getAccount();
        if (transaction.getTransactionType() == TransactionType.INCOME) {
            account.setBalance(account.getBalance().subtract(transaction.getAmount()));
        } else {
            account.setBalance(account.getBalance().add(transaction.getAmount()));
        }
        accountRepository.save(account);

        transactionRepository.delete(transaction);
    }
}
