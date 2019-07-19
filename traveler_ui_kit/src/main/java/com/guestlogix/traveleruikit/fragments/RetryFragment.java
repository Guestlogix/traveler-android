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

public class RetryFragment extends Fragment {
    public interface InteractionListener {
        void onRetry();
    }

    public static final String ARG_ERROR_TITLE = "ARG_ERROR_TITLE";
    public static final String ARG_ERROR_MESSAGE = "ARG_ERROR_MESSAGE";
    public static final String ARG_ERROR_ACTION = "ARG_ERROR_ACTION";

    private InteractionListener interactionListener = null;

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

        actionTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (interactionListener != null)
                    interactionListener.onRetry();
            }
        });

        return view;
    }

    public void setInteractionListener(InteractionListener interactionListener) {
        this.interactionListener = interactionListener;
    }
}
