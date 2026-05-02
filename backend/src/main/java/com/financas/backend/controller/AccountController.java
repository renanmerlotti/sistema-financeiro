package com.financas.backend.controller;

import com.financas.backend.dto.request.AccountRequestDTO;
import com.financas.backend.dto.response.AccountResponseDTO;
import com.financas.backend.entity.User;
import com.financas.backend.service.AccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/accounts")
public class AccountController {

    private final AccountService accountService;

    @PostMapping
    public ResponseEntity<AccountResponseDTO> createAccount(
            @Valid @RequestBody AccountRequestDTO accountRequestDTO,
            @AuthenticationPrincipal User user) {
        AccountResponseDTO accountResponseDTO = accountService.createAccount(accountRequestDTO, user.getId());
        return ResponseEntity.status(201).body(accountResponseDTO);
    }

    @GetMapping
    public ResponseEntity<List<AccountResponseDTO>> listAccounts(@AuthenticationPrincipal User user) {
        List<AccountResponseDTO> accounts = accountService.listAccountsByUserId(user.getId());
        return ResponseEntity.ok(accounts);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AccountResponseDTO> updateAccount(
            @PathVariable("id") Long accountId,
            @RequestBody AccountRequestDTO accountRequestDTO,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        Long userId = ((User) userDetails).getId();

        return  ResponseEntity.status(200).body(accountService.updateAccount(accountId, accountRequestDTO, userId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAccount(@PathVariable("id") Long accountId,
                                              @AuthenticationPrincipal User user) {
        accountService.deleteAccount(accountId, user.getId());

        return ResponseEntity.noContent().build();
    }
}
