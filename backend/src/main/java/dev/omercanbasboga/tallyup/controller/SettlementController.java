package dev.omercanbasboga.tallyup.controller;

import dev.omercanbasboga.tallyup.dto.BalanceResponse;
import dev.omercanbasboga.tallyup.dto.TransferResponse;
import dev.omercanbasboga.tallyup.model.Member;
import dev.omercanbasboga.tallyup.repository.MemberRepository;
import dev.omercanbasboga.tallyup.service.SettlementService;
import dev.omercanbasboga.tallyup.service.Transfer;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/groups/{groupId}")
public class SettlementController {

    private final SettlementService settlementService;
    private final MemberRepository memberRepository;

    public SettlementController(SettlementService settlementService, MemberRepository memberRepository) {
        this.settlementService = settlementService;
        this.memberRepository = memberRepository;
    }

    @GetMapping("/balances")
    public List<BalanceResponse> getBalances(@PathVariable Long groupId) {
        Map<String, String> nameById = nameLookup(groupId);
        return settlementService.getBalances(groupId).entrySet().stream()
                .map(entry -> new BalanceResponse(
                        entry.getKey(),
                        nameById.getOrDefault(String.valueOf(entry.getKey()), "unknown"),
                        entry.getValue()))
                .collect(Collectors.toList());
    }

    @GetMapping("/settlement")
    public List<TransferResponse> getSettlementPlan(@PathVariable Long groupId) {
        Map<String, String> nameById = nameLookup(groupId);
        List<Transfer> transfers = settlementService.getSettlementPlan(groupId);
        return transfers.stream()
                .map(t -> new TransferResponse(
                        t.fromMemberId(),
                        nameById.getOrDefault(String.valueOf(t.fromMemberId()), "unknown"),
                        t.toMemberId(),
                        nameById.getOrDefault(String.valueOf(t.toMemberId()), "unknown"),
                        t.amountCents()))
                .collect(Collectors.toList());
    }

    private Map<String, String> nameLookup(Long groupId) {
        return memberRepository.findByGroupId(groupId).stream()
                .collect(Collectors.toMap(m -> String.valueOf(m.getId()), Member::getName));
    }
}
