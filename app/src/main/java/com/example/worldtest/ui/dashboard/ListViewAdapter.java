package com.example.worldtest.ui.dashboard;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.res.ResourcesCompat;

import com.example.worldtest.Main2Activity;
import com.example.worldtest.R;
import com.example.worldtest.ui.PeopleInfoActivity;
import com.example.worldtest.ui.Report.ReportMomentActivity;
import com.example.worldtest.ui.dashboard.comment.Comment;
import com.example.worldtest.ui.dashboard.comment.CommentAdapter;
import com.example.worldtest.ui.dashboard.comment.Utility;
import com.lcodecore.extextview.ExpandTextView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

import static cn.bmob.v3.Bmob.getApplicationContext;

public class ListViewAdapter extends BaseAdapter {


    private boolean isOpen = false;
    private int mShortHeight;//限定行数高度
    private int mLongHeight;//展开全文高度
    private LinearLayout.LayoutParams mLayoutParams;
    private int mMaxlines = 2;//设定显示的最大行数
    private int maxLine;//真正的最大行数

    private Context context;
    private List<Moment> list;
    private int checkdelete;
    private int flagnum;
    private String[] imageurl;
    private String image;
    private int showMore = 1;
    public static int init;
    GrideViewAdapter adapter;

    private PopupWindow mMorePopupWindow;
    private PopupWindow inputmPopWindow;
    private int mShowMorePopupWindowWidth;
    private int mShowMorePopupWindowHeight;

    private String report_name;
    private String momentPosition;
    TextView like;
    TextView comment;
    String user_name;
    String zhongjianshang;
    String zhongjianshang1;
    ImageView imageView;
    String momentId;
    CommentAdapter commentAdapter;
    TextView delect;

    private DisplayImageOptions options = new DisplayImageOptions.Builder()
            .showStubImage(R.mipmap.ic_launcher)                      //设置图片下载期间显示的图片
            .showImageForEmptyUri(R.drawable.aa)               //设置图片uri为空或者是错位的时候显示的图片
            .showImageOnFail(R.mipmap.ic_launcher)                    //设置图片加载或解码过程中发生错误显示的图片
            .cacheInMemory(true)                                          //设置下载的图片是否缓存在内存中
            .cacheOnDisk(true)                                            //设置下载的图片是否缓存在SD中
            .displayer(new RoundedBitmapDisplayer(20))  // 设置成圆角图片
            .build();                                                     //创建配置或的DisplayImageOption对象

    ImageLoader imageLoader = ImageLoader.getInstance();

