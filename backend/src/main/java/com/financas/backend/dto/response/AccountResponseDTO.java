package com.financas.backend.dto.response;

import com.financas.backend.entity.AccountType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AccountResponseDTO {
    private Long id;

    private String name;

    private AccountType type;

    private BigDecimal balance;
}
