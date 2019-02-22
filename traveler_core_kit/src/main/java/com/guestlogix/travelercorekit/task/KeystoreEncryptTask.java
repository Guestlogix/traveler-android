package com.guestlogix.travelercorekit.task;

import android.os.Build;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import java.security.*;

import static com.guestlogix.travelercorekit.task.KeystoreEncryptTask.ErrorCode.ENCRYPTION_ERROR;

public class KeystoreEncryptTask extends Task {

    private static final String TRANSFORMATION = "AES/GCM/NoPadding";
    private static final String ANDROID_KEY_STORE = "AndroidKeyStore";
    private static final int IV_LENGTH = 128;

    private byte[] encryption;
    private byte[] iv;

    private String mKey;
    private byte[] mData;
    private Error mError;

    public enum ErrorCode {
        EMPTY_DATA, ENCRYPTION_ERROR
    }

    public class KeystoreWriteTaskError extends Error {
        private ErrorCode mCode;
        private String mMessage;

        KeystoreWriteTaskError(ErrorCode code, String message) {
            mCode = code;
            mMessage = message;
        }

        public String toString() {
            return mCode + mMessage;
        }
    }

    public KeystoreEncryptTask(String key) {
        this.mKey = key;
    }

    public String getKey() {
        return mKey;
    }

    public void setData(byte[] data) {

        if (getState() == State.READY) {
            this.mData = data;
        } else {
        }
    }

    @Override
    public void execute() {
        try {

            new String(encryptText(mKey, mData));

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            mError = new KeystoreWriteTaskError(ENCRYPTION_ERROR, e.getMessage());
        } catch (NoSuchProviderException e) {
            mError = new KeystoreWriteTaskError(ENCRYPTION_ERROR, e.getMessage());
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            mError = new KeystoreWriteTaskError(ENCRYPTION_ERROR, e.getMessage());
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            mError = new KeystoreWriteTaskError(ENCRYPTION_ERROR, e.getMessage());
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            mError = new KeystoreWriteTaskError(ENCRYPTION_ERROR, e.getMessage());
            e.printStackTrace();
        } catch (BadPaddingException e) {
            mError = new KeystoreWriteTaskError(ENCRYPTION_ERROR, e.getMessage());
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            mError = new KeystoreWriteTaskError(ENCRYPTION_ERROR, e.getMessage());
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
