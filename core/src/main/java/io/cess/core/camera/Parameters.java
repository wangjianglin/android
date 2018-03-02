package io.cess.core.camera;

import android.media.MediaRecorder;

/**
 * 
 * @author lin
 * @date Jul 7, 2015 4:07:05 PM
 *
 */
public class Parameters {

	public enum Type{
		Picture,Vedio;
	}
	
	private Type type;
	private int width = 480;
	private int height = 480;
	private int maxFileSize = 0;
	private int videoFrameRate = 25;
	
	/**
	 * 产生的输出文件的格式
	 */
	private int outputFormat = MediaRecorder.OutputFormat.DEFAULT;
	
	/**
	 * 视频编码
	 */
	private int videoEncoder = MediaRecorder.VideoEncoder.H264;
	
	/**
	 * 音频编码格式
	 */
	private int audioEncoder = MediaRecorder.AudioEncoder.AMR_NB;

	/**
	 * 毫秒为单位
	 */
	private int maxDuration = 15 * 1000;
	

	/**
	 * 毫秒为单位
	 */
	private int minDuration = 3 * 1000;

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getMaxFileSize() {
		return maxFileSize;
	}

	public void setMaxFileSize(int maxFileSize) {
		this.maxFileSize = maxFileSize;
	}

	public int getVideoFrameRate() {
		return videoFrameRate;
	}

	public void setVideoFrameRate(int videoFrameRate) {
		this.videoFrameRate = videoFrameRate;
	}

	public int getOutputFormat() {
		return outputFormat;
	}

	public void setOutputFormat(int outputFormat) {
		this.outputFormat = outputFormat;
	}

	public int getVideoEncoder() {
		return videoEncoder;
	}

	public void setVideoEncoder(int videoEncoder) {
		this.videoEncoder = videoEncoder;
	}

	public int getMaxDuration() {
		return maxDuration;
	}

	public void setMaxDuration(int maxDuration) {
		this.maxDuration = maxDuration;
	}

	public int getAudioEncoder() {
		return audioEncoder;
	}

	public void setAudioEncoder(int audioEncoder) {
		this.audioEncoder = audioEncoder;
	}

	public int getMinDuration() {
		return minDuration;
	}

	public void setMinDuration(int minDuration) {
		this.minDuration = minDuration;
	} 
}
