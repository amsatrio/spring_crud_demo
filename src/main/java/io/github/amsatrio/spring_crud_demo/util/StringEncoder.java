package io.github.amsatrio.spring_crud_demo.util;

import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class StringEncoder {
    public String removeControlCharacters(String input) {
        return input.replaceAll("[\\x00-\\x1F\\x7F]", "");
    }

    public String removeHexStrings(String input) {
        StringBuffer stringBuilder = new StringBuffer();
        String hexPattern = "\\\\x[0-9A-F]+";
        Pattern pattern = Pattern.compile(hexPattern);
        Matcher matcher = pattern.matcher(input);
        while (matcher.find()) {
            matcher.appendReplacement(stringBuilder, "");
        }
        matcher.appendTail(stringBuilder);
        return stringBuilder.toString();
    }

    public String convertToUtf8(String input) {
        try {
            // convert string to bytes
            byte[] bytes = input.getBytes();

            // Check if the input string is a valid hexadecimal string
            if (isHexString(input)) {
                // Convert the hexadecimal string to bytes
                bytes = hexStringToByteArray(input);
            }

            // Convert bytes to UTF-8 string
            return new String(bytes, StandardCharsets.UTF_8);
        } catch (Exception exception) {
            log.error("StringEncoder > convertToUtf8 > error ", exception);
            return input;
        }
    }

    // Helper method to convert a hexadecimal string to a byte array
    private byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i + 1), 16));
        }
        return data;
    }

    // Function to check if the given bytes represent a valid UTF-8 encoding
    private boolean isValidUtf8(byte[] bytes) {
        try {
            String utf8String = new String(bytes, StandardCharsets.UTF_8);
            byte[] utf8Bytes = utf8String.getBytes(StandardCharsets.UTF_8);
            return java.util.Arrays.equals(bytes, utf8Bytes);
        } catch (Exception exception) {
            log.error("StringEncoder > isValidUtf8 > error ", exception);
            return false;
        }
    }

    // Function to check if the given string is a valid hexadecimal string
    private boolean isHexString(String s) {
        return s.matches("^[0-9A-Fa-f]+$");
    }

    public boolean base64BasicValidation(String input) {
        if (input == null || input.isEmpty()) {
            return false;
        }
        try {
            Base64.getDecoder().decode(input.getBytes(StandardCharsets.UTF_8));
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    public String base64BasicEncoder(String input) {
        String output = "";
        Base64.Encoder encoder = Base64.getEncoder();
        output = encoder.encodeToString(input.getBytes());
        return output;
    }
}
