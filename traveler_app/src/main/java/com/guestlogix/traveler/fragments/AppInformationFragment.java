package com.guestlogix.traveler.fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import com.guestlogix.traveler.R;
import com.guestlogix.traveleruikit.fragments.BaseFragment;

/**
 * A fragment used to display content with minimal controls.
 * Expects the layoutId and the title in safe args.
 */
public class AppInformationFragment extends BaseFragment {

    @LayoutRes
    private int layoutId;
    private String title;

    public AppInformationFragment() {
        // Do nothing.
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AppInformationFragmentArgs args = AppInformationFragmentArgs.fromBundle(getArguments());
        layoutId = args.getLayoutId();
        title = args.getTitle();
        setHasOptionsMenu(true);
    }

    @NonNull
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(layoutId, container, false);

        ImageButton b = v.findViewById(R.id.imageButton_appSettings_back);

        if (b != null) {
            b.setOnClickListener(_v -> Navigation.findNavController(getActivityContext(), R.id.nav_app_settings).popBackStack());
        }

        TextView tv = v.findViewById(R.id.textView_appSettings_title);

        if (tv != null) {
            tv.setText(title);
        }

        return v;
    }
}
