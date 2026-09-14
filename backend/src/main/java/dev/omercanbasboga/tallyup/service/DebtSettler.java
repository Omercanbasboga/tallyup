package dev.omercanbasboga.tallyup.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

/**
 * Turns a group's net balances into a short list of payments that
 * settles everyone up. Positive balance means the group owes that
 * person money, negative means they owe the group.
 *
 * The approach is the standard greedy one: every round, whoever is owed
 * the most pays off whoever owes the most, for as much as the smaller
 * of the two amounts. That's not guaranteed to be the mathematical
 * minimum number of transfers in every possible case (that version of
 * the problem is a partition problem and gets expensive fast), but it's
 * close to minimal in practice and runs in a few milliseconds even for
 * a group of a hundred people.
 */
public final class DebtSettler {

    private DebtSettler() {
    }

    private static final class Balance {
        final Long memberId;
        long amountCents;

        Balance(Long memberId, long amountCents) {
            this.memberId = memberId;
            this.amountCents = amountCents;
        }
    }

    public static List<Transfer> settle(Map<Long, Long> balances) {
        PriorityQueue<Balance> creditors = new PriorityQueue<>(Comparator.comparingLong((Balance b) -> b.amountCents).reversed());
        PriorityQueue<Balance> debtors = new PriorityQueue<>(Comparator.comparingLong((Balance b) -> b.amountCents).reversed());

        for (Map.Entry<Long, Long> entry : balances.entrySet()) {
            long amount = entry.getValue();
            if (amount > 0) {
                creditors.add(new Balance(entry.getKey(), amount));
            } else if (amount < 0) {
                debtors.add(new Balance(entry.getKey(), -amount));
            }
            // exactly zero means already settled, nothing to add
        }

        List<Transfer> transfers = new ArrayList<>();

        while (!creditors.isEmpty() && !debtors.isEmpty()) {
            Balance biggestCreditor = creditors.poll();
            Balance biggestDebtor = debtors.poll();

            long amount = Math.min(biggestCreditor.amountCents, biggestDebtor.amountCents);
            transfers.add(new Transfer(biggestDebtor.memberId, biggestCreditor.memberId, amount));

            biggestCreditor.amountCents -= amount;
            biggestDebtor.amountCents -= amount;

            if (biggestCreditor.amountCents > 0) {
                creditors.add(biggestCreditor);
            }
            if (biggestDebtor.amountCents > 0) {
                debtors.add(biggestDebtor);
            }
        }

        return transfers;
    }
}
