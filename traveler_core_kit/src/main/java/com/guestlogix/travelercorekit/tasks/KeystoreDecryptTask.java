package com.guestlogix.travelercorekit.tasks;

import android.util.Log;

import javax.crypto.*;
import javax.crypto.spec.GCMParameterSpec;
import java.io.IOException;
import java.security.*;
import java.security.cert.CertificateException;

public class KeystoreDecryptTask extends Task {

    private String mKey;
    private byte[] mData;
    private static final int IV_LENGTH = 128;

    public KeystoreDecryptTask(String key) throws CertificateException, NoSuchAlgorithmException, KeyStoreException, IOException {
        this.mKey = key;
        initKeyStore();
    }

    public String getKey() {
        return mKey;
    }

    public void setData(byte[] data) {
        this.mData = data;
    }

    @Override
    public void execute() {
//        decryptData(mKey);

    }

    private static final String TRANSFORMATION = "AES/GCM/NoPadding";
    private static final String ANDROID_KEY_STORE = "AndroidKeyStore";

    private KeyStore keyStore;

    private void initKeyStore() throws KeyStoreException, CertificateException,
            NoSuchAlgorithmException, IOException {
        keyStore = KeyStore.getInstance(ANDROID_KEY_STORE);
        keyStore.load(null);
    }

    String decryptData(final String alias, final byte[] encryptedData, final byte[] encryptionIv)
            throws UnrecoverableEntryException, NoSuchAlgorithmException, KeyStoreException,
            NoSuchPaddingException, InvalidKeyException, IOException,
            BadPaddingException, IllegalBlockSizeException, InvalidAlgorithmParameterException {

        final Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        final GCMParameterSpec spec;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            spec = new GCMParameterSpec(IV_LENGTH, encryptionIv);
            cipher.init(Cipher.DECRYPT_MODE, getSecretKey(alias), spec);
        }

        return new String(cipher.doFinal(encryptedData), "UTF-8");
    }

    private SecretKey getSecretKey(final String alias) throws NoSuchAlgorithmException,
            UnrecoverableEntryException, KeyStoreException {
        return ((KeyStore.SecretKeyEntry) keyStore.getEntry(alias, null)).getSecretKey();
    }

}
