package dev.omercanbasboga.tallyup.dto;

public record TransferResponse(Long fromMemberId, String fromName, Long toMemberId, String toName, long amountCents) {
}
