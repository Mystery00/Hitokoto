package com.mystery0.hitokoto;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>
{
    private List<SettingsClass> list;
    private Context context;

    public RecyclerViewAdapter(List<SettingsClass> list, Context context)
    {
        this.list = list;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycler_view, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        if (list.get(viewHolder.getAdapterPosition()).isCheckBoxAvailable())
        {

        }
        return null;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position)
    {
        SettingsClass settingsClass = list.get(position);
        holder.textView.setText(settingsClass.getText());
        if (settingsClass.isCheckBoxAvailable())
        {
            holder.checkBox.setChecked(settingsClass.isOpen());
        } else
        {
            holder.checkBox.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount()
    {
        return list.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView textView;
        CheckBox checkBox;

        ViewHolder(View itemView)
        {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.content);
            checkBox = (CheckBox) itemView.findViewById(R.id.checkBox);
        }
    }
}
