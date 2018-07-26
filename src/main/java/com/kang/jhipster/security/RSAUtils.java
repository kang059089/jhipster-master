package com.kang.jhipster.security;

import org.bouncycastle.util.encoders.Base64;

import javax.crypto.Cipher;
import java.io.ByteArrayOutputStream;
import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.*;
import java.util.HashMap;
import java.util.Map;

public class RSAUtils {


    private static final String RSA = "RSA";

    private static final String PUBLIC_KEY = "public";

    private static final String PRIVATE_KEY = "private";

    /**
     * RSA最大加密明文大小
     */
    private static final int MAX_ENCRYPT_BLOCK = 117;
    /**
     * RSA最大解密密文大小
     */
    private static final int MAX_DECRYPT_BLOCK = 128;

    /**
     * 生成公钥和私钥
     */
    public static Map<String, Object> getKeys() {
        try {
            KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(RSA);
            keyPairGen.initialize(1024);
            KeyPair keyPair = keyPairGen.generateKeyPair();
            RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
            RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
            Map<String, Object> map = new HashMap<String, Object>();
            map.put(PUBLIC_KEY, publicKey);
            map.put(PRIVATE_KEY, privateKey);
            return map;
        } catch (NoSuchAlgorithmException e) {
        }
        return null;
    }

    /**
     * 使用模和指数生成RSA公钥 注意：【此代码用了默认补位方式，为RSA/None/PKCS1Padding，不同JDK默认的补位方式可能不同，如Android默认是RSA/None/NoPadding】
     *
     * @param modulus  模
     * @param exponent 指数
     * @return
     */
    public static RSAPublicKey getPublicKey(String modulus, String exponent) {
        try {
            BigInteger b1 = new BigInteger(modulus);
            BigInteger b2 = new BigInteger(exponent);
            KeyFactory keyFactory = KeyFactory.getInstance(RSA);
            RSAPublicKeySpec keySpec = new RSAPublicKeySpec(b1, b2);
            return (RSAPublicKey) keyFactory.generatePublic(keySpec);
        } catch (NoSuchAlgorithmException e) {
        } catch (InvalidKeySpecException e) {
        }
        return null;
    }

    /**
     * 使用模和指数生成RSA私钥 注意：【此代码用了默认补位方式，为RSA/None/PKCS1Padding，不同JDK默认的补位方式可能不同，如Android默认是RSA/None/NoPadding】
     *
     * @param modulus  模
     * @param exponent 指数
     * @return
     */
    public static RSAPrivateKey getPrivateKey(String modulus, String exponent) {
        try {
            BigInteger b1 = new BigInteger(modulus);
            BigInteger b2 = new BigInteger(exponent);
            KeyFactory keyFactory = KeyFactory.getInstance(RSA);
            RSAPrivateKeySpec keySpec = new RSAPrivateKeySpec(b1, b2);
            return (RSAPrivateKey) keyFactory.generatePrivate(keySpec);
        } catch (NoSuchAlgorithmException e) {
        } catch (InvalidKeySpecException e) {
        }
        return null;
    }

    /**
     * 公钥加密
     *
     * @param data
     * @param publicKey
     * @return
     */
    public static String encryptByPublicKey(String data, RSAPublicKey publicKey) {
        try {
            Cipher cipher = Cipher.getInstance(RSA);
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            // 模长
            int key_len = publicKey.getModulus().bitLength() / 8;
            // 加密数据长度 <= 模长-11
            String[] datas = splitString(data, key_len - 11);
            String mi = "";
            // 如果明文长度大于模长-11则要分组加密
            for (String s : datas) {
                mi += bcd2Str(cipher.doFinal(s.getBytes()));
            }
            return mi;
        } catch (Exception e) {
        }
        return null;
    }


