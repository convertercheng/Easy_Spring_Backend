package com.qhieco.util;

import java.io.ByteArrayOutputStream;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Cipher;


/**
 * @author 郑旭 790573267@qq.com
 * @version 2.0.1 创建时间: 18-6-20  下午2:27
 * <p>
 * 类说明：
 * ${description}
 */
public class RSAUtil {
    /**
     * 测试方法
     */
    public static void main(String[] args) {
        String publicKey = "MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBAJ3tJBHMNeVP+WDzGhY5YFrCsvsqOutnb9/QmiOBjeeDqcHzHgB6rdPQ1MTC/+kIPe6pVzuHOc5qIxtZdm+6zKsCAwEAAQ==";
        String privateKey = "MIIBVAIBADANBgkqhkiG9w0BAQEFAASCAT4wggE6AgEAAkEAne0kEcw15U/5YPMaFjlgWsKy+yo662dv39CaI4GN54OpwfMeAHqt09DUxML/6Qg97qlXO4c5zmojG1l2b7rMqwIDAQABAkBtl44LR8Iy8q+lBq6Ys5JZCXm48FJY37vz/keo0hPOjymP4Y3ce/9GhL76FRArkRONq71dNLWFDnZQVmoufwlJAiEAzbi4pwnKaufZnwfhfhGCN0ooSJFCEbtw4uph8ua1JOUCIQDEhgn2F/72H25lHLmkMpT/kvOLdD+dEWc7rwBFAlYiTwIgc1fTqEw0Ata1zJW1l2KnuOpKRc2z1o9azs+NVYyHGgUCIF1+04Bj/CWF4JR0LRGkCuToObvPy2/jVrS7Vip+qRHbAiEAx326vF9PLU3hkb28wh8VIlBTrXpjYPCeYct37WOzVlo=";

        String str = "0000000010000001111111.jpg";
        RSAUtil rsaUtil = new RSAUtil(publicKey,privateKey);
        //生成数字签名
        String sign = rsaUtil.generateSign(str,"SHA1withRSA");
        System.out.println(sign);

    }

    //定义加密方式
    public static final String KEY_RSA = "RSA";
    //定义公钥关键词
    public static final String KEY_RSA_PUBLICKEY = "RSAPublicKey";
    //定义私钥关键词
    public static final String KEY_RSA_PRIVATEKEY = "RSAPrivateKey";
    //定义签名算法
    private final static String KEY_RSA_SIGNATURE = "SHA1withRSA";
    //定义加解密最大长度
    private final static Integer MAX_ENCRYPT_BLOCK = 46;

    //公钥
    private String publicKey;
    //私钥
    private String privateKey;
    //签名sign
    private byte[] sign;

    //获取二进制十六进制转换的函数

    /**
     * 构造函数
     * 如果传入公钥私钥，那么就利用传入的公钥私钥
     */
    public RSAUtil(String publicKey,String privateKey){
        this.publicKey = publicKey;
        this.privateKey = privateKey;

    }

    /**
     * 构造函数
     * 如果传入公钥私钥，那么就利用传入的公钥私钥
     */
    public RSAUtil(){
        Map<String,Object> map = this.init();
        this.publicKey = this.getPublicKey(map);
        this.privateKey = this.getPrivateKey(map);

    }


