package com.guestlogix.traveler.fragments;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.guestlogix.traveler.R;
import com.guestlogix.traveler.models.Profile;
import com.guestlogix.traveler.network.Guest;
import com.guestlogix.traveleruikit.fragments.BaseFragment;

/**
 * A fragment which allows the user to edit his/her profile information
 */
@SuppressWarnings("unused")
public class EditProfileFragment extends BaseFragment implements View.OnTouchListener {

    // Views
    private TextInputEditText firstName;
    private TextInputEditText lastName;
    private TextInputEditText email;
    private TextInputLayout firstNameContainer;
    private TextInputLayout lastNameContainer;
    private TextInputLayout emailContainer;
    private ImageView profilePicture;
    private static final String emailRegex = "^([_a-zA-Z0-9-]+(\\.[_a-zA-Z0-9-]+)*@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*(\\.[a-zA-Z]{1,6}))?$";
    private Drawable clearIcon;
    private boolean didMakeChanges = false;
    private Profile profile;

    public EditProfileFragment() {
        // Do nothing.
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        profile = Guest.getInstance().getSignedInUser(getActivityContext());
        clearIcon = getResources().getDrawable(R.drawable.ic_cancel_gray_24dp);

        int w = clearIcon.getIntrinsicWidth();
        int h = clearIcon.getIntrinsicHeight();

        clearIcon.setBounds(0, 0, w, h);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_edit_profile, container, false);

        firstName = v.findViewById(R.id.editText_editProfile_firstName);
        lastName = v.findViewById(R.id.editText_editProfile_lastName);
        email = v.findViewById(R.id.editText_editProfile_email);
        firstNameContainer = v.findViewById(R.id.textInputLayout_editProfile_firstNameContainer);
        lastNameContainer = v.findViewById(R.id.textInputLayout_editProfile_lastNameContainer);
        emailContainer = v.findViewById(R.id.textInputLayout_editProfile_emailContainer);
        profilePicture = v.findViewById(R.id.imageView_editProfile_userPicture);

        if (profile != null) {
            firstName.setText(profile.getFirstName());
            lastName.setText(profile.getLastName());
            email.setText(profile.getEmail());
        }

        firstName.addTextChangedListener(new TextWatcherWrapper(firstName));
        lastName.addTextChangedListener(new TextWatcherWrapper(lastName));
        email.addTextChangedListener(new TextWatcherWrapper(email));

        firstName.setOnTouchListener(this);
        lastName.setOnTouchListener(this);
        email.setOnTouchListener(this);

        firstName.setOnFocusChangeListener(this::onEditTextFocusChange);
        lastName.setOnFocusChangeListener(this::onEditTextFocusChange);
        email.setOnFocusChangeListener(this::onEditTextFocusChange);

        Button button = v.findViewById(R.id.button_editProfile_submit);
        button.setOnClickListener(this::onSaveClick);

        ImageButton cancel = v.findViewById(R.id.imageButton_editProfile_cancel);
        cancel.setOnClickListener(this::onCancelClick);

        return v;
    }

    private void onSaveClick(View _button) {
        boolean valid = true;
        String fName = firstName.getText().toString();

        if (fName.isEmpty()) {
            firstNameContainer.setError(getString(R.string.required));
            valid = false;
        } else {
            profile.setFirstName(fName);
        }

        String lName = lastName.getText().toString();

        if (fName.isEmpty()) {
            lastNameContainer.setError(getString(R.string.required));
            valid = false;
        } else {
            profile.setLastName(lName);
        }

        String emailAddress = email.getText().toString();

        if (emailAddress.isEmpty()) {
            emailContainer.setError(getString(R.string.required));
            valid = false;
        } else {
            profile.setEmail(emailAddress);
        }

        if (!emailAddress.matches(emailRegex)) {
            emailContainer.setError(getString(R.string.invalid_email));
            valid = false;
        }

        if (valid) {
            profile.save(getActivityContext());
            navigateBack();
        }
    }

    private void onCancelClick(View _cancel) {
        if (didMakeChanges) {
            final Dialog d = new Dialog(getActivityContext());

            d.setContentView(R.layout.dialog_alert);
            TextView dTitle = d.findViewById(R.id.textView_alertDialog_title);
            TextView msg = d.findViewById(R.id.textView_alertDialog_message);
            Button cancel = d.findViewById(R.id.button_alertDialog_negativeButton);
            Button ok = d.findViewById(R.id.button_alertDialog_positiveButton);

            dTitle.setVisibility(View.GONE);
            msg.setText(R.string.unsaved_changes_lost);
            cancel.setText(R.string.cancel);
            ok.setText(R.string.ok);

            cancel.setOnClickListener(v -> d.dismiss());
            ok.setOnClickListener(v -> {
                d.dismiss();
                navigateBack();
            });

            d.show();
        } else {
            navigateBack();
        }
    }

    private void navigateBack() {
        NavController nav = Navigation.findNavController(getActivityContext(), R.id.nav_app_settings);
        NavDirections action = EditProfileFragmentDirections.actionEditProfileDestinationToProfileDestination();
        nav.navigate(action);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        TextInputEditText et = (TextInputEditText) v;
        if (event.getAction() == MotionEvent.ACTION_UP && et.getCompoundDrawables()[2] != null) {
            if (event.getX() >= (et.getRight() - et.getLeft() - et.getCompoundDrawables()[2].getBounds().width())) {
                et.setText("");
            }
        }

        return false;
    }

    /*
        If the edit text loses focus, hides the clear icon.
        If it gains focus and has a non empty value, displays the clear icon
     */
    private void onEditTextFocusChange(View v, boolean hasFocus) {
        TextInputEditText et = (TextInputEditText) v;

        if (!hasFocus) {
            et.setCompoundDrawables(null, null, null, null);
        } else if (et.getText() != null && et.getText().length() > 0) {
            et.setCompoundDrawables(null, null, clearIcon, null);
        }
    }

    private class TextWatcherWrapper implements TextWatcher {
        TextInputEditText editText;

        TextWatcherWrapper(TextInputEditText et) {
            editText = et;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            // Do nothing.
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            // Do nothing.
        }

        @Override
        public void afterTextChanged(Editable s) {
            didMakeChanges = true;
            if (s.length() > 0) {
                editText.setCompoundDrawables(null, null, clearIcon, null);
            } else {
                editText.setCompoundDrawables(null, null, null, null);
            }
        }
    }

}