    /**
     * 私钥解密
     *
     * @param data
     * @param privateKey
     * @return
     */
    public static String decryptByPrivateKey(String data, RSAPrivateKey privateKey) {
        try {
            Cipher cipher = Cipher.getInstance(RSA);
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            // 模长
            int key_len = privateKey.getModulus().bitLength() / 8;
            byte[] bytes = data.getBytes();
            byte[] bcd = ASCII_To_BCD(bytes, bytes.length);
            System.err.println(bcd.length);
            // 如果密文长度大于模长则要分组解密
            String ming = "";
            byte[][] arrays = splitArray(bcd, key_len);
            for (byte[] arr : arrays) {
                ming += new String(cipher.doFinal(arr));
            }
            return ming;
        } catch (Exception e) {
        }
        return null;
    }

    /**
     *
     * @param data
     * @param privateKey
     * @return
     */
    public static String RSADecode(String data, RSAPrivateKey privateKey) {
        try {
            byte[] b = Base64.decode(data);
            int inputLen = b.length;
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            int offSet = 0;
            byte[] cache;
            int i = 0;
            Cipher cipher = Cipher.getInstance(RSA);
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            // 对数据分段解密
            while (inputLen - offSet > 0) {
                if (inputLen - offSet > MAX_DECRYPT_BLOCK) {
                    cache = cipher.doFinal(b, offSet, MAX_DECRYPT_BLOCK);
                } else {
                    cache = cipher.doFinal(b, offSet, inputLen - offSet);
                }
                out.write(cache, 0, cache.length);
                i++;
                offSet = i * MAX_DECRYPT_BLOCK;
            }
            byte[] decryptedData = out.toByteArray();
            out.close();
            return new String(decryptedData);
        } catch (Exception e) {
        }
        return null;
    }

    /**
     * 从字符串中加载公钥
     *
     * @param publicKeyStr 公钥数据字符串
     * @throws Exception 加载公钥时产生的异常
     */
    public static RSAPublicKey loadPublicKey(String publicKeyStr) {
        try {
            byte[] buffer = Base64.decode(publicKeyStr);
            KeyFactory keyFactory = KeyFactory.getInstance(RSA);
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(buffer);
            return (RSAPublicKey) keyFactory.generatePublic(keySpec);
        } catch (Exception e) {
        }
        return null;
    }

    public static RSAPrivateKey loadPrivateKey(String privateKeyStr) {
        try {
            byte[] buffer = Base64.decode(privateKeyStr);
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(buffer);
            KeyFactory keyFactory = KeyFactory.getInstance(RSA);
            return (RSAPrivateKey) keyFactory.generatePrivate(keySpec);
        } catch (Exception e) {
        }
        return null;
    }

    /**
     * ASCII码转BCD码
     */
    private static byte[] ASCII_To_BCD(byte[] ascii, int asc_len) {
        byte[] bcd = new byte[asc_len / 2];
        int j = 0;
        for (int i = 0; i < (asc_len + 1) / 2; i++) {
            bcd[i] = asc_to_bcd(ascii[j++]);
            bcd[i] = (byte) (((j >= asc_len) ? 0x00 : asc_to_bcd(ascii[j++])) + (bcd[i] << 4));
        }
        return bcd;
    }

    private static byte asc_to_bcd(byte asc) {
        byte bcd;

        if ((asc >= '0') && (asc <= '9')) bcd = (byte) (asc - '0');
        else if ((asc >= 'A') && (asc <= 'F')) bcd = (byte) (asc - 'A' + 10);
        else if ((asc >= 'a') && (asc <= 'f')) bcd = (byte) (asc - 'a' + 10);
        else bcd = (byte) (asc - 48);
        return bcd;
    }

    /**
     * BCD转字符串
     */
    private static String bcd2Str(byte[] bytes) {
        char temp[] = new char[bytes.length * 2], val;

        for (int i = 0; i < bytes.length; i++) {
            val = (char) (((bytes[i] & 0xf0) >> 4) & 0x0f);
            temp[i * 2] = (char) (val > 9 ? val + 'A' - 10 : val + '0');

            val = (char) (bytes[i] & 0x0f);
            temp[i * 2 + 1] = (char) (val > 9 ? val + 'A' - 10 : val + '0');
        }
        return new String(temp);
    }

