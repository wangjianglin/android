package lin.web.plugin;

import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.xmlpull.v1.XmlPullParserException;

import android.app.Activity;
import android.content.res.XmlResourceParser;
import android.graphics.Color;

/**
 * 
 * @author lin
 * @date Apr 19, 2015 8:29:33 PM
 *
 */
public class Config {

//    public static final String TAG = "Config";

    private Whitelist whitelist = new Whitelist();
    private String startUrl;

    private static Config instance = null;
    private static Object lock = new Object();

    public static void init(Activity action) {
    	if(instance != null){
    		return;
    	}
    	synchronized (lock) {
    		if(instance == null){
    			instance = new Config(action);
    		}
		}
    }

    private static Map<String,Class<?>> plugins = new HashMap<String,Class<?>>();
    public static Map<String,Class<?>> getPlugins(){
    	return plugins;
    }
    private Activity activity;
    private Config(Activity activity) {
    	this.activity = activity;
        if (activity != null) {

            String[] names = new String[]{"app","device","video","httpdns"};

            Class<?>[] classes = new Class<?>[]{lin.web.plugin.LinAppPlugin.class,
                    lin.web.plugin.LinDevicePlugin.class,
                    lin.web.plugin.VideoPlugin.class,
                    lin.web.plugin.HttpDNSPlugin.class};

            for(int n=0;n<names.length;n++){
                plugins.put(names[n],classes[n]);
            }

            this.loadConfig();
            this.loadPlugins();
        }
    }
    private void loadConfig(){
//    	this.activity = action;

        int id = activity.getResources().getIdentifier("web", "xml", activity.getClass().getPackage().getName());
        if (id == 0) {
            id = activity.getResources().getIdentifier("web", "xml", activity.getPackageName());
            if (id == 0) {
                return;
            }
        }

        whitelist.addWhiteListEntry("file:///*", false);
        whitelist.addWhiteListEntry("content:///*", false);
        whitelist.addWhiteListEntry("data:*", false);

        XmlResourceParser xml = activity.getResources().getXml(id);
        int eventType = -1;
        while (eventType != XmlResourceParser.END_DOCUMENT) {
            if (eventType == XmlResourceParser.START_TAG) {
                String strNode = xml.getName();

                if (strNode.equals("access")) {
                    String origin = xml.getAttributeValue(null, "origin");
                    String subdomains = xml.getAttributeValue(null, "subdomains");
                    if (origin != null) {
                        whitelist.addWhiteListEntry(origin, (subdomains != null) && (subdomains.compareToIgnoreCase("true") == 0));
                    }
                }
                else if (strNode.equals("log")) {
                    String level = xml.getAttributeValue(null, "level");
                    if (level != null) {
                    }
                }
                else if (strNode.equals("preference")) {
                    String name = xml.getAttributeValue(null, "name").toLowerCase(Locale.getDefault());
                    /* Java 1.6 does not support switch-based strings
                       Java 7 does, but we're using Dalvik, which is apparently not Java.
                       Since we're reading XML, this has to be an ugly if/else.
                       
                       Also, due to cast issues, each of them has to call their separate putExtra!  
                       Wheee!!! Isn't Java FUN!?!?!?
                       
                       Note: We should probably pass in the classname for the variable splash on splashscreen!
                       */
                    if (name.equalsIgnoreCase("LogLevel")) {
//                        String level = xml.getAttributeValue(null, "value");
                    } else if (name.equalsIgnoreCase("SplashScreen")) {
                        String value = xml.getAttributeValue(null, "value");
                        int resource = 0;
                        if (value == null)
                        {
                            value = "splash";
                        }
                        resource = activity.getResources().getIdentifier(value, "drawable", activity.getClass().getPackage().getName());
                        
                        activity.getIntent().putExtra(name, resource);
                    }
                    else if(name.equalsIgnoreCase("BackgroundColor")) {
                        int value = xml.getAttributeIntValue(null, "value", Color.BLACK);
                        activity.getIntent().putExtra(name, value);
                    }
                    else if(name.equalsIgnoreCase("LoadUrlTimeoutValue")) {
                        int value = xml.getAttributeIntValue(null, "value", 20000);
                        activity.getIntent().putExtra(name, value);
                    }
                    else if(name.equalsIgnoreCase("SplashScreenDelay")) {
                        int value = xml.getAttributeIntValue(null, "value", 3000);
                        activity.getIntent().putExtra(name, value);
                    }
                    else if(name.equalsIgnoreCase("KeepRunning"))
                    {
                        boolean value = xml.getAttributeValue(null, "value").equals("true");
                        activity.getIntent().putExtra(name, value);
                    }
                    else if(name.equalsIgnoreCase("InAppBrowserStorageEnabled"))
                    {
                        boolean value = xml.getAttributeValue(null, "value").equals("true");
                        activity.getIntent().putExtra(name, value);
                    }
                    else if(name.equalsIgnoreCase("DisallowOverscroll"))
                    {
                        boolean value = xml.getAttributeValue(null, "value").equals("true");
                        activity.getIntent().putExtra(name, value);
                    }
                    else
                    {
                        String value = xml.getAttributeValue(null, "value");
                        activity.getIntent().putExtra(name, value);
                    }
                }
                else if (strNode.equals("content")) {
                    String src = xml.getAttributeValue(null, "src");

                    if (src != null) {
                        Pattern schemeRegex = Pattern.compile("^[a-z-]+://");
                        Matcher matcher = schemeRegex.matcher(src);
                        if (matcher.find()) {
                            startUrl = src;
                        } else {
                            if (src.charAt(0) == '/') {
                                src = src.substring(1);
                            }
                            startUrl = "file:///android_asset/" + src;
                        }
                    }
                }

            }

            try {
                eventType = xml.next();
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void loadPlugins() {
        int id = this.activity.getResources().getIdentifier("web", "xml", this.activity.getClass().getPackage().getName());
        if (id == 0) {
            id = this.activity.getResources().getIdentifier("web", "xml", this.activity.getPackageName());
            if (id == 0) {
                return;
            }
        }
        XmlResourceParser xml = this.activity.getResources().getXml(id);
        int eventType = -1;
        String service = "", pluginClass = "", paramType = "";
        boolean onload = false;
        boolean insideFeature = false;
        while (eventType != XmlResourceParser.END_DOCUMENT) {
            if (eventType == XmlResourceParser.START_TAG) {
                String strNode = xml.getName();
                if (strNode.equals("url-filter")) {
                }
                else if (strNode.equals("plugin")) {
                    insideFeature = true;
                    service = xml.getAttributeValue(null, "name");
                }
                else if (insideFeature && strNode.equals("param")) {
                    paramType = xml.getAttributeValue(null, "name");
                    if (paramType.equals("service")) // check if it is using the older service param
                        service = xml.getAttributeValue(null, "value");
                    else if (paramType.equals("package") || paramType.equals("android-package"))
                        pluginClass = xml.getAttributeValue(null,"value");
                    else if (paramType.equals("onload"))
                        onload = "true".equals(xml.getAttributeValue(null, "value"));
                }
            }
            else if (eventType == XmlResourceParser.END_TAG)
            {
                String strNode = xml.getName();
                if (strNode.equals("plugin"))
                {
                    this.addPlugin(service,pluginClass,onload);

                    service = "";
                    pluginClass = "";
                    insideFeature = false;
                }
            }
            try {
                eventType = xml.next();
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
    private void addPlugin(String plugin,String pluginClass,boolean onload){
    	try {
			plugins.put(plugin.toLowerCase(),Class.forName(pluginClass, false, Config.class.getClassLoader()));
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
    }
    
    public static void addWhiteListEntry(String origin, boolean subdomains) {
        if (instance == null) {
//            Log.e(TAG, "Config was not initialised. Did you forget to Config.init(this)?");
            return;
        }
        instance.whitelist.addWhiteListEntry(origin, subdomains);
    }

    public static boolean isUrlWhiteListed(String url) {
        if (instance == null) {
            return false;
        }
        return instance.whitelist.isUrlWhiteListed(url);
    }

    public static String getStartUrl() {
        if (instance == null || instance.startUrl == null) {
            return "file:///android_asset/index.html";
        }
        return instance.startUrl;
    }
}
