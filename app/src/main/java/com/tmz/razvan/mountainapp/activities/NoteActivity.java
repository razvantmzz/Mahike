package com.tmz.razvan.mountainapp.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ToggleButton;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;
import com.tmz.razvan.mountainapp.Adapters.ImageListRecyclerViewAdapter;
import com.tmz.razvan.mountainapp.Constants.NavigationContants;
import com.tmz.razvan.mountainapp.Core.UserCore;
import com.tmz.razvan.mountainapp.Helpers.FirebaseHelper;
import com.tmz.razvan.mountainapp.Listeners.OnMultipleFilesUploadListener;
import com.tmz.razvan.mountainapp.R;
import com.tmz.razvan.mountainapp.TranslationConstants;
import com.tmz.razvan.mountainapp.baseClasses.BaseAppCompat;
import com.tmz.razvan.mountainapp.models.UserNote;

import java.util.ArrayList;
import java.util.List;

public class NoteActivity extends BaseAppCompat implements ImageListRecyclerViewAdapter.ItemClickListener{

    private List<String> mUriList = new ArrayList<>();
    private List<String> mNewUriList = new ArrayList<>();
    private final int PICK_IMAGE_REQUEST = 71;

    private UserNote mCurrentUserNote;

    private EditText mTitleEditText;
    private EditText mContentEditText;
    private ToggleButton mEditSaveButton;
    private Button mAddPhotoButton;
    private RecyclerView mImageRecyclerView;
    private ImageListRecyclerViewAdapter mImageAdapter;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private StorageReference ref;
    private int upload_count = 0;
    int numberOfColumns = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
        String serialisedHikeModel = getIntent().getStringExtra(NavigationContants.USER_HIKE_DATA_KEY);
        mCurrentUserNote = new Gson().fromJson(serialisedHikeModel, UserNote.class);
        if(mCurrentUserNote.getImageUrlList() != null)
        {
            mUriList.addAll(mCurrentUserNote.getImageUrlList());
        }

        boolean shouldEdit = false;
        shouldEdit = getIntent().getBooleanExtra(NavigationContants.NOTE_SHOULD_EDIT, shouldEdit);
        InitialiseViews();
        SetStrings();
        EnableEditViews(shouldEdit);
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        if(shouldEdit)
        {
            mEditSaveButton.setChecked(shouldEdit);
        }
        else
        {
            InitNote();
        }

        mImageRecyclerView = findViewById(R.id.rv_note_activity);
        mImageRecyclerView.setLayoutManager(new GridLayoutManager(this, numberOfColumns));
        mImageAdapter = new ImageListRecyclerViewAdapter(this, shouldEdit, mUriList);
        mImageAdapter.setClickListener(this);
        mImageRecyclerView.setAdapter(mImageAdapter);

        mEditSaveButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                EnableEditViews(b);

                if(!b)
                {
                    if(!IsValid())
                    {
                        EnableEditViews(true);
                        mEditSaveButton.setChecked(true);
                        return;
                    }
                    mCurrentUserNote.setContent(mContentEditText.getText().toString());
                    mCurrentUserNote.setTitle(mTitleEditText.getText().toString());
                    UserCore.Instance().User.getPrivateHikeDataById(mCurrentUserNote.getHikeId()).addNote(mCurrentUserNote);
                    uploadImage();

//                    FirebaseHelper.Instance().SyncUserData();


                }
            }
        });

        mAddPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseImage();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    //region private methods

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null )
        {
            mUriList.add(data.getData().toString());
            mNewUriList.add(new String(data.getData().toString()));
            mImageAdapter.notifyItemInserted(mUriList.size() - 1);
        }
    }

    private void InitialiseViews()
    {
        mContentEditText = findViewById(R.id.et_base_feature_content);
        mTitleEditText = findViewById(R.id.et_base_feature_title);
        mEditSaveButton = findViewById(R.id.toolbar_edit_save_button);
        mAddPhotoButton = findViewById(R.id.btn_toolbar_add_photo);
        mImageRecyclerView = findViewById(R.id.rv_note_activity);
    }

    private void InitNote()
    {
        mContentEditText.setText(mCurrentUserNote.getContent());
        mTitleEditText.setText((mCurrentUserNote.getTitle()));
//        mUriList = mCurrentBaseFeature.getImageUrlList();
    }

    private void SetStrings()
    {
        mTitleEditText.setHint(TranslationConstants.TITLE_VIEW);
    }

    private void EnableEditViews(boolean enable)
    {
        if(enable)
        {
            mAddPhotoButton.setVisibility(View.VISIBLE);
        }
        else
        {
            mAddPhotoButton.setVisibility(View.INVISIBLE);
        }
        mContentEditText.setEnabled(enable);
        mTitleEditText.setEnabled(enable);
    }

    private boolean IsValid()
    {
        if(mTitleEditText.getText().length() < 5)
        {
            Snackbar.make(Toolbar, String.format(TranslationConstants.LENGHT_ERROR, TranslationConstants.TITLE_VIEW, 5), Snackbar.LENGTH_LONG)
                    .setAction("Action", null)
                    .show();
            return false;
        }

        if(mContentEditText.getText().length() < 5)
        {
            Snackbar.make(Toolbar, String.format(TranslationConstants.LENGHT_ERROR, TranslationConstants.CONTENT_VIEW, 5), Snackbar.LENGTH_LONG)
                    .setAction("Action", null)
                    .show();
            return false;
        }

        return true;
    }

    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    private void uploadImage() {
        if(mNewUriList != null && mNewUriList.size() > 0)
        {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog. setTitle("Uploading...");
            progressDialog.show();
            ref = storageReference.child("userImages/").child(UserCore.Instance().User.getUserId() + "/").child("userNoteData")
                    .child(mCurrentUserNote.getHikeId()).child(mCurrentUserNote.getId());
            FirebaseHelper firebaseHelper = new FirebaseHelper();
            firebaseHelper.uploadMultipleImages(mNewUriList, ref).setOnMultipleFilesUploadListener(new OnMultipleFilesUploadListener() {
                @Override
                public void onUploadSucces(List<String> urlList) {
                    mCurrentUserNote.getImageUrlList().addAll(urlList);
                    FirebaseHelper.Instance().SyncUserData();
                    mNewUriList.clear();
                    Snackbar.make(mContentEditText, "Note saved", Snackbar.LENGTH_LONG)
                            .setAction("Action", null)
                            .show();
                    progressDialog.dismiss();
                }
            });
        }
        else {
            FirebaseHelper.Instance().SyncUserData();
            Snackbar.make(mContentEditText, "Note saved", Snackbar.LENGTH_LONG)
                        .setAction("Action", null)
                        .show();
        }
    }

    @Override
    public void onItemClick(View view, int position) {

    }

    //endregion
}
