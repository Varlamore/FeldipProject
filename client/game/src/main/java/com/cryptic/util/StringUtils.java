package com.cryptic.util;

import com.cryptic.Client;
import com.cryptic.cache.graphics.font.AdvancedFont;
import com.cryptic.io.Buffer;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public final class StringUtils {

    public static final Pattern VALID_NAME = Pattern.compile("^[a-zA-Z0-9_ ]{1,12}$");

    private static final char[] BASE_37_CHARACTERS = {'_', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};

    public static int hashString(CharSequence str) {
        int length = str.length();
        int hash = 0;

        for(int i = 0; i < length; ++i) {
            hash = (hash << 5) - hash + charToByteCp1252(str.charAt(i));
        }

        return hash;
    }

    public static String[] wrap(AdvancedFont font, String text, int maximumWidth) {
        String[] words = text.split(" ");

        if (words.length == 0) {
            return new String[] { text };
        }

        List<String> lines = new ArrayList<>();

        String line = new String();

        int lineWidth = 0;

        int spaceWidth = font.getTextWidth(" ");

        for (String word : words) {
            if (word.isEmpty()) {
                continue;
            }
            int wordWidth = font.getTextWidth(word);
            boolean isLastWord = word.equals(words[words.length - 1]);

            if (wordWidth + lineWidth >= maximumWidth && !isLastWord) {
                lines.add(line.trim());
                line = new String(word.concat(" "));
                lineWidth = wordWidth + spaceWidth;
            } else if (isLastWord) {
                if (wordWidth + lineWidth > maximumWidth) {
                    lines.add(line.trim());
                    lines.add(word);
                } else {
                    lines.add(line.concat(word));
                }
            } else {
                line = line.concat(word).concat(" ");
                lineWidth += wordWidth + spaceWidth;
            }
        }

        return lines.toArray(new String[lines.size()]);
    }

    public static byte charToByteCp1252(char var0) {
        byte var1;
        if (var0 > 0 && var0 < 128 || var0 >= 160 && var0 <= 255) {
            var1 = (byte)var0;
        } else if (var0 == 8364) {
            var1 = -128;
        } else if (var0 == 8218) {
            var1 = -126;
        } else if (var0 == 402) {
            var1 = -125;
        } else if (var0 == 8222) {
            var1 = -124;
        } else if (var0 == 8230) {
            var1 = -123;
        } else if (var0 == 8224) {
            var1 = -122;
        } else if (var0 == 8225) {
            var1 = -121;
        } else if (var0 == 710) {
            var1 = -120;
        } else if (var0 == 8240) {
            var1 = -119;
        } else if (var0 == 352) {
            var1 = -118;
        } else if (var0 == 8249) {
            var1 = -117;
        } else if (var0 == 338) {
            var1 = -116;
        } else if (var0 == 381) {
            var1 = -114;
        } else if (var0 == 8216) {
            var1 = -111;
        } else if (var0 == 8217) {
            var1 = -110;
        } else if (var0 == 8220) {
            var1 = -109;
        } else if (var0 == 8221) {
            var1 = -108;
        } else if (var0 == 8226) {
            var1 = -107;
        } else if (var0 == 8211) {
            var1 = -106;
        } else if (var0 == 8212) {
            var1 = -105;
        } else if (var0 == 732) {
            var1 = -104;
        } else if (var0 == 8482) {
            var1 = -103;
        } else if (var0 == 353) {
            var1 = -102;
        } else if (var0 == 8250) {
            var1 = -101;
        } else if (var0 == 339) {
            var1 = -100;
        } else if (var0 == 382) {
            var1 = -98;
        } else if (var0 == 376) {
            var1 = -97;
        } else {
            var1 = 63;
        }

        return var1;
    }

    public static int method785(Buffer var0, String var1) {
        int var2 = var0.pos;
        int var4 = var1.length();
        byte[] var5 = new byte[var4];

        for (int var6 = 0; var6 < var4; ++var6) {
            char var7 = var1.charAt(var6);
            if ((var7 <= 0 || var7 >= 128) && (var7 < 160 || var7 > 255)) {
                if (var7 == 8364) {
                    var5[var6] = -128;
                } else if (var7 == 8218) {
                    var5[var6] = -126;
                } else if (var7 == 402) {
                    var5[var6] = -125;
                } else if (var7 == 8222) {
                    var5[var6] = -124;
                } else if (var7 == 8230) {
                    var5[var6] = -123;
                } else if (var7 == 8224) {
                    var5[var6] = -122;
                } else if (var7 == 8225) {
                    var5[var6] = -121;
                } else if (var7 == 710) {
                    var5[var6] = -120;
                } else if (var7 == 8240) {
                    var5[var6] = -119;
                } else if (var7 == 352) {
                    var5[var6] = -118;
                } else if (var7 == 8249) {
                    var5[var6] = -117;
                } else if (var7 == 338) {
                    var5[var6] = -116;
                } else if (var7 == 381) {
                    var5[var6] = -114;
                } else if (var7 == 8216) {
                    var5[var6] = -111;
                } else if (var7 == 8217) {
                    var5[var6] = -110;
                } else if (var7 == 8220) {
                    var5[var6] = -109;
                } else if (var7 == 8221) {
                    var5[var6] = -108;
                } else if (var7 == 8226) {
                    var5[var6] = -107;
                } else if (var7 == 8211) {
                    var5[var6] = -106;
                } else if (var7 == 8212) {
                    var5[var6] = -105;
                } else if (var7 == 732) {
                    var5[var6] = -104;
                } else if (var7 == 8482) {
                    var5[var6] = -103;
                } else if (var7 == 353) {
                    var5[var6] = -102;
                } else if (var7 == 8250) {
                    var5[var6] = -101;
                } else if (var7 == 339) {
                    var5[var6] = -100;
                } else if (var7 == 382) {
                    var5[var6] = -98;
                } else if (var7 == 376) {
                    var5[var6] = -97;
                } else {
                    var5[var6] = 63;
                }
            } else {
                var5[var6] = (byte)var7;
            }
        }

        var0.writeSmartByteShort(var5.length);
        var0.pos += Client.huffman.compress(var5, 0, var5.length, var0.payload, var0.pos);
        return var0.pos - var2;
    }

    public static long encodeBase37(String string) {
        long encoded = 0L;
        for (int index = 0; index < string.length() && index < 12; index++) {
            char c = string.charAt(index);
            encoded *= 37L;
            if (c >= 'A' && c <= 'Z')
                encoded += (1 + c) - 65;
            else if (c >= 'a' && c <= 'z')
                encoded += (1 + c) - 97;
            else if (c >= '0' && c <= '9')
                encoded += (27 + c) - 48;
        }

        for (; encoded % 37L == 0L && encoded != 0L; encoded /= 37L)
            ;

        return encoded;
    }

    public static String decodeBase37(long encoded) {
        try {
            if (encoded <= 0L || encoded >= 0x5b5b57f8a98a5dd1L)
                return "invalid_name";
            if (encoded % 37L == 0L)
                return "invalid_name";
            int length = 0;
            char[] chars = new char[12];
            while (encoded != 0L) {
                long l1 = encoded;
                encoded /= 37L;
                char c = BASE_37_CHARACTERS[(int) (l1 - encoded * 37L)];
                chars[11 - length++] = c;
            }
            return new String(chars, 12 - length, length);
        } catch (RuntimeException runtimeexception) {
            runtimeexception.printStackTrace();
            System.out.println("81570, " + encoded + ", " + (byte) -99 + ", " + runtimeexception.toString());
        }
        throw new RuntimeException();
    }

    /**
     * Capitalizes any characters succeeding a space otherwise we don't worry about any casing.
     * @param string
     * @return
     */
    public static String capitalizeIf(String string) {
        string = string.trim();
        if (string.trim().length() < 1)
            return "";
        byte[] data = string.getBytes();
        if (data == null || data.length < 1)
            return "";
        for (int i = 0; i < string.length(); i++) {
            byte b = data[i];
            if (b == 95 || b == 32) {
                if (b == 95)
                    data[i] = 32;
                byte next = data[i + 1];
                if (next >= 97 && next <= 122) {
                    data[i + 1] = (byte) (next - 32);
                }
            }
        }
        if (data[0] >= 97 && data[0] <= 122)
            data[0] = (byte) (data[0] - 32);
        return new String(data, 0, string.length());
    }

    public static long hashSpriteName(String name) {
        name = name.toUpperCase();
        long hash = 0L;
        for (int index = 0; index < name.length(); index++) {
            hash = (hash * 61L + (long) name.charAt(index)) - 32L;
            hash = hash + (hash >> 56) & 0xffffffffffffffL;
        }
        return hash;
    }

    /**
     * Used to format a users ip address on the welcome screen.
     */
    public static String decodeIp(int ip) {
        return (ip >> 24 & 0xff) + "." + (ip >> 16 & 0xff) + "." + (ip >> 8 & 0xff) + "." + (ip & 0xff);
    }

    public static String capitalize(String text) {
        if (text == null)
            return text;
        for (int text_length = 0; text_length < text.length(); text_length++) {
            if (text_length == 0) {
                text = String.format("%s%s", Character.toUpperCase(text.charAt(0)), text.substring(1));
            }
            if (!Character.isLetterOrDigit(text.charAt(text_length))) {
                if (text_length + 1 < text.length()) {
                    text = String.format("%s%s%s", text.subSequence(0, text_length + 1),  Character.toUpperCase(text.charAt(text_length + 1)), text.substring(text_length + 2));
                }
            }
        }
        return text;
    }

    /**
     * Used to format a players name.
     */
    public static String formatText(String t) {
        if (t.length() > 0) {
            char chars[] = t.toCharArray();
            for (int index = 0; index < chars.length; index++)
                if (chars[index] == '_') {
                    chars[index] = ' ';
                    if (index + 1 < chars.length && chars[index + 1] >= 'a' && chars[index + 1] <= 'z') {
                        chars[index + 1] = (char) ((chars[index + 1] + 65) - 97);
                    }
                }

            if (chars[0] >= 'a' && chars[0] <= 'z') {
                chars[0] = (char) ((chars[0] + 65) - 97);
            }
            return new String(chars);
        } else {
            return t;
        }
    }

    public static String insertCommasToNumber(String number) {
        return number.length() < 4 ? number : insertCommasToNumber(number
                .substring(0, number.length() - 3))
                + ","
                + number.substring(number.length() - 3, number.length());
    }

    /**
     * Used for the login screen to hide a users password
     */
    public static String passwordAsterisks(String password) {
        StringBuffer stringbuffer = new StringBuffer();
        for (int index = 0; index < password.length(); index++)
            stringbuffer.append("*");
        return stringbuffer.toString();
    }

    public static String toAsterisks(String s) {
        StringBuilder result = new StringBuilder();
        for (int j = 0; j < s.length(); j++) {
            result.append("*");
        }
        return result.toString();
    }
}
