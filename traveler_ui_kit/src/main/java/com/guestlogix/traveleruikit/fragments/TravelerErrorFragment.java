package com.guestlogix.traveleruikit.fragments;


import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.guestlogix.traveleruikit.R;

import static com.guestlogix.traveleruikit.activities.CatalogItemDetailsActivity.*;

/**
 * A simple {@link Fragment} subclass.
 */
public class TravelerErrorFragment extends Fragment {

    public static final String ARG_ERROR_TITLE = "error_title";
    public static final String ARG_ERROR_MESSAGE = "error_message";
    public static final String ARG_ERROR_ACTION = "error_action";

    private TextView titleTextView;
    private TextView messageTextView;
    private TextView actionTextView;
    private View view;

    private OnErrorInteractionListener mListener;

    public TravelerErrorFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_traveler_error, container, false);
        titleTextView = view.findViewById(R.id.titleTextView);
        messageTextView = view.findViewById(R.id.messageTextView);
        actionTextView = view.findViewById(R.id.actionTextView);

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
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnErrorInteractionListener) {
            mListener = (OnErrorInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnErrorInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnErrorInteractionListener {
        void onRetry();
    }

    private View.OnClickListener actionOnClickListener = v -> mListener.onRetry();
}
