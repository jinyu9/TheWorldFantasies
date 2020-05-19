package com.example.worldtest.ui.Report;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.worldtest.Main2Activity;
import com.example.worldtest.R;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

import static com.example.worldtest.ActivityCollectorUtil.addActivity;
import static com.example.worldtest.ActivityCollectorUtil.removeActivity;

public class ReportMomentActivity extends AppCompatActivity implements View.OnClickListener{
    private TextView reportName;
    private EditText reportReason;
    private Button commit;
    private String report_name;
    private String report_reason;
    private String momentID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_moment);
        addActivity(this);
        reportName = findViewById(R.id.report_name);
        reportReason = findViewById(R.id.reason);
        commit = findViewById(R.id.commit);
        Intent intent = this.getIntent();
        report_name = intent.getStringExtra("report_name");
        momentID = intent.getStringExtra("momentID");
        reportName.setText("该动态作者ID："+report_name);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.commit) {
            report_reason = reportReason.getText().toString().trim();
            if (report_reason == null || report_reason.equals("")) {
                Toast.makeText(ReportMomentActivity.this, "举报理由不能为空！", Toast.LENGTH_SHORT).show();
            } else {
                Report_Moment reportMoment = new Report_Moment();
                reportMoment.setUser(Main2Activity.username);
                reportMoment.setMomentId(momentID);
                reportMoment.setReason(report_reason);
                reportMoment.save(new SaveListener<String>() {
                    @Override
                    public void done(String s, BmobException e) {
                        if (e == null) {
                            Toast.makeText(ReportMomentActivity.this, "举报成功！\n我们已收到您的反馈！", Toast.LENGTH_SHORT).show();
//                            Intent intent = new Intent(getApplicationContext(), PeopleInfoActivity.class);
//                            intent.putExtra("user_name", report_name);
//                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(ReportMomentActivity.this, "举报失败！请稍后重试！", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        removeActivity(this);
    }
}
