package com.mystery0.hitokoto.local;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mystery0.hitokoto.App;
import com.mystery0.hitokoto.R;
import com.mystery0.hitokoto.class_class.HitokotoGroup;

import java.util.List;

public class ManagerAdapter extends RecyclerView.Adapter<ManagerAdapter.ViewHolder>
{
    private List<HitokotoGroup> list;
    private ManagerItemListener listener;

    public ManagerAdapter(List<HitokotoGroup> list, ManagerItemListener listener)
    {
        this.list = list;
        this.listener = listener;
    }

    @Override
    public ManagerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_hitokoto_manager, parent, false);
        final ManagerAdapter.ViewHolder holder = new ManagerAdapter.ViewHolder(view);
        holder.textView.setOnClickListener(new View.OnClickListener()
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
    public void onBindViewHolder(ManagerAdapter.ViewHolder holder, int position)
    {
        String temp = list.get(position).getName();
        holder.textView.setText(temp.equals("unclassified") ? App.getContext().getString(R.string.Unclassified) : temp);
    }

    @Override
    public int getItemCount()
    {
        return list.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView textView;

        public ViewHolder(View itemView)
        {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.text);
        }
    }
}
