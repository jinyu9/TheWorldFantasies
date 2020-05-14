package com.example.worldtest.ui.dashboard.comment;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.worldtest.R;

import java.util.List;


public class CommentAdapter extends BaseAdapter {
    Context context;
    List<Comment> data;
    String[] replyname;
    String[] replycontent;
    String zhongjian;
    ReplyCommentAdapter adapter;


    public CommentAdapter(Context c, List<Comment> data){
        this.context = c;
        this.data = data;
       // System.out.println("data.size:"+this.data.size());
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup viewGroup) {
        final ViewHolder holder;
        // 重用convertView
        if(convertView == null){
            convertView = View.inflate(context, R.layout.item_comment, null);
            holder = new ViewHolder();
            holder.comment_name = (TextView) convertView.findViewById(R.id.comment_name);
            holder.comment_content = (TextView) convertView.findViewById(R.id.comment_content);
            holder.reply_list = (ListView) convertView.findViewById(R.id.reply_list);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        // 适配数据
        Comment comment=data.get(position);
        holder.comment_name.setText(comment.getName()+":");
        holder.comment_content.setText(comment.getContent());
        if (comment.getReplyname() == null||comment.getReplyname().equals("")) { // 没有图片资源就隐藏GridView
            holder.reply_list.setVisibility(View.GONE);
        } else {
            zhongjian = comment.getReplyname();
            replyname=zhongjian.split(";;;");
            zhongjian=comment.getReplycontent();
            replycontent=zhongjian.split(";;;");
          //  System.out.println("comment.name:"+comment.getName());

            adapter = new ReplyCommentAdapter(context,replyname,replycontent,comment.getName());
            holder.reply_list.setAdapter(adapter);
            Utility.setListViewHeightBasedOnChildren(holder.reply_list);
            holder.reply_list.setTag(position);
            // System.out.println("进入grid view");
        }

        return convertView;
    }

    /**
     * 静态类，便于GC回收
     */
    public static class ViewHolder{
        TextView comment_name;
        TextView comment_content;
        ListView reply_list;
    }

}
