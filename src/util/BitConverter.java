package util;

import data.Bit;

public class BitConverter {
	
	public static int convert(Bit[] in) {
		int ret = 0;
		
		for(int i = (in.length-1), j = 0; i > -1 && j < in.length; i--, j++) {
			// System.out.println(i + " " + j);
			if (in[j].value())
			ret += Math.pow(2, i);
		}
		return ret;
	}
	
	public static int signedConvert(Bit[] in) {
		int ret = 0;
		
		if (in[0].value()) return negConvert(in);
		
		for(int i = (in.length-1), j = 0; i > -1 && j < in.length; i--, j++) {
			// System.out.println(i + " " + j);
			if (in[j].value())
			ret += Math.pow(2, i);
		}
		return ret;
	}
	
	private static int negConvert(Bit[] in) {
		int ret = Integer.MIN_VALUE;
		for(int i = (in.length-1), j = 1; i > -1 && j < in.length; i--, j++) {
			// System.out.println(i + " " + j);
			if (in[j].value())
			ret += Math.pow(2, i);
		}
		return ret;
	}
	
	public static Bit[] signedConvert(int in, int len) {
		Bit[] ret = new Bit[len];
		String tmp = Integer.toBinaryString(in);
		if (len < tmp.length()) tmp = tmp.substring(tmp.length()-len);
		
		if (in < 0) while (tmp.length() < len) tmp = "1" + tmp;
		else while (tmp.length() < len) tmp = "0" + tmp;
		
		for(int i = 0; i < len; i++) {
			if (tmp.charAt(i) == '1')
				ret[i] = new Bit(true);
			else ret[i] = new Bit(false);
		}
		
		return ret;
	}
	
	public static Bit[] convert(int in, int len) {
		Bit[] ret = new Bit[len];
		String tmp = Integer.toBinaryString(in);
		while (tmp.length() < len) tmp = "0" + tmp;
		
		for(int i = 0; i < len; i++) {
			if (tmp.charAt(i) == '1')
				ret[i] = new Bit(true);
			else ret[i] = new Bit(false);
		}
		
		return ret;
	}
	
	public static Bit[] signedConvert(int in) {
		return signedConvert(in, 32);
	}
	
	public static Bit[] convert(int in) {
		return convert(in, 32);
	}
}
