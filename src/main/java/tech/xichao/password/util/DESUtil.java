package tech.xichao.password.util;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;
import java.security.SecureRandom;

/**
 * @author xichao
 * @version 2017.02.06
 */
public class DESUtil extends EncryptUtil {

    //加密算法
    private static final String ALGORITHM = "DES";
    //初始向量
    private static final String TRANS = "DES/CBC/PKCS5Padding";

    public DESUtil() {
        super();
        super.trans = TRANS;
    }
    public DESUtil(byte[] iv) {
        this();
        super.iv = iv;
    }
    public DESUtil(String trans, byte[] iv) {
        this(iv);
        super.trans = trans;
    }

    @Override
    public byte[] encrypt(byte[] src, byte[] key) throws Exception {
        // DES算法要求有一个可信任的随机数源
        SecureRandom sr = new SecureRandom();

        // 从原始密匙数据创建DESKeySpec对象
        DESKeySpec dks = new DESKeySpec(key);

        // 创建一个密匙工厂，然后用它把DESKeySpec转换成一个SecretKey对象
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(ALGORITHM);
        SecretKey securekey = keyFactory.generateSecret(dks);

        // Cipher对象实际完成加密操作
        IvParameterSpec param = new IvParameterSpec(iv);
        Cipher cipher = Cipher.getInstance(trans);

        // 用密匙初始化Cipher对象
        cipher.init(Cipher.ENCRYPT_MODE, securekey, param, sr);

        // 执行加密操作
        return cipher.doFinal(src);
    }

    @Override
    public byte[] decrypt(byte[] src, byte[] key) throws Exception {
        // DES算法要求有一个可信任的随机数源
        SecureRandom sr = new SecureRandom();
        // 从原始密匙数据创建一个DESKeySpec对象
        DESKeySpec dks = new DESKeySpec(key);
        // 创建一个密匙工厂，然后用它把DESKeySpec对象转换成一个SecretKey对象
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(ALGORITHM);
        SecretKey securekey = keyFactory.generateSecret(dks);
        // Cipher对象实际完成解密操作
        Cipher cipher = Cipher.getInstance(trans);
        // 用密匙初始化Cipher对象
        IvParameterSpec param = new IvParameterSpec(iv);
        cipher.init(Cipher.DECRYPT_MODE, securekey, param, sr);

        // 正式执行解密操作
        return cipher.doFinal(src);
    }

}
