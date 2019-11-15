package com.guestlogix.traveleruikit.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.guestlogix.travelercorekit.models.PartnerOffering;
import com.guestlogix.travelercorekit.models.Pass;
import com.guestlogix.travelercorekit.models.Product;
import com.guestlogix.travelercorekit.models.ProductType;
import com.guestlogix.travelercorekit.models.PurchasePass;
import com.guestlogix.travelercorekit.models.PurchasedBookingProduct;
import com.guestlogix.travelercorekit.models.PurchasedPartnerOfferingProduct;
import com.guestlogix.traveleruikit.R;
import com.guestlogix.traveleruikit.TravelerUI;

import java.util.ArrayList;
import java.util.List;

/**
 * Fragment which displays a summary of a list of products.
 */
public class ProductSummaryFragment extends BaseFragment {

    private RecyclerView recyclerView;
    private ProductsAdapter productsAdapter;

    public ProductSummaryFragment() {
        // Do nothing
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_product_summary, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.recyclerView_orderSummary_productsContainer);
    }

    public void setProducts(List<Product> products) {
        if (productsAdapter == null) {
            productsAdapter = new ProductsAdapter();
            recyclerView.setAdapter(productsAdapter);
        }

        productsAdapter.products = products;

        if (recyclerView.getLayoutManager() == null) {
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivityContext()));
        }

        productsAdapter.notifyDataSetChanged();
    }


    private class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.ViewHolder> {
        private List<Product> products;

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(getActivity()).inflate(R.layout.item_product_summary, parent, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            Product product = products.get(position);

            holder.title.setText(product.getTitle());
            holder.subtitle.setVisibility(View.GONE);
            //TODO: this is not open closed we should have a intermediate abstraction product type called something like PassableProduct. then here only check if the product is passable if it is we show them.
            if (product.getProductType() == ProductType.BOOKABLE) {
                List<PurchasePass> purchasePasses = new ArrayList<>();
                for (Pass pass : ((PurchasedBookingProduct) product).getPasses()) {
                    purchasePasses.add(pass.toPurchasePass());
                }
                holder.recyclerView.setAdapter(new PassAdapter(purchasePasses));
            } else if (product.getProductType() == ProductType.PARTNER_OFFERING) {
                List<PurchasePass> purchasePasses = new ArrayList<>();
                for (PartnerOffering partnerOffering : ((PurchasedPartnerOfferingProduct) product).getPartnerOfferings()) {
                    purchasePasses.add(partnerOffering.toPurchasePass());
                }
                holder.recyclerView.setAdapter(new PassAdapter(purchasePasses));
            }

            LinearLayoutManager lm = new LinearLayoutManager(ProductSummaryFragment.this.getActivityContext());
            holder.recyclerView.setLayoutManager(lm);
            DividerItemDecoration decoration = new DividerItemDecoration(recyclerView.getContext(), lm.getOrientation());
            holder.recyclerView.addItemDecoration(decoration);
        }

        @Override
        public int getItemCount() {
            return products != null ? products.size() : 0;
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            TextView title;
            TextView subtitle;
            RecyclerView recyclerView;

            ViewHolder(@NonNull View itemView) {
                super(itemView);
                title = itemView.findViewById(R.id.textView_orderSummaryItem_title);
                subtitle = itemView.findViewById(R.id.textView_orderSummaryItem_subtitle);
                recyclerView = itemView.findViewById(R.id.recyclerView_orderSummaryItem_passesContainer);
            }
        }
    }

    private class PassAdapter extends RecyclerView.Adapter<PassAdapter.ViewHolder> {
        List<PurchasePass> passes;

        PassAdapter(List<PurchasePass> passes) {
            this.passes = passes;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(getActivity()).inflate(R.layout.pass_summary_item, parent, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            PurchasePass purchasePass = passes.get(position);

            holder.title.setText(purchasePass.getName());
            if (purchasePass.getDescription() == null || purchasePass.getDescription().isEmpty()) {
                holder.subtitle.setText(purchasePass.getDescription());
            } else {
                holder.subtitle.setVisibility(View.GONE);
            }

            holder.value.setText(purchasePass.getPrice().getLocalizedDescription(TravelerUI.getPreferredCurrency()));
        }

        @Override
        public int getItemCount() {
            return passes != null ? passes.size() : 0;
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            TextView title;
            TextView subtitle;
            TextView value;

            ViewHolder(@NonNull View itemView) {
                super(itemView);

                title = itemView.findViewById(R.id.textView_passSummaryItem_title);
                subtitle = itemView.findViewById(R.id.textView_passSummaryItem_subtitle);
                value = itemView.findViewById(R.id.textView_passSummaryItem_value);
            }
        }
    }
}
