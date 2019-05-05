package com.devlopertechie.whatstore;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by ShivamSharma on 5/5/19.
 */
public class CountryCodeAdapter extends RecyclerView.Adapter<CountryCodeAdapter.ViewHolder> {
    private List<CountryCode> countryCodeList;

    public CountryCodeAdapter(List<CountryCode> countryCodeList) {
        this.countryCodeList = countryCodeList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_country_code_item, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        CountryCode countryCode= countryCodeList.get(position);
        holder.txtCode.setText(countryCode.code);
        holder.txtName.setText(countryCode.name);
        holder.txtDialCode.setText(countryCode.dialCode);

    }

    @Override
    public int getItemCount() {
        return countryCodeList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView txtCode, txtName, txtDialCode;

        public ViewHolder(View itemView) {
            super(itemView);

            txtCode = itemView.findViewById(R.id.txtCode);
            txtName = itemView.findViewById(R.id.txtCountryName);
            txtDialCode = itemView.findViewById(R.id.txtDialCode);

        }
    }
}
