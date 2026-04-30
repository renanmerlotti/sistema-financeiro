package com.financas.backend.controller;

import com.financas.backend.dto.request.AccountRequestDTO;
import com.financas.backend.dto.response.AccountResponseDTO;
import com.financas.backend.service.AccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/accounts")
public class AccountController {

    private final AccountService accountService;

    @PostMapping
    public ResponseEntity<AccountResponseDTO> createAccount(@Valid @RequestBody AccountRequestDTO accountRequestDTO){
        AccountResponseDTO accountResponseDTO = accountService.createAccount(accountRequestDTO);

        return ResponseEntity.status(201).body(accountResponseDTO);
    }

    @GetMapping
    public ResponseEntity<List<AccountResponseDTO>> listAccountsByUserId(@RequestParam Long userId) {
        List<AccountResponseDTO> accountResponseDTOList = accountService.listAccountsByUserId(userId);

        return ResponseEntity.ok(accountResponseDTOList);
    }
}
