package com.guestlogix.traveleruikit.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.guestlogix.travelercorekit.models.CancellationReason;
import com.guestlogix.traveleruikit.R;
import com.guestlogix.traveleruikit.adapters.CancellationReasonSpinnerAdapter;

import java.util.ArrayList;
import java.util.List;

public class CancellationReasonsFragment extends Fragment {
    private final static String TAG = "CatalogFragment";
    private static final String ARG_CANCELLATION_REASONS = "arg_cancellation_reasons";

    private List<CancellationReason> cancellationReasons;

    public static CancellationReasonsFragment newInstance(List<CancellationReason> cancellationReasons) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_CANCELLATION_REASONS, new ArrayList<>(cancellationReasons));
        CancellationReasonsFragment fragment = new CancellationReasonsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getArguments();
        if (bundle == null || !bundle.containsKey(ARG_CANCELLATION_REASONS)) {
            Log.e(TAG, "No CancellationReasons in arguments");
            return;
        }

        cancellationReasons = (List<CancellationReason>) bundle.get(ARG_CANCELLATION_REASONS);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cancellation_reasons, container, false);

        Spinner cancellationReasonsSpinner = view.findViewById(R.id.cancellation_reasons_spinner);

        CancellationReasonSpinnerAdapter adapter = new CancellationReasonSpinnerAdapter(requireContext(), android.R.layout.simple_spinner_item, cancellationReasons);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        cancellationReasonsSpinner.setAdapter(adapter);

        EditText cancellationReasonEditText = view.findViewById(R.id.cancellation_reason_text);

        cancellationReasonsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                CancellationReason reason = (CancellationReason) cancellationReasonsSpinner.getSelectedItem();
                cancellationReasonEditText.setVisibility(reason.isExplanationRequired() ? View.VISIBLE : View.GONE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        return view;
    }
}
