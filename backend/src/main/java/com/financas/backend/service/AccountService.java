package com.financas.backend.service;

import com.financas.backend.dto.request.AccountRequestDTO;
import com.financas.backend.dto.response.AccountResponseDTO;

import java.util.List;

public interface AccountService {
    AccountResponseDTO createAccount(AccountRequestDTO accountRequestDTO, Long userId);

    List<AccountResponseDTO> listAccountsByUserId(Long userId);

    AccountResponseDTO updateAccount(Long accountId, AccountRequestDTO dto, Long userId);

    void deleteAccount(Long accountId, Long userId);
}
