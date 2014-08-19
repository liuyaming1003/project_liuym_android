package com.liuym.destest;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.net.util.Base64;

public class MyDes{
	private static MyDes myDes = null;
	private byte[] desKey="nssy2014".getBytes();
	private MyDes(){
		
	}
	
	public static MyDes shareMyDes(){
		if(myDes == null){
			myDes = new MyDes();
		}
		return myDes;
	}
	
	public boolean setDesKey(String key){
		if(key.length() != 8){
			return false;
		}
		
		desKey = key.getBytes();
		return true;
	}
	
	public String encDes(String data) throws Exception{
		if(data == null){
			throw new Exception("加密前数据不能为空");
		}
		/*byte[] encData = new byte[((data.length() - 1)/8 + 1) * 8];
		for(int i = 0; i < encData.length; i++){
			if(i >= data.length()){
				encData[i] = 0x00;
			}else{
				encData[i] = data.getBytes()[i];
			}
		}*/
		
		byte[] encByte = encryptMode(data.getBytes(), desKey);
		String retStr = null;
		if(encByte != null){
			retStr = new String(Base64.encodeBase64(encByte));
		}else{
			throw new Exception("加密后数据为空");
		}
		return retStr;
	}
	
	public String decDes(String data){
		String retStr = null;
		if(data != null){
			// base64 to byte[]
			byte[] encByte = Base64.decodeBase64(data);
			byte[] decByte = decryptMode(encByte, desKey);
			if(decByte != null){
				retStr = new String(decByte);
			}
		}
		return retStr;
	}
	
	//private static final String Algorithm = "DESede/ECB/NoPadding"; //定义 加密算法,可用 DES,DESede,Blowfish
	private static final String Algorithm = "DES"; //定义 加密算法,可用 DES,DESede,Blowfish
	private byte[] encryptMode(byte[] data, byte[] key){
		try{
			SecretKey deskey = new SecretKeySpec(key, Algorithm);
			
			//加密
			Cipher encC = Cipher.getInstance(Algorithm);
			encC.init(Cipher.ENCRYPT_MODE, deskey);
			return encC.doFinal(data);
		}catch (java.security.NoSuchAlgorithmException e1) {
            e1.printStackTrace();
        } catch (javax.crypto.NoSuchPaddingException e2) {
            e2.printStackTrace();
        } catch (java.lang.Exception e3) {
            e3.printStackTrace();
        }
        return null;
	}
	
    private byte[] decryptMode(byte[] data, byte[] key) {
    	try {
            //生成密钥
            SecretKey deskey = new SecretKeySpec(key, Algorithm);

            //解密
            Cipher c1 = Cipher.getInstance(Algorithm);
            c1.init(Cipher.DECRYPT_MODE, deskey);
            return c1.doFinal(data);
        } catch (java.security.NoSuchAlgorithmException e1) {
            e1.printStackTrace();
        } catch (javax.crypto.NoSuchPaddingException e2) {
            e2.printStackTrace();
        } catch (java.lang.Exception e3) {
            e3.printStackTrace();
        }
        return null;
    }
    
    /**
	 * byte数组转换成16进制字符串
	 * @param src
	 * @param len
	 * @return
	 */
    private String bytesToHexString(byte[] src, int len) {
		StringBuilder stringBuilder = new StringBuilder("");
		if (src == null || len <= 0) {		
			return null;		
		}
	    for (int i = 0; i < len; i++) {	
	    	int v = src[i] & 0xFF;	
	    	String hv = Integer.toHexString(v);	
	    	if (hv.length() < 2) {	
		    	stringBuilder.append(0);		
		    }	
	    	stringBuilder.append(hv);	
	    }
	    return stringBuilder.toString();
    }

    /**
	 * 16进制字符串转换成byte数组
	 * @param str
	 * @return
	 */
    private byte[] hexString2ByteArray(String str) {
    	byte[] out = new byte[str.length()/2];   
    	byte[] outb = str.getBytes();
    	int len = str.length();
    	for(int i = 0; i < len/2; i++){
    		out[i] = (byte)(byte2int(outb[2*i])*0x10 + byte2int(outb[2*i+1]));
    	}
    	return out;
    }
    
    /**
     * 16进制byte字符转换成整型
     * @param c
     * @return
     */
    private int byte2int(Byte c) {
		if(c>='0'&&c<='9') return (c-'0');
		else if(c>='a'&&c<='z') return (c-'a'+10);
		else if(c>='A'&&c<='Z') return (c-'A'+10);
		return -1;	  
    }
}