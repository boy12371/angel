package com.feinno.ha.service.common;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.BreakIterator;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

public class CommonStringUtil {

	public static String getCurrentDatetime() {
		return getCurrentDatetime(new Date());
	}

	public static String getCurrentDatetime(Date date) {
		DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String datetime = formatter.format(date);
		return datetime;
	}

	public static String getCurrentDate() {
		DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String datetime = formatter.format(new Date());
		return datetime;
	}

	public static String stringLeftPading(int strSrcLength, String strSrc,
			String ch) {
		String strReturn = "";
		String strtemp = "";
		int curLength = strSrc.trim().length();
		if (strSrc != null && curLength > strSrcLength) {
			strReturn = strSrc.trim().substring(0, strSrcLength);
		} else if (strSrc != null && curLength == strSrcLength) {
			strReturn = strSrc.trim();
		} else {
			for (int i = 0; i < (strSrcLength - curLength); i++) {
				strtemp = strtemp + ch;
			}
			strReturn = strtemp + strSrc.trim();
		}
		return strReturn;
	}

	public static final String replace(String string, String oldString,
			String newString) {
		if (string == null)
			return null;
		if (newString == null)
			return string;
		int i = 0;
		if ((i = string.indexOf(oldString, i)) >= 0) {
			char string2[] = string.toCharArray();
			char newString2[] = newString.toCharArray();
			int oLength = oldString.length();
			StringBuffer buf = new StringBuffer(string2.length);
			buf.append(string2, 0, i).append(newString2);
			i += oLength;
			int j;
			for (j = i; (i = string.indexOf(oldString, i)) > 0; j = i) {
				buf.append(string2, j, i - j).append(newString2);
				i += oLength;
			}

			buf.append(string2, j, string2.length - j);
			return buf.toString();
		} else {
			return string;
		}
	}

	public static final String replaceIgnoreCase(String line, String oldString,
			String newString) {
		if (line == null)
			return null;
		String lcLine = line.toLowerCase();
		String lcOldString = oldString.toLowerCase();
		int i = 0;
		if ((i = lcLine.indexOf(lcOldString, i)) >= 0) {
			char line2[] = line.toCharArray();
			char newString2[] = newString.toCharArray();
			int oLength = oldString.length();
			StringBuffer buf = new StringBuffer(line2.length);
			buf.append(line2, 0, i).append(newString2);
			i += oLength;
			int j;
			for (j = i; (i = lcLine.indexOf(lcOldString, i)) > 0; j = i) {
				buf.append(line2, j, i - j).append(newString2);
				i += oLength;
			}

			buf.append(line2, j, line2.length - j);
			return buf.toString();
		} else {
			return line;
		}
	}

	public static final String replaceIgnoreCase(String line, String oldString,
			String newString, int count[]) {
		if (line == null)
			return null;
		String lcLine = line.toLowerCase();
		String lcOldString = oldString.toLowerCase();
		int i = 0;
		if ((i = lcLine.indexOf(lcOldString, i)) >= 0) {
			int counter = 1;
			char line2[] = line.toCharArray();
			char newString2[] = newString.toCharArray();
			int oLength = oldString.length();
			StringBuffer buf = new StringBuffer(line2.length);
			buf.append(line2, 0, i).append(newString2);
			i += oLength;
			int j;
			for (j = i; (i = lcLine.indexOf(lcOldString, i)) > 0; j = i) {
				counter++;
				buf.append(line2, j, i - j).append(newString2);
				i += oLength;
			}

			buf.append(line2, j, line2.length - j);
			count[0] = counter;
			return buf.toString();
		} else {
			return line;
		}
	}

