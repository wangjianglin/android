package lin.web;

import org.xwalk.core.XWalkJavascriptResult;
import org.xwalk.core.XWalkUIClient;
import org.xwalk.core.XWalkView;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Message;
import android.view.KeyEvent;
import android.webkit.ValueCallback;

/**
 * 
 * @author lin
 * @date Apr 23, 2015 12:43:56 PM
 *
 */
public class LinXWalkChromeClient extends XWalkUIClient{

	private Activity activity;
	public LinXWalkChromeClient(XWalkView arg0,Activity activity) {
		super(arg0);
		this.activity = activity;
	}

	@Override
	public boolean onCreateWindowRequested(XWalkView view,
			InitiateBy initiator, ValueCallback<XWalkView> callback) {
		return super.onCreateWindowRequested(view, initiator, callback);
	}

	@Override
	public void onFullscreenToggled(XWalkView view, boolean enterFullscreen) {
		super.onFullscreenToggled(view, enterFullscreen);
	}

	@Override
	public void onIconAvailable(XWalkView view, String url,
			Message startDownload) {
		super.onIconAvailable(view, url, startDownload);
	}

	@Override
	public void onJavascriptCloseWindow(XWalkView view) {
		super.onJavascriptCloseWindow(view);
	}

	@Override
	public boolean onJavascriptModalDialog(XWalkView view,
			JavascriptMessageType type, String url, String message,
			String defaultValue, XWalkJavascriptResult result) {
		 switch(type) {
         case JAVASCRIPT_ALERT:
             return onJsAlert(view, url, message, result);
         case JAVASCRIPT_CONFIRM:
             return onJsConfirm(view, url, message, result);
//         case JAVASCRIPT_PROMPT:
//             return onJsPrompt(view, url, message, defaultValue, result);
         case JAVASCRIPT_BEFOREUNLOAD:
             // Reuse onJsConfirm to show the dialog.
             return onJsConfirm(view, url, message, result);
         default:
             break;
     }
//     assert(false);
     return false;
	}
	
	 /**
     * Tell the client to display a javascript alert dialog.
     *
     * @param view
     * @param url
     * @param message
     * @param result
     * @see Other implementation in the Dialogs plugin.
     */
    private boolean onJsAlert(XWalkView view, String url, String message,
            final XWalkJavascriptResult result) {
        AlertDialog.Builder dlg = new AlertDialog.Builder(this.activity);
        dlg.setMessage(message);
        dlg.setTitle("提示");
        //Don't let alerts break the back button
        dlg.setCancelable(true);
        dlg.setPositiveButton(android.R.string.ok,
                new AlertDialog.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        result.confirm();
                    }
                });
        dlg.setOnCancelListener(
                new DialogInterface.OnCancelListener() {
                    public void onCancel(DialogInterface dialog) {
                        result.cancel();
                    }
                });
        dlg.setOnKeyListener(new DialogInterface.OnKeyListener() {
            //DO NOTHING
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK)
                {
                    result.confirm();
                    return false;
                }
                else
                    return true;
            }
        });
        dlg.show();
        return true;
    }

    /**
     * Tell the client to display a confirm dialog to the user.
     *
     * @param view
     * @param url
     * @param message
     * @param result
     * @see Other implementation in the Dialogs plugin.
     */
    private boolean onJsConfirm(XWalkView view, String url, String message,
            final XWalkJavascriptResult result) {
        AlertDialog.Builder dlg = new AlertDialog.Builder(this.activity);
        dlg.setMessage("\n"+message+"\n");
        dlg.setTitle("");
        dlg.setCancelable(true);
        dlg.setPositiveButton(android.R.string.cancel,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        result.cancel();
                    }
                });
        dlg.setNegativeButton(android.R.string.ok,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        result.confirm();
                    }
                });
        dlg.setOnCancelListener(
                new DialogInterface.OnCancelListener() {
                    public void onCancel(DialogInterface dialog) {
                        result.cancel();
                    }
                });
        dlg.setOnKeyListener(new DialogInterface.OnKeyListener() {
            //DO NOTHING
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK)
                {
                    result.cancel();
                    return false;
                }
                else
                    return true;
            }
        });
        dlg.show();
        return true;
    }

    /**
     * Tell the client to display a prompt dialog to the user.
     * If the client returns true, WebView will assume that the client will
     * handle the prompt dialog and call the appropriate JsPromptResult method.
     *
     * Since we are hacking prompts for our own purposes, we should not be using them for
     * this purpose, perhaps we should hack console.log to do this instead!
     *
     * @see Other implementation in the Dialogs plugin.
     */
