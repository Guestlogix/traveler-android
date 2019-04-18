package com.guestlogix.traveleruikit.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import com.guestlogix.travelercorekit.models.Catalog;
import com.guestlogix.travelercorekit.models.CatalogItem;
import com.guestlogix.traveleruikit.R;
import com.guestlogix.traveleruikit.TravelerUI;
import com.guestlogix.traveleruikit.widgets.CatalogWidget;

/**
 * Fragment to hold the Catalog widget.
 * <p>
 * TODO: Let other developers override click events.
 */
public class CatalogFragment extends BaseFragment implements CatalogWidget.OnCatalogItemClickListener {

    private static String ARG_CATALOG = "arg_catalog";

    private Catalog catalog;

    public CatalogFragment() {
        // Do nothing.
    }

    public static CatalogFragment newInstance(Catalog catalog) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_CATALOG, catalog);
        CatalogFragment fragment = new CatalogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View catalogFragmentView = inflater.inflate(R.layout.fragment_catalog, container, false);
        CatalogWidget catalogWidget = catalogFragmentView.findViewById(R.id.catalogWidget_catalogFragment_container);
        catalogWidget.setOnCatalogItemClickListener(this);

        Bundle args = getArguments();

        if (null != args && args.containsKey(ARG_CATALOG)) {

            catalog = (Catalog) args.getSerializable(ARG_CATALOG);
            if (catalog != null) {
                catalogWidget.setCatalog(catalog);
            }
        }

        return catalogFragmentView;
    }

    @Override
    public void onCatalogItemClick(int section, int item) {
        CatalogItem catalogItem = catalog.getGroups().get(section).getItems().get(item);

        Intent catalogItemDetailsIntent = TravelerUI.getCatalogItemDetailsIntent(catalogItem, getActivityContext());
        startActivity(catalogItemDetailsIntent);
    }
}


