package com.idms.repo;

import com.idms.entity.SideNote;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SideNoteRepository extends JpaRepository<SideNote, Long> {
    List<SideNote> findByAccountId(Integer accountId);
    Optional<SideNote> findByAccountIdAndNoteType(Integer accountId, String noteType);
}
