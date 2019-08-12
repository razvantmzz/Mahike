package com.tmz.razvan.mountainapp.CustomUIViews;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.widget.TextView;

import com.tmz.razvan.mountainapp.R;
import com.tmz.razvan.mountainapp.TranslationConstants;
import com.tmz.razvan.mountainapp.validationCommands.RegexValidationCommand;

public class FormEditText extends android.support.v7.widget.AppCompatEditText {

    private RegexValidationCommand validationCommand;
    private CharSequence originalValue;

    public FormEditText(Context context) {
        super(context);
        init();
    }

    public FormEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public FormEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public RegexValidationCommand getValidationCommand() {
        return validationCommand;
    }

    public void setValidationCommand(RegexValidationCommand validationCommand) {
        this.validationCommand = validationCommand;
    }

    public boolean isValid(boolean showError)
    {
        if(validationCommand == null || getText().toString().equals(""))
        {
            return true;
        }

        if(validationCommand.isValid())
        {
            setBackground(getResources().getDrawable(R.drawable.presenter_edit_text));
            return true;
        }

        setBackground(getResources().getDrawable(R.drawable.custom_edit_text_error_line));
        if(showError)
        {
            Snackbar sn =  Snackbar.make(this, validationCommand.getErrorMessage(), Snackbar.LENGTH_LONG);
            sn.getView().setBackgroundColor(getResources().getColor(R.color.error));
            sn.show();
        }

        return false;
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        super.setText(text, type);
        originalValue = text;
    }

    public void cancelEdit()
    {
        setText(originalValue);
        setBackground(getResources().getDrawable(R.drawable.presenter_edit_text));
    }

    private void init()
    {
        setBackground(getResources().getDrawable(R.drawable.presenter_edit_text));
        setTextColor(getResources().getColorStateList(R.color.edit_text_color));
        originalValue = getText().toString();

        addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                isValid(false);
            }
        });
    }

}
