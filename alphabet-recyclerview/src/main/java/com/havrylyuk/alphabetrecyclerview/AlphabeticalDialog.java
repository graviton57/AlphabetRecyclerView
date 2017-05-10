package com.havrylyuk.alphabetrecyclerview;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Alphabetical Dialog
 * Created by Igor Havrylyuk on 29.04.2017.
 */

public class AlphabeticalDialog extends DialogFragment {

    public static final String ALPHABETICAL_DIALOG_TAG = "ALPHABETICAL_DIALOG_TAG";
    public static final String EXTRA_SELECTED_LETTER = "EXTRA_SELECTED_LETTER";
    public static final String EXTRA_LIST_LETTERS = "EXTRA_LIST_LETTERS";

    private static final int SPAN_COUNT_PORTRAIT = 4;
    private static final int SPAN_COUNT_LANDSCAPE = 6;

    private int spanCount = SPAN_COUNT_PORTRAIT;
    private int tilesColor;
    private int lettersColor;

    private RecyclerView gridRecyclerView;
    private AlphabeticalDialogAdapter alphabeticalAdapter;
    private OnLetterClickListener onLetterClickListener = null;
    private int letter;
    private Set<Character> listLetters;

    public static AlphabeticalDialog newInstance(int selectedLetter, TreeSet<Character> letters) {
        Bundle bundle = new Bundle();
        bundle.putInt(EXTRA_SELECTED_LETTER, selectedLetter);
        bundle.putSerializable(EXTRA_LIST_LETTERS, letters);
        AlphabeticalDialog contentDialog = new AlphabeticalDialog();
        contentDialog.setArguments(bundle);
        return contentDialog;
    }

    public AlphabeticalDialog() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            onLetterClickListener = (OnLetterClickListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement AlphabeticalDialog onLetterClickListener");
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
        if (arguments != null) {
            letter = arguments.getInt(EXTRA_SELECTED_LETTER);
            listLetters = (SortedSet<Character>) arguments.getSerializable(EXTRA_LIST_LETTERS);
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog builder = new Dialog(getActivity());
        View view = getActivity().getLayoutInflater().inflate(R.layout.widget_grid_dialog, null);
        initializeRecyclerView(view);
        builder.requestWindowFeature(Window.FEATURE_NO_TITLE);
        builder.setContentView(view);
        if (null != builder.getWindow()){
            builder.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            builder.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
        }
        builder.setCanceledOnTouchOutside(false);
        return  builder;
    }

    private void initializeRecyclerView(View view){
        gridRecyclerView = (RecyclerView) view.findViewById(R.id.alphabetical_grid);
        int orientation = getResources().getConfiguration().orientation;
        if (Configuration.ORIENTATION_LANDSCAPE == orientation) {
            spanCount = SPAN_COUNT_LANDSCAPE;
        }
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), spanCount);
        gridRecyclerView.setLayoutManager(gridLayoutManager);
        gridRecyclerView.setHasFixedSize(true);
        alphabeticalAdapter = new AlphabeticalDialogAdapter(getActivity(),
                new AlphabeticalDialogAdapter.OnLetterClickListener() {
            @Override
            public void onLetterClick(Character character) {
                if (null != onLetterClickListener) {
                    onLetterClickListener.onLetterClick(character);
                    dismiss();
                }
            }
        });
        alphabeticalAdapter.setLetters(listLetters);
        alphabeticalAdapter.setTilesColor(tilesColor);
        alphabeticalAdapter.setLettersColor(lettersColor);
        gridRecyclerView.setAdapter(alphabeticalAdapter);
        scrollToLetter((char) letter);
    }

    public void setTilesColor(int tileColor) {
        this.tilesColor = tileColor;
    }

    public void setLettersColor(int lettersColor) {
        this.lettersColor = lettersColor;
    }

    private void scrollToLetter(Character letter){
        int position = ((SortedSet<Character>)listLetters).headSet(letter).size();
        if (null != gridRecyclerView) {
            gridRecyclerView.getLayoutManager().scrollToPosition(position);
        }
    }
}
