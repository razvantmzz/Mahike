package com.tmz.razvan.mountainapp.baseClasses;

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
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.bumptech.glide.Glide;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.tmz.razvan.mountainapp.Adapters.ImageListRecyclerViewAdapter;
import com.tmz.razvan.mountainapp.Constants.NavigationContants;
import com.tmz.razvan.mountainapp.Helpers.FirebaseHelper;
import com.tmz.razvan.mountainapp.Listeners.OnMultipleFilesUploadListener;
import com.tmz.razvan.mountainapp.Listeners.OnSwipeTouchListener;
import com.tmz.razvan.mountainapp.R;
import com.tmz.razvan.mountainapp.TranslationConstants;
import com.tmz.razvan.mountainapp.models.BaseFeature;

import java.util.ArrayList;
import java.util.List;

public abstract class AddBaseActivity extends BaseAppCompat implements ImageListRecyclerViewAdapter.ItemClickListener {

    public List<String> mUriList = new ArrayList<>();
    public List<String> mNewUriList = new ArrayList<>();
    private final int PICK_IMAGE_REQUEST = 71;

    public BaseFeature mCurrentBaseFeature;

    public EditText mTitleEditText;
    public EditText mContentEditText;
    public ToggleButton mEditSaveButton;
    public Button mAddPhotoButton;
    public RecyclerView mImageRecyclerView;
    public  ImageView mPreviewImageView;
    public boolean shouldEdit = false;
    private ImageListRecyclerViewAdapter mImageAdapter;
    private FirebaseStorage storage;
    public StorageReference storageReference;
    public StorageReference ref;
    private int upload_count = 0;
    int numberOfColumns = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
            InitNote(mCurrentBaseFeature);
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
                    mCurrentBaseFeature.setContent(mContentEditText.getText().toString());
                    mCurrentBaseFeature.setTitle(mTitleEditText.getText().toString());

                    uploadImage(mCurrentBaseFeature);

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

    public abstract void SyncFeatureData();

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
        mPreviewImageView = findViewById(R.id.iv_base_feature_preview);

        mPreviewImageView.setOnTouchListener(new OnSwipeTouchListener(AddBaseActivity.this) {
            @Override
            public void onSwipeRight() {
                listPos -= 1;
                if (listPos >= 0) {
                    Glide.with(AddBaseActivity.this)
                            .load(mUriList.get(listPos))
                            .placeholder(R.drawable.ic_load_image)
                            .into(mPreviewImageView);
                }
            }

            @Override
            public void onSwipeLeft() {
                listPos += 1;
                if (listPos < mUriList.size()) {
                    Glide.with(AddBaseActivity.this)
                            .load(mUriList.get(listPos))
                            .placeholder(R.drawable.ic_load_image)
                            .into(mPreviewImageView);                }
            }
        });
    }
    private void InitNote(BaseFeature baseFeature)
    {
        mContentEditText.setText(baseFeature.getContent());
        mTitleEditText.setText((baseFeature.getTitle()));
        SetToolbarTitle(baseFeature.getTitle());
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

    private void uploadImage(final BaseFeature feature) {
        if(mNewUriList != null && mNewUriList.size() > 0)
        {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog. setTitle("Uploading...");
            progressDialog.show();
//            ref = storageReference.child("userImages/").child(UserCore.Instance().User.getUserId() + "/").child("userNoteData")
//                    .child(feature.getHikeId()).child(feature.getId());
            if (ref == null)
            {
                Toast.makeText(AddBaseActivity.this, "Implement var ref", Toast.LENGTH_SHORT).show();
                return;
            }
            FirebaseHelper firebaseHelper = new FirebaseHelper();
            firebaseHelper.uploadMultipleImages(mNewUriList, ref).setOnMultipleFilesUploadListener(new OnMultipleFilesUploadListener() {
                @Override
                public void onUploadSucces(List<String> urlList) {
                    feature.getImageUrlList().addAll(urlList);
                    FirebaseHelper.Instance().SyncUserData();
                    SyncFeatureData();
                    mNewUriList.clear();
                    Snackbar.make(mContentEditText, "Note saved", Snackbar.LENGTH_LONG)
                            .setAction("Action", null)
                            .show();
                    progressDialog.dismiss();
                }
            });
        }
        else {
            SyncFeatureData();
            FirebaseHelper.Instance().SyncUserData();
            Snackbar.make(mContentEditText, "Note saved", Snackbar.LENGTH_LONG)
                    .setAction("Action", null)
                    .show();
        }
    }

    int listPos;

    @Override
    public void onItemClick(View view, int position) {
        listPos = position;
        mPreviewImageView.setVisibility(View.VISIBLE);
        Glide.with(this).load(mUriList.get(position)).into(mPreviewImageView);
    }

    @Override
    public void onBackPressed() {
        if(mPreviewImageView.getVisibility() == View.VISIBLE)
        {
            mPreviewImageView.setVisibility(View.GONE);
            return;
        }
        super.onBackPressed();
    }
}



