package dev.omercanbasboga.tallyup.service;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ShareSplitterTest {

    @Test
    void splitsEvenlyWhenItDividesCleanly() {
        Map<Long, Long> shares = ShareSplitter.splitEqually(9000, List.of(1L, 2L, 3L));

        assertEquals(3000L, shares.get(1L));
        assertEquals(3000L, shares.get(2L));
        assertEquals(3000L, shares.get(3L));
    }

    @Test
    void handsTheOddCentsToTheFirstFewPeople() {
        // 1000 cents / 3 = 333 with 1 left over, so the first person
        // in the list gets the extra cent
        Map<Long, Long> shares = ShareSplitter.splitEqually(1000, List.of(1L, 2L, 3L));

        assertEquals(334L, shares.get(1L));
        assertEquals(333L, shares.get(2L));
        assertEquals(333L, shares.get(3L));

        long total = shares.values().stream().mapToLong(Long::longValue).sum();
        assertEquals(1000L, total);
    }

    @Test
    void neverLosesOrGainsACentNoMatterHowManyPeopleAreInvolved() {
        for (int n = 1; n <= 13; n++) {
            List<Long> memberIds = java.util.stream.LongStream.rangeClosed(1, n).boxed().toList();
            Map<Long, Long> shares = ShareSplitter.splitEqually(9999, memberIds);
            long total = shares.values().stream().mapToLong(Long::longValue).sum();
            assertEquals(9999L, total, "split among " + n + " people should still add up to 99.99");
        }
    }

    @Test
    void oneParticipantGetsTheWholeBill() {
        Map<Long, Long> shares = ShareSplitter.splitEqually(1234, List.of(7L));
        assertEquals(1234L, shares.get(7L));
    }

    @Test
    void rejectsAnEmptyParticipantList() {
        assertThrows(IllegalArgumentException.class, () -> ShareSplitter.splitEqually(1000, List.of()));
    }
}
