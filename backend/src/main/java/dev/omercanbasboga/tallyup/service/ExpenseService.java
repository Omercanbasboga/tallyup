package dev.omercanbasboga.tallyup.service;

import dev.omercanbasboga.tallyup.exception.BadRequestException;
import dev.omercanbasboga.tallyup.model.Expense;
import dev.omercanbasboga.tallyup.model.ExpenseShare;
import dev.omercanbasboga.tallyup.repository.ExpenseRepository;
import dev.omercanbasboga.tallyup.repository.ExpenseShareRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
public class ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final ExpenseShareRepository expenseShareRepository;

    public ExpenseService(ExpenseRepository expenseRepository, ExpenseShareRepository expenseShareRepository) {
        this.expenseRepository = expenseRepository;
        this.expenseShareRepository = expenseShareRepository;
    }

    @Transactional
    public Expense addExpense(Long groupId, String description, long amountCents, Long paidByMemberId, List<Long> participantMemberIds) {
        if (amountCents <= 0) {
            throw new BadRequestException("amount has to be positive");
        }
        if (participantMemberIds == null || participantMemberIds.isEmpty()) {
            throw new BadRequestException("an expense needs at least one participant");
        }

        Expense expense = expenseRepository.save(new Expense(groupId, description, amountCents, paidByMemberId));

        Map<Long, Long> shares = ShareSplitter.splitEqually(amountCents, participantMemberIds);
        for (Map.Entry<Long, Long> entry : shares.entrySet()) {
            expenseShareRepository.save(new ExpenseShare(expense.getId(), entry.getKey(), entry.getValue()));
        }

        return expense;
    }

    public List<Expense> listExpenses(Long groupId) {
        return expenseRepository.findByGroupIdOrderByCreatedAtDesc(groupId);
    }

    public List<ExpenseShare> listShares(Long expenseId) {
        return expenseShareRepository.findByExpenseId(expenseId);
    }
}
