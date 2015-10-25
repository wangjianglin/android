package lin.client.tcp;

import lin.util.ByteUtils;

public class CommandPackageMessageHeader {
	private int command;
	// / <summary>
	// / 数据包类型号
	// / </summary>
	// public int command
	// {
	// get { return _command; }
	// set { this._command = value; }
	// }

	private byte major;

	// / <summary>
	// / 主版本号
	// / </summary>
	// public byte majorVersion { get { return _majorVersion; } set {
	// this._majorVersion = value; } }
	private byte minor;
	// / <summary>
	// / 副版本号
	// / </summary>
	// public byte minorVersion { get { return _minorVersion; } set {
	// this._minorVersion = value; } }
	private byte revise;

	// / <summary>
	// / 修正版本号
	// / </summary>
	// public byte correctVersion { get { return _correctVersion; } set {
	// this._correctVersion = value; } }

	private int length = 0;

	// / <summary>
	// / 数据长度
	// / </summary>
	// public int length { get { return _length; } set { this._length = value; }
	// } //数据长度
	// private long _sequeueid;

	// /// <summary>
	// /// 序列号
	// /// </summary>
	// public long sequeueid { get { return _sequeueid; } set { this._sequeueid
	// = value; } }

	public void read(byte[] headers) {
		 major = ByteUtils.readByte(headers);
		 minor = ByteUtils.readByte(headers, 1);
		 revise = ByteUtils.readByte(headers, 2);
		 command = ByteUtils.readInt(headers, 3);
		 length = ByteUtils.readInt(headers, 7);
	}

	public int getCommand() {
		return command;
	}

	public void setCommand(int command) {
		this.command = command;
	}

	public byte getMajor() {
		return major;
	}

	public void setMajor(byte major) {
		this.major = major;
	}

	public byte getMinor() {
		return minor;
	}

	public void setMinor(byte minor) {
		this.minor = minor;
	}

	public byte getRevise() {
		return revise;
	}

	public void setRevise(byte revise) {
		this.revise = revise;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

}
