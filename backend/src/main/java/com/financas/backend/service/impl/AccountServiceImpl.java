package com.financas.backend.service.impl;

import com.financas.backend.dto.mapper.AccountMapper;
import com.financas.backend.dto.request.AccountRequestDTO;
import com.financas.backend.dto.response.AccountResponseDTO;
import com.financas.backend.entity.Account;
import com.financas.backend.exception.BusinessRuleException;
import com.financas.backend.exception.ResourceNotFoundException;
import com.financas.backend.repository.AccountRepository;
import com.financas.backend.repository.TransactionRepository;
import com.financas.backend.repository.UserRepository;
import com.financas.backend.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;

    @Override
    public AccountResponseDTO createAccount(AccountRequestDTO accountRequestDTO, Long userId) {
        Account account = AccountMapper.mapAccountRequestDTOtoAccount(accountRequestDTO);
        account.setUser(userRepository.getReferenceById(userId));
        Account savedAccount = accountRepository.save(account);

        return AccountMapper.mapAccountToAccountResponseDTO(savedAccount);
    }

    @Override
    public List<AccountResponseDTO> listAccountsByUserId(Long userId) {
        return accountRepository.findByUserId(userId)
                .stream()
                .map(account -> AccountMapper.mapAccountToAccountResponseDTO(account))
                .collect(Collectors.toList());
    }

    @Override
    public void deleteAccount(Long accountId, Long userId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException("Account with id " + accountId + " not found"));

        if (!account.getUser().getId().equals(userId)) {
            throw new ResourceNotFoundException("Account with id " + accountId + " not found");
        }

        if (transactionRepository.existsByAccountId(accountId)) {
            throw new BusinessRuleException("Cannot delete account with associated transactions");
        }

        accountRepository.delete(account);
    }


}
