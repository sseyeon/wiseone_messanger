package com.messanger.engine.uc.utils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.codec.binary.Base64;
import org.springframework.util.StringUtils;

/**
 * @author Administrator
 *
 */
public class DigestUtil {

	private static final char[] HEX_DIGITS = {'0', '1', '2', '3', '4', '5',
        '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

	public static String encodeToString(final String pass, String algorithm, String charEncoding) {
		return getFormattedText(encodeToBytes(pass, algorithm, charEncoding));
    }
	
	public static byte[] encodeToBytes(final String pass, String algorithm, String charEncoding) {
		if (pass == null) {
            return null;
        }

        try {
            MessageDigest messageDigest = MessageDigest.getInstance(algorithm);

            if (StringUtils.hasText(charEncoding)) {
                messageDigest.update(pass.getBytes(charEncoding));
            } else {
                messageDigest.update(pass.getBytes());
            }
            final byte[] digest = messageDigest.digest();

            return digest;
        } catch (final NoSuchAlgorithmException e) {
            throw new SecurityException(e);
        } catch (final UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
	}
	
	/**
	 * Calculate digest of given String using given algorithm.
	 * Encode digest in MIME-like base64.
	 *
	 * @param pass the String to be hashed
	 * @param algorithm the algorithm to be used
	 * @return String Base-64 encoding of digest
	 *
	 * @throws NoSuchAlgorithmException if the algorithm passed in cannot be found
	 */
	public static String woDigestPassword(String pass, String algorithm) throws NoSuchAlgorithmException {
		MessageDigest md;		
		try {
			md = MessageDigest.getInstance(algorithm);
			byte[] digest = md.digest(pass.getBytes("iso-8859-1"));
			//wo encoder 에 close를 하지 않아 맞추기 위해 2byte를 빼준다.
			byte[] tmp = new byte[18];			
			System.arraycopy(digest, 0, tmp, 0, 18);
			byte[] encoded = Base64.encodeBase64(tmp);			
			return new String(encoded, "iso-8859-1");		
		} catch (IOException ioe) {
			throw new RuntimeException("Fatal error: " + ioe);
		} 
	}
	
	public static String hashPassword(String passwordBuilderClassName, String password) throws Exception {
//		PasswordDirector.init(passwordBuilderClassName);
//		PasswordBuilderIF pb = PasswordDirector.builder();
//		return pb.hash(password);
		return org.apache.commons.lang.StringUtils.EMPTY;
	}
	
	public static String encodeToBase64(String text) throws UnsupportedEncodingException {
		byte[] encoded = Base64.encodeBase64(text.getBytes("UTF-8"));			
		return new String(encoded, "iso-8859-1");		
	}
	
	public static String decodeToBase64(String text) throws UnsupportedEncodingException {
		byte[] decoded = Base64.decodeBase64(text.getBytes("UTF-8"));
		return new String(decoded, "UTF-8");
	}
/*
	public static String woDigestPassword2(String pass, String algorithm) throws NoSuchAlgorithmException {
		MessageDigest md;
		ByteArrayOutputStream bos;
		try {
			md = MessageDigest.getInstance(algorithm);
			byte[] digest = md.digest(pass.getBytes("iso-8859-1"));
			bos = new ByteArrayOutputStream();			
			OutputStream encodedStream = MimeUtility.encode(bos, "base64");
			encodedStream.write(digest);
			//encodedStream.close();
			return bos.toString("iso-8859-1");			
		} catch (IOException ioe) {
			throw new RuntimeException("Fatal error: " + ioe);
		} catch (MessagingException me) {
			throw new RuntimeException("Fatal error: " + me);
		}
	}
*/
    public static String getFormattedText(byte[] bytes) {
        final StringBuilder buf = new StringBuilder(bytes.length * 2);

        for (int j = 0; j < bytes.length; j++) {
            buf.append(HEX_DIGITS[(bytes[j] >> 4) & 0x0f]);
            buf.append(HEX_DIGITS[bytes[j] & 0x0f]);
        }
        return buf.toString();
    }
    
//	public static void main(String[] args) throws Exception {
//		String sp = "TCKwm7tdi/gbuSXuATax9yUi";
//		String p = "masic";		
//		//System.out.println(woDigestPassword2(p, "SHA"));
//		System.out.println(woDigestPassword(p, "SHA"));
//		System.out.println(encodeToString(p, "MD5", "UTF-8"));
//	}
}

