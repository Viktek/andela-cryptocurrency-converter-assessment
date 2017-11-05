package com.example.ceh.cryptoconverter.activities;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.ceh.cryptoconverter.R;
import com.example.ceh.cryptoconverter.models.CardItem;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class ConversionActivity extends AppCompatActivity implements View.OnClickListener,TextWatcher,AdapterView.OnItemSelectedListener{
    //Toolbar toolbar;
    LinearLayout mCurrencyToCryptoLayout,mCryptoToCurrencyLayout;
    TextView mCurrencyLabel,mCurrencyValue,mUserInputLabel;
    EditText mUserInputCrypto;
    Spinner mChooseCryptoSpinner;
    TextView mBtcValue,mEthValue;
    EditText mUserInputCurrency;
    Button mChangeLayoutButton;
    ArrayList<String> cryptoList;
    String crypto_chosen="BTC";

    CardItem cardItem;
    Boolean isCurrencyToCryptoLayout=true;
    DecimalFormat df;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
        setContentView(R.layout.activity_conversion);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Base currency to crypto");
        mBtcValue=(TextView)findViewById(R.id.btc_txt);
        mEthValue=(TextView)findViewById(R.id.eth_txt);
        mUserInputCurrency=(EditText) findViewById(R.id.user_input);
        mUserInputCrypto=(EditText)findViewById(R.id.user_input_crypto);
        mCurrencyValue=(TextView)findViewById(R.id.amount_in_currency);
        mUserInputLabel=(TextView)findViewById(R.id.user_input_label);
        mChooseCryptoSpinner=(Spinner)findViewById(R.id.spinner_crypto);
        mCryptoToCurrencyLayout=(LinearLayout)findViewById(R.id.crypto_currency_layout);
        mCurrencyToCryptoLayout=(LinearLayout)findViewById(R.id.currency_crypto_layout);
        mCurrencyLabel=(TextView)findViewById(R.id.currency_label);
        mChangeLayoutButton=(Button)findViewById(R.id.button_change_layout);
        mChangeLayoutButton.setOnClickListener(this);
        cryptoList= new ArrayList<>();
        cryptoList.add("BTC(Bitcoin)");
        cryptoList.add("ETH(Ethereum)");
        mChooseCryptoSpinner.setOnItemSelectedListener(this);
        ArrayAdapter<String> adapterCrypto= new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,cryptoList);
        mChooseCryptoSpinner.setAdapter(adapterCrypto);

        df = new DecimalFormat("#.#####");
        df.setRoundingMode(RoundingMode.CEILING);
        cardItem=new CardItem();
        cardItem=(CardItem) getIntent().getSerializableExtra("currency_detail");

        mCurrencyLabel.setText(cardItem.getCurrency_name()+" value");
        mUserInputLabel.setText("Enter amount in "+cardItem.getCurrency_name()+":");
        mUserInputCrypto.addTextChangedListener(this);

        mUserInputCurrency.addTextChangedListener(this);

        //to set an initial value to the output amount;
        convertToCurrency("");
        convertToCrypto("");

    }

    @Override
    public void onClick(View v) {
        toggleLayout();
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        //to identify which editText is being watched
        if(mUserInputCurrency.getText().hashCode()==s.hashCode()){
            convertToCrypto(s);
        }else if(mUserInputCrypto.getText().hashCode()==s.hashCode()){
            convertToCurrency(s);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Log.e("pos", String.valueOf(position));
        switch (position){
            case 0:
                crypto_chosen="BTC";
                //to cater for spinner changes made when there is already an input in th editText
                CharSequence sequence= mUserInputCrypto.getText().toString().trim();
                convertToCurrency(sequence);
                break;
            case 1:
                crypto_chosen="ETH";
                //to cater for spinner changes made when there is already an input in th editText
                CharSequence sequence1= mUserInputCrypto.getText().toString().trim();
                convertToCurrency(sequence1);
                break;
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void convertToCrypto(CharSequence s){
        double amount_to_convert;
        if(mUserInputCurrency.getText().toString().trim().isEmpty()){
            amount_to_convert=0;
        }else{
            amount_to_convert= Double.parseDouble(s.toString());
        }
        double btc_amount= amount_to_convert/cardItem.getBtc_value();
        double eth_amount= amount_to_convert/cardItem.getEth_value();
        mBtcValue.setText(String.valueOf(df.format(btc_amount)));
        mEthValue.setText(String.valueOf(df.format(eth_amount)));
    }

    private void convertToCurrency(CharSequence s){
        double result;
        double amount_to_convert;
        if(mUserInputCrypto.getText().toString().trim().isEmpty()){
            amount_to_convert=0;
        }else{
            amount_to_convert= Double.parseDouble(s.toString());
        }

        if(crypto_chosen.equals("BTC")){
            result= amount_to_convert*cardItem.getBtc_value();
            String answer= cardItem.getCurrency_symbol()+String.valueOf(result);
            mCurrencyValue.setText(answer);
        }else if(crypto_chosen.equals("ETH")){
            result= amount_to_convert*cardItem.getEth_value();
            String answer= cardItem.getCurrency_symbol()+String.valueOf(result);
            mCurrencyValue.setText(answer);
        }

    }

    private void toggleLayout(){
        if(isCurrencyToCryptoLayout){
            getSupportActionBar().setTitle("Base currency to Crypto");
            mChangeLayoutButton.setText("convert from crypto instead");
            mCryptoToCurrencyLayout.setVisibility(View.VISIBLE);
            mCurrencyToCryptoLayout.setVisibility(View.GONE);
            isCurrencyToCryptoLayout=false;
        }else {
            getSupportActionBar().setTitle("Crypto to Base currency");
            mChangeLayoutButton.setText("convert from base currency instead");
            mCryptoToCurrencyLayout.setVisibility(View.GONE);
            mCurrencyToCryptoLayout.setVisibility(View.VISIBLE);
            isCurrencyToCryptoLayout=true;
        }
    }

}
