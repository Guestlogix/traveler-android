package com.guestlogix.traveleruikit.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.guestlogix.travelercorekit.models.BookableProduct;
import com.guestlogix.travelercorekit.models.Order;
import com.guestlogix.travelercorekit.models.Product;
import com.guestlogix.travelercorekit.utilities.DateHelper;
import com.guestlogix.traveleruikit.R;

import java.util.ArrayList;
import java.util.List;

public class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.OrderItemViewHolder> {

    private List<Order> orders;
    private RecyclerView.RecycledViewPool viewPool;
    private OnOrderItemClickListener onOrderItemClickListener;

    public OrdersAdapter() {
        this.viewPool = new RecyclerView.RecycledViewPool();
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
        Order item = orders.get(position);

        holder.productsRecyclerView.setRecycledViewPool(viewPool);

        holder.productsAdapter = new OrderProductsAdapter(item.getProducts());
        holder.title.setText(DateHelper.formatDate(item.getCreatedDate()));
        holder.totalAmount.setText(item.getTotal().getFormattedValue());

        holder.itemView.setTag(position);
        holder.itemView.setOnClickListener(onClickListener);
    }

    @Override
    public int getItemCount() {
        return orders.size();
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

    public interface OnOrderItemClickListener {
        void onOrderItemClicked(int position);
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

    public void setOnOrderItemClickListener(OnOrderItemClickListener onOrderItemClickListener) {
        this.onOrderItemClickListener = onOrderItemClickListener;
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int position = (Integer) v.getTag();

            if (null != onOrderItemClickListener) {
                onOrderItemClickListener.onOrderItemClicked(position);
            }
        }
    };
}