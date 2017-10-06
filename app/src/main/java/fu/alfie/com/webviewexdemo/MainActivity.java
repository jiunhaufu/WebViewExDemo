package fu.alfie.com.webviewexdemo;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.customtabs.CustomTabsClient;
import android.support.customtabs.CustomTabsIntent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editText = (EditText) findViewById(R.id.editText);
    }

    public void onBrowserChromeClick(View view){
        String inputUrl = String.valueOf(editText.getText());
        Intent intent=new Intent(Intent.ACTION_VIEW,Uri.parse(inputUrl));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setPackage("com.android.chrome");
        try {
            startActivity(intent);
        } catch (ActivityNotFoundException ex) {
            intent.setPackage(null); //沒安裝chrome就用原生瀏覽器開啟
            startActivity(intent);
            Toast.makeText(this,"沒安裝chrome",Toast.LENGTH_SHORT).show();
        }
    }

    public void onWebViewClick(View view){
        String inputUrl = String.valueOf(editText.getText());
        startActivity(new Intent(this,WebViewActivity.class).putExtra("inputUrl",inputUrl));
    }

    public void onAdvancedWebViewClick(View view){
        String inputUrl = String.valueOf(editText.getText());
        startActivity(new Intent(this,AdvancedWebViewActivity.class).putExtra("inputUrl",inputUrl));
    }

    public void onFullscreenWebViewClick(View view){
        String inputUrl = String.valueOf(editText.getText());
        startActivity(new Intent(this,FullscreenWebViewActivity.class).putExtra("inputUrl",inputUrl));
    }

    public void onChromeCustomTabs(View view){
        String inputUrl = String.valueOf(editText.getText());
//        CustomTabsClient.connectAndInitialize(this, "com.android.chrome"); //简单的预启动要放在onStart
        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
        CustomTabsIntent customTabsIntent = builder
                .setToolbarColor(ContextCompat.getColor(this, R.color.colorPrimary))
                .setShowTitle(true)
                .build();
        customTabsIntent.launchUrl(this, Uri.parse(inputUrl));

    }

    public void onHtml5WebViewClick(View view){
        String inputUrl = String.valueOf(editText.getText());
        startActivity(new Intent(this,Html5WebViewActivity.class).putExtra("inputUrl",inputUrl));
    }

    //=========================================MY=WEBVIEW===========================================

    public void onMyWebViewClick(View view){
        if (isConnected()){
            String inputUrl = String.valueOf(editText.getText());  //跳轉網址
            //ANDROID 5.0以上使用Webview
            if (Build.VERSION.SDK_INT >= 21){
                startActivity(new Intent(this,MyWebViewActivity.class).putExtra("inputUrl",inputUrl));
            //ANDROID 5.0以下使用Chrome Custom Tab
            }else{
                //檢查是否安裝Chrome瀏覽器
                PackageManager packageManager = getPackageManager();
                if (isPackageInstalled("com.android.chrome",packageManager)){
                    //安裝ChromeTab   compile 'com.android.support:customtabs:26.+'
                    CustomTabsIntent customTabsIntent = new CustomTabsIntent.Builder()
                            .setToolbarColor(ContextCompat.getColor(this, R.color.colorPrimary))
                            .setShowTitle(true)
                            .build();
                    customTabsIntent.intent.setPackage("com.android.chrome"); //指定使用chrome開啟
                    customTabsIntent.launchUrl(this, Uri.parse(inputUrl));
                //引導安裝ChromeTab
                }else{
                    new AlertDialog.Builder(this)
                            .setIcon(R.mipmap.ic_launcher)
                            .setTitle("瀏覽器版本不相容")
                            .setMessage("Android 5.0以下手機請安裝Chrome瀏覽器")
                            .setPositiveButton("前往安裝", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.android.chrome")));
                                    dialogInterface.dismiss();
                                }
                            })
                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Toast.makeText(getApplicationContext(),"很抱歉~\n無法使用此應用服務",Toast.LENGTH_SHORT).show();
                                    dialogInterface.cancel();
                                }
                            })
                            .show();
                }
            }
        }else{
            new AlertDialog.Builder(this)
                    .setIcon(R.mipmap.ic_launcher_round)
                    .setTitle("連線異常")
                    .setMessage("請檢查是否開啟網路")
                    .setPositiveButton("開啟網路設定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            startActivity(new Intent(Settings.ACTION_SETTINGS));
                            dialogInterface.dismiss();
                        }
                    })
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    })
                    .show();
        }
    }

    //檢查網路連線是否正常
    //<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
    private boolean isConnected(){
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        }
        return false;
    }
    //檢查是否安裝並啟用應用程式
    private boolean isPackageInstalled(String packagename, PackageManager packageManager) {
        try {
            packageManager.getPackageInfo(packagename, 0);
        } catch (PackageManager.NameNotFoundException e) {
            return false; //沒安裝
        }
        if (packageManager.getApplicationEnabledSetting(packagename) == PackageManager.COMPONENT_ENABLED_STATE_DISABLED){
            return false; //沒啟用
        }else{
            return true; //可呼叫
        }
    }
}
