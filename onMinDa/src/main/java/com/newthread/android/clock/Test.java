package com.newthread.android.clock;

import android.app.Activity;
import android.os.Bundle;

import com.newthread.android.util.Loger;

/**
 * 测试时，需要在manifest里面声明
 */
public class Test extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Loger.V("启动activity");
	}

}
