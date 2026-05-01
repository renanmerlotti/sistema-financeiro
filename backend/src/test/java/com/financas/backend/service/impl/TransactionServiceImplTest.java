package com.financas.backend.service.impl;

import com.financas.backend.dto.request.TransactionRequestDTO;
import com.financas.backend.dto.response.TransactionResponseDTO;
import com.financas.backend.entity.Account;
import com.financas.backend.entity.Transaction;
import com.financas.backend.entity.TransactionType;
import com.financas.backend.exception.ResourceNotFoundException;
import com.financas.backend.repository.AccountRepository;
import com.financas.backend.repository.CategoryRepository;
import com.financas.backend.repository.TransactionRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TransactionServiceImplTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private TransactionServiceImpl transactionService;


    @Test
    @DisplayName("Should create transaction when transaction is saved")
    void createTransactionCase1() {
        TransactionRequestDTO transactionRequestDTO = new TransactionRequestDTO(
                "Ifood", new BigDecimal("53.2"), LocalDate.of(2026, 4, 29), TransactionType.EXPENSE, 1L, null
        );

        Account account = new Account();
        account.setId(1L);

        Transaction transaction = new Transaction(1L, "Ifood", new BigDecimal("53.2"), LocalDate.of(2026, 4, 29), TransactionType.EXPENSE, account, null);

        when(accountRepository.findById(account.getId())).thenReturn(Optional.of(account));
        when(transactionRepository.save(any(Transaction.class))).thenReturn(transaction);

        TransactionResponseDTO result = transactionService.createTransaction(transactionRequestDTO);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Ifood", result.getDescription());
        assertEquals(1L, result.getAccountId());
        assertEquals(0, result.getAmount().compareTo(new BigDecimal("53.2")));

    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when accountId not found")
    void createTransactionCase2() {
        when(accountRepository.findById(1L)).thenReturn(Optional.empty());

        TransactionRequestDTO transactionRequestDTO = new TransactionRequestDTO(
                "Ifood", new BigDecimal("53.2"), LocalDate.of(2026, 4, 29), TransactionType.EXPENSE, 1L, null
        );

        assertThrows(ResourceNotFoundException.class, () -> transactionService.createTransaction(transactionRequestDTO));
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when categoryId not found")
    void createTransactionCase3() {
        TransactionRequestDTO transactionRequestDTO = new TransactionRequestDTO(
                "Ifood", new BigDecimal("53.2"), LocalDate.of(2026, 4, 29), TransactionType.EXPENSE, 1L, 1L
        );

        Account account = new Account();
        account.setId(1L);

        when(accountRepository.findById(account.getId())).thenReturn(Optional.of(account));
        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> transactionService.createTransaction(transactionRequestDTO));

    }

    @Test
    @DisplayName("Should list all transactions when transactions is found")
    void listAllTransactions() {
        Account account = new Account();
        account.setId(1L);

        Transaction transaction1 = new Transaction(1L, "Ifood", new BigDecimal("53.2"), LocalDate.of(2026, 4, 29), TransactionType.EXPENSE, account, null);
        Transaction transaction2 = new Transaction(2L, "Uber", new BigDecimal("24.6"), LocalDate.of(2026, 4, 27), TransactionType.EXPENSE, account, null);

        Pageable pageable = PageRequest.of(0, 10);

        List<Transaction> transactions = List.of(transaction1, transaction2);
        Page<Transaction> page = new PageImpl<>(transactions, pageable, transactions.size());

        when(transactionRepository.findAll(pageable)).thenReturn(page);

        Page<TransactionResponseDTO> result = transactionService.listAllTransactions(pageable);

        assertNotNull(result);
        assertEquals(2, result.getTotalElements());
        assertEquals("Ifood", result.getContent().get(0).getDescription());
        assertEquals("Uber", result.getContent().get(1).getDescription());
    }
}