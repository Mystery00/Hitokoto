package com.mystery0.hitokoto.local;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.mystery0.hitokoto.App;
import com.mystery0.hitokoto.R;
import com.mystery0.hitokoto.class_class.HitokotoGroup;

import java.util.List;

public class ManagerAdapter extends RecyclerView.Adapter<ManagerAdapter.ViewHolder>
{
    private List<HitokotoGroup> list;
    private ManagerItemListener listener;
    private boolean isShow;

    public ManagerAdapter(List<HitokotoGroup> list, boolean isShow, ManagerItemListener listener)
    {
        this.list = list;
        this.isShow = isShow;
        this.listener = listener;
    }

    @Override
    public ManagerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_hitokoto_manager, parent, false);
        final ManagerAdapter.ViewHolder holder = new ManagerAdapter.ViewHolder(view);
        holder.fullView.setOnLongClickListener(new View.OnLongClickListener()
        {
            @Override
            public boolean onLongClick(View v)
            {
                listener.onLongClick();
                return true;
            }
        });
        holder.textView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                listener.onItemClick(list.get(holder.getAdapterPosition()), holder.getAdapterPosition());
            }
        });
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                listener.onItemSelect(list.get(holder.getAdapterPosition()), holder.getAdapterPosition(), isChecked);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ManagerAdapter.ViewHolder holder, int position)
    {
        String temp = list.get(position).getName();
        holder.textView.setText(temp.equals(App.getContext().getString(R.string.unclassified)) ? App.getContext().getString(R.string.Unclassified) : temp);
        holder.checkBox.setVisibility(isShow ? View.VISIBLE : View.GONE);
    }

    @Override
    public int getItemCount()
    {
        return list.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder
    {
        View fullView;
        TextView textView;
        CheckBox checkBox;

        public ViewHolder(View itemView)
        {
            super(itemView);
            fullView = itemView;
            textView = (TextView) itemView.findViewById(R.id.text);
            checkBox = (CheckBox) itemView.findViewById(R.id.checkbox);
        }
    }
}
