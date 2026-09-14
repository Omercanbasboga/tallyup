package dev.omercanbasboga.tallyup.service;

import dev.omercanbasboga.tallyup.model.Expense;
import dev.omercanbasboga.tallyup.model.ExpenseShare;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class BalanceCalculator {

    private BalanceCalculator() {
    }

    /**
     * Net balance per member: the payer of an expense gets credited the
     * full amount they fronted, then everyone with a share in that
     * expense (payer included, if they were also a participant) gets
     * debited their share. What's left over per person is what the
     * group owes them, or what they owe the group.
     */
    public static Map<Long, Long> calculate(List<Expense> expenses, List<ExpenseShare> shares) {
        Map<Long, Long> balances = new HashMap<>();

        for (Expense expense : expenses) {
            balances.merge(expense.getPaidByMemberId(), expense.getAmountCents(), Long::sum);
        }

        for (ExpenseShare share : shares) {
            balances.merge(share.getMemberId(), -share.getShareCents(), Long::sum);
        }

        return balances;
    }
}
