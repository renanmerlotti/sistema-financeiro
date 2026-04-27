package com.financas.backend.dto.mapper;

import com.financas.backend.dto.request.AccountRequestDTO;
import com.financas.backend.dto.response.AccountResponseDTO;
import com.financas.backend.entity.Account;

public class AccountMapper {

    public static AccountResponseDTO mapAccountToAccountResponseDTO(Account account) {
        return new AccountResponseDTO(
                account.getId(),
                account.getName(),
                account.getAccountType(),
                account.getBalance()
        );
    }

    public static Account mapAccountRequestDTOtoAccount(AccountRequestDTO accountRequestDTO) {
        Account account = new Account();

        account.setName(accountRequestDTO.getName());
        account.setAccountType(accountRequestDTO.getType());

        return account;
    }
}