    /**
     * 私钥加密生成数字签名
     */
    public String generateSign(String encryptingStr,String KEY_RSA_SIGNATURE){
        String str = "";
        try {
            //将私钥加密数据字符串转换为字节数组
            byte[] data = encryptingStr.getBytes();
            // 解密由base64编码的私钥
            byte[] bytes = decryptBase64(this.privateKey);
            // 构造PKCS8EncodedKeySpec对象
            PKCS8EncodedKeySpec pkcs = new PKCS8EncodedKeySpec(bytes);
            // 指定的加密算法
            KeyFactory factory = KeyFactory.getInstance(KEY_RSA);
            // 取私钥对象
            PrivateKey key = factory.generatePrivate(pkcs);
            // 用私钥对信息生成数字签名
            Signature signature = Signature.getInstance(KEY_RSA_SIGNATURE);
            signature.initSign(key);
            signature.update(data);
            byte[] sign = signature.sign();
            this.sign = sign;
            return AesUtil.parseByte2HexStr(sign);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }


    /**
     * 1、公钥加密，私钥解密用于信息加密
     * 2、私钥加密，公钥解密用于数字签名
     */


    /**
     * 公钥加密
     * @param encryptingStr
     * @return
     */
    public String encryptByPublic(String encryptingStr){
        try{

            //将公钥转换成UTF-8格式的字节数组
            byte[] publicKeyBytes = decryptBase64(this.publicKey);
            //获得公钥
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKeyBytes);
            //取得待加密数据
            byte[] data = encryptingStr.getBytes("UTF-8");
            KeyFactory factory;
            factory = KeyFactory.getInstance(KEY_RSA);
            PublicKey publicKey = factory.generatePublic(keySpec);

            //对数据加密
            Cipher cipher = Cipher.getInstance(factory.getAlgorithm());
            cipher.init(Cipher.ENCRYPT_MODE,publicKey);


            // 返回加密后由Base64编码的加密信息
            return encryptBase64(EncryptionAndDecryptionSegment(cipher,data)) ;

        } catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }
    /**
     * 私钥解密
     * @param encryptedStr
     * @return
     */
    public String decryptByPrivate(String encryptedStr){
        try {
            // 对私钥解密
            byte[] privateKeyBytes = decryptBase64(this.privateKey);
            // 获得私钥
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
            // 获得待解密数据
            byte[] data = decryptBase64(encryptedStr);
            KeyFactory factory = KeyFactory.getInstance(KEY_RSA);
            PrivateKey privateKey = factory.generatePrivate(keySpec);
            // 对数据解密
            Cipher cipher = Cipher.getInstance(factory.getAlgorithm());
            cipher.init(Cipher.DECRYPT_MODE, privateKey);

            // 返回UTF-8编码的解密信息
            return new String(EncryptionAndDecryptionSegment(cipher,data), "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 私钥加密
     * @param encryptingStr
     * @return
     */
    public String encryptByPrivate(String encryptingStr){
        try {
            byte[] privateKeyBytes = decryptBase64(this.privateKey);
            // 获得私钥
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
            // 取得待加密数据
            byte[] data = encryptingStr.getBytes("UTF-8");
            KeyFactory factory = KeyFactory.getInstance(KEY_RSA);
            PrivateKey privateKey = factory.generatePrivate(keySpec);
            // 对数据加密
            Cipher cipher = Cipher.getInstance(factory.getAlgorithm());
            cipher.init(Cipher.ENCRYPT_MODE, privateKey);

            // 返回加密后由Base64编码的加密信息
            return encryptBase64(EncryptionAndDecryptionSegment(cipher,data)) ;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 公钥解密
     * @param encryptedStr
     * @return
     */
    public String decryptByPublic(String encryptedStr){
        try {
            // 对公钥解密
            byte[] publicKeyBytes = decryptBase64(this.publicKey);
            // 取得公钥
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKeyBytes);
            // 取得待加密数据
            byte[] data = decryptBase64(encryptedStr);
            KeyFactory factory = KeyFactory.getInstance(KEY_RSA);
            PublicKey publicKey = factory.generatePublic(keySpec);
            // 对数据解密
            Cipher cipher = Cipher.getInstance(factory.getAlgorithm());
            cipher.init(Cipher.DECRYPT_MODE, publicKey);

            return new String(EncryptionAndDecryptionSegment(cipher,data), "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
     /* @param encryptedStr
     * @return 校验成功返回true，失败返回false
     */
    public boolean verify(String encryptedStr) {
        boolean flag = false;
        try {
            //将私钥加密数据字符串转换为字节数组
            byte[] data = encryptedStr.getBytes();
            // 解密由base64编码的公钥
            byte[] bytes = decryptBase64(publicKey);
            // 构造X509EncodedKeySpec对象
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(bytes);
            // 指定的加密算法
            KeyFactory factory = KeyFactory.getInstance(KEY_RSA);
            // 取公钥对象
            PublicKey key = factory.generatePublic(keySpec);
            // 用公钥验证数字签名
            Signature signature = Signature.getInstance(KEY_RSA_SIGNATURE);
            signature.initVerify(key);
            signature.update(data);
            flag = signature.verify(this.sign);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }

    /**
     * 生成公私密钥对
     */
    private Map<String, Object> init(){
        Map <String,Object> map= null;
        try{
            KeyPairGenerator generator = KeyPairGenerator.getInstance(KEY_RSA);
            //设置密钥对的bit数，越大越安全，但速度减慢，一般使用512或1024
            generator.initialize(512);
            KeyPair keyPair = generator.generateKeyPair();
            //获取公钥
            RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
            //获取私钥
            RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
            //将密钥封装成Map
            map = new HashMap<String, Object>();
            map.put(KEY_RSA_PUBLICKEY,publicKey);
            map.put(KEY_RSA_PRIVATEKEY,privateKey);


        }catch(NoSuchAlgorithmException e){
            e.printStackTrace();
        }
        return map;
    }

    /**
     * 获取Base64编码的公钥字符串
     */
    private String getPublicKey(Map<String, Object> map) {
        String str = "";
        Key key = (Key) map.get(KEY_RSA_PUBLICKEY);
        str = encryptBase64(key.getEncoded());
        return str;
    }

    /**
     * 获取Base64编码的私钥字符串
     */
    private String getPrivateKey(Map<String, Object> map) {
        String str = "";
        Key key = (Key) map.get(KEY_RSA_PRIVATEKEY);
        str = encryptBase64(key.getEncoded());
        return str;
    }

    /**
     * BASE64 解码
     * @param key 需要Base64解码的字符串
     * @return 字节数组
     */
    private byte[] decryptBase64(String key) {
        return Base64.getDecoder().decode(key);
    }

    /**
     * BASE64 编码
     * @param key 需要Base64编码的字节数组
     * @return 字符串
     */
    private String encryptBase64(byte[] key) {
        return new String(Base64.getEncoder().encode(key));
    }


    /**
     * 用于分段加密，解密的函数
     * @param data 待加密/解密的数据
     * @param cipher 加密/解密器实例
     */
    private byte[] EncryptionAndDecryptionSegment(Cipher cipher, byte[] data) throws Exception{
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int inputLen = data.length;
        int i = 0;
        int offset = 0;
        byte[] cache;
        //对数据分段加密
        while(inputLen - offset > 0){
            if(inputLen - offset > MAX_ENCRYPT_BLOCK){
                cache = cipher.doFinal(data,offset,MAX_ENCRYPT_BLOCK);
            }else{
                cache = cipher.doFinal(data,offset,inputLen - offset);
            }
            out.write(cache,0,cache.length);
            i++;
            offset = MAX_ENCRYPT_BLOCK * i;

        }
        byte[] encryptedData = out.toByteArray();
        out.close();
        return encryptedData;
    }
}