//    private boolean onJsPrompt(XWalkView view, String origin, String message, String defaultValue, XWalkJavascriptResult result) {
//        // Unlike the @JavascriptInterface bridge, this method is always called on the UI thread.
//        String handledRet = appView.bridge.promptOnJsPrompt(origin, message, defaultValue);
//        if (handledRet != null) {
//            result.confirmWithResult(handledRet);
//        } else {
//            // Returning false would also show a dialog, but the default one shows the origin (ugly).
//            final XWalkJavascriptResult res = result;
//            AlertDialog.Builder dlg = new AlertDialog.Builder(this.activity);
//            dlg.setMessage(message);
//            final EditText input = new EditText(this.activity);
//            if (defaultValue != null) {
//                input.setText(defaultValue);
//            }
//            dlg.setView(input);
//            dlg.setCancelable(false);
//            dlg.setPositiveButton(android.R.string.ok,
//                    new DialogInterface.OnClickListener() {
//                        public void onClick(DialogInterface dialog, int which) {
//                            String usertext = input.getText().toString();
//                            res.confirmWithResult(usertext);
//                        }
//                    });
//            dlg.setNegativeButton(android.R.string.cancel,
//                    new DialogInterface.OnClickListener() {
//                        public void onClick(DialogInterface dialog, int which) {
//                            res.cancel();
//                        }
//                    });
//            dlg.show();
//        }
//        return true;
//    }

	@Override
	public void onPageLoadStarted(XWalkView view, String url) {
		System.out.println("url:"+url);
		super.onPageLoadStarted(view, url);
	}

	@Override
	public void onPageLoadStopped(XWalkView view, String url, LoadStatus status) {
		super.onPageLoadStopped(view, url, status);
	}

	@Override
	public void onReceivedIcon(XWalkView view, String url, Bitmap icon) {
		super.onReceivedIcon(view, url, icon);
	}

	@Override
	public void onReceivedTitle(XWalkView view, String title) {
		super.onReceivedTitle(view, title);
	}

	@Override
	public void onRequestFocus(XWalkView view) {
		super.onRequestFocus(view);
	}

	@Override
	public void onScaleChanged(XWalkView view, float oldScale, float newScale) {
		super.onScaleChanged(view, oldScale, newScale);
	}

	@Override
	public void onUnhandledKeyEvent(XWalkView view, KeyEvent event) {
		super.onUnhandledKeyEvent(view, event);
	}

	@Override
	public void openFileChooser(XWalkView view, ValueCallback<Uri> uploadFile,
			String acceptType, String capture) {
//		super.openFileChooser(view, uploadFile, acceptType, capture);
		this.uploadFile = uploadFile;
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
		intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType(acceptType);
//        intent.setType("image/*");
        this.activity.startActivityForResult(Intent.createChooser(intent, "选择文件"),
                FILECHOOSER_RESULTCODE);
	}
	private ValueCallback<Uri> uploadFile;
	public final static int FILECHOOSER_RESULTCODE = 15173;
	public ValueCallback<Uri> getValueCallback() {
		return uploadFile;
	}
	@Override
	public boolean shouldOverrideKeyEvent(XWalkView view, KeyEvent event) {
		return super.shouldOverrideKeyEvent(view, event);
	}

	

}
