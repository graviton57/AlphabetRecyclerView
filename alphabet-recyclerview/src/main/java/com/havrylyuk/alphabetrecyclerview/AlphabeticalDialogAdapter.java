package com.havrylyuk.alphabetrecyclerview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Alphabetical Dialog RecyclerView Adapter
 * Created by Igor Havrylyuk on 29.04.2017.
 */

public class AlphabeticalDialogAdapter extends RecyclerView.Adapter<AlphabeticalDialogAdapter.LetterItemHolder> {

    public interface  OnLetterClickListener{
        void onLetterClick(Character character);
    }

    private Context context;
    private List<Character> letters;
    private OnLetterClickListener listener = null;
    private int tileColor;
    private int letterColor;

    public AlphabeticalDialogAdapter(Context context, OnLetterClickListener listener) {
        this.context =context;
        this.listener = listener;
    }

    public void setLetters(Set<Character> letters) {

        this.letters = new ArrayList<>(letters);
        notifyDataSetChanged();
    }

    public void setTilesColor(int tileColor) {
        this.tileColor = tileColor;
    }

    public void setLettersColor(int letterColor) {
        this.letterColor = letterColor;
    }

    @Override
    public LetterItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (parent instanceof RecyclerView) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.layout_alphabet_item, parent, false);
            view.setFocusable(true);
            return new LetterItemHolder(view);
        } else {
            throw new RuntimeException("Not bound to RecyclerView");
        }
    }

    @Override
    public void onBindViewHolder(LetterItemHolder holder, int position) {
        final char letter = letters.get(position);
        holder.letter.setText(String.valueOf(letter));
        if (tileColor == 0) {
            tileColor = context.getResources().getColor(android.R.color.holo_green_dark);
        }
        holder.letter.setBackgroundColor(tileColor);
        if (letterColor == 0) {
            letterColor = context.getResources().getColor(android.R.color.white);
        }
        holder.letter.setTextColor(letterColor);
        holder.letter.setAlpha(0.95f);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != listener) {
                    listener.onLetterClick(letter);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return letters == null ? 0 : letters.size();
    }

    public final class LetterItemHolder extends RecyclerView.ViewHolder {

      public TextView letter;

      public LetterItemHolder(View itemView) {
          super(itemView);
          letter = (TextView) itemView.findViewById(R.id.item_letter);
      }
  }
}
