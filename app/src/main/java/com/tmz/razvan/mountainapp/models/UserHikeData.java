package com.tmz.razvan.mountainapp.models;

import android.provider.ContactsContract;

import com.google.android.gms.maps.model.LatLng;
import com.tmz.razvan.mountainapp.Core.UserCore;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class UserHikeData {

    private String Id;
    private Boolean IsFavorite;
    private List<UserNote> NoteList;

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public Boolean getFavorite() {
        return IsFavorite;
    }

    public void setFavorite(Boolean favorite) {
        IsFavorite = favorite;
    }

    public List<UserNote> getNoteList() {
        if(NoteList == null)
        {
            NoteList = new ArrayList<UserNote>();
        }
        return NoteList;
    }

    public void setNoteList(List<UserNote> noteList) {
        NoteList = noteList;
    }

    public void addNote(UserNote userNote)
    {
        if(NoteList == null)
        {
            NoteList = new ArrayList<UserNote>();
        }

        for (int i = 0; i < NoteList.size(); i++) {
            if(NoteList.get(i) == null)
            {
                continue;
            }
            if(NoteList.get(i).getId().contains(userNote.getId()))
            {
                NoteList.set(i, userNote);
                return;
            }
        }

        NoteList.add(userNote);
    }

    public UserNote getNoteByCoordinates(Coordinates coord)
    {
        if(NoteList == null)
        {
            NoteList = new ArrayList<UserNote>();
        }

        for(UserNote userNote : NoteList)
        {
            if(userNote == null)
            {
                continue;
            }
            if(userNote.getCoordinates() == coord)
            {
                return userNote;
            }
        }

        UserNote userNote = new UserNote();
        userNote.setId(UUID.randomUUID().toString());
        userNote.setAuthorId(UserCore.Instance().User.getUserId());
        userNote.setCoordinates(coord);

        return userNote;
    }
}
