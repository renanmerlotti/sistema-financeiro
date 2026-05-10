package com.financas.backend.service.impl;

import com.financas.backend.dto.request.TransactionRequestDTO;
import com.financas.backend.dto.response.TransactionResponseDTO;
import com.financas.backend.entity.Account;
import com.financas.backend.entity.AccountType;
import com.financas.backend.entity.Transaction;
import com.financas.backend.entity.TransactionType;
import com.financas.backend.entity.User;
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
import static org.mockito.Mockito.verify;
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
        User user = new User(1L, "renan", "renan@email.com", "123456");

        Account account = new Account();
        account.setId(1L);
        account.setUser(user);

        TransactionRequestDTO transactionRequestDTO = new TransactionRequestDTO(
                "Ifood", new BigDecimal("53.2"), LocalDate.of(2026, 4, 29), TransactionType.EXPENSE, 1L, null
        );

        Transaction transaction = new Transaction(1L, "Ifood", new BigDecimal("53.2"), LocalDate.of(2026, 4, 29), TransactionType.EXPENSE, account, null);

        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));
        when(transactionRepository.save(any(Transaction.class))).thenReturn(transaction);

        TransactionResponseDTO result = transactionService.createTransaction(transactionRequestDTO, 1L);

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

        assertThrows(ResourceNotFoundException.class, () -> transactionService.createTransaction(transactionRequestDTO, 1L));
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when categoryId not found")
    void createTransactionCase3() {
        User user = new User(1L, "renan", "renan@email.com", "123456");

        Account account = new Account();
        account.setId(1L);
        account.setUser(user);

        TransactionRequestDTO transactionRequestDTO = new TransactionRequestDTO(
                "Ifood", new BigDecimal("53.2"), LocalDate.of(2026, 4, 29), TransactionType.EXPENSE, 1L, 1L
        );

        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));
        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> transactionService.createTransaction(transactionRequestDTO, 1L));
    }

    @Test
    @DisplayName("Should list all transactions filtered by user")
    void listAllTransactionsCase1() {
        User user = new User(1L, "renan", "renan@email.com", "123456");

        Account account = new Account();
        account.setId(1L);
        account.setUser(user);

        Transaction transaction1 = new Transaction(1L, "Ifood", new BigDecimal("53.2"), LocalDate.of(2026, 4, 29), TransactionType.EXPENSE, account, null);
        Transaction transaction2 = new Transaction(2L, "Uber", new BigDecimal("24.6"), LocalDate.of(2026, 4, 27), TransactionType.EXPENSE, account, null);

        Pageable pageable = PageRequest.of(0, 10);
        Page<Transaction> page = new PageImpl<>(List.of(transaction1, transaction2), pageable, 2);

        when(transactionRepository.findByAccountUserId(1L, pageable)).thenReturn(page);

        Page<TransactionResponseDTO> result = transactionService.listAllTransactions(1L, null, null, pageable);

        assertNotNull(result);
        assertEquals(2, result.getTotalElements());
        assertEquals("Ifood", result.getContent().get(0).getDescription());
        assertEquals("Uber", result.getContent().get(1).getDescription());
    }

    @Test
    @DisplayName("Should list transactions filtered by user and date range")
    void listAllTransactionsCase2() {
        User user = new User(1L, "renan", "renan@email.com", "123456");

        Account account = new Account();
        account.setId(1L);
        account.setUser(user);

        LocalDate startDate = LocalDate.of(2026, 4, 1);
        LocalDate endDate = LocalDate.of(2026, 4, 30);

        Transaction transaction = new Transaction(1L, "Ifood", new BigDecimal("53.2"), LocalDate.of(2026, 4, 15), TransactionType.EXPENSE, account, null);

        Pageable pageable = PageRequest.of(0, 10);
        Page<Transaction> page = new PageImpl<>(List.of(transaction), pageable, 1);

        when(transactionRepository.findByAccountUserIdAndDateBetween(1L, startDate, endDate, pageable)).thenReturn(page);

        Page<TransactionResponseDTO> result = transactionService.listAllTransactions(1L, startDate, endDate, pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals("Ifood", result.getContent().getFirst().getDescription());
    }

    @Test
    @DisplayName("Should return updated TransactionResponseDTO when transaction is found")
    void updateTransactionCase1() {
        User user = new User(1L, "renan", "renan@email.com", "123456");
        Account account = new Account(1L, "Corrente", new BigDecimal("500"), AccountType.CHECKING, user);
        Transaction transaction = new Transaction(1L, "Ifood", new BigDecimal("50"), LocalDate.of(2026, 4, 29), TransactionType.EXPENSE, account, null);

        TransactionRequestDTO dto = new TransactionRequestDTO(
                "Ifood Atualizado", new BigDecimal("30"), LocalDate.of(2026, 4, 29), TransactionType.EXPENSE, 1L, null
        );

        Transaction updated = new Transaction(1L, "Ifood Atualizado", new BigDecimal("30"), LocalDate.of(2026, 4, 29), TransactionType.EXPENSE, account, null);

        when(transactionRepository.findByIdAndAccountUserId(1L, 1L)).thenReturn(Optional.of(transaction));
        when(transactionRepository.save(any(Transaction.class))).thenReturn(updated);

        TransactionResponseDTO result = transactionService.updateTransaction(1L, dto, 1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Ifood Atualizado", result.getDescription());
        assertEquals(0, result.getAmount().compareTo(new BigDecimal("30")));
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when transaction is not found on update")
    void updateTransactionCase2() {
        TransactionRequestDTO dto = new TransactionRequestDTO(
                "Ifood", new BigDecimal("50"), LocalDate.of(2026, 4, 29), TransactionType.EXPENSE, 1L, null
        );

        when(transactionRepository.findByIdAndAccountUserId(1L, 1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> transactionService.updateTransaction(1L, dto, 1L));
    }

    @Test
    @DisplayName("Should delete transaction when transaction is found")
    void deleteTransactionCase1() {
        User user = new User(1L, "renan", "renan@email.com", "123456");
        Account account = new Account(1L, "Corrente", new BigDecimal("500"), AccountType.CHECKING, user);
        Transaction transaction = new Transaction(1L, "Ifood", new BigDecimal("53.2"), LocalDate.of(2026, 4, 29), TransactionType.EXPENSE, account, null);

        when(transactionRepository.findById(1L)).thenReturn(Optional.of(transaction));

        transactionService.deleteTransaction(1L, 1L);

        verify(transactionRepository).delete(transaction);
    }
}
