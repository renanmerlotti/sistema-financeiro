package com.financas.backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DashboardResponseDTO {

    private BigDecimal totalBalance;

    private BigDecimal totalIncome;

    private BigDecimal totalExpenses;

    private BigDecimal savingsRate;

    private List<DailyCashflowDTO> dailyCashflow;

    private List<CategorySpendingDTO> spendingByCategory;

    private List<TransactionResponseDTO> recentTransactions;

    private List<AccountResponseDTO> accounts;
}
