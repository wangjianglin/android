package lin.client.http2;

import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.InputStreamBody;



/**
 * 
 * @author 王江林
 * @date 2013年12月27日 上午10:18:39
 * add*方法都为protected，如果为public,虽然更方便了，但增加了出错的可能，同时也减轻了调时的难度
 *
 */
public class FileUploadPackage extends HttpPackage {

	@Override
	public Map<String, Object> getParams() {
		Map<String, Object> params = super.getParams();
		
		for(String key : files.keySet()){
			params.put(key, files.get(key));
		}
		return params;
	}
	
	private Map<String,ContentBody> files = new HashMap<String,ContentBody>();
	private static ContentType mimeType = ContentType.DEFAULT_BINARY;
	@Override
	public final boolean isMultipart(){
		return true;
	}
	
	protected void addByte(String name,byte[] bytes){
		if(bytes == null){
			files.remove(name);
			return;
		}
		files.put(name, new ByteArrayBody(bytes,mimeType,name));
	}
	
	protected void addByte(String name,byte[] bytes,String fileName){
		if(bytes == null){
			files.remove(name);
			return;
		}
		files.put(name, new ByteArrayBody(bytes,mimeType,fileName));
	}
	
//	public void addJpgByte(String name,byte[] bytes){
//		if(bytes == null){
//			files.remove(name);
//			return;
//		}
//		files.put(name, new ByteArrayBody(bytes,mimeType,name+".jpg"));
//	}
	
	protected void addInputStream(String name,InputStream in){
		if(in == null){
			files.remove(name);
			return;
		}
		files.put(name, new InputStreamBody(in,mimeType,name));
	}
	
	protected void addInputStream(String name,InputStream in,String fileName){
		if(in == null){
			files.remove(name);
			return;
		}
		files.put(name, new InputStreamBody(in,mimeType,fileName));
	}
	
	protected void addFile(String name,File file){
		if(file == null){
			files.remove(file);
			return;
		}
		files.put(name, new FileBody(file,mimeType,file.getName()));
	}
	
	protected void removeFile(String name){
		files.remove(name);
	}
//	private File file;
	
//	 public FileUploadPackage(){
//		 this(null);
//	 }
	 
//	 public FileUploadPackage(File file){
//		 super("");
//		 this.file = file;
////		 this.setRequestHandle(HttpRequestHandle);
//	 }
//
//	public File getFile() {
//		return file;
//	}
//
//	public void setFile(File file) {
//		this.file = file;
//	}
	 
}
