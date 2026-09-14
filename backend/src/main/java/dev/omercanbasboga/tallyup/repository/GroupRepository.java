package dev.omercanbasboga.tallyup.repository;

import dev.omercanbasboga.tallyup.model.Group;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupRepository extends JpaRepository<Group, Long> {
}
