package com.financas.backend.dto.response;

import com.financas.backend.entity.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TransactionResponseDTO {

    private Long id;

    private String description;

    private BigDecimal amount;

    private LocalDate date;

    private TransactionType transactionType;

    private Long accountId;

    private Long categoryId;
}
