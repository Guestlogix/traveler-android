package com.guestlogix.traveleruikit.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.guestlogix.travelercorekit.models.BookableProduct;
import com.guestlogix.travelercorekit.models.Order;
import com.guestlogix.travelercorekit.models.OrderResult;
import com.guestlogix.travelercorekit.models.Product;
import com.guestlogix.travelercorekit.utilities.DateHelper;
import com.guestlogix.traveleruikit.R;

import java.util.ArrayList;
import java.util.List;

public class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.OrderItemViewHolder> {

    private OrderResult orderResult;
    private View.OnClickListener onItemClickListener;
    private RecyclerView.RecycledViewPool viewPool;

    public OrdersAdapter(OrderResult orderResult, View.OnClickListener onItemClickListener) {
        this.orderResult = orderResult;
        this.onItemClickListener = onItemClickListener;
        this.viewPool = new RecyclerView.RecycledViewPool();
    }

    public void setOrderResult(OrderResult orderResult) {
        this.orderResult = orderResult;
    }

    @NonNull
    @Override
    public OrdersAdapter.OrderItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_order, parent, false);
        return new OrdersAdapter.OrderItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrdersAdapter.OrderItemViewHolder holder, int position) {
        Order item = orderResult.getOrders().get(position);

        if (null == item) {
            //TODO:  Shimmer cell
            holder.title.setText("Loading...");
            holder.totalAmount.setText("");
            holder.productsAdapter = new OrderProductsAdapter(null);
            holder.productsRecyclerView.setAdapter(holder.productsAdapter);

            holder.itemView.setOnClickListener(null);

            return;
        }
        holder.productsRecyclerView.setRecycledViewPool(viewPool);

        holder.productsAdapter = new OrderProductsAdapter(item.getProducts());
        holder.title.setText(DateHelper.formatDate(item.getCreatedDate()));
        holder.totalAmount.setText(item.getTotal().getFormattedValue());

        holder.itemView.setTag(position);
        holder.itemView.setOnClickListener(onItemClickListener);
        holder.productsRecyclerView.setAdapter(holder.productsAdapter);
    }

    @Override
    public int getItemCount() {
        return orderResult.getTotal();
    }

    class OrderItemViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        RecyclerView productsRecyclerView;
        TextView totalAmount;

        OrderProductsAdapter productsAdapter;

        OrderItemViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.textView_orderItem_dateOrdered);
            productsRecyclerView = itemView.findViewById(R.id.recyclerView_orderItem_products);
            totalAmount = itemView.findViewById(R.id.textView_orderItem_totalAmount);
            productsRecyclerView.setLayoutManager(new LinearLayoutManager(itemView.getContext(), LinearLayoutManager.HORIZONTAL, false));
        }
    }

    class OrderProductsAdapter extends RecyclerView.Adapter<OrderProductsAdapter.ProductViewHolder> {

        ArrayList<Product> products = new ArrayList<>();

        OrderProductsAdapter(List<Product> products) {
            this.products.addAll(null != products ? products : new ArrayList<>());
        }

        @NonNull
        @Override
        public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order_product, parent, false);
            return new ProductViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
            BookableProduct product = (BookableProduct) products.get(position);
            holder.nameTextView.setText(product.getTitle());
            holder.itemView.setOnClickListener(onItemClickListener);
        }

        @Override
        public int getItemCount() {
            return products.size();
        }

        class ProductViewHolder extends RecyclerView.ViewHolder {

            TextView nameTextView;

            ProductViewHolder(@NonNull View itemView) {
                super(itemView);
                nameTextView = itemView.findViewById(R.id.textView_productName);
            }
        }
    }
}