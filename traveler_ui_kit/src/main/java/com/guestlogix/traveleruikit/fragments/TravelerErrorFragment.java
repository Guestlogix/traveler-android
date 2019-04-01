package com.guestlogix.traveleruikit.fragments;


import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.guestlogix.traveleruikit.R;

/**
 * Abstract Fragment to show error state with title, message and action.
 * Set title message and action via bundle arguments.
 * Subclass must implement {@link #onAttachFragment}
 */
public abstract class TravelerErrorFragment extends Fragment {

    public static final String ARG_ERROR_TITLE = "error_title";
    public static final String ARG_ERROR_MESSAGE = "error_message";
    public static final String ARG_ERROR_ACTION = "error_action";

    OnErrorInteractionListener onErrorFragmentInteractionListener = null;

    abstract void onAttachFragment(Context context);

    public TravelerErrorFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_traveler_error, container, false);
        TextView titleTextView = view.findViewById(R.id.textView_errorFragment_title);
        TextView messageTextView = view.findViewById(R.id.textView_errorFragment_message);
        TextView actionTextView = view.findViewById(R.id.textView_errorFragment_action);

        Bundle bundle = getArguments();
        if (null != bundle) {
            String title = bundle.getString(ARG_ERROR_TITLE);
            String message = bundle.getString(ARG_ERROR_MESSAGE);
            String action = bundle.getString(ARG_ERROR_ACTION);

            if (null != title && !title.isEmpty()) {
                titleTextView.setText(title);
            }
            if (null != message && !message.isEmpty()) {
                messageTextView.setText(message);
            }
            if (null != action && !action.isEmpty()) {
                actionTextView.setText(action);
            }
        }

        actionTextView.setOnClickListener(actionOnClickListener);

        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        onAttachFragment(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        onErrorFragmentInteractionListener = null;
    }

    public interface OnErrorInteractionListener {
        void onRetry();
    }

    private View.OnClickListener actionOnClickListener = v -> onErrorFragmentInteractionListener.onRetry();
}
