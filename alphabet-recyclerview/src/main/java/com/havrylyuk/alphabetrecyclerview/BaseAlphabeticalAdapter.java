package com.havrylyuk.alphabetrecyclerview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;


@SuppressWarnings({"FieldCanBeLocal", "unused"})
public abstract class BaseAlphabeticalAdapter<E> extends RecyclerView.Adapter<RecyclerView.ViewHolder>
        implements StickyHeadersAdapter<RecyclerView.ViewHolder> {

    public interface  OnItemClickListener<E> {
         void  onItemClick(int position, E entity);
    }

    protected OnItemClickListener listener;
    protected List<E> entityList;
    private final Context context;
    private Set<Character> headersLetters = new TreeSet<>();

    public BaseAlphabeticalAdapter(Context context ) {
        this.context = context;
        setHasStableIds(true);
    }

    public void setData(List<E> entityList) {
        this.entityList = entityList;
        sortList();
        initHeadersLetters();
        notifyDataSetChanged();
    }

    protected void sortList(){
    }

    protected void initHeadersLetters() {
        throw new RuntimeException("headersLetters must initialize!");
    }

    public Set<Character> getHeadersLetters() {
        return headersLetters;
    }

    protected void setHeadersLetters(Set<Character> headersLetters) {
        this.headersLetters = headersLetters;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public abstract RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType);

    public abstract void onBindViewHolder(RecyclerView.ViewHolder  holder,  int position);

    public abstract RecyclerView.ViewHolder onCreateHeaderViewHolder(ViewGroup parent);

    public abstract void onBindHeaderViewHolder(RecyclerView.ViewHolder viewHolder, int position);

    public abstract long getHeaderId(int position);

    @Override
    public int getItemCount() {
        return entityList == null ? 0 : entityList.size();
        }

    @Override
    public long getItemId(int position) {
        return position;
    }

}