    public ListViewAdapter(Context context, List<Moment> list, int flagnum, String user_name) {
        this.context = context;
        this.list = list;
        this.flagnum = flagnum;
        this.user_name = user_name;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_gridview, null);
            viewHolder = new ViewHolder();
            viewHolder.image = (ImageView) convertView.findViewById(R.id.image);
            viewHolder.name = (TextView) convertView.findViewById(R.id.name);
            viewHolder.text = (ExpandTextView) convertView.findViewById(R.id.text);
//            viewHolder.tv_toggle = (TextView) convertView.findViewById(R.id.tv_toggle);
            viewHolder.time = (TextView) convertView.findViewById(R.id.time);
            viewHolder.gridview = (MyGridView) convertView.findViewById(R.id.gridview);
            viewHolder.delete = (TextView) convertView.findViewById(R.id.delete);
            viewHolder.other = (LinearLayout) convertView.findViewById(R.id.other);
            viewHolder.more = (TextView) convertView.findViewById(R.id.more);
            viewHolder.reportMoment = (TextView) convertView.findViewById(R.id.reportMoment);
            viewHolder.withDraw = (TextView) convertView.findViewById(R.id.withDraw);
            viewHolder.imageView = (ImageView) convertView.findViewById(R.id.img_click_praise_or_comment);
            viewHolder.listView = (ListView) convertView.findViewById(R.id.comment_list);
            Typeface typeface = ResourcesCompat.getFont(context, R.font.siyuansongti);
            viewHolder.text.setTypeface(typeface);
            viewHolder.name.setTypeface(typeface);
            viewHolder.more.setTypeface(typeface);
            viewHolder.delete.setTypeface(typeface);
            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }


        Moment moment = list.get(position);
        viewHolder.name.setText(moment.getUser_name());//用户名
        viewHolder.text.setText(moment.getContent());

        viewHolder.time.setText(moment.getCreatedAt());//发表时间
        viewHolder.other.setVisibility(View.GONE);
        if((FindDiscover.username!=null&&FindDiscover.username.equals(moment.getUser_name()))||(Main2Activity.username!=null&&Main2Activity.username.equals(moment.getUser_name()))){
            viewHolder.delete.setVisibility(View.VISIBLE);
            showMore = 0;
            viewHolder.more.setVisibility(View.GONE);
            notifyDataSetChanged();
        }else{
            viewHolder.delete.setVisibility(View.GONE);
            viewHolder.more.setVisibility(View.VISIBLE);
            notifyDataSetChanged();
        }
        viewHolder.more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewHolder.other.setVisibility(View.VISIBLE);
            }
        });
        if(showMore == 1) {
            report_name = moment.getUser_name();
            momentPosition = moment.getObjectId();
            viewHolder.reportMoment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, ReportMomentActivity.class);
                    intent.putExtra("report_name",moment.getUser_name());
                    intent.putExtra("momentID",moment.getObjectId());
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            });
            viewHolder.withDraw.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    viewHolder.other.setVisibility(View.GONE);
                    notifyDataSetChanged();
                }
            });
        }
        viewHolder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, PeopleInfoActivity.class);
                intent.putExtra("user_name",moment.getUser_name());
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
        viewHolder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                notifyDataSetChanged();
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setIcon(null);
                builder.setTitle("提示");
                builder.setMessage("确定要删除这条动态吗？");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    moment.delete(new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e == null) {
                                Toast toast = Toast.makeText(getApplicationContext(), "删除成功!", Toast.LENGTH_SHORT);
                                toast.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL, 0, 0);
                                toast.show();
                                list.remove(position);
                                notifyDataSetChanged();
                            } else {
                                Toast.makeText(getApplicationContext(), "删除失败！", Toast.LENGTH_SHORT).show();
                                System.out.println("error:" + e.getMessage());
                            }
                        }
                    });
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast toast = Toast.makeText(getApplicationContext(),"您已取消删除！",Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER_VERTICAL| Gravity.CENTER_HORIZONTAL, 0, 0);
                        toast.show();
                    }
                });
                AlertDialog dialog=builder.create();
                dialog.show();
            }
        });

