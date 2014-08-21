package com.newthread.android.util;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.newthread.android.R;

public class UpdateManager {
	// 下载中...
	private static final int DOWNLOAD = 1;
	// 下载完成
	private static final int DOWNLOAD_FINISH = 2;
	// 保存解析的XML信息
	HashMap<String, String> mHashMap;
	// 下载保存路径
//	private String mSavePath;
	// 记录进度条数量
	private int progress;
	// 是否取消更新
	private boolean cancelUpdate = false;
	// 上下文对象
	private Context mContext;
	// 进度条
	private ProgressBar mProgressBar;
	private TextView mUpdateRate;
	// 更新进度条的对话框
	private Dialog mDownloadDialog;
	private int queryVersion = 0;	// 查询版本号 
	
	boolean queryResult = false;
	public static String UPDATE_URL = "http://59.68.29.99:8080/versionCompare/apk/update_info.txt";
	private String downloadURL;		// 应用下载地址
	
	/* 下载包安装路径 */  
    private static final String savePath = Environment.getExternalStorageDirectory().getPath()  + "/人在民大/";  
    private static final String saveFileName = savePath + "OnMinDa.apk";  

	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			// 下载中。。。
			case DOWNLOAD:
				// 更新进度条
				System.out.println(progress);
				mUpdateRate.setText(progress + "%");
				mProgressBar.setProgress(progress);
				break;
			// 下载完成
			case DOWNLOAD_FINISH:
				// 安装文件
				installApk();
				break;
			case 1000:
				if (queryVersion > getVersionCode(mContext)) {
					// 显示提示对话框
					showNoticeDialog();
				} else {
					 Toast.makeText(mContext, "无最新版本", Toast.LENGTH_SHORT).show();
				}
				break;
			}
		};
	};

	public UpdateManager(Context context) {
		super();
		this.mContext = context;
	}

	/**
	 * 检测软件更新
	 */
	public void checkUpdate() {
		isUpdate();
	}

	private void showNoticeDialog() {
		// 构造对话框
		AlertDialog.Builder builder = new Builder(mContext);
		builder.setTitle("软件更新");
		builder.setMessage("有最新版本，建议更新");
		// 更新
		builder.setPositiveButton("立即更新", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				// 显示下载对话框
				showDownloadDialog();
			}
		});
		// 稍后更新
		builder.setNegativeButton("暂不更新", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		Dialog noticeDialog = builder.create();
		noticeDialog.show();
	}

	private void showDownloadDialog() {
		// 构造软件下载对话框
		AlertDialog.Builder builder = new Builder(mContext);
		builder.setTitle("正在更新");
		// 给下载对话框增加进度条
		final LayoutInflater inflater = LayoutInflater.from(mContext);
		View view = inflater.inflate(R.layout.view_update_progress, null);
		mProgressBar = (ProgressBar) view.findViewById(R.id.progress);
		mUpdateRate = (TextView) view.findViewById(R.id.update_rate);
		builder.setView(view);
		builder.setNegativeButton("取消", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				// 设置取消状态
				cancelUpdate = true;
			}
		});
		mDownloadDialog = builder.create();
		mDownloadDialog.show();
		// 下载文件
		downloadApk();
	}

	/**
	 * 下载APK文件
	 */
	private void downloadApk() {
		// 启动新线程下载软件
		new DownloadApkThread().start();
	}

	/**
	 * 检查软件是否有更新版本
	 * @return
	 */
	public void isUpdate() {
		// 获取当前软件版本
		
		queryApkInfo();
	}

	/**
	 * 获取软件版本号
	 * @param context
	 * @return
	 */
	private int getVersionCode(Context context) {
		int versionCode = 0;

		// 获取软件版本号，对应AndroidManifest.xml下android:versionCode
		try {
			versionCode = context.getPackageManager().getPackageInfo("com.example.appupdate", 0).versionCode;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return versionCode;
	}

	/**
	 * 下载文件线程
	 * @author Administrator
	 * 
	 */
	private class DownloadApkThread extends Thread {
		@Override
		public void run() {
			try {
				// 判断SD卡是否存在，并且是否具有读写权限
				if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
					URL url = new URL(downloadURL);
					// 创建连接
					HttpURLConnection conn = (HttpURLConnection) url.openConnection();
					conn.connect();
					// 获取文件大小
					int length = conn.getContentLength();
					
					System.out.println("*******length:  " + length);
					// 创建输入流
					InputStream is = conn.getInputStream();

					File file = new File(savePath);
					// 如果文件不存在，新建目录
					if (!file.exists()) {
						file.mkdir();
					}
					File apkFile = new File(saveFileName);
					FileOutputStream fos = new FileOutputStream(apkFile);
					
					int count = 0;
					// 缓存
					byte buf[] = new byte[1024];
					// 写入到文件中
					do {
						int numread = is.read(buf);
						count += numread;
						// 计算进度条的位置
						progress = (int) (((float) count / length) * 100);
						// 更新进度
						mHandler.sendEmptyMessage(DOWNLOAD);
						if (numread <= 0) {
							// 下载完成
							mHandler.sendEmptyMessage(DOWNLOAD_FINISH);
							break;
						}
						// 写入文件
						fos.write(buf, 0, numread);
					} while (!cancelUpdate);// 点击取消就停止下载
					fos.close();
					is.close();
				}
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
			// 取消下载对话框显示
			mDownloadDialog.dismiss();
		}
	}
	
	// 查询服务端安装文件信息
	private void queryApkInfo() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					HttpGet httpRequest = new HttpGet(UPDATE_URL);
					HttpParams httpParameters = new BasicHttpParams();
					HttpConnectionParams.setConnectionTimeout(httpParameters, 15000);
					HttpConnectionParams.setSoTimeout(httpParameters, 15000);
					// HttpClient对象
					HttpClient httpClient = new DefaultHttpClient(httpParameters);
					// 获得HttpResponse对象
					HttpResponse httpResponse = httpClient.execute(httpRequest);
					int response = httpResponse.getStatusLine().getStatusCode();
					if (response == HttpStatus.SC_OK) {
						// 取得返回的数据
						String result = EntityUtils.toString(httpResponse.getEntity());
						Log.v("resultAgnecy", result);
						
						parse(result);
						
						mHandler.sendEmptyMessage(1000);
					} else {
						System.out.println(httpResponse.getStatusLine().getStatusCode() + "");
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
	}
	
	// 解析
	private void parse(String result) {
		try {
			//创建DocumentBuilderFactory，该对象将创建DocumentBuilder。  
	        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();  
	        //创建DocumentBuilder，DocumentBuilder将实际进行解析以创建Document对象  
	        DocumentBuilder builder;
			builder = factory.newDocumentBuilder();
			
			// 解析该文件以创建Document对象  
	        Document document = builder.parse(new ByteArrayInputStream(result.getBytes()));
	        // 获取XML文件根节点   
	        Element root = document.getDocumentElement();  
	        // 获得所有子节点  
	        NodeList childNodes = root.getChildNodes();  
	        for(int i = 0; i < childNodes.getLength(); i++) {  
	            Node childNode = (Node) childNodes.item(i);  
	            if(childNode.getNodeType() == Node.ELEMENT_NODE) {  
	                Element childElement = (Element) childNode;  
	                //版本号   
	                if("version".equals(childElement.getNodeName())) {
	                	System.out.println("**version**:  " + childElement.getFirstChild().getNodeValue());
	                	
	                	queryVersion = Integer.parseInt(childElement.getFirstChild().getNodeValue());
	                	System.out.println("**version**:  " + queryVersion);
//			                    hashMap.put("version", childElement.getFirstChild().getNodeValue());  
	                //软件名称   
	                } else if("name".equals(childElement.getNodeName())) {  
//			                	System.out.println("**version**:  " + childElement.getFirstChild().getNodeValue());
//			                    hashMap.put("name", childElement.getFirstChild().getNodeValue());  
	                //下载地址  
	                } else if("url".equals(childElement.getNodeName())) {  
	                	downloadURL = childElement.getFirstChild().getNodeValue();
//			                    hashMap.put("url", childElement.getFirstChild().getNodeValue());  
	                }  
	            }  
	        }  
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 安装APK文件
	 */
	private void installApk() {
		System.out.println("*********installApk");
		
		File apkfile = new File(saveFileName);
		if (!apkfile.exists()) {
			return;
		}
		Intent i = new Intent(Intent.ACTION_VIEW);
		i.setDataAndType(Uri.parse("file://" + apkfile.toString()), "application/vnd.android.package-archive");
		mContext.startActivity(i);
	}
}
