package com.guestlogix.traveleruikit.fragments;

import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.guestlogix.travelercorekit.models.BookableProduct;
import com.guestlogix.travelercorekit.models.Pass;
import com.guestlogix.travelercorekit.models.Product;
import com.guestlogix.travelercorekit.utilities.DateHelper;
import com.guestlogix.traveleruikit.R;
import com.guestlogix.traveleruikit.viewmodels.OrderSummaryViewModel;
import org.w3c.dom.Text;

import java.util.Date;
import java.util.List;

/**
 * A fragment which displays the general information for a list of products.
 * Observes {@link com.guestlogix.traveleruikit.viewmodels.OrderSummaryViewModel} for products to display.
 */
public class ProductSummaryFragment extends BaseFragment {
    private OrderSummaryViewModel viewModel;

    private RecyclerView recyclerView;
    private ProductsAdapter productsAdapter;

    public ProductSummaryFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_product_summary, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.recyclerView_orderSummary_productsContainer);
        viewModel = ViewModelProviders.of(getActivity()).get(OrderSummaryViewModel.class);

        viewModel.getProducts().observe(this, products -> {
            productsAdapter = new ProductsAdapter(products);
            recyclerView.setAdapter(productsAdapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(ProductSummaryFragment.this.getActivityContext()));
        });
    }

    private class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.ViewHolder> {
        private List<Product> products;

        ProductsAdapter(List<Product> p) {
            this.products = p;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(getActivity()).inflate(R.layout.product_summary_item, parent, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            BookableProduct product = (BookableProduct) products.get(position);

            holder.title.setText(product.getTitle());
            holder.subtitle.setVisibility(View.GONE); // TODO: We can't display the date for this product since we do have it in the model nor the payload.
            holder.recyclerView.setAdapter(new PassAdapter(product.getPasses()));
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

    private class PassAdapter extends  RecyclerView.Adapter<PassAdapter.ViewHolder> {
        List<Pass> passes;

        PassAdapter(List<Pass> passes) {
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
            Pass pass = passes.get(position);

            holder.title.setText(pass.getName());
            if (pass.getDescription() == null || pass.getDescription().isEmpty()) {
                holder.subtitle.setText(pass.getDescription());
            } else {
                holder.subtitle.setVisibility(View.GONE);
            }
            holder.value.setText(pass.getPrice().getFormattedValue());
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
