package com.newthread.android.ui.library;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.newthread.android.R;
import com.newthread.android.util.StringUtils;

public class LibraryActivity extends SherlockFragmentActivity {
    private Button queryBtn;
    private EditText keyEt;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library);

        initView();
    }

//	@Override
//	public boolean onOptionsItemSelected(MenuItem item) {
//		switch (item.getItemId()) {
//		case android.R.id.home:
//			this.finish(  );
//			overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
//			break;
//		}
//
//		if (item.getTitle().equals("登录")) {
//			Intent _intent = new Intent(this, LibraryLoginActivity.class);
//			startActivity(_intent);
//			overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
//		}
//		return super.onOptionsItemSelected(item);
//	}

//	@Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        menu.add("登录").setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
//        return true;
//    }

    public void initView() {
//        ActionBar ab = getSupportActionBar();
//        ab.setDisplayHomeAsUpEnabled(true);
//        ab.setDisplayShowHomeEnabled(false);	// 去掉ABS的Logo
//        ab.setHomeButtonEnabled(true);
//        ab.setTitle("图书馆");

        queryBtn = (Button) this.findViewById(R.id.library_search_execute);
        keyEt = (EditText) this.findViewById(R.id.library_search_et);

        queryBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (!StringUtils.isEmpty(keyEt.getText().toString().trim())) {
                    Intent _intent = new Intent(LibraryActivity.this, LibraryQueryListActivity.class);
                    _intent.putExtra("key", keyEt.getText().toString().trim());
                    startActivity(_intent);
                } else {
                    Toast.makeText(getApplicationContext(), R.string.prompt_no_content, Toast.LENGTH_SHORT).show();
                }
            }
        });

        keyEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        this.findViewById(R.id.btn_back).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // 登录
        this.findViewById(R.id.tv_login).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent _intent = new Intent(LibraryActivity.this, LibraryLoginActivity.class);
                startActivity(_intent);
                overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
            }
        });
    }
}
