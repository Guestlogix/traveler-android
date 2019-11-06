/*
 * Copyright 2018 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.guestlogix.traveler.fragments;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.preference.EditTextPreference;
import androidx.preference.PreferenceDialogFragmentCompat;

import com.guestlogix.travelercorekit.Router;

/**
 * Fork of androidx.preferences.EditTextPreferenceDialogFragment
 */
public class UrlEditTextPreferenceDialogFragmentCompat extends PreferenceDialogFragmentCompat {
    private static final String ARG_CURRENT_VALUE = "current_value";
    private static final String SAVE_STATE_TEXT = "UrlEditTextPreferenceDialogFragmentCompat.text";

    private EditText mEditText;

    private CharSequence mText;

    static UrlEditTextPreferenceDialogFragmentCompat newInstance(String key, String currentValue) {
        final UrlEditTextPreferenceDialogFragmentCompat
                fragment = new UrlEditTextPreferenceDialogFragmentCompat();
        final Bundle b = new Bundle();
        b.putString(ARG_KEY, key);
        b.putString(ARG_CURRENT_VALUE, currentValue);
        fragment.setArguments(b);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            mText = getEditTextPreference().getText();
            if (mText == null && getArguments() != null) {
                mText = getArguments().getString(ARG_CURRENT_VALUE);
            }
        } else {
            mText = savedInstanceState.getCharSequence(SAVE_STATE_TEXT);
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putCharSequence(SAVE_STATE_TEXT, mText);
    }

    @Override
    protected void onBindDialogView(View view) {
        super.onBindDialogView(view);

        mEditText = view.findViewById(android.R.id.edit);
        mEditText.requestFocus();

        if (mEditText == null) {
            throw new IllegalStateException("Dialog view must contain an EditText with id" +
                    " @android:id/edit");
        }

        mEditText.setText(mText);
        // Place cursor at the end
        mEditText.setSelection(mEditText.getText().length());
    }

    private EditTextPreference getEditTextPreference() {
        return (EditTextPreference) getPreference();
    }

    @Override
    protected boolean needInputMethod() {
        // We want the input method to show, if possible, when dialog is displayed
        return true;
    }

    @Override
    public void onDialogClosed(boolean positiveResult) {

        if (positiveResult) {
            String value = mEditText.getText().toString();
            if (getEditTextPreference().callChangeListener(value)) {
                Router.clearSdkEndpoint();
                getEditTextPreference().setText(value);
            }
        }
    }
}
