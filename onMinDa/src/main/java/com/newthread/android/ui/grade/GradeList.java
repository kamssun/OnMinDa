package com.newthread.android.ui.grade;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.newthread.android.R;
import com.newthread.android.adapter.GradeQueryListAdapter;
import com.newthread.android.adapter.GradeQueryListAdapter.ViewHolder;
import com.newthread.android.bean.GradeInfo;
import com.newthread.android.bean.GradeListAdapterVo;
import com.newthread.android.util.MyAnimation;

public class GradeList extends SherlockFragmentActivity implements OnClickListener{
	private ListView listView;
	private GradeQueryListAdapter adapter;
	private View bottonView;
	private Button btnGAP, btnWeightingAver, btnAver;
	
	private ArrayList<GradeInfo> list;
	private GradeListAdapterVo vo;
	private String absTitle;
	
	private boolean isMultipleSelectStatus = false;
	private static final boolean DEBUG = true;
	
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_grade_list);

		initData();
		initView();
	}

	// 初始化数据
	private void initData() {
		list = new ArrayList<GradeInfo>();
		vo = new GradeListAdapterVo(false);
		
		absTitle = this.getIntent().getStringExtra("title");
		list = (ArrayList<GradeInfo>) this.getIntent().getBundleExtra("grades").getSerializable("grade_list");
	}

	// 初始化界面
	private void initView() {
		ActionBar ab = getSupportActionBar();
		ab.setDisplayUseLogoEnabled(false);
		ab.setDisplayHomeAsUpEnabled(true);
		ab.setHomeButtonEnabled(true);
		ab.setDisplayShowHomeEnabled(false);
		ab.setTitle(absTitle);
		
		bottonView = (View) this.findViewById(R.id.bottom_view);
		btnGAP = (Button) this.findViewById(R.id.GPA);
		btnWeightingAver = (Button) this.findViewById(R.id.weighting_average);
		btnAver = (Button) this.findViewById(R.id.average);
		
		listView = (ListView) this.findViewById(R.id.listView);
		adapter = new GradeQueryListAdapter(list, getApplicationContext(), vo);
		
		listView.setAdapter(adapter);
		
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
				ViewHolder holder = (ViewHolder) arg1.getTag();
				holder.CKMultipleChoice.toggle();
				
				
				if (isMultipleSelectStatus) {
					// 多选状态,用来计算
//					list.get(position).setSelected(!GradeQueryListAdapter.getIsSelected().get(position));
					GradeQueryListAdapter.getIsSelected().put(position, !list.get(position).isSelected());
					// 将CheckBox的选中状况记录下来
					list.get(position).setSelected(!list.get(position).isSelected());
				} else {
					// 单选，查看详细信息
					Intent _intent = new Intent(getApplicationContext(), GradeDetail.class);
					_intent.putExtra("grade_info", list.get(position));
					startActivity(_intent);
				}
			}
		});
		
		btnGAP.setOnClickListener(this);
		btnAver.setOnClickListener(this);
		btnWeightingAver.setOnClickListener(this);
	}

	// 控件监听
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.GPA:
			// 绩点计算
			calculateGPA();
			break;
		case R.id.weighting_average:
			// 加权平均
			calculateWeightingAverage();
			break;
		case R.id.average:
			// 平均分计算 
			calculateAverage();
			break;
		default:
			break;
		}
	}
	
	// 绩点计算
	private void calculateGPA() {
		Intent _intent = new Intent(getApplicationContext(), GradeCalculate.class);
		_intent.putExtra("calculate_type", "GPA");
		Bundle bundle = new Bundle();
		
		ArrayList<GradeInfo> recordList = new ArrayList<GradeInfo>();
		for (int i = 0; i< list.size(); i++) {
			if (list.get(i).isSelected()) { 
				recordList.add(list.get(i));
			}
		}
		bundle.putSerializable("grade_list", recordList);
		_intent.putExtra("grades", bundle);
		startActivity(_intent);
	}
	
	// 加权平均
	private void calculateWeightingAverage() {
		Intent _intent = new Intent(getApplicationContext(), GradeCalculate.class);
		_intent.putExtra("calculate_type", "weighting_average");
		Bundle bundle = new Bundle();
		
		ArrayList<GradeInfo> recordList = new ArrayList<GradeInfo>();
		for (int i = 0; i< list.size(); i++) {
			if (list.get(i).isSelected()) { 
				Log.v("select",list.get(i).isSelected()+"");
				recordList.add(list.get(i));
			}
		}
		bundle.putSerializable("grade_list", recordList);
		_intent.putExtra("grades", bundle);
		startActivity(_intent);
	}
	
	// 平均分计算
	private void calculateAverage() {
		Intent _intent = new Intent(getApplicationContext(), GradeCalculate.class);
		_intent.putExtra("calculate_type", "average");
		Bundle bundle = new Bundle(); 
		ArrayList<GradeInfo> recordList = new ArrayList<GradeInfo>();
		for (int i = 0; i< list.size(); i++) {
			if (list.get(i).isSelected()) { 
				recordList.add(list.get(i));
			}
		}
		bundle.putSerializable("grade_list", recordList);
		_intent.putExtra("grades", bundle);
		startActivity(_intent);
	}
	
	// 计算过程
	private void performCalculate() {
		isMultipleSelectStatus = !isMultipleSelectStatus;
		if(isMultipleSelectStatus) {
			vo.setMultipleSelect(true);
			adapter.notifyDataSetChanged();
			
			bottonView.setVisibility(View.VISIBLE);
			bottonView.setAnimation(MyAnimation.B_bottomToUp());
			ViewGroup.LayoutParams params =listView.getLayoutParams();
			params.height=listView.getHeight()-100;
		} else {
			 int fristParamsHight = listView.getHeight()+100;
			vo.setMultipleSelect(false);
			adapter.notifyDataSetChanged();
			bottonView.setVisibility(View.GONE);
			bottonView.setAnimation(MyAnimation.B_upToBottom());
			ViewGroup.LayoutParams params =listView.getLayoutParams();
			params.height=fristParamsHight;
			listView.setLayoutParams(params);
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add("计算").setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getTitle().equals("计算")) {
			performCalculate();
			item.setTitle("取消");
		} else if (item.getTitle().equals("取消")) {
			performCalculate();
			item.setTitle("计算");
		}

		switch (item.getItemId()) {
		case android.R.id.home:
			this.finish();
			overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
			break;
		}

		return super.onOptionsItemSelected(item);
	}
}
