package com.devlopertechie.whatstore;

import android.os.Bundle;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static com.devlopertechie.whatstore.Utils.readJSONFromAsset;

public class DirectMessageActvity extends AppCompatActivity {

    private static final String COUNRTY_CODE_FILE_NAME = "countryList";

    private LinearLayout llBottomSheet;
    private BottomSheetBehavior<LinearLayout> sheetBehavior;
    private EditText edtMobileNumber;
    private LinearLayout llCountryCode;
    private ImageView imgCountryFalg;
    private TextView txtCountryCode;
    private Button btnStartChat;
    private RecyclerView recyclerCountryCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_direct_message_actvity);
//        Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        initView();

        setupRecyclerView();


        sheetBehavior = BottomSheetBehavior.from(llBottomSheet);
        sheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);

        llCountryCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            }
        });
    }

    private void setupRecyclerView() {
        String countryList = readJSONFromAsset(this, COUNRTY_CODE_FILE_NAME);
        Gson gson = new Gson();
        Type founderListType = new TypeToken<ArrayList<CountryCode>>(){}.getType();
        List<CountryCode> countryCodeList = gson.fromJson(countryList, founderListType);
        CountryCodeAdapter adapter = new CountryCodeAdapter(countryCodeList);
        recyclerCountryCode.setAdapter(adapter);
    }

    private void initView() {
        llBottomSheet = findViewById(R.id.ll_bottom_sheet);
        edtMobileNumber = findViewById(R.id.edt_mobileNumber);
        llCountryCode = findViewById(R.id.llCountryCode);
        imgCountryFalg = findViewById(R.id.imgFlag);
        txtCountryCode = findViewById(R.id.txtCountryCode);
        btnStartChat = findViewById(R.id.btnStartChat);
        recyclerCountryCode = findViewById(R.id.recycler_country_list);

        recyclerCountryCode.setLayoutManager(new LinearLayoutManager(this));


    }

}
