package fu.alfie.com.webviewexdemo;

import android.content.Intent;
import android.net.Uri;
import android.support.customtabs.CustomTabsClient;
import android.support.customtabs.CustomTabsIntent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    private EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editText = (EditText) findViewById(R.id.editText);
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
}
