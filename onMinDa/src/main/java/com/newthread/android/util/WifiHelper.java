package com.newthread.android.util;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import java.util.List;

/**
 * Created by jindongping on 14-9-6.
 */
public class WifiHelper {
    private Context context;
    private WifiManager wifiManager;
    private static WifiHelper instance = null;

    public static WifiHelper getInstance(Context context) {
        if (instance != null) {
            return instance;
        }
        instance = new WifiHelper(context);
        return instance;

    }

    private WifiHelper(Context context) {
        this.context = context;
        wifiManager = (WifiManager) context.getSystemService(Service.WIFI_SERVICE);
    }
    /**
     * @return - 1 代表wifi没开  3 代表wifi开了
     */
    public int getWifiState() {
        return wifiManager.getWifiState();
    }

    public WifiInfo getConnectionInfo(){
       return wifiManager.getConnectionInfo();
    }

    public boolean isWifiConnect() {
        ConnectivityManager connManager = (ConnectivityManager)context.getSystemService(Service.CONNECTIVITY_SERVICE);
        NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        return mWifi.isConnected();
    }

    /**
     * 扫描wifi结果可以在wifireceiver中回调
     */
    public void scanWifi() {
        wifiManager.startScan();
    }

    private WifiReceiver wifiReceiver;

    /**
     * @param callBack －自定义毁掉接口 将回调出扫描结果
     */
    public void registWifiRecevier(CallBack callBack) {
        wifiReceiver = new WifiReceiver(callBack);
        context.registerReceiver(wifiReceiver, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
    }

    /**
     * 取消应用内注册的接收wifi扫描结果的广播
     */
    public void unRegistWifiRecevier() {
        if (wifiManager == null)
            return;
        context.unregisterReceiver(wifiReceiver);
    }

    public interface CallBack {
        public void getScanResult(List<ScanResult> scanResults);

    }

    /**
     * wifi扫描后接收的广播
     */
    private class WifiReceiver extends BroadcastReceiver {
        private CallBack callBack;

        public WifiReceiver(CallBack callBack) {
            this.callBack = callBack;
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            List<ScanResult> wifiList = wifiManager.getScanResults();
            callBack.getScanResult(wifiList);
        }
    }

    public Boolean connectWifi(ScanResult scanResult) {
        WifiConnect wifiConnect = new WifiConnect(wifiManager);
        return wifiConnect.Connect(scanResult.SSID, "dongqiaqia711", WifiConnect.WifiCipherType.WIFICIPHER_NOPASS);
    }
}

