package dev.omercanbasboga.tallyup.controller;

import dev.omercanbasboga.tallyup.dto.AddExpenseRequest;
import dev.omercanbasboga.tallyup.model.Expense;
import dev.omercanbasboga.tallyup.service.ExpenseService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/groups/{groupId}/expenses")
public class ExpenseController {

    private final ExpenseService expenseService;

    public ExpenseController(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Expense addExpense(@PathVariable Long groupId, @Valid @RequestBody AddExpenseRequest request) {
        return expenseService.addExpense(
                groupId,
                request.description(),
                request.amountCents(),
                request.paidByMemberId(),
                request.participantMemberIds()
        );
    }

    @GetMapping
    public List<Expense> listExpenses(@PathVariable Long groupId) {
        return expenseService.listExpenses(groupId);
    }
}
