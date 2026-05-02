package com.financas.backend.controller;

import com.financas.backend.dto.request.TransactionRequestDTO;
import com.financas.backend.dto.response.TransactionResponseDTO;
import com.financas.backend.entity.User;
import com.financas.backend.service.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping
    public ResponseEntity<TransactionResponseDTO> createTransaction(
            @Valid @RequestBody TransactionRequestDTO transactionRequestDTO,
            @AuthenticationPrincipal User user) {
        TransactionResponseDTO transactionResponseDTO = transactionService.createTransaction(transactionRequestDTO, user.getId());
        return ResponseEntity.status(201).body(transactionResponseDTO);
    }

    @GetMapping
    public ResponseEntity<Page<TransactionResponseDTO>> listAllTransactions(
            @AuthenticationPrincipal User user,
            Pageable pageable) {
        return ResponseEntity.ok(transactionService.listAllTransactions(user.getId(), pageable));
    }
}
