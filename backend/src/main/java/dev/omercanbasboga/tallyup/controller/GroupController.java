package dev.omercanbasboga.tallyup.controller;

import dev.omercanbasboga.tallyup.dto.AddMemberRequest;
import dev.omercanbasboga.tallyup.dto.CreateGroupRequest;
import dev.omercanbasboga.tallyup.model.Group;
import dev.omercanbasboga.tallyup.model.Member;
import dev.omercanbasboga.tallyup.service.GroupService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/groups")
public class GroupController {

    private final GroupService groupService;

    public GroupController(GroupService groupService) {
        this.groupService = groupService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Group createGroup(@Valid @RequestBody CreateGroupRequest request) {
        return groupService.createGroup(request.name());
    }

    @GetMapping
    public List<Group> listGroups() {
        return groupService.listGroups();
    }

    @GetMapping("/{groupId}")
    public Group getGroup(@PathVariable Long groupId) {
        return groupService.getGroup(groupId);
    }

    @PostMapping("/{groupId}/members")
    @ResponseStatus(HttpStatus.CREATED)
    public Member addMember(@PathVariable Long groupId, @Valid @RequestBody AddMemberRequest request) {
        return groupService.addMember(groupId, request.name());
    }

    @GetMapping("/{groupId}/members")
    public List<Member> listMembers(@PathVariable Long groupId) {
        return groupService.listMembers(groupId);
    }
}
