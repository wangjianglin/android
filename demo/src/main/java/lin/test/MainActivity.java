package lin.test;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import lin.core.UpdateService;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        UpdateService.update(this, "https://www.feicuibaba.com/chuncui/chuncui.php?version=true&android=true", "https://www.feicuibaba.com/chuncui/ccn.apk", "ccn.apk");
        Button button = (Button) this.findViewById(R.id.pullToRefreshTestId);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, PullToRefresh.class);
                MainActivity.this.startActivity(intent);
            }
        });


        Button mergeButton = (Button) this.findViewById(R.id.mergeViewId);

        mergeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =  new Intent(MainActivity.this,MergeActivity.class);
                MainActivity.this.startActivity(intent);
            }
        });

        Button tabbarButton = (Button) this.findViewById(R.id.tabbarTestId);

        tabbarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =  new Intent(MainActivity.this,TabbarActivity.class);
                MainActivity.this.startActivity(intent);
            }
        });

        Button fitChartButton = (Button) this.findViewById(R.id.fitChartId);
        fitChartButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,FitChartActivity.class);
                MainActivity.this.startActivity(intent);
            }
        });

        Button imagesButton = (Button) this.findViewById(R.id.imagesId);
        imagesButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,ImagesActivity.class);
                MainActivity.this.startActivity(intent);
            }
        });

        Button controlsButton = (Button) this.findViewById(R.id.controlsId);
        controlsButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,ControlsActivity.class);
                MainActivity.this.startActivity(intent);
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK){
            this.finish();
            return true;
        }
        return super.onKeyUp(keyCode, event);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
