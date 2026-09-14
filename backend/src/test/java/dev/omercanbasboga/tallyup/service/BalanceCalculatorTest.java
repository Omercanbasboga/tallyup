package dev.omercanbasboga.tallyup.service;

import dev.omercanbasboga.tallyup.model.Expense;
import dev.omercanbasboga.tallyup.model.ExpenseShare;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BalanceCalculatorTest {

    @Test
    void payerIsCreditedTheFullAmountAndEveryoneIsDebitedTheirShare() {
        // dinner for three, 3000 cents, member 1 paid, split evenly
        Expense dinner = new Expense(1L, "dinner", 3000, 1L);
        List<ExpenseShare> shares = List.of(
                new ExpenseShare(1L, 1L, 1000),
                new ExpenseShare(1L, 2L, 1000),
                new ExpenseShare(1L, 3L, 1000)
        );

        Map<Long, Long> balances = BalanceCalculator.calculate(List.of(dinner), shares);

        assertEquals(2000L, balances.get(1L)); // paid 3000, owes back 1000 of their own share
        assertEquals(-1000L, balances.get(2L));
        assertEquals(-1000L, balances.get(3L));
    }

    @Test
    void multipleExpensesAccumulateOnTopOfEachOther() {
        Expense lunch = new Expense(1L, "lunch", 2000, 1L);
        Expense taxi = new Expense(1L, "taxi", 1000, 2L);

        List<ExpenseShare> shares = List.of(
                new ExpenseShare(1L, 1L, 1000),
                new ExpenseShare(1L, 2L, 1000),
                new ExpenseShare(2L, 1L, 500),
                new ExpenseShare(2L, 2L, 500)
        );

        Map<Long, Long> balances = BalanceCalculator.calculate(List.of(lunch, taxi), shares);

        // member 1: paid 2000, owes 1000 (lunch) + 500 (taxi) = 1500, net +500
        assertEquals(500L, balances.get(1L));
        // member 2: paid 1000, owes 1000 (lunch) + 500 (taxi) = 1500, net -500
        assertEquals(-500L, balances.get(2L));
    }

    @Test
    void withNoExpensesEverybodyIsAtZero() {
        Map<Long, Long> balances = BalanceCalculator.calculate(List.of(), List.of());
        assertEquals(0, balances.size());
    }
}
