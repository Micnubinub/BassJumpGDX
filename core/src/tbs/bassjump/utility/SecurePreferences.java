package tbs.bassjump.utility;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class SecurePreferences {

    private static final String TRANSFORMATION = "AES/CBC/PKCS5Padding";
    private static final String KEY_TRANSFORMATION = "AES/ECB/PKCS5Padding";
    private static final String SECRET_KEY_HASH_TRANSFORMATION = "SHA-256";
    private static final String CHARSET = "UTF-8";
    private final boolean encryptKeys;
    private final Cipher writer;
    private final Cipher reader;
    private final Cipher keyWriter;
    private final SharedPreferences preferences;

    public SecurePreferences(Context context, String preferenceName, String secureKey, boolean encryptKeys)
            throws SecurePreferencesException {
        try {
            this.writer = Cipher.getInstance(TRANSFORMATION);
            this.reader = Cipher.getInstance(TRANSFORMATION);
            this.keyWriter = Cipher.getInstance(KEY_TRANSFORMATION);

            initCiphers(secureKey);

            this.preferences = context.getSharedPreferences(preferenceName, Context.MODE_PRIVATE);
            this.encryptKeys = encryptKeys;

        } catch (GeneralSecurityException e) {
            throw new SecurePreferencesException(e);
        } catch (UnsupportedEncodingException e) {
            throw new SecurePreferencesException(e);
        }
    }

    private static byte[] convert(Cipher cipher, byte[] bs)
            throws SecurePreferencesException {
        try {
            return cipher.doFinal(bs);
        } catch (Exception e) {
            throw new SecurePreferencesException(e);
        }
    }

    void initCiphers(String secureKey)
            throws UnsupportedEncodingException, NoSuchAlgorithmException,
            InvalidKeyException, InvalidAlgorithmParameterException {
        IvParameterSpec ivSpec = getIv();
        SecretKeySpec secretKey = getSecretKey(secureKey);

        writer.init(Cipher.ENCRYPT_MODE, secretKey, ivSpec);
        reader.init(Cipher.DECRYPT_MODE, secretKey, ivSpec);
        keyWriter.init(Cipher.ENCRYPT_MODE, secretKey);
    }

    IvParameterSpec getIv() {
        byte[] iv = new byte[writer.getBlockSize()];
        System.arraycopy("fldsjfodasjifudslfjdsaofshaufihadsf".getBytes(), 0,
                iv, 0, writer.getBlockSize());
        return new IvParameterSpec(iv);
    }

    SecretKeySpec getSecretKey(String key)
            throws UnsupportedEncodingException, NoSuchAlgorithmException {
        byte[] keyBytes = createKeyBytes(key);
        return new SecretKeySpec(keyBytes, TRANSFORMATION);
    }

    byte[] createKeyBytes(String key)
            throws UnsupportedEncodingException, NoSuchAlgorithmException {
        final MessageDigest md = MessageDigest.getInstance(SECRET_KEY_HASH_TRANSFORMATION);
        md.reset();
        return md.digest(key.getBytes(CHARSET));
    }

    public void put(String key, String value) {
        if (value == null) {
            preferences.edit().remove(toKey(key)).commit();
        } else {
            putValue(toKey(key), value);
        }
    }

    public boolean containsKey(String key) {
        return preferences.contains(toKey(key));
    }

    public void removeValue(String key) {
        preferences.edit().remove(toKey(key)).commit();
    }

    public String getString(String key) throws SecurePreferencesException {
        if (preferences.contains(toKey(key))) {
            String securedEncodedValue = preferences.getString(toKey(key), "");
            return decrypt(securedEncodedValue);
        }
        return null;
    }

    public void clear() {
        preferences.edit().clear().commit();
    }

    private String toKey(String key) {
        if (encryptKeys)
            return encrypt(key, keyWriter);
        else
            return key;
    }

    private void putValue(String key, String value)
            throws SecurePreferencesException {
        preferences.edit().putString(key, encrypt(value, writer)).commit();
    }

    String encrypt(String value, Cipher writer)
            throws SecurePreferencesException {
        byte[] secureValue;
        try {
            secureValue = convert(writer, value.getBytes(CHARSET));
        } catch (UnsupportedEncodingException e) {
            throw new SecurePreferencesException(e);
        }
        return Base64.encodeToString(secureValue,
                Base64.NO_WRAP);
    }

    String decrypt(String securedEncodedValue) {
        final byte[] value = convert(reader, Base64.decode(securedEncodedValue, Base64.NO_WRAP));
        try {
            return new String(value, CHARSET);
        } catch (UnsupportedEncodingException e) {
            throw new SecurePreferencesException(e);
        }
    }

    public static class SecurePreferencesException extends RuntimeException {

        public SecurePreferencesException(Throwable e) {
            super(e);
        }

    }
}