	public static final String replace(String line, String oldString,
			String newString, int count[]) {
		if (line == null)
			return null;
		int i = 0;
		if ((i = line.indexOf(oldString, i)) >= 0) {
			int counter = 1;
			char line2[] = line.toCharArray();
			char newString2[] = newString.toCharArray();
			int oLength = oldString.length();
			StringBuffer buf = new StringBuffer(line2.length);
			buf.append(line2, 0, i).append(newString2);
			i += oLength;
			int j;
			for (j = i; (i = line.indexOf(oldString, i)) > 0; j = i) {
				counter++;
				buf.append(line2, j, i - j).append(newString2);
				i += oLength;
			}

			buf.append(line2, j, line2.length - j);
			count[0] = counter;
			return buf.toString();
		} else {
			return line;
		}
	}

	public static final String stripTags(String in) {
		if (in == null)
			return null;
		int i = 0;
		int last = 0;
		char input[] = in.toCharArray();
		int len = input.length;
		StringBuffer out = new StringBuffer((int) ((double) len * 1.3D));
		for (; i < len; i++) {
			char ch = input[i];
			if (ch <= '>')
				if (ch == '<') {
					if (i + 3 < len && input[i + 1] == 'b'
							&& input[i + 2] == 'r' && input[i + 3] == '>') {
						i += 3;
					} else {
						if (i > last)
							out.append(input, last, i - last);
						last = i + 1;
					}
				} else if (ch == '>')
					last = i + 1;
		}

		if (last == 0)
			return in;
		if (i > last)
			out.append(input, last, i - last);
		return out.toString();
	}

	public static final String escapeHTMLTags(String in) {
		if (in == null)
			return null;
		int i = 0;
		int last = 0;
		char input[] = in.toCharArray();
		int len = input.length;
		StringBuffer out = new StringBuffer((int) ((double) len * 1.3D));
		for (; i < len; i++) {
			char ch = input[i];
			if (ch <= '>')
				if (ch == '<') {
					if (i > last)
						out.append(input, last, i - last);
					last = i + 1;
					out.append(LT_ENCODE);
				} else if (ch == '>') {
					if (i > last)
						out.append(input, last, i - last);
					last = i + 1;
					out.append(GT_ENCODE);
				}
		}

		if (last == 0)
			return in;
		if (i > last)
			out.append(input, last, i - last);
		return out.toString();
	}

	public static final synchronized String hash(String data) {
		if (digest == null)
			try {
				digest = MessageDigest.getInstance("MD5");
			} catch (NoSuchAlgorithmException nosuchalgorithmexception) {
			}
		try {
			digest.update(data.getBytes("utf-8"));
		} catch (UnsupportedEncodingException unsupportedencodingexception) {
		}
		return encodeHex(digest.digest());
	}

	public static final String encodeHex(byte bytes[]) {
		StringBuffer buf = new StringBuffer(bytes.length * 2);
		for (int i = 0; i < bytes.length; i++) {
			if ((bytes[i] & 0xff) < 16)
				buf.append("0");
			buf.append(Long.toString(bytes[i] & 0xff, 16));
		}

		return buf.toString();
	}

	public static final byte[] decodeHex(String hex) {
		char chars[] = hex.toCharArray();
		byte bytes[] = new byte[chars.length / 2];
		int byteCount = 0;
		for (int i = 0; i < chars.length; i += 2) {
			int newByte = 0;
			newByte |= hexCharToByte(chars[i]);
			newByte <<= 4;
			newByte |= hexCharToByte(chars[i + 1]);
			bytes[byteCount] = (byte) newByte;
			byteCount++;
		}

		return bytes;
	}

