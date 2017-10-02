package tech.xichao.password.util;

/**
 * @author xichao
 * @version 2017.03.29
 */
public abstract class EncryptUtil {

    /**
     * 加密算法/工作模式/填充模式
     * e.g.
     *   AES/CBC/NoPadding (128)
     *   AES/CBC/PKCS5Padding (128)
     *   AES/ECB/NoPadding (128)
     *   AES/ECB/PKCS5Padding (128)
     *   DES/CBC/NoPadding (56)
     *   DES/CBC/PKCS5Padding (56)
     *   DES/ECB/NoPadding (56)
     *   DES/ECB/PKCS5Padding (56)
     *   DESede/CBC/NoPadding (168)
     *   DESede/CBC/PKCS5Padding (168)
     *   DESede/ECB/NoPadding (168)
     *   DESede/ECB/PKCS5Padding (168)
     *   RSA/ECB/PKCS1Padding (1024, 2048)
     *   RSA/ECB/OAEPWithSHA-1AndMGF1Padding (1024, 2048)
     *   RSA/ECB/OAEPWithSHA-256AndMGF1Padding (1024, 2048)
     */
    protected String trans = "AES/CBC/PKCS5Padding";
    //初始向量
    protected byte[] iv = {'1', '2', '3', '4', '5', '6', '7', '8'};

    public abstract byte[] encrypt(byte[] src, byte[] key) throws Exception;

    /**
     * 加密
     * @param plainTxt 明文
     * @param key 密钥
     * @return 密文
     */
    public String encrypt(String plainTxt, String key) {
        try {
            byte[] cipherBytes = encrypt(plainTxt.getBytes("UTF-8"), key.getBytes());
            return Base64Util.encode(cipherBytes);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public abstract byte[] decrypt(byte[] src, byte[] key) throws Exception;

    /**
     * 解密
     * @param cipherTxt 密文
     * @param key 密钥
     * @return 明文
     */
    public String decrypt(String cipherTxt, String key) {
        try {
            byte[] cipherBytes = Base64Util.decode(cipherTxt);
            return new String(decrypt(cipherBytes, key.getBytes()), "UTF-8");
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    /**
     * 将二进制转换成16进制
     */
//    public String parseByte2HexStr(byte buf[]) {
//        StringBuffer sb = new StringBuffer();
//        for (int i = 0; i < buf.length; i++) {
//            String hex = Integer.toHexString(buf[i] & 0xFF);
//            if (hex.length() == 1) {
//                hex = '0' + hex;
//            }
//            sb.append(hex.toUpperCase());
//        }
//        return sb.toString();
//    }

    /**
     * 将16进制转换为二进制
     */
//    public byte[] parseHexStr2Byte(String hexStr) {
//        if (hexStr.length() < 1)
//            return null;
//        byte[] result = new byte[hexStr.length()/2];
//        for (int i = 0;i< hexStr.length()/2; i++) {
//            int high = Integer.parseInt(hexStr.substring(i*2, i*2+1), 16);
//            int low = Integer.parseInt(hexStr.substring(i*2+1, i*2+2), 16);
//            result[i] = (byte) (high * 16 + low);
//        }
//        return result;
//    }

}
