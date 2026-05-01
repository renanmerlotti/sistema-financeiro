package com.financas.backend.controller;

import com.financas.backend.dto.request.TransactionRequestDTO;
import com.financas.backend.dto.response.TransactionResponseDTO;
import com.financas.backend.service.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping
    public ResponseEntity<TransactionResponseDTO> createTransaction(@Valid @RequestBody TransactionRequestDTO transactionRequestDTO) {
        TransactionResponseDTO transactionResponseDTO = transactionService.createTransaction(transactionRequestDTO);

        return ResponseEntity.status(201).body(transactionResponseDTO);
    }

    @GetMapping
    public ResponseEntity<Page<TransactionResponseDTO>> listAllTransactions(Pageable pageable) {
        return ResponseEntity.ok(transactionService.listAllTransactions(pageable));
    }

}