/*
        //显示发表消息的用户的头像
        BmobQuery<Advertisement> categoryBmobQuery = new BmobQuery<>();
        categoryBmobQuery.addWhereEqualTo("name", moment.getUser_name());//根据用户名查找对应的图片头像
        categoryBmobQuery.findObjects(new FindListener<Advertisement>() {
            @Override
            public void done(List<Advertisement> object, BmobException e) {
                if (e == null) {
                    String touxiang = object.get(0).getAvatar();
                    Log.w("BMOB",touxiang);
                    imageLoader.displayImage(touxiang, viewHolder.image, options);

                } else {
                    Log.e("BMOB", e.toString());
                }
            }
        });
*/
        if (moment.getPicture() == null||moment.getPicture().equals("")) { // 没有图片资源就隐藏GridView
            System.out.println("no picture.");
            viewHolder.gridview.setVisibility(View.GONE);
        } else {
            image = moment.getPicture();
            imageurl = image.split(";");
            System.out.println("image = "+imageurl[0]);
            adapter = new GrideViewAdapter(context,imageurl);
            viewHolder.gridview.setTag(position);
            viewHolder.gridview.setAdapter(adapter);

            System.out.println("position="+position);
            viewHolder.gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    init = i;
                    int lv_item_position= (Integer) adapterView.getTag();//GridView在ListView条目里的位置
                    //获取点击的图片,查看对应消息的所有大图
                    List<String> b = new ArrayList<>();
                    if(FindDiscover.size!=0&&FindDiscover.a!=null&&FindDiscover.n!=null&&flagnum == 1){
                        System.out.println("it's find.");
                        for (int k = 0; k < FindDiscover.size; k++) {
                            if (k == lv_item_position) {
                                System.out.println("FindDiscover.n[k] = " + FindDiscover.n[k]);
                                for (int j = 0; j < FindDiscover.n[k]; j++) {
                                    b.add(FindDiscover.a[k][j]);
                                }
                            }
                        }
                    }else if(PeopleInfoActivity.size!=0&&PeopleInfoActivity.a!=null&&PeopleInfoActivity.n!=null&&flagnum == 2){
                        System.out.println("it's Peopleinfo.");
                        for (int k = 0; k < PeopleInfoActivity.size; k++) {
                            if (k == lv_item_position) {
                                for (int j = 0; j < PeopleInfoActivity.n[k]; j++) {
                                    b.add(PeopleInfoActivity.a[k][j]);
                                }
                            }
                        }
                    }else {
                        for (int k = 0; k < DashboardFragment.size; k++) {
                            if (k == lv_item_position) {
                                System.out.println("DashboardFragment.n[k] = " + DashboardFragment.n[k]);
                                for (int j = 0; j < DashboardFragment.n[k]; j++) {
                                    b.add(DashboardFragment.a[k][j]);
                                    System.out.println("lv_item_position=" + lv_item_position);
                                    System.out.println("b=" + b.get(j));
                                }
                            }
                        }
                    }
                    Image image = new Image();
                    image.setId(position);
                    image.setImageurl(b);

                    Intent intent = new Intent(context,ShowImageActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("data",image);
                    context.startActivity(intent);
                }
            });
          //  System.out.println("进入grid view");
        }

        momentId=moment.getObjectId();
        BmobQuery<Comment> bmobQuery1 = new BmobQuery<Comment>();
        bmobQuery1.addWhereEqualTo("momentId",momentId);
        ListView listView = viewHolder.listView;
        bmobQuery1.findObjects(new FindListener<Comment>() {  //按行查询
            @Override
            public void done(List<Comment> list, BmobException e) {
                if (e == null) {
                    commentAdapter = new CommentAdapter(context, list);
                    listView.setAdapter(commentAdapter);
                    Utility.setListViewHeightBasedOnChildren(listView);
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            //  int lv_item_position = (Integer) adapterView.getTag();
                            reply(list.get(i));
                        }
                    });
                    listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                       @Override
                       public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                           delect(list.get(position),view,list.get(position).getName());
                            return true;
                       }
                    });
                } else {
                    System.out.println(e.getMessage());
                }
            }
        });



        viewHolder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMore(v,moment);
            }
        });



        return convertView;
    }
    /*
public void showDialog(){
    AlertDialog.Builder builder=new AlertDialog.Builder(context);
    builder.setTitle("温馨提示");
    builder.setMessage("您确定要删除该动态吗？");
    builder.setPositiveButton("是的，删除",
            new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    checkdelete = 1;
                }
            });
    builder.setNegativeButton("不删了", new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
            checkdelete = 0;
        }
    });
    AlertDialog dialog=builder.create();
    dialog.show();

}
*/

    public  class ViewHolder {


        ImageView image;
        TextView name;
        ExpandTextView text;
        ImageView icon;
        TextView time;
        TextView delete;
        TextView more;
        TextView reportMoment;
        TextView withDraw;
        LinearLayout other;
        MyGridView gridview;
        ImageView imageView;
        ListView listView;
    }

    private void showMore(View moreBtnView,Moment moment) {

            View content;
            LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            content = li.inflate(R.layout.popup_window_praise_or_comment_view, null, false);
            mMorePopupWindow = new PopupWindow(content, ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            mMorePopupWindow.setBackgroundDrawable(new BitmapDrawable());
            mMorePopupWindow.setOutsideTouchable(true);
            mMorePopupWindow.setTouchable(true);
            content.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
            mShowMorePopupWindowWidth = content.getMeasuredWidth();
            mShowMorePopupWindowHeight = content.getMeasuredHeight();
            View parent = mMorePopupWindow.getContentView();
            like = (TextView) parent.findViewById(R.id.like);
            comment = (TextView) parent.findViewById(R.id.comment);
            imageView=(ImageView)parent.findViewById(R.id.heart);

            // 点赞的监听器

        if(moment.getPrise_username()==null)
        {
            imageView.setImageResource(R.drawable.heart_drawable_white);
        }else if(moment.getPrise_username().equals(""))
        {
            imageView.setImageResource(R.drawable.heart_drawable_white);
        }
        else{
            int flag1=0;
            String[] spString = moment.getPrise_username().split(";");
            //System.out.println("spString.length:"+spString.length);
            for (int i = 0; i < spString.length; i++) {
                if (spString[i].equals(user_name)) {
                    flag1 = 1;
                    imageView.setImageResource(R.drawable.heart_drawable_red);
                }
            }
            if(flag1==0){
                imageView.setImageResource(R.drawable.heart_drawable_white);
            }
        }
        if(moment.getPrise()==null||Integer.parseInt(moment.getPrise())==0){
            // System.out.println("Integer.parseInt(prise):"+Integer.parseInt(moment.getPrise()));
            like.setText("赞");
            notifyDataSetChanged();
        }else if(Integer.parseInt(moment.getPrise())!=0){
            like.setText(moment.getPrise());
            notifyDataSetChanged();
        }

        like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(moment.getPrise_username()==null)
                {
                    like.setText("1");
                    moment.setPrise("1");
                    zhongjianshang=user_name;
                    moment.setPrise_username(zhongjianshang+";");
                    imageView.setImageResource(R.drawable.heart_drawable_red);
                    moment.update(new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if(e==null){
                                Toast.makeText(context,"点赞成功!",Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(context,e.getMessage(),Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
                else if(moment.getPrise_username().equals(""))
                {
                    like.setText("1");
                    moment.setPrise("1");
                    zhongjianshang=user_name;
                    moment.setPrise_username(zhongjianshang+";");
                    imageView.setImageResource(R.drawable.heart_drawable_red);
                    moment.update(new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if(e==null){
                                Toast.makeText(context,"点赞成功",Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(context,e.getMessage(),Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }else{
                    int flag=0;
                    String[] spString = moment.getPrise_username().split(";");
                    //System.out.println("spString.length:"+spString.length);
                    for (int i = 0; i < spString.length; i++) {
                        if (spString[i].equals(user_name)) {
                            flag = 1;
                            int n = Integer.parseInt(moment.getPrise()) - 1;
                            like.setText("" + n);
                            moment.setPrise("" + n);
                            zhongjianshang = moment.getPrise_username();
                            //System.out.println("初始点赞者"+zhongjianshang);
                            zhongjianshang1 = zhongjianshang.replace(spString[i] + ";", "");
                            // System.out.println("目前点赞者"+zhongjianshang1);
                            moment.setPrise_username(zhongjianshang1);
                            imageView.setImageResource(R.drawable.heart_drawable_white);
                            moment.update(new UpdateListener() {
                                @Override
                                public void done(BmobException e) {
                                    if (e == null) {
                                        Toast.makeText(context, "取消点赞", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    }
                    if(flag==0){
                        int n1=Integer.parseInt(moment.getPrise())+1;
                        like.setText(""+n1);
                        moment.setPrise(""+n1);
                        // System.out.println("zhongjianshang:"+zhongjianshang);
                        // System.out.println("user_name:"+user_name);
                        zhongjianshang=moment.getPrise_username();
                        zhongjianshang1=zhongjianshang+user_name;
                        //System.out.println("zhongjianshang1:"+zhongjianshang1);
                        moment.setPrise_username(zhongjianshang1+";");
                        imageView.setImageResource(R.drawable.heart_drawable_red);
                        moment.update(new UpdateListener() {
                            @Override
                            public void done(BmobException e) {
                                if(e==null){
                                    Toast.makeText(context,"点赞成功",Toast.LENGTH_SHORT).show();
                                }else{
                                    Toast.makeText(context,e.getMessage(),Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                }
                notifyDataSetChanged();

            }
        });
        // 评论的监听器
        comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupWindow(moment);
            }
        });

        if (mMorePopupWindow.isShowing()) {
            mMorePopupWindow.dismiss();
        } else {
            int heightMoreBtnView = moreBtnView.getHeight();

            mMorePopupWindow.showAsDropDown(moreBtnView, -mShowMorePopupWindowWidth,
                    -(mShowMorePopupWindowHeight + heightMoreBtnView) / 2);
        }
    }

    static String zhongjian1;

    //回复评论
    private void reply( Comment comment) {
        //设置contentView

        View contentView = LayoutInflater.from(context).inflate(R.layout.comment, null);
        inputmPopWindow = new PopupWindow(contentView,
                ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT, true);
        inputmPopWindow.setContentView(contentView);
        //防止PopupWindow被软件盘挡住（可能只要下面一句，可能需要这两句）
//        mPopWindow.setSoftInputMode(PopupWindow.INPUT_METHOD_NEEDED);
        inputmPopWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        //设置软键盘弹出
        InputMethodManager inputMethodManager = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInput(1000, InputMethodManager.HIDE_NOT_ALWAYS);//这里给它设置了弹出的时间
        //设置各个控件的点击响应
        final EditText editText = contentView.findViewById(R.id.comment_content);
        Button btn = contentView.findViewById(R.id.comment_send);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inputString = editText.getText().toString();
                if(inputString.equals("")){
                    Toast.makeText(context, "回复不能为空！", Toast.LENGTH_SHORT).show();
                }else{
                    // 更新回复数据
                    if(comment.getReplyname()!=null){
                        zhongjian1=comment.getReplyname()+user_name;
                    }else {
                        zhongjian1=user_name;
                    }
                    comment.setReplyname(zhongjian1+";;;");

                    if(comment.getReplycontent()!=null){
                        zhongjian1=comment.getReplycontent()+inputString;
                    }else {
                        zhongjian1=inputString;
                    }
                    comment.setReplycontent(zhongjian1+";;;");
                    comment.update(comment.getObjectId(),new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if(e==null){
                                notifyDataSetChanged();
                                Toast.makeText(context,"回复成功!",Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(context,e.getMessage(),Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    // 发送完，清空输入框
                    editText.setText("");

                }
                inputmPopWindow.dismiss();//让PopupWindow消失
            }
        });
        //是否具有获取焦点的能力
        inputmPopWindow.setFocusable(true);
        //显示PopupWindow
        View rootview = LayoutInflater.from(context).inflate(R.layout.fragment_dashboard, null);
        inputmPopWindow.showAtLocation(rootview, Gravity.BOTTOM, 0, 0);
    }


    //评论
    private void showPopupWindow(Moment moment) {
        //设置contentView

        View contentView = LayoutInflater.from(context).inflate(R.layout.comment, null);
        inputmPopWindow = new PopupWindow(contentView,
                ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT, true);
        inputmPopWindow.setContentView(contentView);
        //防止PopupWindow被软件盘挡住（可能只要下面一句，可能需要这两句）
//        mPopWindow.setSoftInputMode(PopupWindow.INPUT_METHOD_NEEDED);
        inputmPopWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        //设置软键盘弹出
        InputMethodManager inputMethodManager = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInput(1000, InputMethodManager.HIDE_NOT_ALWAYS);//这里给它设置了弹出的时间
        //设置各个控件的点击响应
        final EditText editText = contentView.findViewById(R.id.comment_content);
        Button btn = contentView.findViewById(R.id.comment_send);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inputString = editText.getText().toString();
                if(inputString.equals("")){
                    Toast.makeText(context, "评论不能为空！", Toast.LENGTH_SHORT).show();
                }else{
                    // 生成评论数据
                    Comment comment = new Comment();
                    comment.setName(user_name);
                    comment.setContent(inputString);
                    comment.setMomentId(moment.getObjectId());
                    comment.save(new SaveListener<String>() {
                        @Override
                        public void done(String objectId,BmobException e) {
                            if(e==null){
                                notifyDataSetChanged();
                                Toast.makeText(context, "评论成功！", Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(context, "评论失败！", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    // 发送完，清空输入框
                    editText.setText("");

                }
                inputmPopWindow.dismiss();//让PopupWindow消失
            }
        });
        //是否具有获取焦点的能力
        inputmPopWindow.setFocusable(true);
        //显示PopupWindow
        View rootview = LayoutInflater.from(context).inflate(R.layout.fragment_dashboard, null);
        inputmPopWindow.showAtLocation(rootview, Gravity.BOTTOM, 0, 0);
    }

    //删除评论
    private void delect(Comment comment,View view,String user_name) {
        //设置contentView
            View content;
            LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            content = li.inflate(R.layout.delect, null, false);
            mMorePopupWindow = new PopupWindow(content, ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            mMorePopupWindow.setBackgroundDrawable(new BitmapDrawable());
            mMorePopupWindow.setOutsideTouchable(true);
            mMorePopupWindow.setTouchable(true);
            content.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
            mShowMorePopupWindowWidth = content.getMeasuredWidth();
            mShowMorePopupWindowHeight = content.getMeasuredHeight();
            View parent = mMorePopupWindow.getContentView();
            delect = parent.findViewById(R.id.delect);

            // linearLayout=parent.findViewById(R.id.popup);
            // 点赞的监听器

        delect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Main2Activity.username.equals(user_name))
                {
                    comment.delete(new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if(e==null){
                                notifyDataSetChanged();
                                Toast.makeText(context, "删除成功！", Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(context, "删除失败！", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }else {
                    Toast.makeText(context, "不可以删除别人的评论哦", Toast.LENGTH_SHORT).show();
                }

            }
        });
        if (mMorePopupWindow.isShowing()) {
            mMorePopupWindow.dismiss();
        } else {
            int heightMoreBtnView = view.getHeight();

            mMorePopupWindow.showAsDropDown(view, 0,
                    -(mShowMorePopupWindowHeight + heightMoreBtnView) );
        }

    }
}
