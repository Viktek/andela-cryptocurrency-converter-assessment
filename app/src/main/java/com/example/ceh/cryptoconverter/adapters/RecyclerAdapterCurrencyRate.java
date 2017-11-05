package com.example.ceh.cryptoconverter.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ceh.cryptoconverter.activities.ConversionActivity;
import com.example.ceh.cryptoconverter.R;
import com.example.ceh.cryptoconverter.models.CardItem;

import java.util.ArrayList;

/**
 * Created by CEH on 11/4/2017.
 */

public class RecyclerAdapterCurrencyRate extends RecyclerView.Adapter<RecyclerAdapterCurrencyRate.Holder> {
    Context context;
    ArrayList<CardItem> cardItemArrayList;

    public RecyclerAdapterCurrencyRate(Context context, ArrayList<CardItem> cardItemArrayList) {
        this.context = context;
        this.cardItemArrayList = cardItemArrayList;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.currency_card_item, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(Holder holder, final int position) {
        holder.mCurrencyImg.setImageResource(cardItemArrayList.get(position).getCurrency_img_drawable());
        holder.mCurrencyName.setText(cardItemArrayList.get(position).getCurrency_name());
        holder.mBtcValue.setText(cardItemArrayList.get(position).getCurrency_symbol()+cardItemArrayList.get(position).getBtc_value());
        holder.mEthValue.setText(cardItemArrayList.get(position).getCurrency_symbol()+cardItemArrayList.get(position).getEth_value());
        holder.mCurrencyCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(context, ConversionActivity.class);
                intent.putExtra("currency_detail",cardItemArrayList.get(position));     //put serialized data as a bundle in the intent
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return cardItemArrayList.size();
    }


    public static class Holder extends RecyclerView.ViewHolder{
        CardView mCurrencyCard;
        ImageView mCurrencyImg;
        TextView mCurrencyName,mBtcValue,mEthValue;
        public Holder(View itemView) {
            super(itemView);
            mCurrencyCard=(CardView)itemView.findViewById(R.id.currency_card);
            mCurrencyImg=(ImageView)itemView.findViewById(R.id.currency_img);
            mCurrencyName=(TextView)itemView.findViewById(R.id.currency_name);
            mBtcValue=(TextView)itemView.findViewById(R.id.btc_value);
            mEthValue=(TextView)itemView.findViewById(R.id.eth_value);

        }

    }
}