	private static final byte hexCharToByte(char ch) {
		switch (ch) {
		case 48: // '0'
			return 0;

		case 49: // '1'
			return 1;

		case 50: // '2'
			return 2;

		case 51: // '3'
			return 3;

		case 52: // '4'
			return 4;

		case 53: // '5'
			return 5;

		case 54: // '6'
			return 6;

		case 55: // '7'
			return 7;

		case 56: // '8'
			return 8;

		case 57: // '9'
			return 9;

		case 97: // 'a'
			return 10;

		case 98: // 'b'
			return 11;

		case 99: // 'c'
			return 12;

		case 100: // 'd'
			return 13;

		case 101: // 'e'
			return 14;

		case 102: // 'f'
			return 15;

		case 58: // ':'
		case 59: // ';'
		case 60: // '<'
		case 61: // '='
		case 62: // '>'
		case 63: // '?'
		case 64: // '@'
		case 65: // 'A'
		case 66: // 'B'
		case 67: // 'C'
		case 68: // 'D'
		case 69: // 'E'
		case 70: // 'F'
		case 71: // 'G'
		case 72: // 'H'
		case 73: // 'I'
		case 74: // 'J'
		case 75: // 'K'
		case 76: // 'L'
		case 77: // 'M'
		case 78: // 'N'
		case 79: // 'O'
		case 80: // 'P'
		case 81: // 'Q'
		case 82: // 'R'
		case 83: // 'S'
		case 84: // 'T'
		case 85: // 'U'
		case 86: // 'V'
		case 87: // 'W'
		case 88: // 'X'
		case 89: // 'Y'
		case 90: // 'Z'
		case 91: // '['
		case 92: // '\\'
		case 93: // ']'
		case 94: // '^'
		case 95: // '_'
		case 96: // '`'
		default:
			return 0;
		}
	}

	public static String encodeBase64(String data) {
		byte bytes[] = (byte[]) null;
		try {
			bytes = data.getBytes("ISO-8859-1");
		} catch (UnsupportedEncodingException unsupportedencodingexception) {
		}
		return encodeBase64(bytes);
	}

	public static String encodeBase64(byte data[]) {
		int len = data.length;
		StringBuffer ret = new StringBuffer((len / 3 + 1) * 4);
		for (int i = 0; i < len; i++) {
			int c = data[i] >> 2 & 0x3f;
			ret.append("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/"
					.charAt(c));
			c = data[i] << 4 & 0x3f;
			if (++i < len)
				c |= data[i] >> 4 & 0xf;
			ret.append("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/"
					.charAt(c));
			if (i < len) {
				c = data[i] << 2 & 0x3f;
				if (++i < len)
					c |= data[i] >> 6 & 0x3;
				ret.append("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/"
						.charAt(c));
			} else {
				i++;
				ret.append('=');
			}
			if (i < len) {
				c = data[i] & 0x3f;
				ret.append("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/"
						.charAt(c));
			} else {
				ret.append('=');
			}
		}

		return ret.toString();
	}

	public static String decodeBase64(String data) {
		byte bytes[] = (byte[]) null;
		try {
			bytes = data.getBytes("ISO-8859-1");
		} catch (UnsupportedEncodingException unsupportedencodingexception) {
		}
		return decodeBase64(bytes);
	}

	public static String decodeBase64(byte data[]) {
		int len = data.length;
		StringBuffer ret = new StringBuffer((len * 3) / 4);
		for (int i = 0; i < len; i++) {
			int c = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/"
					.indexOf(data[i]);
			i++;
			int c1 = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/"
					.indexOf(data[i]);
			c = c << 2 | c1 >> 4 & 0x3;
			ret.append((char) c);
			if (++i < len) {
				c = data[i];
				if (61 == c)
					break;
				c = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/"
						.indexOf(c);
				c1 = c1 << 4 & 0xf0 | c >> 2 & 0xf;
				ret.append((char) c1);
			}
			if (++i >= len)
				continue;
			c1 = data[i];
			if (61 == c1)
				break;
			c1 = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/"
					.indexOf(c1);
			c = c << 6 & 0xc0 | c1;
			ret.append((char) c);
		}

		return ret.toString();
	}

