package com.newthread.android.util;

import android.support.v4.app.FragmentActivity;
import de.keyboardsurfer.android.widget.crouton.Configuration;
import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;

public class CroutonUtil {
	public static final int DURATION_SHORT = 3000;
	public static final int DURATION_LONG = 5000;
	
	// 显示提示消息: 3s
	public static void showInfoCrouton(final FragmentActivity fa, boolean isTop, String prompt, int bottomOfthis) {
		final Crouton crouton;
		if (isTop) {
			crouton = Crouton.makeText(fa, prompt, Style.INFO);
		} else {
			crouton = Crouton.makeText(fa, prompt, Style.INFO, bottomOfthis);
		}
		
		Configuration croutonConfiguration = new Configuration.Builder().setDuration(DURATION_SHORT).build();
		crouton.setConfiguration(croutonConfiguration).show();
	}
	
	// 显示提示消息: 5s
	public static void showInfoCroutonLong(final FragmentActivity fa, boolean isTop, String prompt, int bottomOfthis) {
		final Crouton crouton;
		if (isTop) {
			crouton = Crouton.makeText(fa, prompt, Style.INFO);
		} else {
			crouton = Crouton.makeText(fa, prompt, Style.INFO, bottomOfthis);
		}
		
		Configuration croutonConfiguration = new Configuration.Builder().setDuration(DURATION_LONG).build();
		crouton.setConfiguration(croutonConfiguration).show();
	}
	
	// 显示警示消息
	public static void showAlertCrouton(final FragmentActivity fa, boolean isTop, String prompt, int bottomOfthis) {
		final Crouton crouton;
		if (isTop) {
			crouton = Crouton.makeText(fa, prompt, Style.ALERT);
		} else {
			crouton = Crouton.makeText(fa, prompt, Style.ALERT, bottomOfthis);
		}
		crouton.setConfiguration(Configuration.DEFAULT).show();
	}
	
	// 显示通知消息
	public static void showConfirmCrouton(final FragmentActivity fa, boolean isTop, String prompt, int bottomOfthis) {
		final Crouton crouton;
		if (isTop) {
			crouton = Crouton.makeText(fa, prompt, Style.CONFIRM);
		} else {
			crouton = Crouton.makeText(fa, prompt, Style.CONFIRM, bottomOfthis);
		}
		crouton.setConfiguration(Configuration.DEFAULT).show();
	}
}
