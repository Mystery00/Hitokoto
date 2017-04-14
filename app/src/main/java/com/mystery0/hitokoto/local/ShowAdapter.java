package com.mystery0.hitokoto.local;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mystery0.hitokoto.R;
import com.mystery0.hitokoto.class_class.HitokotoLocal;
import com.mystery0.tools.Logs.Logs;

import java.util.List;

public class ShowAdapter extends RecyclerView.Adapter<ShowAdapter.ViewHolder>
{
    private static final String TAG = "ShowAdapter";
    private List<HitokotoLocal> list;
    private ShowItemListener listener;

    public ShowAdapter(List<HitokotoLocal> list, ShowItemListener listener)
    {
        this.list = list;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_hitokoto, parent, false);
        final ViewHolder holder = new ViewHolder(view);
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
        HitokotoLocal hitokotoLocal = list.get(position);
        holder.content.setText(hitokotoLocal.getContent());
        holder.source.setText(hitokotoLocal.getSource());
    }

    @Override
    public int getItemCount()
    {
        return list.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder
    {
        View fullView;
        TextView content;
        TextView source;

        public ViewHolder(View itemView)
        {
            super(itemView);
            fullView = itemView;
            content = (TextView) itemView.findViewById(R.id.content);
            source = (TextView) itemView.findViewById(R.id.source);
        }
    }
}
