package com.example.ceh.cryptoconverter.activities;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.ceh.cryptoconverter.R;
import com.example.ceh.cryptoconverter.URLs;
import com.example.ceh.cryptoconverter.adapters.RecyclerAdapterCurrencyRate;
import com.example.ceh.cryptoconverter.models.CardItem;
import com.example.ceh.cryptoconverter.utils.SingletonRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    RecyclerView mRecyclerView;
    SwipeRefreshLayout swipeRefreshLayout;
    TextView mInfoTxt;
    LinearLayoutManager linearLayoutManager;
    RecyclerAdapterCurrencyRate adapterCurrencyRate;
    ArrayList<CardItem> cardItemArrayList= new ArrayList<>();
    String[] currency_abbreviation= new String[20];        //used to get the string value of the currency from the json object in API
    String[] currency_symbol_array = new String[]{};
    String[] currencyArray = new String[]{};
    int[] currency_img_array;
    String currency_list_url;           //list of currencies to be used in the API route
    String url;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
        mRecyclerView=(RecyclerView)findViewById(R.id.currency_recycler_view);
        swipeRefreshLayout=(SwipeRefreshLayout)findViewById(R.id.swipe_refresh);
        mInfoTxt=(TextView)findViewById(R.id.info_txt);
        linearLayoutManager=new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        adapterCurrencyRate= new RecyclerAdapterCurrencyRate(this,cardItemArrayList);
        mRecyclerView.setAdapter(adapterCurrencyRate);      //setting adapter with empty list so that swipe refresh works when recyclerview is empty
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                exchangeRate();
            }
        });
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,android.R.color.holo_green_light,android.R.color.holo_orange_light,android.R.color.holo_red_light);



        //array  of currency image drawable
        currency_img_array = new int[]{
                R.drawable.ic_aud,
                R.drawable.ic_brl,
                R.drawable.ic_cnd,
                R.drawable.ic_chf,
                R.drawable.ic_cny,
                R.drawable.ic_eur,
                R.drawable.ic_gbp,
                R.drawable.ic_hkd,
                R.drawable.ic_inr,
                R.drawable.ic_jpy,
                R.drawable.ic_krw,
                R.drawable.ic_mxn,
                R.drawable.ic_ngn,
                R.drawable.ic_nok,
                R.drawable.ic_nzd,
                R.drawable.ic_rub,
                R.drawable.ic_sek,
                R.drawable.ic_sgd,
                R.drawable.ic_usd,
                R.drawable.ic_zar
        };


        currencyArray= getResources().getStringArray(R.array.currency_list);
        currency_symbol_array= getResources().getStringArray(R.array.currency_symbols_array);

        //formatting currency_list_url to contain comma so it can be added to the api route url
        for(int i=0; i<currencyArray.length; i++){
            String temp= currencyArray[i];
            int endPosition=temp.indexOf("(");
            temp= temp.substring(0,endPosition);
            currency_abbreviation[i]=temp;
            if (i==0){
                currency_list_url=temp+",";
            }else if(i<19){
                currency_list_url= currency_list_url+temp+",";
            }else{
                currency_list_url= currency_list_url+temp;
            }

        }
        url= URLs.URL_RATE+ currency_list_url;
        exchangeRate();


    }



    private void exchangeRate(){
        swipeRefreshLayout.setRefreshing(true);
        mInfoTxt.setVisibility(View.GONE);
        JsonObjectRequest jsonObjectRequest= new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.e("ss",response.toString());
                try {
                    JSONObject jsonObjectBTC= response.getJSONObject("BTC");
                    JSONObject jsonObjectETH= response.getJSONObject("ETH");
                    cardItemArrayList= new ArrayList<>();  //to clear the previous data in the arraylist
                    //iterate through the list of base currencies and populate the list of card items
                    for (int i=0; i<currencyArray.length; i++){
                        CardItem cardItem= new CardItem();
                        cardItem.setCurrency_name(currencyArray[i]);
                        cardItem.setCurrency_img_drawable(currency_img_array[i]);
                        cardItem.setCurrency_symbol(currency_symbol_array[i]);
                        cardItem.setBtc_value(jsonObjectBTC.getDouble(currency_abbreviation[i]));
                        cardItem.setEth_value(jsonObjectETH.getDouble(currency_abbreviation[i]));
                        cardItemArrayList.add(cardItem);
                    }
                    Log.e("hig", String.valueOf(cardItemArrayList.size()));
                    setRecyclerView(cardItemArrayList);
                    swipeRefreshLayout.setRefreshing(false);
                } catch (JSONException e) {
                    swipeRefreshLayout.setRefreshing(false);
                   if(cardItemArrayList.isEmpty()){
                       mInfoTxt.setVisibility(View.VISIBLE);
                   }

                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                swipeRefreshLayout.setRefreshing(false);
                if(cardItemArrayList.isEmpty()){
                    mInfoTxt.setVisibility(View.VISIBLE);
                }

            }
        });
        SingletonRequest.getInstance().addToRequestQueue(jsonObjectRequest);
    }

    public void setRecyclerView(ArrayList<CardItem> cardList){
        adapterCurrencyRate= new RecyclerAdapterCurrencyRate(this,cardList);
        mRecyclerView.setAdapter(adapterCurrencyRate);
    }

}
