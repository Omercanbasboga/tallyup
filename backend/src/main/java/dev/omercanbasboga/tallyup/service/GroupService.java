package dev.omercanbasboga.tallyup.service;

import dev.omercanbasboga.tallyup.exception.NotFoundException;
import dev.omercanbasboga.tallyup.model.Group;
import dev.omercanbasboga.tallyup.model.Member;
import dev.omercanbasboga.tallyup.repository.GroupRepository;
import dev.omercanbasboga.tallyup.repository.MemberRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GroupService {

    private final GroupRepository groupRepository;
    private final MemberRepository memberRepository;

    public GroupService(GroupRepository groupRepository, MemberRepository memberRepository) {
        this.groupRepository = groupRepository;
        this.memberRepository = memberRepository;
    }

    public Group createGroup(String name) {
        return groupRepository.save(new Group(name));
    }

    public List<Group> listGroups() {
        return groupRepository.findAll();
    }

    public Group getGroup(Long groupId) {
        return groupRepository.findById(groupId)
                .orElseThrow(() -> new NotFoundException("no group with id " + groupId));
    }

    public Member addMember(Long groupId, String name) {
        getGroup(groupId); // throws if the group doesn't exist
        return memberRepository.save(new Member(groupId, name));
    }

    public List<Member> listMembers(Long groupId) {
        return memberRepository.findByGroupId(groupId);
    }
}
