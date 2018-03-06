package io.cess.comm.http;

import android.content.Context;
import android.os.Environment;

import java.io.File;


/**
 * Created by lin on 9/24/15.
 */
public abstract class AbstractHttpCommunicateDownloadFile implements HttpCommunicateDownloadFile {

    protected static final int DOWNLOAD_SIZE = 800 * 1024;

    protected ProgressResultListener mListener;

    protected HttpCommunicateImpl mImpl;
    protected HttpCommunicate.Params mParams;
    protected Context mContext;
    protected HttpPackage mPack;
    protected HttpFileInfo mHttpFileInfo;

    public AbstractHttpCommunicateDownloadFile(Context context){
        this.mContext = context;
    }

    @Override
    public void setImpl(HttpCommunicateImpl impl) {
        this.mImpl = impl;
    }

    @Override
    public void setListener(ProgressResultListener listener) {
        this.mListener = listener;
    }

    @Override
    public void setPackage(HttpPackage pack) {
        this.mPack = pack;
    }

    @Override
    public void setParams(HttpCommunicate.Params params) {
        this.mParams = params;
    }

    final public void download(String url) {

        FileInfo fileInfo = null;
        try{
            fileInfo = downloadImpl(url);
        }catch (Throwable e){
            //有问题，异常有可能是 listener 中产生的
            e.printStackTrace();
//                    lin.client.http.Error error = new Error();
//                    error.setStackTrace(Utils.printStackTrace(e));
            Error error = new Error(-2,
                    e.getMessage(),
                    io.cess.util.Utils.printStackTrace(e.getCause()),
                    io.cess.util.Utils.printStackTrace(e));
            mListener.fault(error);
            return;
        }
        //FileInfo对象
        if(fileInfo != null) {
            mListener.result(fileInfo, null);
//            mListener.result(false,null);
        }else{
            Error error = new Error(-3,"","","");
            mListener.fault(error);
        }
    }

    //解决重复下载问题
    private FileInfo downloadImpl(final String url)throws Throwable{


        String md5s = io.cess.util.MD5.digest(url);

        File path = mContext.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS + "/cache");

        if (path == null) {
            path = new File(mContext.getCacheDir() + "/" + Environment.DIRECTORY_DOWNLOADS + "/cache");
        }

        //conn.getLastModified()
        String cacheFileName = path.getAbsoluteFile() + "/download-cache-" + md5s;// + (new Date()).getTime();// + "-" + conn.getLastModified();
        File file = new File(cacheFileName + ".cache");
        //                file = File.createTempFile(md5s,".cache");

        long length = 0;
        long lastModified = 0;

        if(file.exists()){
            //                    fileInfo(url, null);
            HttpFileInfo info = getFileInfo(url);
            length = info.getFileSize();
            lastModified = info.getLastModified();
            if(file.length() == length && file.lastModified() == lastModified){
                return new FileInfo(url,file, info.getFileName(), lastModified,length);
            }
            file.delete();
        }


        File dFile = new File(cacheFileName + ".download");

        path.mkdirs();

        byte[] buffer = new byte[1024 * 4];

        FileInfo fileInfo = null;
        do {
            fileInfo = downFilePartial(url, dFile, buffer);
        }while (getStatusCode() == 206);

        //416 (Requested Range Not Satisfiable/请求范围无法满足)
        //416表示客户端包含了一个服务器无法满足的Range头信息的请求。该状态是新加入 HTTP 1.1的。
        if(getStatusCode() == 416){//如果请求范围错误，重新下载
            dFile.delete();
            do {
                fileInfo = downFilePartial(url, dFile, buffer);
            }while (getStatusCode() == 206);
        }

        //                if((statusCode == HttpURLConnection.HTTP_OK
        //                        || statusCode == HttpURLConnection.HTTP_PARTIAL)
        //                        && length > 0 && dFile.length() == length){
        //                    dFile.delete();
        //                }else{
        //                    //dFile.renameTo(file);
        //                }
        length = fileInfo.getLength();
        if(length <= 0 || (length > 0 && dFile.length() == length)){
            dFile.renameTo(file);
            file.setLastModified(lastModified);
        }else{
            dFile.delete();
            throw new RuntimeException("文件损坏");
        }

        return fileInfo;
        //                HttpURLConnection.setFollowRedirects(true);

    }

    protected abstract FileInfo downFilePartial(String url, File dFile, byte[] buffer) throws Throwable;

    protected abstract int getStatusCode();

    protected abstract HttpFileInfo getFileInfoImpl(String url);


    @Override
    final public HttpFileInfo getFileInfo(String url) {
        if(mHttpFileInfo == null){
            mHttpFileInfo = getFileInfoImpl(url);;
        }
        return mHttpFileInfo;
    }

    //    abstract protected FileInfo downloadImpl(String url) throws Throwable;
}