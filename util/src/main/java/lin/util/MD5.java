package lin.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class MD5 {

	public static String digest(String string){
		MessageDigest md5 = null;
		try {
			md5 = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}  
	    byte[] bytes = md5.digest(string.getBytes());  
	    StringBuffer stringBuffer = new StringBuffer();  
	    for (byte b : bytes){  
	        int bt = b&0xff;  
	        if (bt < 16){  
	            stringBuffer.append(0);  
	        }   
	        stringBuffer.append(Integer.toHexString(bt));  
	    }  
	    return stringBuffer.toString();  
	}
//	
//	private final int[] state;
//
//	private MD5() {
//		this.state = new int[4];
//		implReset();
//	}
//
//	private void implReset() {
//		this.state[0] = 1732584193;
//		this.state[1] = -271733879;
//		this.state[2] = -1732584194;
//		this.state[3] = 271733878;
//	}
//	private byte[] result(){
//		byte[] r = new byte[16];
//		for(int n=0;n<4;n++){
//			writeD(r,n*4,state[n]);
//		}
//		return r;
//	}
//	private static byte[] writeD(byte[] _bao,int pos,int value)
//	{
//		_bao[3+pos]=(byte) (value >> 24 & 0xff);
//		_bao[2+pos]=(byte) (value >> 16 & 0xff);
//		_bao[1+pos]=(byte) (value >> 8 & 0xff);
//		_bao[0+pos]=(byte) (value & 0xff);
//		return _bao;
//	}
//	
//	
//	public static byte[] digest(String s){
//		byte[] source = s.getBytes();
//		//int length = source.length;
//		int[] x = new int[16];
//		int paramInt = 0;
//		MD5 md5 = new MD5();
//		boolean flag = true;
//		while(flag){
//			flag = b2iLittle64(source,paramInt,x);
//			paramInt += 64;
//			md5.implCompress(x);
//		}
//		return md5.result();
//	}
//	private static boolean b2iLittle64(byte[] source,int paramInt, int[] x){
//		int n=paramInt;
//		boolean result;
//		if(paramInt + 63 < source.length){
//			for(n=0;n<16;n++){
//				x[n]=bytes4ToInt(source,paramInt+n*4);
//			}
//			result = true;
//		}
//		else{
//			byte[] tmp = new byte[64];
//			for(;n<source.length;n++){
//				tmp[n-paramInt]=source[n];
//			}
//			if(n-paramInt<64 && paramInt <= source.length){
//				tmp[n-paramInt]=-128;
//			}
//			if(n-paramInt < 56){
//				writeDD(tmp,source.length*8);
//				result = false;
//			}else{
//				result = true;
//			}
//			for(n=0;n<16;n++){
//				x[n]=bytes4ToInt(tmp,n*4);
//			}
//		}
//		return result;
//	}
//	
//	private static byte[] writeDD(byte[] _bao,long value)
//	{
//		_bao[0+56]=(byte) (value & 0xff);
//		_bao[1+56]=(byte) (value >> 8 & 0xff);
//		_bao[2+56]=(byte) (value >> 16 & 0xff);
//		_bao[3+56]=(byte) (value >> 24 & 0xff);
//		_bao[4+56]=(byte) (value >> 32 & 0xff);
//		_bao[5+56]=(byte) (value >> 40 & 0xff);
//		_bao[6+56]=(byte) (value >> 48 & 0xff);
//		_bao[7+56]=(byte) (value >> 56 & 0xff);
//
//		return _bao;
//	}
//	
//	private static int bytes4ToInt(byte body[], int index)
//	{
//		int tmp =
//			(0xff & body[3 + index])
//				<< 24 | (0xff & body[2 + index])
//				<< 16 | (0xff & body[1 + index])
//				<< 8 | 0xff & body[0
//				+ index];
//		return tmp;
//	}
//
//	private static int FF(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7)
//	  {
//	    paramInt1 += (paramInt2 & paramInt3 | (paramInt2 ^ 0xFFFFFFFF) & paramInt4) + paramInt5 + paramInt7;
//	    return ((paramInt1 << paramInt6 | paramInt1 >>> 32 - paramInt6) + paramInt2);
//	  }
//
//	  private static int GG(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7) {
//	    paramInt1 += (paramInt2 & paramInt4 | paramInt3 & (paramInt4 ^ 0xFFFFFFFF)) + paramInt5 + paramInt7;
//	    return ((paramInt1 << paramInt6 | paramInt1 >>> 32 - paramInt6) + paramInt2);
//	  }
//
//	  private static int HH(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7) {
//	    paramInt1 += (paramInt2 ^ paramInt3 ^ paramInt4) + paramInt5 + paramInt7;
//	    return ((paramInt1 << paramInt6 | paramInt1 >>> 32 - paramInt6) + paramInt2);
//	  }
//
//	  private static int II(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7) {
//	    paramInt1 += (paramInt3 ^ (paramInt2 | paramInt4 ^ 0xFFFFFFFF)) + paramInt5 + paramInt7;
//	    return ((paramInt1 << paramInt6 | paramInt1 >>> 32 - paramInt6) + paramInt2);
//	  }
//
//	  private void implCompress(int[] x)
//	  {
//	    int i = this.state[0];
//	    int j = this.state[1];
//	    int k = this.state[2];
//	    int l = this.state[3];
//
//	    i = FF(i, j, k, l, x[0], 7, -680876936);
//	    l = FF(l, i, j, k, x[1], 12, -389564586);
//	    k = FF(k, l, i, j, x[2], 17, 606105819);
//	    j = FF(j, k, l, i, x[3], 22, -1044525330);
//	    i = FF(i, j, k, l, x[4], 7, -176418897);
//	    l = FF(l, i, j, k, x[5], 12, 1200080426);
//	    k = FF(k, l, i, j, x[6], 17, -1473231341);
//	    j = FF(j, k, l, i, x[7], 22, -45705983);
//	    i = FF(i, j, k, l, x[8], 7, 1770035416);
//	    l = FF(l, i, j, k, x[9], 12, -1958414417);
//	    k = FF(k, l, i, j, x[10], 17, -42063);
//	    j = FF(j, k, l, i, x[11], 22, -1990404162);
//	    i = FF(i, j, k, l, x[12], 7, 1804603682);
//	    l = FF(l, i, j, k, x[13], 12, -40341101);
//	    k = FF(k, l, i, j, x[14], 17, -1502002290);
//	    j = FF(j, k, l, i, x[15], 22, 1236535329);
//
//	    i = GG(i, j, k, l, x[1], 5, -165796510);
//	    l = GG(l, i, j, k, x[6], 9, -1069501632);
//	    k = GG(k, l, i, j, x[11], 14, 643717713);
//	    j = GG(j, k, l, i, x[0], 20, -373897302);
//	    i = GG(i, j, k, l, x[5], 5, -701558691);
//	    l = GG(l, i, j, k, x[10], 9, 38016083);
//	    k = GG(k, l, i, j, x[15], 14, -660478335);
//	    j = GG(j, k, l, i, x[4], 20, -405537848);
//	    i = GG(i, j, k, l, x[9], 5, 568446438);
//	    l = GG(l, i, j, k, x[14], 9, -1019803690);
//	    k = GG(k, l, i, j, x[3], 14, -187363961);
//	    j = GG(j, k, l, i, x[8], 20, 1163531501);
//	    i = GG(i, j, k, l, x[13], 5, -1444681467);
//	    l = GG(l, i, j, k, x[2], 9, -51403784);
//	    k = GG(k, l, i, j, x[7], 14, 1735328473);
//	    j = GG(j, k, l, i, x[12], 20, -1926607734);
//
//	    i = HH(i, j, k, l, x[5], 4, -378558);
//	    l = HH(l, i, j, k, x[8], 11, -2022574463);
//	    k = HH(k, l, i, j, x[11], 16, 1839030562);
//	    j = HH(j, k, l, i, x[14], 23, -35309556);
//	    i = HH(i, j, k, l, x[1], 4, -1530992060);
//	    l = HH(l, i, j, k, x[4], 11, 1272893353);
//	    k = HH(k, l, i, j, x[7], 16, -155497632);
//	    j = HH(j, k, l, i, x[10], 23, -1094730640);
//	    i = HH(i, j, k, l, x[13], 4, 681279174);
//	    l = HH(l, i, j, k, x[0], 11, -358537222);
//	    k = HH(k, l, i, j, x[3], 16, -722521979);
//	    j = HH(j, k, l, i, x[6], 23, 76029189);
//	    i = HH(i, j, k, l, x[9], 4, -640364487);
//	    l = HH(l, i, j, k, x[12], 11, -421815835);
//	    k = HH(k, l, i, j, x[15], 16, 530742520);
//	    j = HH(j, k, l, i, x[2], 23, -995338651);
//
//	    i = II(i, j, k, l, x[0], 6, -198630844);
//	    l = II(l, i, j, k, x[7], 10, 1126891415);
//	    k = II(k, l, i, j, x[14], 15, -1416354905);
//	    j = II(j, k, l, i, x[5], 21, -57434055);
//	    i = II(i, j, k, l, x[12], 6, 1700485571);
//	    l = II(l, i, j, k, x[3], 10, -1894986606);
//	    k = II(k, l, i, j, x[10], 15, -1051523);
//	    j = II(j, k, l, i, x[1], 21, -2054922799);
//	    i = II(i, j, k, l, x[8], 6, 1873313359);
//	    l = II(l, i, j, k, x[15], 10, -30611744);
//	    k = II(k, l, i, j, x[6], 15, -1560198380);
//	    j = II(j, k, l, i, x[13], 21, 1309151649);
//	    i = II(i, j, k, l, x[4], 6, -145523070);
//	    l = II(l, i, j, k, x[11], 10, -1120210379);
//	    k = II(k, l, i, j, x[2], 15, 718787259);
//	    j = II(j, k, l, i, x[9], 21, -343485551);
//
//	    this.state[0] += i;
//	    this.state[1] += j;
//	    this.state[2] += k;
//	    this.state[3] += l;
//	  }
}
