package com.example.cookbook.user;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.example.cookbook.R;
import com.example.cookbook.util.RequestUtils;
import com.example.cookbook.util.User;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class CommentpageActivity extends Activity {
    private int cookbook_id;
    private Button submit;
    private EditText inf;
    private Map<String, Object> params;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 0:
                    Toast.makeText(CommentpageActivity.this,"发表评论失败",Toast.LENGTH_LONG).show();
                    break;
                case 1:
                    Toast.makeText(CommentpageActivity.this,"发表评论成功",Toast.LENGTH_LONG).show();
                    finish();
                    break;

                default:
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.commentpage);
        Intent intent = getIntent();
        cookbook_id = intent.getIntExtra("cookbook_id",0);
        initView();
//        CommentpageController Controller = new CommentpageController(this.findViewById(R.id.commentpage), this);
//        setListeners(Controller);
    }
    public void initView(){
        inf=(EditText)findViewById(R.id.cookbook_comment);
        submit=(Button)findViewById(R.id.cookbook_submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder confirmDialog = new AlertDialog.Builder(CommentpageActivity.this);
                confirmDialog.setTitle("发表评论");
                confirmDialog.setMessage("确定要发表评论吗？");
                confirmDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Add add =new Add();
                        add.start();
                        try {
                            add.join();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
                confirmDialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                confirmDialog.show();
            }
        });
    }
    public class Add extends Thread{
        @Override
        public void run(){
            try {
                params = new HashMap<>();
                params.put("cookbookId", cookbook_id);
                params.put("userId", User.getInstance().getUserId());
                params.put("commentInf", inf.getText().toString());
                String res = RequestUtils.post("/comment/add", params);
                try {
                    JSONObject jsonObject1 = new JSONObject(res);
                    if (jsonObject1.getInt("code") == 200) {
                        mHandler.sendEmptyMessage(1);
                    } else {
                        mHandler.sendEmptyMessage(0);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
//    public void setListeners(View.OnClickListener onClickListener){
//        findViewById(R.id.cookbook_submit).setOnClickListener(onClickListener);
//    }
//    @Override
//    public void add(){
//        final EditText comment=(EditText)findViewById(R.id.cookbook_comment);
//        if(comment.getText().equals("null")){
//            Toast.makeText(CommentpageActivity.this, "评论不能为空", Toast.LENGTH_SHORT).show();
//        }
//        else{
//            BmobQuery<Cookbook> bmobQuery = new BmobQuery<Cookbook>();
//            bmobQuery.getObject(cookbook_id, new QueryListener<Cookbook>() {
//                @Override
//                public void done(final Cookbook cookbook, BmobException e) {
//                    if (e == null) {
//                        Comment p2 = new Comment();
//                        p2.setCm_content(comment.getText().toString());
//                        p2.setCookbook(cookbook);
//                        p2.setAuthor(BmobUser.getCurrentUser(User.class));
//                        p2.save(new SaveListener<String>() {
//                            @Override
//                            public void done(String objectId,BmobException e) {
//                                if(e==null){
//                                    Toast.makeText(CommentpageActivity.this, "评论成功", Toast.LENGTH_SHORT).show();
//                                    finish();
//                                }else{
//                                    Toast.makeText(CommentpageActivity.this, "不能重复评论", Toast.LENGTH_SHORT).show();
//                                }
//                            }
//                        });
//                    } else {
//                        Log.i("bmob", "失败：" + e.getMessage());
//                    }
//                }
//            });
//        }
//    }
}
