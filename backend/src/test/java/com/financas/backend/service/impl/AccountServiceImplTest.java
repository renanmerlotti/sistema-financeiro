package com.financas.backend.service.impl;

import com.financas.backend.dto.request.AccountRequestDTO;
import com.financas.backend.dto.response.AccountResponseDTO;
import com.financas.backend.entity.Account;
import com.financas.backend.entity.AccountType;
import com.financas.backend.entity.User;
import com.financas.backend.exception.BusinessRuleException;
import com.financas.backend.exception.ResourceNotFoundException;
import com.financas.backend.repository.AccountRepository;
import com.financas.backend.repository.TransactionRepository;
import com.financas.backend.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AccountServiceImplTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private AccountServiceImpl accountService;

    @Test
    @DisplayName("Should return AccountResponseDTO when account is saved")
    void createAccountCase1() {
        User user = new User(1L, "renan", "renan@email.com", "123456");
        AccountRequestDTO accountRequestDTO = new AccountRequestDTO("Poupanca", AccountType.SAVINGS);
        Account account = new Account(1L, "Poupanca", new BigDecimal("1000"), AccountType.SAVINGS, user);

        when(userRepository.getReferenceById(1L)).thenReturn(user);
        when(accountRepository.save(any(Account.class))).thenReturn(account);

        AccountResponseDTO result = accountService.createAccount(accountRequestDTO, 1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Poupanca", result.getName());
    }

    @Test
    @DisplayName("Should return list of AccountResponseDTO when accounts are found by user id")
    void listAccountsByUserId() {
        User user = new User(1L, "renan", "renan@email.com", "1235652");

        Account account1 = new Account(1L, "Poupanca", new BigDecimal("1000"), AccountType.SAVINGS, user);
        Account account2 = new Account(2L, "Corrente", new BigDecimal("200"), AccountType.CHECKING, user);

        List<Account> accounts = List.of(account1, account2);

        when(accountRepository.findByUserId(user.getId())).thenReturn(accounts);

        List<AccountResponseDTO> accountResponseDTOList = accountService.listAccountsByUserId(user.getId());

        assertNotNull(accountResponseDTOList);
        assertEquals(2, accountResponseDTOList.size());
        assertEquals(1L, accountResponseDTOList.getFirst().getId());
        assertEquals("Poupanca", accountResponseDTOList.getFirst().getName());
        assertEquals(2L, accountResponseDTOList.get(1).getId());
        assertEquals("Corrente", accountResponseDTOList.get(1).getName());
    }

    @Test
    @DisplayName("Should return updated AccountResponseDTO when account is found")
    void updateAccountCase1() {
        User user = new User(1L, "renan", "renan@email.com", "123456");
        Account account = new Account(1L, "Corrente", new BigDecimal("500"), AccountType.CHECKING, user);
        AccountRequestDTO dto = new AccountRequestDTO("Corrente Atualizada", AccountType.CHECKING);
        Account updated = new Account(1L, "Corrente Atualizada", new BigDecimal("500"), AccountType.CHECKING, user);

        when(accountRepository.findByIdAndUserId(1L, 1L)).thenReturn(Optional.of(account));
        when(accountRepository.save(any(Account.class))).thenReturn(updated);

        AccountResponseDTO result = accountService.updateAccount(1L, dto, 1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Corrente Atualizada", result.getName());
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when account is not found on update")
    void updateAccountCase2() {
        AccountRequestDTO dto = new AccountRequestDTO("Corrente", AccountType.CHECKING);

        when(accountRepository.findByIdAndUserId(1L, 1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> accountService.updateAccount(1L, dto, 1L));
    }

    @Test
    @DisplayName("Should delete account when account exists and has no transactions")
    void deleteAccountCase1() {
        User user = new User(1L, "renan", "renan@email.com", "123456");
        Account account = new Account(1L, "Poupanca", new BigDecimal("1000"), AccountType.SAVINGS, user);

        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));
        when(transactionRepository.existsByAccountId(1L)).thenReturn(false);

        accountService.deleteAccount(1L, 1L);

        verify(accountRepository).delete(account);
    }

    @Test
    @DisplayName("Should throw BusinessRuleException when account has associated transactions")
    void deleteAccountCase2() {
        User user = new User(1L, "renan", "renan@email.com", "123456");
        Account account = new Account(1L, "Poupanca", new BigDecimal("1000"), AccountType.SAVINGS, user);

        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));
        when(transactionRepository.existsByAccountId(1L)).thenReturn(true);

        assertThrows(BusinessRuleException.class, () -> accountService.deleteAccount(1L, 1L));
    }
}
