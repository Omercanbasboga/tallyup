package dev.omercanbasboga.tallyup.service;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DebtSettlerTest {

    @Test
    void twoPeopleNeedExactlyOneTransfer() {
        Map<Long, Long> balances = Map.of(1L, 3000L, 2L, -3000L);

        List<Transfer> transfers = DebtSettler.settle(balances);

        assertEquals(1, transfers.size());
        assertEquals(2L, transfers.get(0).fromMemberId());
        assertEquals(1L, transfers.get(0).toMemberId());
        assertEquals(3000L, transfers.get(0).amountCents());
    }

    @Test
    void everyoneAlreadySettledMeansNoTransfers() {
        Map<Long, Long> balances = Map.of(1L, 0L, 2L, 0L);

        assertTrue(DebtSettler.settle(balances).isEmpty());
    }

    @Test
    void onePersonPaidForEveryoneStarPattern() {
        // A fronted a 4000-cent bill for a group of four, so everyone
        // else owes A a quarter of it
        Map<Long, Long> balances = Map.of(
                1L, 3000L, // A is owed by the other three
                2L, -1000L,
                3L, -1000L,
                4L, -1000L
        );

        List<Transfer> transfers = DebtSettler.settle(balances);

        assertEquals(3, transfers.size());
        for (Transfer t : transfers) {
            assertEquals(1L, t.toMemberId());
            assertEquals(1000L, t.amountCents());
        }
    }

    @Test
    void needsThreeTransfersWhenNobodysBalanceLinesUpWithAnyoneElses() {
        Map<Long, Long> balances = Map.of(
                1L, 6000L,
                2L, 4000L,
                3L, -5000L,
                4L, -5000L
        );

        List<Transfer> transfers = DebtSettler.settle(balances);

        assertEquals(3, transfers.size());
        assertNothingLeftOver(balances, transfers);
    }

    @Test
    void totalMoneyMovedAlwaysEqualsWhatWasOwed() {
        Map<Long, Long> balances = Map.of(
                1L, 12345L,
                2L, 5000L,
                3L, -7345L,
                4L, -6000L,
                5L, -4000L
        );

        List<Transfer> transfers = DebtSettler.settle(balances);
        assertNothingLeftOver(balances, transfers);
    }

    private void assertNothingLeftOver(Map<Long, Long> originalBalances, List<Transfer> transfers) {
        Map<Long, Long> remaining = new java.util.HashMap<>(originalBalances);
        for (Transfer t : transfers) {
            remaining.merge(t.fromMemberId(), t.amountCents(), Long::sum);
            remaining.merge(t.toMemberId(), -t.amountCents(), Long::sum);
        }
        remaining.forEach((memberId, balance) ->
                assertEquals(0L, balance, "member " + memberId + " should be settled to zero"));
    }
}
