package lin.web;//package lin.web;
//
//import android.app.Activity;
//import android.os.Bundle;
//import android.view.ViewGroup;
//import android.webkit.ConsoleMessage;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//
///**
// * 
// * @author lin
// * @date Mar 20, 2015 3:06:25 PM
// *
// */
//public class LinWebViewConsoleActivity extends Activity{
//
//	private TextView textView;
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		
//		textView = new TextView(this.getApplicationContext());
//		textView.setLayoutParams(new LinearLayout.LayoutParams(
//	            ViewGroup.LayoutParams.MATCH_PARENT,
//	            ViewGroup.LayoutParams.MATCH_PARENT,
//	        1.0F));
//		this.setContentView(textView);
//		
////		savedInstanceState.get
//	}
//	
//	public void setWebView(LinWebView webView){
//		webView.getWebChromeClient().setLinWebViewConsole(new LinWebViewConsole(){
//
//			@Override
//			public void consoleMessage(ConsoleMessage message) {
//				textView.append(message.sourceId()+":"+message.lineNumber()+"\n" +message.message() + "\n");
//			}});
//		
//		for(int n=0;n<40;n++){
//			textView.append("message:"+n+"\n");
//		}
//	}
//}
