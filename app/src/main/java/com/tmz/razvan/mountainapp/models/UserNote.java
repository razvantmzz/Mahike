package com.tmz.razvan.mountainapp.models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class UserNote extends BaseFeature {

    public static UserNote newNoteFromNote(UserNote note)
    {
        UserNote newNote = new UserNote();
        newNote.setAuthorId(note.getAuthorId());
        newNote.setCoordinates(note.getCoordinates());
        newNote.setCreateDate(note.getCreateDate());
        newNote.setHikeId(note.getHikeId());
        newNote.setId(note.getId());
        newNote.setImageUrlList(note.getImageUrlList());
        newNote.setContent(note.getContent());
        newNote.setTitle(note.getTitle());

        return newNote;
    }
}
