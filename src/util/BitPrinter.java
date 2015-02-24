package util;

import data.Bit;

public class BitPrinter {
	
	public static String toHex(Bit[] in) {
		return "0x" + Integer.toHexString(BitConverter.convert(in));
	}
	
	public static String toHex8(Bit[] in) {
		String ret = "";
		for(int i = 0; i < in.length/4; i++) {
//			System.out.println(BitPrinter.toBinary32(in));
//			System.out.print((0+i*4) + "|" + in[0+i*4] + in[1+i*4] + in[2+i*4] + in[3+i*4] + "|");
			ret = ret + Integer.toHexString(BitConverter.convert(new Bit[]{in[0+i*4], in[1+i*4], in[2+i*4], in[3+i*4]}));
		}
		while (ret.length() < 8) ret = "0" + ret;
		return "0x" + ret;
	}
	
	public static String toBinary(Bit[] in) {
		return Integer.toBinaryString(BitConverter.convert(in));
	}
	
	public static String toBinary(Bit[] in, int len) {
		String ret = "";
		
		for(int i = in.length-1; i > -1; i--) {
			if (in[i].value()) ret = "1" + ret;
			else ret = "0" + ret;
		}

		if (in[0].value())
			while (ret.length() < len) ret = "1" + ret;
		else
			while (ret.length() < len) ret = "0" + ret;
		return ret;
	}
	
	public static String toBinary32(Bit[] in) {
		String ret = "";
		
		for(int i = in.length-1; i > -1; i--) {
			if (in[i].value()) ret = "1" + ret;
			else ret = "0" + ret;
		}

		if (in[0].value())
			while (ret.length() < 32) ret = "1" + ret;
		else
			while (ret.length() < 32) ret = "0" + ret;
		return ret;
	}
}
