package com.havrylyuk.alphabetrecyclerviewdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.havrylyuk.alphabetrecyclerview.AlphabeticalDialog;
import com.havrylyuk.alphabetrecyclerview.BaseAlphabeticalAdapter;
import com.havrylyuk.alphabetrecyclerview.OnHeaderClickListener;
import com.havrylyuk.alphabetrecyclerview.OnLetterClickListener;
import com.havrylyuk.alphabetrecyclerview.StickyHeadersBuilder;
import com.havrylyuk.alphabetrecyclerview.StickyHeadersItemDecoration;
import com.havrylyuk.alphabetrecyclerviewdemo.model.Countries;
import com.havrylyuk.alphabetrecyclerviewdemo.model.Country;
import com.havrylyuk.alphabetrecyclerviewdemo.service.ApiClient;
import com.havrylyuk.alphabetrecyclerviewdemo.service.ApiService;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.TreeSet;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class YourActivity extends AppCompatActivity
        implements OnHeaderClickListener, OnLetterClickListener {

    private static final String LOG_TAG = YourActivity.class.getSimpleName();
    private static final String SELECTED_LANG = "SELECTED_LANG";

    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private TextView errorMessage;

    private ApiService service;
    private List<Country> countries;
    private YourRecyclerViewAdapter adapter;
    private String selectedLang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        errorMessage = (TextView) findViewById(R.id.error_message);
        initRecyclerView();
        service = ApiClient.getClient().create(ApiService.class);
        if (savedInstanceState != null) {
            selectedLang = savedInstanceState.getString(SELECTED_LANG);
        } else {
            selectedLang = Locale.getDefault().getLanguage();
        }
        fetchCountries(selectedLang);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(SELECTED_LANG, selectedLang);
        super.onSaveInstanceState(outState);
    }

    private void fetchCountries(String language) {
        countries = new ArrayList<>();
        setVisibilityProgressBar(true);
        Call<Countries> articleResponseCall = service.getCountries(language, BuildConfig.GEONAME_API_KEY, "FULL");
        articleResponseCall.enqueue(new Callback<Countries>() {
            @Override
            public void onResponse(Call<Countries> call, Response<Countries> response) {
                if (response.body().getStatus() == null) {
                    if (!response.body().getCountries().isEmpty()) {
                        countries.addAll(response.body().getCountries());
                        adapter.setData(countries);
                    } else {
                        showError(getString(R.string.empty_list));
                        Toast.makeText(YourActivity.this, R.string.something_went_wrong, Toast.LENGTH_SHORT).show();
                    }
                } else {
                        showError(response.body().getStatus().toString());
                }
                setVisibilityProgressBar(false);
            }

            @Override
            public void onFailure(Call<Countries> call, Throwable t) {
                Toast.makeText(YourActivity.this, "Error:" + t.getMessage(), Toast.LENGTH_LONG).show();
                Log.e(LOG_TAG, "Error:" + t.getMessage());
                setVisibilityProgressBar(false);
            }
        });
    }

    private void initRecyclerView(){
        recyclerView = (RecyclerView)findViewById(R.id.id_rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new YourRecyclerViewAdapter(this);
        adapter.setOnItemClickListener(new BaseAlphabeticalAdapter.OnItemClickListener<Country>() {
            @Override
            public void onItemClick(int position, Country entity) {
                Toast.makeText(YourActivity.this, "Item position=" +
                        position+" Capital:"+ entity.getCapital(), Toast.LENGTH_SHORT).show();
             }
        });
        StickyHeadersItemDecoration topStickyHeadersItemDecoration = new StickyHeadersBuilder()
                .setAdapter(adapter)
                .setRecyclerView(recyclerView)
                .setOnHeaderClickListener(this)
                .build();
        recyclerView.addItemDecoration(topStickyHeadersItemDecoration);
    }

    @Override
    public void onHeaderClick(View header, long headerId) {
        Character clickedHeaderLetter;
        TextView headerTextView= (TextView) header.findViewById(R.id.id_tv_head_item);
        if (null != headerTextView && !TextUtils.isEmpty(headerTextView.getText())) {
            clickedHeaderLetter = headerTextView.getText().charAt(0);
            AlphabeticalDialog dialog = (AlphabeticalDialog) getSupportFragmentManager()
                    .findFragmentByTag(AlphabeticalDialog.ALPHABETICAL_DIALOG_TAG);
            if (dialog == null) {
                dialog = AlphabeticalDialog.newInstance(clickedHeaderLetter,
                        (TreeSet<Character>) adapter.getHeadersLetters());
            }
            //set custom tiles background color
            dialog.setTilesColor(getResources().getColor(android.R.color.holo_green_dark));
            //set custom tiles text color
            dialog.setLettersColor(getResources().getColor(android.R.color.white));
            if (!dialog.isAdded()){
                dialog.show(getSupportFragmentManager().beginTransaction()
                        , AlphabeticalDialog.ALPHABETICAL_DIALOG_TAG);
            }
        }
    }

    @Override
    public void onLetterClick(Character letter) {
        for (int i = 0; i < countries.size(); i++) {
            if (countries.get(i).getCountryName().charAt(0)==letter){
                recyclerView.getLayoutManager().scrollToPosition(i);
                break;
            }
        }
    }

    private void setVisibilityProgressBar(boolean visible) {
        if (progressBar == null) {
            return;
        }
        if (visible) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
        }
        if (null!= errorMessage){
            errorMessage.setVisibility(View.GONE);
        }
    }

    private void showError(String message){
        if (null != errorMessage) {
            errorMessage.setVisibility(View.VISIBLE);
            errorMessage.setText(message);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        selectedLang = getLanguage(item.getItemId());
        fetchCountries(selectedLang);
        return true;
    }

    private String getLanguage(int id){
        switch (id){
            case R.id.action_en:
                return "en";
            case R.id.action_ru:
                return "ru";
            case R.id.action_zh:
                return "zh";
            case R.id.action_hi:
                return "hi";
            case R.id.action_es:
                return "es";
            case R.id.action_ar:
                return "ar";
            case R.id.action_bn:
                return "bn";
            case R.id.action_ja:
                return "ja";
            case R.id.action_de:
                return "de";
            case R.id.action_pt:
                return "pt";
            default:return "en";
        }
    }


}
