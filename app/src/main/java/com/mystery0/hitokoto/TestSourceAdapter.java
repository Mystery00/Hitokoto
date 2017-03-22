package com.mystery0.hitokoto;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mystery0.hitokoto.class_class.HitokotoSource;

import java.util.List;

public class TestSourceAdapter extends RecyclerView.Adapter<TestSourceAdapter.ViewHolder>
{
    private List<HitokotoSource> list;

    static class ViewHolder extends RecyclerView.ViewHolder
    {
        View fullView;
        TextView name;
        TextView address;
        TextView enable;

        public ViewHolder(View itemView)
        {
            super(itemView);
            fullView = itemView;
            name = (TextView) itemView.findViewById(R.id.source_name);
            address = (TextView) itemView.findViewById(R.id.source_address);
            enable = (TextView) itemView.findViewById(R.id.source_enable);
        }
    }

    public TestSourceAdapter(List<HitokotoSource> list)
    {
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_test_source, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position)
    {
        holder.name.setText(list.get(position).getName());
        holder.address.setText(list.get(position).getAddress());
        holder.enable.setText(list.get(position).getEnable());
    }

    @Override
    public int getItemCount()
    {
        return list.size();
    }
}
