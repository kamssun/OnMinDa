package com.newthread.android.activity.main;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;
import com.newthread.android.R;
import org.kymjs.aframe.ui.BindView;

public class AboutActivity extends SherlockActivity {
    private TextView description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        initView();
    }

    private void initView() {
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setDisplayShowHomeEnabled(false);
        ab.setHomeButtonEnabled(true);
        ab.setTitle("关于");
        description=(TextView)findViewById(R.id.description2);
        description.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("http://www.new-thread.com");
                Intent it = new Intent(Intent.ACTION_VIEW,uri);
                startActivity(it);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
                break;
        }

        return super.onOptionsItemSelected(item);
    }


}