    /**
     * 拆分字符串
     */
    private static String[] splitString(String string, int len) {
        int x = string.length() / len;
        int y = string.length() % len;
        int z = 0;
        if (y != 0) {
            z = 1;
        }
        String[] strings = new String[x + z];
        String str = "";
        for (int i = 0; i < x + z; i++) {
            if (i == x + z - 1 && y != 0) {
                str = string.substring(i * len, i * len + y);
            } else {
                str = string.substring(i * len, i * len + len);
            }
            strings[i] = str;
        }
        return strings;
    }

    /**
     * 拆分数组
     */
    private static byte[][] splitArray(byte[] data, int len) {
        int x = data.length / len;
        int y = data.length % len;
        int z = 0;
        if (y != 0) {
            z = 1;
        }
        byte[][] arrays = new byte[x + z][];
        byte[] arr;
        for (int i = 0; i < x + z; i++) {
            arr = new byte[len];
            if (i == x + z - 1 && y != 0) {
                System.arraycopy(data, i * len, arr, 0, y);
            } else {
                System.arraycopy(data, i * len, arr, 0, len);
            }
            arrays[i] = arr;
        }
        return arrays;
    }


    public static void main(String[] args) {
        String privateKeyStr =
            "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAJ5wFglnCZaYGyBx" +
                "NfKQOlaHzNNZe/qE+HRLI4ivUJ4ldMfslUjRShxxBl3Xzw9UkH9OJngJaVeObrve" +
                "0MCJ46hg01deviYIC3S+9vvej9fT+HgO0yGhZ86s0iXEBB727tO6nig5HlloJHaZ" +
                "WZS7Z4cKhwcJoUgovP99SPSF7jVlAgMBAAECgYAEseGVSb5Y/gzfuzsUAv4XjlKd" +
                "eKtZ0xLhr1BQPpGu/gEl43bQ/5KooRjFMx3poGHfw9sT94NtmsThQEsDSZK7YKN8" +
                "08imAJiEeBx6ImwM7L4vm0QtxcuRpcz2J5gNzV33FnHMUC+nivU7RsX5M1VDfIbV" +
                "um7Kgo438fXWcHx+iQJBAMsNH9b5Rl/66gp3EhXEeGs33v6ASN2HGwIkJmbTnWyU" +
                "KvIsf9eKmq7grhqhTd4vfFZcswu1QH7+me9VGPGWZ3sCQQDHwMAx47+XYuwrznJ/" +
                "ef4E8qUAAYHD9uvoB2Eadakp4/VzK22XGfn2l5d0zNg5bwkmSBYhtZg6ICSskGkd" +
                "UNCfAkBnxTRRdCGwKZZ0dLfMYhU8jlgrbrpOZJ678Gejw2A/vlVYYL+REyfMWc0A" +
                "lRErjM8Zf9SNFjt463sWIkJWLQyDAkEAm6GE3Rn560Qqh9L4iHOOw2IdxkxmQz09" +
                "/fDJ6iikHTw7v3ilkOWvSD5BxcHX8Z+ePFJL1AW9TgQ/LqfxDeMqNwJAJUJ1J9qF" +
                "QiCRKfgolssqeXCpjKY/64MvvVlYYvV7SPlFSpzl8F7lzfjTXxtYWB9Mb1NUo+t1" +
                "GLfwbcRexF2Rrw==";
        String data = "VBJRZQfQyXIEY2wt3JrGSa24oLTOvk4lp5Bf2rdXpmSTUctL3iGVhLP6sfL6xC72L/kk1EVlNKMYPzW3CgBtzXh6aAGcsSSHDr7/vRUiYKds+VoYifHt3Jmu98Vy9KIouZBpUS19KCxo1hTBTjaRFbkxJoowVLdtQp/ljsNfmzQ=";
        RSAUtils rsa = new RSAUtils();
        RSAPrivateKey rsaPrivateKey = rsa.loadPrivateKey(privateKeyStr);
        String s = rsa.RSADecode(data, rsaPrivateKey);
        System.out.println(s);
    }

}

