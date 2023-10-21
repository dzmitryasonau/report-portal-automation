package com.reportportal.services;

import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import com.reportportal.exceptions.AutomationException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class AesCryptoService
{
    @Value("${aes.key}")
    private String key;

    public String encrypt(String encodedString)
    {
        try
        {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "AES"),
                    new IvParameterSpec(new byte[16]));
            byte[] encryptedBytes = cipher.doFinal(encodedString.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(encryptedBytes);
        }
        catch (NoSuchPaddingException | NoSuchAlgorithmException | InvalidAlgorithmParameterException |
                InvalidKeyException | IllegalBlockSizeException | BadPaddingException e)
        {
            throw new AutomationException("Unable to encode string", encodedString);
        }
    }

    public String decrypt(String decodedString)
    {
        try
        {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "AES"),
                    new IvParameterSpec(new byte[16]));
            byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(decodedString));
            return new String(decryptedBytes, StandardCharsets.UTF_8);
        }
        catch (NoSuchPaddingException | NoSuchAlgorithmException | InvalidAlgorithmParameterException |
                InvalidKeyException | IllegalBlockSizeException | BadPaddingException e)
        {
            throw new AutomationException("Unable to decode string", decodedString);
        }
    }
}
