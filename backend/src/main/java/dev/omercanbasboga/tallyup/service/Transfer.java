package dev.omercanbasboga.tallyup.service;

public record Transfer(Long fromMemberId, Long toMemberId, long amountCents) {
}
