package dev.omercanbasboga.tallyup.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.time.Instant;

@Entity
public class Expense {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long groupId;

    private String description;

    // stored in cents, never as a float/double - see ExpenseShareSplitter for why
    private long amountCents;

    private Long paidByMemberId;

    private Instant createdAt = Instant.now();

    protected Expense() {
    }

    public Expense(Long groupId, String description, long amountCents, Long paidByMemberId) {
        this.groupId = groupId;
        this.description = description;
        this.amountCents = amountCents;
        this.paidByMemberId = paidByMemberId;
    }

    public Long getId() {
        return id;
    }

    public Long getGroupId() {
        return groupId;
    }

    public String getDescription() {
        return description;
    }

    public long getAmountCents() {
        return amountCents;
    }

    public Long getPaidByMemberId() {
        return paidByMemberId;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}
