package com.example.pharma.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pharma.R;
import com.example.pharma.module.PharmaciesModule;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class recycler_adapter extends RecyclerView.Adapter<recycler_adapter.Holder> implements Filterable {


    private Context context;

    private List<PharmaciesModule> modelList;
    private List<PharmaciesModule> pharmaciesRecycler_models_all;

    public recycler_adapter(Context context, List<PharmaciesModule> modelList) {
        this.context = context;
        this.modelList = modelList;
        pharmaciesRecycler_models_all = new ArrayList<>(modelList);
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<PharmaciesModule> list_filter = new ArrayList<>();

            if (constraint.toString().isEmpty()) {
                list_filter.addAll(pharmaciesRecycler_models_all);
            } else {
                for (PharmaciesModule model : pharmaciesRecycler_models_all) {
                    if (model.getPharmName().toLowerCase().contains(constraint.toString().toLowerCase())) {
                        list_filter.add(model);
                    }
                }
            }

            FilterResults filterResults = new FilterResults();
            filterResults.values = list_filter;


            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            modelList.clear();
            modelList.addAll((Collection<? extends PharmaciesModule>) results.values);
            notifyDataSetChanged();

        }
    };


    public class Holder extends RecyclerView.ViewHolder {
        public TextView name, address, near, phone;

        public Holder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.pharmacy_name);
            address = itemView.findViewById(R.id.address);
            near = itemView.findViewById(R.id.near);
            phone = itemView.findViewById(R.id.phone);

        }
    }

    @NonNull
    @Override
    public recycler_adapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new Holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.pharmacy_details_modle, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull recycler_adapter.Holder holder, int position) {
        PharmaciesModule model = modelList.get(position);
        holder.name.setText(model.getPharmName());
        holder.address.setText(model.getPharmAddress());
        holder.near.setText(model.getPharmNear());
        holder.phone.setText(model.getPharmPhone());
        holder.phone.setText(model.getPharmPhone());

    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }


}
