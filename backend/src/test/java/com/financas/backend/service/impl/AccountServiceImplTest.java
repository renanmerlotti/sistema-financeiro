package com.financas.backend.service.impl;

import com.financas.backend.dto.request.AccountRequestDTO;
import com.financas.backend.dto.response.AccountResponseDTO;
import com.financas.backend.entity.Account;
import com.financas.backend.entity.AccountType;
import com.financas.backend.entity.User;
import com.financas.backend.repository.AccountRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AccountServiceImplTest {

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private AccountServiceImpl accountService;

    @Test
    @DisplayName("Should return AccountResponseDTO when account is saved")
    void createAccountCase1() {
        AccountRequestDTO accountRequestDTO = new AccountRequestDTO("Poupanca", AccountType.SAVINGS);
        Account account = new Account(1L, "Poupanca", new BigDecimal("1000"), AccountType.SAVINGS, null);

        when(accountRepository.save(any(Account.class))).thenReturn(account);

        AccountResponseDTO result = accountService.createAccount(accountRequestDTO);

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
}