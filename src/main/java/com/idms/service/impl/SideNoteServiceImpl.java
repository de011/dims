package com.idms.service.impl;

import com.idms.dto.SideNoteDTO;
import com.idms.entity.SideNote;
import com.idms.exception.DuplicateSideNoteException;
import com.idms.exception.InvalidDateFormatException;
import com.idms.exception.SideNoteServiceException;
import com.idms.repo.SideNoteRepository;
import com.idms.service.SideNoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
public class SideNoteServiceImpl implements SideNoteService {
    @Autowired
    private SideNoteRepository sideNoteRepository;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");


    @Override
    public SideNote addOrUpdateSideNote(SideNoteDTO sideNoteDTO) {
        if (sideNoteDTO == null) {
            throw new SideNoteServiceException("SideNote DTO cannot be null.");
        }

        try {
            if (sideNoteDTO.getAccountId() == null) {
                throw new SideNoteServiceException("Account ID cannot be null.");
            }

            if (sideNoteDTO.getNoteType() == null || sideNoteDTO.getNoteType().isEmpty()) {
                throw new SideNoteServiceException("Note Type cannot be null or empty.");
            }

            if (sideNoteDTO.getDescription() == null || sideNoteDTO.getDescription().isEmpty()) {
                throw new SideNoteServiceException("Description cannot be null or empty.");
            }

            Optional<SideNote> existingSideNote = sideNoteRepository.findByAccountIdAndNoteType(sideNoteDTO.getAccountId(), sideNoteDTO.getNoteType());

            if (existingSideNote.isPresent()) {
                throw new DuplicateSideNoteException(
                        "Duplicate SideNote found for Account ID: " + sideNoteDTO.getAccountId() + " and Note Type: " + sideNoteDTO.getNoteType()
                );
            }

            SideNote sideNote = new SideNote();
            sideNote.setAccountId(sideNoteDTO.getAccountId());
            sideNote.setNoteType(sideNoteDTO.getNoteType());
            sideNote.setDescription(sideNoteDTO.getDescription());

            try {
                sideNote.setCreatedDate(LocalDate.parse(sideNoteDTO.getCreatedDate(), formatter));
            } catch (Exception e) {
                throw new InvalidDateFormatException("Invalid date format. Expected yyyy-MM-dd.", e);
            }

            return sideNoteRepository.save(sideNote);

        } catch (SideNoteServiceException | InvalidDateFormatException | DuplicateSideNoteException e) {

            System.out.println("Error: " + e.getMessage());
            throw e;
        } catch (Exception e) {

            System.out.println("Unexpected error: " + e.getMessage());
            throw new SideNoteServiceException("An unexpected error occurred while adding/updating the side note: " + e.getMessage(), e);
        }
    }


    @Override
    public List<SideNote> getSideNotesByAccountId(Integer accountId) {
        try {
            if (accountId == null) {
                throw new SideNoteServiceException("Account ID cannot be null.");
            }
            return sideNoteRepository.findByAccountId(accountId);
        } catch (Exception e) {
            throw new SideNoteServiceException("Error retrieving side notes for account ID " + accountId + ": " + e.getMessage(), e);
        }
    }

    @Override
    public List<SideNote> getAllSideNotes() {
        try {
            return sideNoteRepository.findAll();
        } catch (Exception e) {
            throw new SideNoteServiceException("Error retrieving all side notes: " + e.getMessage(), e);
        }
    }
}
