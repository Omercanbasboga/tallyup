package dev.omercanbasboga.tallyup.repository;

import dev.omercanbasboga.tallyup.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MemberRepository extends JpaRepository<Member, Long> {
    List<Member> findByGroupId(Long groupId);
}
