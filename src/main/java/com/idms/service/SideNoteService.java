package com.idms.service;

import com.idms.dto.SideNoteDTO;
import com.idms.entity.SideNote;

import java.util.List;

public interface SideNoteService {
    SideNote addOrUpdateSideNote(SideNoteDTO sideNoteDTO);
    List<SideNote> getSideNotesByAccountId(Integer accountId);
    List<SideNote> getAllSideNotes();
}

