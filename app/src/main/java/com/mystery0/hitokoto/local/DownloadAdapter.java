package com.mystery0.hitokoto.local;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mystery0.hitokoto.R;
import com.mystery0.hitokoto.class_class.ShareFile;

import java.util.ArrayList;
import java.util.List;

public class DownloadAdapter extends RecyclerView.Adapter<DownloadAdapter.ViewHolder>
{
    private List<ShareFile> list = new ArrayList<>();
    private DownloadItemListener listener;

    public DownloadAdapter(List<ShareFile> list, DownloadItemListener listener)
    {
        this.list = list;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_share_file, parent, false);
        final ViewHolder holder = new ViewHolder(view);
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
    public void onBindViewHolder(ViewHolder holder, int position)
    {
        holder.textView.setText(list.get(position).getGroup());
        if (position == list.size() - 1)
        {
            holder.line.setVisibility(View.GONE);
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
        View line;

        public ViewHolder(View itemView)
        {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.text);
            line = itemView.findViewById(R.id.divider);
        }
    }
}