	public static final String[] toLowerCaseWordArray(String text) {
		if (text == null || text.length() == 0)
			return new String[0];
		ArrayList<String> wordList = new ArrayList<String>();
		BreakIterator boundary = BreakIterator.getWordInstance();
		boundary.setText(text);
		int start = 0;
		for (int end = boundary.next(); end != -1; end = boundary.next()) {
			String tmp = text.substring(start, end).trim();
			tmp = replace(tmp, "+", "");
			tmp = replace(tmp, "/", "");
			tmp = replace(tmp, "\\", "");
			tmp = replace(tmp, "#", "");
			tmp = replace(tmp, "*", "");
			tmp = replace(tmp, ")", "");
			tmp = replace(tmp, "(", "");
			tmp = replace(tmp, "&", "");
			if (tmp.length() > 0)
				wordList.add(tmp);
			start = end;
		}

		return (String[]) wordList.toArray(new String[wordList.size()]);
	}

	public static final String randomString(int length) {
		if (length < 1)
			return null;
		char randBuffer[] = new char[length];
		for (int i = 0; i < randBuffer.length; i++)
			randBuffer[i] = numbersAndLetters[randGen.nextInt(71)];

		return new String(randBuffer);
	}

	public static final String chopAtWord(String string, int length) {
		if (string == null || string.length() == 0)
			return string;
		char charArray[] = string.toCharArray();
		int sLength = string.length();
		if (length < sLength)
			sLength = length;
		for (int i = 0; i < sLength - 1; i++) {
			if (charArray[i] == '\r' && charArray[i + 1] == '\n')
				return string.substring(0, i + 1);
			if (charArray[i] == '\n')
				return string.substring(0, i);
		}

		if (charArray[sLength - 1] == '\n')
			return string.substring(0, sLength - 1);
		if (string.length() <= length)
			return string;
		for (int i = length - 1; i > 0; i--)
			if (charArray[i] == ' ')
				return string.substring(0, i).trim();

		return string.substring(0, length);
	}

	public static String chopAtWordsAround(String input, String wordList[],
			int numChars) {
		if (input == null || "".equals(input.trim()) || wordList == null
				|| wordList.length == 0 || numChars == 0)
			return null;
		String lc = input.toLowerCase();
		for (int i = 0; i < wordList.length; i++) {
			int pos = lc.indexOf(wordList[i]);
			if (pos > -1) {
				int beginIdx = pos - numChars;
				if (beginIdx < 0)
					beginIdx = 0;
				int endIdx = pos + numChars;
				if (endIdx > input.length() - 1)
					endIdx = input.length() - 1;
				char chars[];
				for (chars = input.toCharArray(); beginIdx > 0
						&& chars[beginIdx] != ' ' && chars[beginIdx] != '\n'
						&& chars[beginIdx] != '\r'; beginIdx--)
					;
				for (; endIdx < input.length() && chars[endIdx] != ' '
						&& chars[endIdx] != '\n' && chars[endIdx] != '\r'; endIdx++)
					;
				return input.substring(beginIdx, endIdx);
			}
		}

		return input.substring(0, input.length() >= 200 ? 200 : input.length());
	}

	public static String wordWrap(String input, int width, Locale locale) {
		if (input == null)
			return "";
		if (width < 5)
			return input;
		if (width >= input.length())
			return input;
		StringBuffer buf = new StringBuffer(input);
		boolean endOfLine = false;
		int lineStart = 0;
		for (int i = 0; i < buf.length(); i++) {
			if (buf.charAt(i) == '\n') {
				lineStart = i + 1;
				endOfLine = true;
			}
			if (i > (lineStart + width) - 1)
				if (!endOfLine) {
					int limit = i - lineStart - 1;
					BreakIterator breaks = BreakIterator
							.getLineInstance(locale);
					breaks.setText(buf.substring(lineStart, i));
					int end = breaks.last();
					if (end == limit + 1
							&& !Character.isWhitespace(buf.charAt(lineStart
									+ end)))
						end = breaks.preceding(end - 1);
					if (end != -1 && end == limit + 1) {
						buf.replace(lineStart + end, lineStart + end + 1, "\n");
						lineStart += end;
					} else if (end != -1 && end != 0) {
						buf.insert(lineStart + end, '\n');
						lineStart = lineStart + end + 1;
					} else {
						buf.insert(i, '\n');
						lineStart = i + 1;
					}
				} else {
					buf.insert(i, '\n');
					lineStart = i + 1;
					endOfLine = false;
				}
		}

		return buf.toString();
	}

