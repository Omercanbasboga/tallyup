package dev.omercanbasboga.tallyup.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

/**
 * One member's slice of one expense. A five-person dinner split into
 * shares generates five of these rows, one per participant, even if
 * that participant is also the payer.
 */
@Entity
public class ExpenseShare {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long expenseId;

    private Long memberId;

    private long shareCents;

    protected ExpenseShare() {
    }

    public ExpenseShare(Long expenseId, Long memberId, long shareCents) {
        this.expenseId = expenseId;
        this.memberId = memberId;
        this.shareCents = shareCents;
    }

    public Long getId() {
        return id;
    }

    public Long getExpenseId() {
        return expenseId;
    }

    public Long getMemberId() {
        return memberId;
    }

    public long getShareCents() {
        return shareCents;
    }
}
