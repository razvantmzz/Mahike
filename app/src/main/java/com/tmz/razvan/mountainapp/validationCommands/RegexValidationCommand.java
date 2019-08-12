package com.tmz.razvan.mountainapp.validationCommands;

import android.widget.EditText;

import java.util.regex.Pattern;

public class RegexValidationCommand {

    private EditText editText;
    private String errorMessage;
    private String regex;
    private Pattern pattern;

    public RegexValidationCommand(EditText field, String errorMessage)
    {
        this.editText = field;
        this.errorMessage = errorMessage;
        pattern = Pattern.compile(regex);
    }

    public RegexValidationCommand(EditText field, String errorMessage, String regex)
    {
        this.editText = field;
        this.errorMessage = errorMessage;
        this.regex = regex;
        pattern = Pattern.compile(regex);
    }

    public boolean isValid()
    {
        if(pattern.matcher(editText.getText()).matches())
        {
           return true;
        }
        return false;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
