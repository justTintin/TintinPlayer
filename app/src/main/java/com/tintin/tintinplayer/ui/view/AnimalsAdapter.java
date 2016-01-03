package com.tintin.tintinplayer.ui.view;

import android.support.v7.widget.RecyclerView;

import com.tintin.tintinplayer.module.VideoModule;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

/**
 * Adapter holding a list of animal names of type String. Note that each item must be unique.
 */
public abstract class AnimalsAdapter<VH extends RecyclerView.ViewHolder>
        extends RecyclerView.Adapter<VH>
{
    private ArrayList<VideoModule> items = new ArrayList<>();

    public AnimalsAdapter()
    {
        setHasStableIds(true);
    }

    public void add(VideoModule object)
    {
        items.add(object);
        notifyDataSetChanged();
    }

    public void add(int index, VideoModule object)
    {
        items.add(index, object);
        notifyDataSetChanged();
    }

    public void addAll(Collection<? extends VideoModule> collection)
    {
        if (collection != null)
        {
            items.addAll(collection);
            notifyDataSetChanged();
        }
    }

    public void addAll(VideoModule... items)
    {
        addAll(Arrays.asList(items));
    }

    public void clear()
    {
        items.clear();
        notifyDataSetChanged();
    }

    public void remove(VideoModule object)
    {
        items.remove(object);
        notifyDataSetChanged();
    }

    public VideoModule getItem(int position)
    {
        return items.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return getItem(position).hashCode();
    }

    @Override
    public int getItemCount()
    {
        return items.size();
    }
}
