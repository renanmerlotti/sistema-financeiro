package com.financas.backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CategorySpendingDTO {

    private String categoryName;

    private BigDecimal amount;

    private BigDecimal percentage;
}
