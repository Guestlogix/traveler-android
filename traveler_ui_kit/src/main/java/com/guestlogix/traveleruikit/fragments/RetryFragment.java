package com.guestlogix.traveleruikit.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.guestlogix.traveleruikit.R;

public class RetryFragment extends Fragment {
    public static final String ARG_ERROR_TITLE = "ARG_ERROR_TITLE";
    public static final String ARG_ERROR_MESSAGE = "ARG_ERROR_MESSAGE";
    public static final String ARG_ERROR_ACTION = "ARG_ERROR_ACTION";

    private RetryFragmentInteractionListener onErrorFragmentInteractionListener = null;

    public RetryFragment() {
        // Required empty public constructor
    }

    public static RetryFragment newInstance(String title, String message) {
        RetryFragment fragment = new RetryFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_ERROR_TITLE, title);
        args.putSerializable(ARG_ERROR_MESSAGE, message);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_error, container, false);
        TextView titleTextView = view.findViewById(R.id.textView_errorFragment_title);
        TextView messageTextView = view.findViewById(R.id.textView_errorFragment_message);
        TextView actionTextView = view.findViewById(R.id.textView_errorFragment_action);

        Bundle bundle = getArguments();
        if (null != bundle) {
            String title = bundle.getString(ARG_ERROR_TITLE);
            String message = bundle.getString(ARG_ERROR_MESSAGE);
            String action = bundle.getString(ARG_ERROR_ACTION);

            titleTextView.setText(title);

            if (null != message) {
                messageTextView.setText(message);
            } else {
                messageTextView.setText("Something went wrong");
            }

            if (null != action && !action.isEmpty()) {
                actionTextView.setText(action);
            } else {
                actionTextView.setText("Retry");
            }
        }

        actionTextView.setOnClickListener(actionOnClickListener);

        return view;
    }


    @Override
    public void onDetach() {
        super.onDetach();
        onErrorFragmentInteractionListener = null;
    }

    public void setOnInteractionListener(RetryFragmentInteractionListener onErrorFragmentInteractionListener) {
        this.onErrorFragmentInteractionListener = onErrorFragmentInteractionListener;
    }

    public interface RetryFragmentInteractionListener {
        void onRetry();
    }

    private View.OnClickListener actionOnClickListener = v -> onErrorFragmentInteractionListener.onRetry();
}
