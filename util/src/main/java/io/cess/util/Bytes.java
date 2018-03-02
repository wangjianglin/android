package io.cess.util;

/**
 *
 * @author lin
 * @date Nov 1, 2014 11:52:28 PM
 *
 * 各种数据类型与byte之间进行转换
 */
public class Bytes {

    public static void writeByte(byte[] dest, byte value) {
        dest[0] = value;
    }

    public static void writeByte(byte[] dest, byte value, int offset) {
        dest[offset] = value;
    }

    public static byte readByte(byte[] dest) {
        return dest[0];
    }
    public static byte readByte(byte[] dest, int offset) {
        return dest[offset];
    }

    public static void writeShort(byte[] dest, short value) {
        writeShort(dest,value,0);
    }

    public static void writeShort(byte[] dest, short value, int offset) {
        dest[offset] = (byte) ((value & 0xFF00) >> 8);
        dest[offset + 1] = (byte) (value & 0x00FF);
    }

    public static short readShort(byte[] dest) {
        return readShort(dest,0);
    }

    public static short readShort(byte[] dest, int offset) {
        return (short) (dest[offset] * 0x100 + dest[offset + 1]);
    }

    public static void writeInt(byte[] dest, int value) {
        writeInt(dest,value,0);
    }
    public static void writeInt(byte[] dest, int value, int offset) {
        dest[offset] = (byte) ((value & 0xff000000) >> 24);
        dest[offset + 1] = (byte) ((value & 0x00ff0000) >> 16);
        dest[offset + 2] = (byte) ((value & 0x0000ff00) >> 8);
        dest[offset + 3] = (byte) (value & 0x000000ff);
    }

    public static int readInt(byte[] dest) {

        return readInt(dest,0);
    }

    public static int readInt(byte[] dest, int offset) {

        return (dest[offset] * 0x1000000 + dest[offset + 1] * 0x10000
                + dest[offset + 2] * 0x100 + dest[offset + 3]);
    }

    public static void writeLong(byte[] dest, long value) {
        writeLong(dest,value,0);
    }

    public static void writeLong(byte[] dest, long value, int offset) {
        dest[offset] = (byte) ((value & 0xFF00000000000000L) >> 56);
        dest[offset + 1] = (byte) ((value & 0x00FF000000000000L) >> 48);
        dest[offset + 2] = (byte) ((value & 0xFF0000000000L) >> 40);
        dest[offset + 3] = (byte) ((value & 0x00FF00000000L) >> 32);
        dest[offset + 4] = (byte) ((value & 0xFF000000L) >> 24);
        dest[offset + 5] = (byte) ((value & 0xFF0000L) >> 16);
        dest[offset + 6] = (byte) ((value & 0xFF00L) >> 8);
        dest[offset + 7] = (byte) (value & 0x00FFL);
    }

    public static long readLong(byte[] dest) {// offset偏移量
        return readLong(dest,0);
    }

    public static long readLong(byte[] dest, int offset) {// offset偏移量
        return (dest[offset] * 0x100000000000000L
                + dest[offset + 1] * 0x1000000000000L
                + dest[offset + 2] * 0x10000000000L
                + dest[offset + 3] * 0x100000000L
                + dest[offset + 4] * 0x1000000L
                + dest[offset + 5] * 0x10000L
                + dest[offset + 6] * 0x100L
                + dest[offset + 7]);
    }

    public static void writeBytes(byte[] dest, byte[] value, int lenth) {
        writeBytes(dest,value,lenth,0,0);
    }

    public static void writeBytes(byte[] dest, byte[] value, int lenth,
                                  int destoff, int sourceoff) {
        for (int n = 0; n < lenth; n++) {
            dest[destoff + n] = value[sourceoff + n];
        }
    }

    public static void readBytes(byte[] dest, byte[] value, int lenth) {
        readBytes(dest,value,lenth,0,0);
    }

    public static void readBytes(byte[] dest, byte[] value, int lenth,
                                 int destoff, int sourceoff) {
        for (int n = 0; n < lenth; n++) {
            value[sourceoff + n] = dest[destoff + n];
        }
    }

    public static void writeFloat(byte[] dest, float value) {
        writeFloat(dest, value, 0);
    }
    public static void writeFloat(byte[] dest, float value, int offset) {
        writeInt(dest, Float.floatToIntBits(value), offset);
    }

    public static float readFloat(byte[] dest) {
        return readFloat(dest,0);
    }

    public static float readFloat(byte[] dest, int offset) {
        return Float.intBitsToFloat(readInt(dest, offset));
    }

    public static void writeDouble(byte[] dest, double value) {
        writeDouble(dest,value, 0);
    }
    public static void writeDouble(byte[] dest, double value, int offset) {
        writeLong(dest, Double.doubleToLongBits(value), offset);
    }

    public static double readDouble(byte[] dest) {
        return readDouble(dest, 0);
    }
    public static double readDouble(byte[] dest, int offset) {
        return Double.longBitsToDouble(readLong(dest, offset));
    }
}
