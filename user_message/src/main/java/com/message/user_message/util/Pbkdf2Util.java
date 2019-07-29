package com.message.user_message.util;

import com.common.parent_model.common.CodeEnum;
import com.common.parent_model.exception.BaseException;
import lombok.extern.slf4j.Slf4j;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Random;

/**
 * @Auther: wyx
 * @Date: 2019-07-25 17:19
 * @Description: Pbkdf2 加密算法
 */
@Slf4j
public class Pbkdf2Util {

    /**
     * 默认迭代次数
     */
    private static final Integer DEFAULT_ITERATIONS = 2000;

    /**
     * salt 长度
     */
    private static final Integer SALT_BYTE_SIZE = 16;

    /**
     * 生成密文的长度
     */
    private static final Integer HASH_BIT_SIZE = 64 * 4;

    /**
     * 算法名称
     */
    private static final String algorithm = "PBKDF2WithHmacSHA256";

    public static String encodedHash(String password, String salt, Integer iterations){
        SecretKeyFactory secretKeyFactory = null;

        try {
            secretKeyFactory = SecretKeyFactory.getInstance(algorithm);
        }catch (NoSuchAlgorithmException e){
            log.error("找不到该算法 ", e);
            throw new BaseException(CodeEnum.SYSTEM_ERROR.getCode(), CodeEnum.SYSTEM_ERROR.getMsg());
        }

        KeySpec keySpec = new PBEKeySpec(password.toCharArray(), salt.getBytes(StandardCharsets.UTF_8), iterations, HASH_BIT_SIZE);
        SecretKey secretKey = null;
        try {
            secretKey =secretKeyFactory.generateSecret(keySpec);
        }catch (InvalidKeySpecException e){
            log.error("无法生成密钥", e);
            throw new BaseException(CodeEnum.SYSTEM_ERROR.getCode(), CodeEnum.SYSTEM_ERROR.getMsg());
        }

        // 使用十六进制密文
        return toHex(secretKey.getEncoded());
    }

    /**
     * 十六进制字符串转二进制
     * @param hex
     * @return
     */
    private static byte[] fromHex(String hex){
        byte[] binary = new byte[hex.length() / 2];
        for(int i = 0; i < binary.length; i++){
            binary[i] = (byte) Integer.parseInt(hex.substring(2 * i, 2 * i + 2), 16);
        }

        return binary;
    }

    /**
     * 二进制数组转十六进制
     * @param array
     * @return
     */
    private static String toHex(byte[] array){
        BigInteger bigInteger = new BigInteger(1, array);
        String hex = bigInteger.toString(16);
        Integer paddingLength = (array.length * 2) - hex.length();
        if(paddingLength > 0){
            return String.format("%0" + paddingLength + "d", 0) + hex;
        }
        return hex;
    }

    /**
     * 获取 salt
     * @return
     */
    public static String getSalt(){
        Random random = new Random();
        char[] chars = new char[SALT_BYTE_SIZE];
        for(int i = 0; i < SALT_BYTE_SIZE; i++){
            int randNum = random.nextInt(3);
            if(randNum == 0){
                chars[i] = (char) (random.nextInt(10) + 48);
            }else if(randNum == 1){
                chars[i] = (char) (random.nextInt(10) + 65);
            }else{
                chars[i] = (char) (random.nextInt(10) + 97);
            }
        }
        return new String(chars);
    }

    public static String encode(String password){
        return encodedHash(password, getSalt(), DEFAULT_ITERATIONS);
    }

    public static String encode(String password, String salt){
        return encodedHash(password, salt, DEFAULT_ITERATIONS);
    }

    public static Boolean verification(String password, String salt, String hashedPassword){
        String encodePassword = encode(password, salt);
        return encodePassword.equals(hashedPassword);
    }

}
