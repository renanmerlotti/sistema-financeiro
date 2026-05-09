package com.financas.backend.service.impl;

import com.financas.backend.dto.mapper.AccountMapper;
import com.financas.backend.dto.mapper.TransactionMapper;
import com.financas.backend.dto.response.AccountResponseDTO;
import com.financas.backend.dto.response.CategorySpendingDTO;
import com.financas.backend.dto.response.DailyCashflowDTO;
import com.financas.backend.dto.response.DashboardResponseDTO;
import com.financas.backend.dto.response.TransactionResponseDTO;
import com.financas.backend.entity.Transaction;
import com.financas.backend.entity.TransactionType;
import com.financas.backend.entity.Account;
import com.financas.backend.repository.AccountRepository;
import com.financas.backend.repository.TransactionRepository;
import com.financas.backend.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;

    @Override
    public DashboardResponseDTO getDashboard(Long userId, String period) {
        DateRange range = resolvePeriod(period);

        List<Account> accounts = accountRepository.findByUserId(userId);
        BigDecimal totalBalance = accounts.stream()
                .map(account -> account.getBalance())
                .reduce(BigDecimal.ZERO, (a, b) -> a.add(b));

        List<Transaction> periodTransactions = range.startDate() != null
                ? transactionRepository.findByAccountUserIdAndDateBetween(userId, range.startDate(), range.endDate(), Pageable.unpaged()).getContent()
                : transactionRepository.findByAccountUserId(userId, Pageable.unpaged()).getContent();

        BigDecimal totalIncome = periodTransactions.stream()
                .filter(t -> t.getTransactionType() == TransactionType.INCOME)
                .map(t -> t.getAmount())
                .reduce(BigDecimal.ZERO, (a, b) -> a.add(b));

        BigDecimal totalExpenses = periodTransactions.stream()
                .filter(t -> t.getTransactionType() == TransactionType.EXPENSE)
                .map(t -> t.getAmount())
                .reduce(BigDecimal.ZERO, (a, b) -> a.add(b));

        BigDecimal savingsRate = totalIncome.compareTo(BigDecimal.ZERO) > 0
                ? totalIncome.subtract(totalExpenses).divide(totalIncome, 4, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100))
                : BigDecimal.ZERO;

        List<DailyCashflowDTO> dailyCashflow = buildDailyCashflow(periodTransactions);
        List<CategorySpendingDTO> spendingByCategory = buildSpendingByCategory(periodTransactions, totalExpenses);

        List<TransactionResponseDTO> recentTransactions = transactionRepository
                .findByAccountUserId(userId, PageRequest.of(0, 6, Sort.by(Sort.Direction.DESC, "date")))
                .getContent()
                .stream()
                .map(t -> TransactionMapper.mapTransactionToTransactionResponseDTO(t))
                .collect(Collectors.toList());

        List<AccountResponseDTO> accountDTOs = accounts.stream()
                .map(account -> AccountMapper.mapAccountToAccountResponseDTO(account))
                .collect(Collectors.toList());

        return new DashboardResponseDTO(totalBalance, totalIncome, totalExpenses, savingsRate,
                dailyCashflow, spendingByCategory, recentTransactions, accountDTOs);
    }

    private List<DailyCashflowDTO> buildDailyCashflow(List<Transaction> transactions) {
        Map<LocalDate, BigDecimal> incomeByDate = new TreeMap<>();
        Map<LocalDate, BigDecimal> expensesByDate = new TreeMap<>();

        for (Transaction t : transactions) {
            if (t.getTransactionType() == TransactionType.INCOME) {
                incomeByDate.merge(t.getDate(), t.getAmount(), (a, b) -> a.add(b));
            } else {
                expensesByDate.merge(t.getDate(), t.getAmount(), (a, b) -> a.add(b));
            }
        }

        Set<LocalDate> allDates = new TreeSet<>();
        allDates.addAll(incomeByDate.keySet());
        allDates.addAll(expensesByDate.keySet());

        return allDates.stream()
                .map(date -> new DailyCashflowDTO(
                        date,
                        incomeByDate.getOrDefault(date, BigDecimal.ZERO),
                        expensesByDate.getOrDefault(date, BigDecimal.ZERO)
                ))
                .collect(Collectors.toList());
    }

    private List<CategorySpendingDTO> buildSpendingByCategory(List<Transaction> transactions, BigDecimal totalExpenses) {
        Map<String, BigDecimal> byCategory = new LinkedHashMap<>();

        transactions.stream()
                .filter(t -> t.getTransactionType() == TransactionType.EXPENSE && t.getCategory() != null)
                .forEach(t -> byCategory.merge(t.getCategory().getName(), t.getAmount(), (a, b) -> a.add(b)));

        return byCategory.entrySet().stream()
                .sorted(Map.Entry.<String, BigDecimal>comparingByValue().reversed())
                .map(e -> {
                    BigDecimal percentage = totalExpenses.compareTo(BigDecimal.ZERO) > 0
                            ? e.getValue().divide(totalExpenses, 4, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100))
                            : BigDecimal.ZERO;
                    return new CategorySpendingDTO(e.getKey(), e.getValue(), percentage);
                })
                .collect(Collectors.toList());
    }

    private DateRange resolvePeriod(String period) {
        LocalDate now = LocalDate.now();
        if ("THIS_MONTH".equals(period)) {
            return new DateRange(now.withDayOfMonth(1), now);
        } else if ("LAST_MONTH".equals(period)) {
            LocalDate first = now.minusMonths(1).withDayOfMonth(1);
            return new DateRange(first, first.with(TemporalAdjusters.lastDayOfMonth()));
        }
        return new DateRange(null, null);
    }

    private record DateRange(LocalDate startDate, LocalDate endDate) {}
}
