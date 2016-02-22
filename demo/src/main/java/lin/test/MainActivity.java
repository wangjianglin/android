package lin.test;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import org.apache.http.impl.client.SystemDefaultCredentialsProvider;

import java.util.List;

import lin.client.http.*;
import lin.client.http.Error;
import lin.core.UpdateManager;
import lin.core.UpdateService;
import lin.util.Procedure;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final String url = "http://i.feicuibaba.com/test/test.txt.gzip";
//        String url = "https://www.feicuibaba.com/proxy/proxy-channel2.apk.php?channel=own";
        //HttpCommunicate.download("https://www.feicuibaba.com/proxy/proxy-channel.apk.php?channel=own", new ResultListener() {
//        HttpCommunicate.download("http://i.feicuibaba.com/apk/buyers/0.3.3-alpha-2/buyers_alpha.apk", new ResultListener() {
//        HttpCommunicate.download(url, new ResultListener() {
//            @Override
//            public void result(Object obj, List<Error> warning) {
//                System.out.println("ok.");
//            }
//
//            @Override
//            public void fault(Error error) {
//                System.out.println("ok.");
//            }
//        });

//        UpdateService.update(this, "https://www.feicuibaba.com/proxy/proxy.php?version=new&android=true&channel=own", "https://www.feicuibaba.com/proxy/proxy-channel2.apk.php?channel=own", "buyers.apk");
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

        Button groupListButton = (Button) this.findViewById(R.id.groupListId);
        groupListButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,GroupListActivity.class);
                MainActivity.this.startActivity(intent);
            }
        });

        Button webViewButton = (Button) this.findViewById(R.id.webViewId);
        webViewButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,WebViewActivity.class);
                MainActivity.this.startActivity(intent);
            }
        });


        Button httpButton = (Button) this.findViewById(R.id.httpId);
        httpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                HttpCommunicate.download(url, new ResultListener() {
//                    @Override
//                    public void result(Object obj, List<Error> warning) {
//                        System.out.println("ok.");
//                    }
//
//                    @Override
//                    public void fault(Error error) {
//                        System.out.println("ok.");
//                    }
//                });
//                TestPackage pack = new TestPackage();
//
//                pack.setData("test!");
//
//
//                HttpCommunicate.request(pack, new ResultListener() {
//                    @Override
//                    public void result(Object obj, List<Error> warning) {
//                        System.out.println("obj:"+obj);
//                    }
//
//                    @Override
//                    public void fault(Error error) {
//                        System.out.println("error:"+error);
//                    }
//                }).waitForEnd();
//
//
////                Assert.assertEquals("通信测试失败！",testCommResult,pack.getData());
//
//                pack.setData("测试中文数据！");
//
//                HttpCommunicate.request(pack, new ResultListener() {
//                    @Override
//                    public void result(Object obj, List<Error> warning) {
//                        System.out.println("obj:"+obj);
//                    }
//
//                    @Override
//                    public void fault(Error error) {
//                        System.out.println("error:"+error);
//                    }
//                }).waitForEnd();
//
//
////                Assert.assertEquals("通信传递中文失败！",testCommResult,pack.getData());
//
//                pack.setData(null);
//
//                HttpCommunicate.request(pack, new ResultListener() {
//                    @Override
//                    public void result(Object obj, List<Error> warning) {
//                        System.out.println("obj:"+obj);
//                    }
//
//                    @Override
//                    public void fault(Error error) {
//                        System.out.println("error:"+error);
//                    }
//                }).waitForEnd();
//
//                SessionIdPackage sessionPackage = new SessionIdPackage();
//
//                for (int n=0;n<10;n++){
//                    HttpCommunicate.request(sessionPackage, new ResultListener() {
//                        @Override
//                        public void result(Object obj, List<Error> warning) {
//                            System.out.println("session id:"+obj);
//                        }
//
//                        @Override
//                        public void fault(Error error) {
//                            System.out.println("error:"+error);
//                        }
//                    }).waitForEnd();
//                }

                HttpCommunicate.download("https://www.feicuibaba.com/proxy/proxy-channel.apk.php?channel=own", new ResultListener() {
                    @Override
                    public void result(Object obj, List<Error> warning) {
                        System.out.println(obj);
                    }

                    @Override
                    public void fault(Error error) {
                        System.out.println(error);
                    }
                });

//                Assert.assertEquals("返回null值失败！",testCommResult,pack.getData());
            }
        });


        HttpCommunicate.download("https://www.feicuibaba.com/proxy/proxy-channel2.apk.php?channel=own", new ResultListener() {
            @Override
            public void result(Object obj, List<Error> warning) {
                System.out.println(obj);
            }

            @Override
            public void fault(Error error) {
                System.out.println(error);
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

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
//    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }
}
