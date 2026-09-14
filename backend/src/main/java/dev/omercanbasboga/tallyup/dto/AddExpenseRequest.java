package dev.omercanbasboga.tallyup.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.List;

public record AddExpenseRequest(
        @NotBlank String description,
        @Positive long amountCents,
        @NotNull Long paidByMemberId,
        @NotEmpty List<Long> participantMemberIds
) {
}
