package dev.omercanbasboga.tallyup.dto;

public record BalanceResponse(Long memberId, String memberName, long amountCents) {
}
