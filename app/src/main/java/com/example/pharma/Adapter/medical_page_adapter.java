package com.example.pharma.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pharma.Activity.medical_page;
import com.example.pharma.R;
import com.example.pharma.getAllDataFromAPI;
import com.example.pharma.module.DrugsModule;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class medical_page_adapter extends RecyclerView.Adapter<medical_page_adapter.HOlder> implements Filterable {

    List<DrugsModule> list;
    List<DrugsModule> list_of_all;
    Context context;

    public medical_page_adapter(List<DrugsModule> list, Context context) {
        this.list = list;
        this.context = context;
        list_of_all = new ArrayList<>(list);

    }


    @Override
    public Filter getFilter() {
        return filter;
    }

    Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<DrugsModule> list_filter = new ArrayList<>();

            if (constraint.toString().isEmpty()) {
                list_filter.addAll(list_of_all);
            } else {
                for (DrugsModule model : list_of_all) {
                    if (model.getName().toLowerCase().contains(constraint.toString().toLowerCase())) {
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
            list.clear();
            list.addAll((Collection<? extends DrugsModule>) results.values);
            notifyDataSetChanged();

        }
    };


    @NonNull
    @Override
    public medical_page_adapter.HOlder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new HOlder(LayoutInflater.from(parent.getContext()).inflate(R.layout.medical_page_modle, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final medical_page_adapter.HOlder holder, int position) {
        DrugsModule page_modle = list.get(position);

        String currentUserMail = getAllDataFromAPI.currentUser.getPharmMail();

        try {
            holder.id.setText(page_modle.getId() + "");
            holder.name.setText(page_modle.getName());
            holder.salary.setText(page_modle.getPrice() + "");
            int x;
            for (int i = 0; i < page_modle.getPharmaciesList().size(); i++) {
                if (page_modle.getPharmaciesList().get(i).getPharmMail().equals(currentUserMail)) {
                    x = 1;
                } else {
                    x = 2;
                }

                if (x == 1) {
                    holder.aSwitch.setChecked(true);
                    holder.cardView.setBackgroundResource(R.drawable.card_edit);

                } else {
                    holder.aSwitch.setChecked(false);
                    holder.cardView.setBackgroundResource(R.drawable.card_back);

                }

            }
        } catch (Exception e) {
        }

        holder.aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                boolean isPharmacyExist = checkPharmacyExist(Integer.parseInt(holder.id.getText().toString()));
                if (isChecked) {
                    //make available
                    //check is exist
                    if (!isPharmacyExist) {
                        medical_page.dm.setId(Integer.parseInt(holder.id.getText().toString()));

                        medical_page.MakeDrugAvailable makeDrugAvailable = new medical_page.MakeDrugAvailable();
                        makeDrugAvailable.execute();

                        holder.cardView.setBackgroundResource(R.drawable.card_edit);
                    }
                } else {
                    //make unavailable
                    //check exisiting
                    if (isPharmacyExist) {
                        medical_page.dm.setId(Integer.parseInt(holder.id.getText().toString()));

                        medical_page.MakeDrugUnAvailable makeDrugAvailable = new medical_page.MakeDrugUnAvailable();
                        makeDrugAvailable.execute();

                        holder.cardView.setBackgroundResource(R.drawable.card_back);
                    }
                }
            }
        });


    }

    private boolean checkPharmacyExist(int drugId) {

        DrugsModule currentDrug = null;

        for (int i = 0; i < getAllDataFromAPI.drugs_list.size(); i++) {
            if (getAllDataFromAPI.drugs_list.get(i).getId() == drugId) {
                currentDrug = getAllDataFromAPI.drugs_list.get(i);
            }
        }
        if (currentDrug == null) {
            return false;
        } else {
            for (int i = 0; i < currentDrug.getPharmaciesList().size(); i++) {
                if (currentDrug.getPharmaciesList().get(i).getPharmMail()
                        .equals(getAllDataFromAPI.currentUser.getPharmMail())) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class HOlder extends RecyclerView.ViewHolder {

        TextView id, name, salary;
        Switch aSwitch;
        CardView cardView;

        public HOlder(@NonNull View itemView) {
            super(itemView);
            id = itemView.findViewById(R.id.medical_number);
            name = itemView.findViewById(R.id.medical_name);
            salary = itemView.findViewById(R.id.salary);
            aSwitch = itemView.findViewById(R.id.available_switch);
            cardView = itemView.findViewById(R.id.card_module);

        }
    }
}
