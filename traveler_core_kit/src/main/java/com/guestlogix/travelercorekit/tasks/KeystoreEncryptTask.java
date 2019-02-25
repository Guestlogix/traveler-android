package com.guestlogix.travelercorekit.tasks;

import android.os.Build;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import java.security.*;

import static com.guestlogix.travelercorekit.tasks.KeystoreEncryptTask.ErrorCode.ENCRYPTION_ERROR;

public class KeystoreEncryptTask extends Task {

    private static final String TRANSFORMATION = "AES/GCM/NoPadding";
    private static final String ANDROID_KEY_STORE = "AndroidKeyStore";
    private static final int IV_LENGTH = 128;

    private byte[] encryption;
    private byte[] iv;

    private String key;
    private byte[] data;
    private Error error;

    public enum ErrorCode {
        EMPTY_DATA, ENCRYPTION_ERROR
    }

    public class KeystoreWriteTaskError extends Error {
        private ErrorCode code;
        private String message;

        KeystoreWriteTaskError(ErrorCode code, String message) {
            this.code = code;
            this.message = message;
        }

        public String toString() {
            return code + message;
        }
    }

    public KeystoreEncryptTask(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    public void setData(byte[] data) {

        if (getState() == State.READY) {
            this.data = data;
        } else {
        }
    }

    @Override
    public void execute() {
        try {

            new String(encryptText(key, data));

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            error = new KeystoreWriteTaskError(ENCRYPTION_ERROR, e.getMessage());
        } catch (NoSuchProviderException e) {
            error = new KeystoreWriteTaskError(ENCRYPTION_ERROR, e.getMessage());
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            error = new KeystoreWriteTaskError(ENCRYPTION_ERROR, e.getMessage());
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            error = new KeystoreWriteTaskError(ENCRYPTION_ERROR, e.getMessage());
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            error = new KeystoreWriteTaskError(ENCRYPTION_ERROR, e.getMessage());
            e.printStackTrace();
        } catch (BadPaddingException e) {
            error = new KeystoreWriteTaskError(ENCRYPTION_ERROR, e.getMessage());
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            error = new KeystoreWriteTaskError(ENCRYPTION_ERROR, e.getMessage());
            e.printStackTrace();
        } finally {
            finish();
        }
    }


    private SecretKey getSecretKey(final String key) throws NoSuchAlgorithmException,
            NoSuchProviderException, InvalidAlgorithmParameterException {

        final KeyGenerator keyGenerator = KeyGenerator
                .getInstance(KeyProperties.KEY_ALGORITHM_AES, ANDROID_KEY_STORE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            keyGenerator.init(new KeyGenParameterSpec.Builder(key,
                    KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT)
                    .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                    .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                    .build());
        }

        return keyGenerator.generateKey();
    }

    byte[] encryptText(final String key, byte[] data)
            throws NoSuchAlgorithmException,
            NoSuchProviderException, NoSuchPaddingException, InvalidKeyException,
            InvalidAlgorithmParameterException, BadPaddingException,
            IllegalBlockSizeException {

        byte[] iv = new byte[IV_LENGTH];
        new SecureRandom().nextBytes(iv);

        final Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        cipher.init(Cipher.ENCRYPT_MODE, getSecretKey(key), new IvParameterSpec(iv));

        iv = cipher.getIV();

        return (encryption = cipher.doFinal(data));
    }

}
