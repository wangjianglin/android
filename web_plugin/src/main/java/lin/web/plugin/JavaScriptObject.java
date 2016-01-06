package lin.web.plugin;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.provider.Settings;
import lin.util.JsonUtil;

public class JavaScriptObject {
	private Context context;
	
	public JavaScriptObject(Context context){
		this.context = context;
	}
	private Map<String,LinWebPlugin> objs = new HashMap<String,LinWebPlugin>();
	
	private Class<?> pluginClass(String plugin){
		Map<String,Class<?>> plugins = Config.getPlugins();
		if(plugins == null){
			return null;
		}
		return plugins.get(plugin);
	}
	private LinWebPlugin pluginObject(String pluginName){
			LinWebPlugin plugin = objs.get(pluginName);
			if(plugin == null){
				Class<?> pluginClass = pluginClass(pluginName);
				try {
					Constructor<?> c = pluginClass.getConstructor(Context.class);
					if(c != null){
						plugin = (LinWebPlugin) c.newInstance(context);
					}else{
						plugin = (LinWebPlugin) pluginClass.newInstance();
					}
					objs.put(pluginName, plugin);
				} catch (Throwable e) {
					e.printStackTrace();
				}
			}
			return plugin;
	}
	
	private String actions(String action){
	    if ("platform".equals(action)) {
	        return "android";
	    }else if ("productName".equals(action)){
	    	return android.os.Build.PRODUCT;
	    }else if ("versionName".equals(action)){
	    	return android.os.Build.VERSION.RELEASE;
	    }else if ("version".equals(action)){
	    	return android.os.Build.VERSION.SDK;
	    }else if ("model".equals(action)){
	    	return android.os.Build.MODEL;
	    }else if ("uuid".equals(action)){
	    	return Settings.Secure.getString(context.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
	    }
		return "";
	}
	
	public String exec(String args){
		Object json = null;
		try {
			json = JsonUtil.deserialize(args);
			@SuppressWarnings("unchecked")
			Map<String,Object> map = (Map<String,Object>)json;
			String pluginName = (String) map.get("plugin");
			String action = (String) map.get("action");
			if(pluginName == null || "".equals(pluginName)){
				
				String r = JsonUtil.serialize(actions(action));
				return r;
			}
			LinWebPlugin plugin = pluginObject(pluginName);
			Method method = null;
			try {
				method = plugin.getClass().getDeclaredMethod(action,Map.class);
			} catch (NoSuchMethodException e) {
//				e.printStackTrace();
			}
			Object result = null;
			if(method == null){
				method = plugin.getClass().getDeclaredMethod(action);
				result = method.invoke(plugin);
			}else{
				result = method.invoke(plugin, map.get("params"));
			}
			if(result == null){
				result = "";
			}
			if(result instanceof AsynResult){
				AsynResult asynResult = (AsynResult)result;
				result = asynResult.getResult();
			}
			return JsonUtil.serialize(result);
		} catch (Throwable e) {
			e.printStackTrace();
		} 
		return "";
	}
}
