package com.tintin.tintinplayer.ui.view;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tintin.module.stickyheadersrecyclerviewlibrary.StickyRecyclerHeadersAdapter;
import com.tintin.tintinplayer.R;
import com.tintin.tintinplayer.ui.fragment.dummy.DummyContent;

import java.security.SecureRandom;

/**
 * Created by alive on 2016/1/3.
 */
public class AnimalsHeadersAdapter extends
        AnimalsAdapter<RecyclerView.ViewHolder> implements
        StickyRecyclerHeadersAdapter<RecyclerView.ViewHolder>
{
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent,
            int viewType)
    {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_item, parent, false);
        return new RecyclerView.ViewHolder(view)
        {
        };
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position)
    {

        TextView textView = (TextView) holder.itemView;
        textView.setText(getItem(position).getDisplayName());
    }

    @Override
    public long getHeaderId(int position)
    {
        //        if (position == 0) {
        //            return -1;
        //        } else {
        //            return getItem(position).getAlbum().charAt(0);
        //        }
        return getItem(position).getAlbum().charAt(0);
    }

    @Override
    public RecyclerView.ViewHolder onCreateHeaderViewHolder(ViewGroup parent)
    {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_header, parent, false);
        return new RecyclerView.ViewHolder(view)
        {
        };
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder,
            int position)
    {
        TextView textView = (TextView) holder.itemView;
        textView.setText(String.valueOf(getItem(position).getAlbum()));
        holder.itemView.setBackgroundColor(getRandomColor());
    }

    private int getRandomColor()
    {
        SecureRandom rgen = new SecureRandom();
        return Color.HSVToColor(150, new float[] { rgen.nextInt(359), 1, 1 });
    }

    public class HeadViewHolder extends RecyclerView.ViewHolder
    {
        public final View mView;

        public final TextView mIdView;

        public final TextView mContentView;

        public DummyContent.DummyItem mItem;

        public HeadViewHolder(View view)
        {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.id);
            mContentView = (TextView) view.findViewById(R.id.content);
        }

        @Override
        public String toString()
        {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }

}