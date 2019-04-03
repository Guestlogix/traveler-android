package com.guestlogix.traveler.fragments;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.guestlogix.traveler.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class AppSettingsFragment extends Fragment implements View.OnClickListener {
    List<String> actions = new ArrayList<>();

    public AppSettingsFragment() {
        // Do nothing.
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.app_settings_fragment, container, false);

        // Items
        actions.add(getString(R.string.support));
        actions.add(getString(R.string.about));
        actions.add(getString(R.string.faq));
        actions.add(getString(R.string.legal));
        actions.add(getString(R.string.privacy_policy));
        actions.add(getString(R.string.delete_profile));
        actions.add(getString(R.string.sign_out));

        RecyclerView recyclerView = v.findViewById(R.id.recyclerView_appSettings_actionsList);
        recyclerView.setAdapter(new SettingAdapter());

        LinearLayoutManager lm = new LinearLayoutManager(v.getContext());
        recyclerView.setLayoutManager(lm);
        recyclerView.addItemDecoration(new DividerItemDecoration(v.getContext(), lm.getOrientation()));

        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }


    @Override
    public void onClick(View v) {
        int position = (int) v.getTag();

        switch (position) {
            case 0:
                break;
            case 1:
                break;
            case 2:
                break;
            case 3:
                break;
            case 4:
                break;
            case 5:
                break;
            case 6:
                break;
        }
    }

    class SettingAdapter extends RecyclerView.Adapter<ViewHolder> {
        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(getActivity()).inflate(R.layout.item_action_settings, parent, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            holder.title.setText(actions.get(position));
            holder.itemView.setTag(position);
            holder.itemView.setOnClickListener(AppSettingsFragment.this);
        }

        @Override
        public int getItemCount() {
            return actions.size();
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView title;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setClickable(true);
            title = itemView.findViewById(R.id.textView_appSettings_title);
        }
    }
}
