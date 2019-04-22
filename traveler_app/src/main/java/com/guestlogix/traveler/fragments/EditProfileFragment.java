package com.guestlogix.traveler.fragments;

import android.annotation.SuppressLint;
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
    // TODO: Add profile picture
    private ImageView profilePicture;

    private Profile profile;
    private Drawable clearIcon;

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

        // TODO: Handle null profile.
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

        if (valid) {
            profile.save(getActivityContext());
            NavController nav = Navigation.findNavController(getActivityContext(), R.id.nav_app_settings);
            NavDirections action = EditProfileFragmentDirections.actionEditProfileDestinationToProfileDestination();
            nav.navigate(action);
        }
    }

    private void onCancelClick(View _cancel) {
        // TODO: Get dialog
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
            if (s.length() > 0) {
                editText.setCompoundDrawables(null, null, clearIcon, null);
            } else {
                editText.setCompoundDrawables(null, null, null, null);
            }
        }
    }


}