	public static final String highlightWords(String string, String words[],
			String startHighlight, String endHighlight) {
		if (string == null || words == null || startHighlight == null
				|| endHighlight == null)
			return null;
		StringBuffer regexp = new StringBuffer();
		for (int x = 0; x < words.length; x++) {
			regexp.append(words[x]);
			if (x != words.length - 1)
				regexp.append("|");
		}

		regexp.insert(0, "s/\\b(");
		regexp.append(")\\b/");
		regexp.append(startHighlight);
		regexp.append("$1");
		regexp.append(endHighlight);
		regexp.append("/igm");
		return null;
	}

	public static final String escapeForXML(String string) {
		if (string == null)
			return null;
		int i = 0;
		int last = 0;
		char input[] = string.toCharArray();
		int len = input.length;
		StringBuffer out = new StringBuffer((int) ((double) len * 1.3D));
		for (; i < len; i++) {
			char ch = input[i];
			if (ch <= '>')
				if (ch == '<') {
					if (i > last)
						out.append(input, last, i - last);
					last = i + 1;
					out.append(LT_ENCODE);
				} else if (ch == '&') {
					if (i > last)
						out.append(input, last, i - last);
					last = i + 1;
					out.append(AMP_ENCODE);
				} else if (ch == '"') {
					if (i > last)
						out.append(input, last, i - last);
					last = i + 1;
					out.append(QUOTE_ENCODE);
				} else if (ch != '\n' && ch != '\r' && ch != '\t' && ch < ' ') {
					if (i > last)
						out.append(input, last, i - last);
					last = i + 1;
				}
		}

		if (last == 0)
			return string;
		if (i > last)
			out.append(input, last, i - last);
		return out.toString();
	}

	public static final String unescapeFromXML(String string) {
		string = replace(string, "&lt;", "<");
		string = replace(string, "&gt;", ">");
		string = replace(string, "&quot;", "\"");
		return replace(string, "&amp;", "&");
	}

	public static final String zeroPadString(String string, int length) {
		if (string == null || string.length() > length) {
			return string;
		} else {
			StringBuffer buf = new StringBuffer(length);
			buf.append(zeroArray, 0, length - string.length()).append(string);
			return buf.toString();
		}
	}

	public static final String firstWordToUpper(String text) {
		char f = text.charAt(0);
		String upper = ("" + f).toUpperCase();
		return upper + text.substring(1);
	}

	public static final String firstWordToLower(String text) {
		char f = text.charAt(0);
		String lower = ("" + f).toLowerCase();
		return lower + text.substring(1);
	}

	public static final String dateToMillis(Date date) {
		return zeroPadString(Long.toString(date.getTime()), 15);
	}
	
	private static final char QUOTE_ENCODE[] = "&quot;".toCharArray();
	private static final char AMP_ENCODE[] = "&amp;".toCharArray();
	private static final char LT_ENCODE[] = "&lt;".toCharArray();
	private static final char GT_ENCODE[] = "&gt;".toCharArray();
	private static MessageDigest digest = null;
	// private static final int fillchar = 61;
	// private static final String cvt =
	// "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";
	private static Random randGen = new Random();
	private static char numbersAndLetters[] = "0123456789abcdefghijklmnopqrstuvwxyz0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ"
			.toCharArray();
	private static final char zeroArray[] = "0000000000000000000000000000000000000000000000000000000000000000"
			.toCharArray();
}
