package tech.xichao.password.util;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import java.security.SecureRandom;

/**
 * @author xichao
 * @version 2017.03.28
 */
public class AESUtil extends EncryptUtil {

    //加密算法
    private static final String ALGORITHM = "AES";

    public AESUtil() {
        super();
    }
    public AESUtil(byte[] iv) {
        this();
        super.iv = iv;
    }
    public AESUtil(String trans, byte[] iv) {
        this(iv);
        super.trans = trans;
    }

    /**
     * 加密
     */
    @Override
    public byte[] encrypt(byte[] src, byte[] key) throws Exception {
        KeyGenerator kgen = KeyGenerator.getInstance(ALGORITHM);
        kgen.init(128, new SecureRandom(key));
        SecretKey secretKey = kgen.generateKey();
        IvParameterSpec param = new IvParameterSpec(iv);
        Cipher cipher = Cipher.getInstance(trans);// 创建密码器
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, param);// 初始化
        return cipher.doFinal(src); // 加密
    }

    /**
     * 解密
     */
    @Override
    public byte[] decrypt(byte[] src, byte[] key) throws Exception {
        KeyGenerator kgen = KeyGenerator.getInstance(ALGORITHM);
        kgen.init(128, new SecureRandom(key));
        SecretKey secretKey = kgen.generateKey();
        IvParameterSpec param = new IvParameterSpec(iv);
        Cipher cipher = Cipher.getInstance(trans);// 创建密码器
        cipher.init(Cipher.DECRYPT_MODE, secretKey, param);// 初始化
        return cipher.doFinal(src); //解密
    }

}
