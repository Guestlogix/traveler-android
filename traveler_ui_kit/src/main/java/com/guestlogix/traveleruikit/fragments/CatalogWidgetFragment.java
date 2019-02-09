package com.guestlogix.traveleruikit.fragments;


import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import com.guestlogix.travelercorekit.models.CatalogQuery;
import com.guestlogix.travelercorekit.models.Flight;
import com.guestlogix.traveleruikit.R;
import com.guestlogix.traveleruikit.viewmodels.CatalogWidgetViewModel;

import java.util.ArrayList;
import java.util.List;

import static com.guestlogix.traveleruikit.fragments.TravelerErrorFragment.ARG_ERROR_ACTION;
import static com.guestlogix.traveleruikit.fragments.TravelerErrorFragment.ARG_ERROR_MESSAGE;
import static com.guestlogix.traveleruikit.fragments.TravelerErrorFragment.ARG_ERROR_TITLE;

/**
 * A widget to show catalog. Add a fragment tag in layout.
 */
public class CatalogWidgetFragment extends Fragment implements TravelerErrorFragment.OnErrorInteractionListener {
    private NavController navController;
    private View catalogFragmentView;
    private CatalogWidgetViewModel catalogWidgetViewModel;
    private List<Flight> flights = new ArrayList<>();
    public CatalogWidgetFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        catalogFragmentView = inflater.inflate(R.layout.fragment_catalog_widget, container, false);
        return catalogFragmentView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(catalogFragmentView.findViewById(R.id.catalogHostFragment));

        catalogWidgetViewModel = ViewModelProviders.of(getActivity()).get(CatalogWidgetViewModel.class);
        catalogWidgetViewModel.getViewStateObservable().observe(this, this::onStateChange);

        updateCatalog(new ArrayList<>());
    }

    public void updateCatalog(List<Flight> flights) {
        this.flights = flights;
        fetchCatalog();
    }

    private void fetchCatalog(){
        CatalogQuery catalogQuery = new CatalogQuery(flights);
        catalogWidgetViewModel.updateCatalog(catalogQuery);
    }

    private void onStateChange(CatalogWidgetViewModel.CatalogWidgetViewState state) {
        switch (state) {
            case LOADING:
                navController.navigate(R.id.loading_action);
                break;
            case SUCCESS:
                navController.navigate(R.id.catalog_destination);
                break;
            case ERROR:
                Bundle arguments = new Bundle();
                arguments.putString(ARG_ERROR_TITLE, getString(R.string.label_sorry));
                arguments.putString(ARG_ERROR_MESSAGE, getString(R.string.label_nothing_to_show));
                arguments.putString(ARG_ERROR_ACTION, getString(R.string.try_again));

                navController.navigate(R.id.error_action, arguments);
                break;
        }
    }

    @Override
    public void onRetry() {
        fetchCatalog();
    }
}
