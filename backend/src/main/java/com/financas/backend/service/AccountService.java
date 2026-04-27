package com.financas.backend.service;

import com.financas.backend.dto.request.AccountRequestDTO;
import com.financas.backend.dto.response.AccountResponseDTO;

import java.util.List;

public interface AccountService {
    AccountResponseDTO createAccount(AccountRequestDTO accountRequestDTO);

    List<AccountResponseDTO> listAccountsByUserId(Long userId);
}
