package dev.omercanbasboga.tallyup.service;

import dev.omercanbasboga.tallyup.model.Expense;
import dev.omercanbasboga.tallyup.model.ExpenseShare;
import dev.omercanbasboga.tallyup.repository.ExpenseRepository;
import dev.omercanbasboga.tallyup.repository.ExpenseShareRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class SettlementService {

    private final ExpenseRepository expenseRepository;
    private final ExpenseShareRepository expenseShareRepository;

    public SettlementService(ExpenseRepository expenseRepository, ExpenseShareRepository expenseShareRepository) {
        this.expenseRepository = expenseRepository;
        this.expenseShareRepository = expenseShareRepository;
    }

    public Map<Long, Long> getBalances(Long groupId) {
        List<Expense> expenses = expenseRepository.findByGroupIdOrderByCreatedAtDesc(groupId);
        List<Long> expenseIds = expenses.stream().map(Expense::getId).collect(Collectors.toList());
        List<ExpenseShare> shares = expenseShareRepository.findByExpenseIdIn(expenseIds);
        return BalanceCalculator.calculate(expenses, shares);
    }

    public List<Transfer> getSettlementPlan(Long groupId) {
        return DebtSettler.settle(getBalances(groupId));
    }
}
