package com.example.worldtest.ui.dashboard.comment;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.worldtest.R;

public class ReplyCommentAdapter extends BaseAdapter {
    Context context;
    String[] replyname;
    String[] replycontent;
    String bereplyname;

    public ReplyCommentAdapter(Context c, String[] replyname, String[] replycontent,String bereplyname) {
        this.context = c;
        this.replyname = replyname;
        this.replycontent =replycontent;
        this.replyname=replyname;
        this.bereplyname=bereplyname;
    }

    @Override
    public int getCount() {
        return replyname.length;
    }

    @Override
    public Object getItem(int position) {
        return replyname[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup viewGroup) {
        final ReplyCommentAdapter.ViewHolder holder;
        // 重用convertView
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_comment_reply, null);
            holder = new ReplyCommentAdapter.ViewHolder();
            holder.repley_name = (TextView) convertView.findViewById(R.id.repley_name);
            holder.bereply_name = (TextView) convertView.findViewById(R.id.bereply_name);
            holder.comment_content=(TextView) convertView.findViewById(R.id.comment_content);
            convertView.setTag(holder);
        } else {
            holder = (ReplyCommentAdapter.ViewHolder) convertView.getTag();
        }
        // 适配数据
        holder.repley_name.setText(replyname[position]);
        holder.bereply_name.setText(bereplyname+":");
        holder.comment_content.setText(replycontent[position]);

        return convertView;
    }


    /**
     * 静态类，便于GC回收
     */
    public static class ViewHolder {
        TextView repley_name;
        TextView bereply_name;
        TextView comment_content;
    }
}

