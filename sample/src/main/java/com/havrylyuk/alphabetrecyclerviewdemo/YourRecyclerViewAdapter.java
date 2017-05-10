package com.havrylyuk.alphabetrecyclerviewdemo;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.havrylyuk.alphabetrecyclerview.BaseAlphabeticalAdapter;
import com.havrylyuk.alphabetrecyclerviewdemo.model.Country;

import java.util.Collections;
import java.util.Comparator;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Created by Igor Havrylyuk on 08.05.2017.
 */

public class YourRecyclerViewAdapter extends BaseAlphabeticalAdapter<Country> {

    private static final String GEONAME_IMAGES_URL = "http://www.geonames.org/flags/m/";


    public YourRecyclerViewAdapter(Context context) {
        super(context);
    }

    @Override
    public void sortList() {
        //Sort the list if necessary
        if (null != entityList) {
            Collections.sort(entityList, new Comparator<Country>() {
                @Override
                public int compare(Country c1, Country c2) {
                    return c1.getCountryName().compareTo(c2.getCountryName());
                }
            });
        }
    }

    @Override
    public void initHeadersLetters() {
        SortedSet<Character> characters = new TreeSet<>();
        if (null != entityList) {
            for (Country country : entityList) {
                if (!TextUtils.isEmpty(country.getCountryName())) {
                    characters.add(country.getCountryName().charAt(0));
                }
            }
            setHeadersLetters(characters);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder( RecyclerView.ViewHolder holder, int position) {
        ItemViewHolder viewHolder= (ItemViewHolder) holder;
        viewHolder.textView.setText(entityList.get(position).getCountryName());
        final int pos = position;
        String flagUrl = GEONAME_IMAGES_URL +
                entityList.get(position).getCountryCode().toLowerCase() + ".png";
        viewHolder.flagImage.setImageURI(Uri.parse(flagUrl));
        viewHolder.textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != listener) {
                    listener.onItemClick(pos, entityList.get(pos));
                }
            }
        });
    }

    @Override
    public RecyclerView.ViewHolder onCreateHeaderViewHolder(ViewGroup parent) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_header, parent, false);
        return new HeaderViewHolder(itemView);
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        HeaderViewHolder headerViewHolder = (HeaderViewHolder) viewHolder;
        String itemTitle = String.valueOf(entityList.get(position).getCountryName().charAt(0));
        headerViewHolder.title.setText(itemTitle);
    }

    @Override
    public long getHeaderId(int position) {
        return entityList.get(position).getCountryName().charAt(0);
    }


    public class ItemViewHolder extends RecyclerView.ViewHolder {

        private TextView textView;
        private SimpleDraweeView flagImage;

        public ItemViewHolder(View itemView) {
            super(itemView);
            this.textView = (TextView) itemView.findViewById(R.id.id_tv_item);
            this.flagImage = (SimpleDraweeView) itemView.findViewById(R.id.id_image_item);
        }
    }

    public static class HeaderViewHolder extends RecyclerView.ViewHolder {

        private TextView title;

        public HeaderViewHolder(View headerView) {
            super(headerView);
            this.title = (TextView) headerView.findViewById(R.id.id_tv_head_item);
        }
    }

}
