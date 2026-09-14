package dev.omercanbasboga.tallyup.service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Splits a bill into per-person shares that always add back up to the
 * original amount. Amounts are cents (longs), never double - splitting
 * 10.00 three ways as a double gives you 3.333333333333333 and rounding
 * errors that don't sum back to 10.00. Doing it as 1000 cents / 3 lets
 * us hand out the leftover cent deterministically instead.
 */
public final class ShareSplitter {

    private ShareSplitter() {
    }

    public static Map<Long, Long> splitEqually(long totalCents, List<Long> memberIds) {
        if (memberIds.isEmpty()) {
            throw new IllegalArgumentException("can't split an expense with no participants");
        }

        int n = memberIds.size();
        long baseShare = totalCents / n;
        long leftoverCents = totalCents % n;

        Map<Long, Long> shares = new LinkedHashMap<>();
        for (int i = 0; i < n; i++) {
            long share = baseShare;
            if (i < leftoverCents) {
                share += 1;
            }
            shares.put(memberIds.get(i), share);
        }
        return shares;
    }
}
