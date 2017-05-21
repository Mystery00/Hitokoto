package com.mystery0.hitokoto.custom;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mystery0.hitokoto.R;
import com.mystery0.hitokoto.class_class.HitokotoSource;
import com.mystery0.tools.Logs.Logs;

import java.util.List;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder>
{
    private static final String TAG = "CustomAdapter";
    private List<HitokotoSource> list;
    private CustomItemListener listener;

    CustomAdapter(List<HitokotoSource> list, CustomItemListener listener)
    {
        this.list = list;
        this.listener = listener;
    }

    static class ViewHolder extends RecyclerView.ViewHolder
    {
        View fullView;
        TextView name;
        TextView content;
        TextView from;
        TextView enable;

        ViewHolder(View itemView)
        {
            super(itemView);
            fullView = itemView;
            name = itemView.findViewById(R.id.source_name);
            content = itemView.findViewById(R.id.source_content);
            from = itemView.findViewById(R.id.source_from);
            enable = itemView.findViewById(R.id.source_enable);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_source, parent, false);
        final ViewHolder holder = new ViewHolder(view);
        Logs.i(TAG, "onCreateViewHolder: " + holder.getAdapterPosition());
        holder.fullView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                listener.onItemClick(list.get(holder.getAdapterPosition()), holder.getAdapterPosition());
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position)
    {
        holder.name.setText(list.get(position).getName());
        holder.content.setText(list.get(position).getContent_key());
        holder.from.setText(list.get(position).getFrom_key());
        holder.enable.setText(list.get(position).getEnable());
    }

    @Override
    public int getItemCount()
    {
        return list.size();
    }
}
