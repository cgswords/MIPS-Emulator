package util;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Scanner;

import data.Bit;

public class CodeParser {

	public static HashMap<String,MipsFunc> functions = new HashMap<String,MipsFunc>();

	public static HashMap<String,Integer> regs = new HashMap<String,Integer>();
	
	public static HashMap<String, Integer> labels;
	
	public static File inFile;

	public static void initFuncs() {

		functions.put("and", new MipsFunc("and","R",
				new Bit[]{new Bit(false),new Bit(false),new Bit(false),new Bit(false),new Bit(false),new Bit(false)},
				new Bit[]{new Bit(true),new Bit(false),new Bit(false),new Bit(true),new Bit(false),new Bit(false)}));

		functions.put("or", new MipsFunc("or","R",
				new Bit[]{new Bit(false),new Bit(false),new Bit(false),new Bit(false),new Bit(false),new Bit(false)},
				new Bit[]{new Bit(true),new Bit(false),new Bit(false),new Bit(true),new Bit(false),new Bit(true)}));

		functions.put("add", new MipsFunc("add","R",
				new Bit[]{new Bit(false),new Bit(false),new Bit(false),new Bit(false),new Bit(false),new Bit(false)},
				new Bit[]{new Bit(true),new Bit(false),new Bit(false),new Bit(false),new Bit(false),new Bit(false)}));

		functions.put("sub", new MipsFunc("sub","R",
				new Bit[]{new Bit(false),new Bit(false),new Bit(false),new Bit(false),new Bit(false),new Bit(false)},
				new Bit[]{new Bit(true),new Bit(false),new Bit(false),new Bit(false),new Bit(true),new Bit(false)}));

		functions.put("lw", new MipsFunc("lw","I",
				new Bit[]{new Bit(true),new Bit(false),new Bit(false),new Bit(false),new Bit(true),new Bit(true)},
				new Bit[]{new Bit(false),new Bit(false),new Bit(false),new Bit(false),new Bit(false),new Bit(false)}));

		functions.put("sw", new MipsFunc("sw","I",
				new Bit[]{new Bit(true),new Bit(false),new Bit(true),new Bit(false),new Bit(true),new Bit(true)},
				new Bit[]{new Bit(false),new Bit(false),new Bit(false),new Bit(false),new Bit(false),new Bit(false)}));

		functions.put("j", new MipsFunc("j","J",
				new Bit[]{new Bit(false),new Bit(false),new Bit(false),new Bit(false),new Bit(true),new Bit(false)},
				new Bit[]{new Bit(false),new Bit(false),new Bit(false),new Bit(false),new Bit(false),new Bit(false)}));

		functions.put("beq", new MipsFunc("beq","I",
				new Bit[]{new Bit(false),new Bit(false),new Bit(false),new Bit(true),new Bit(false),new Bit(false)},
				new Bit[]{new Bit(false),new Bit(false),new Bit(false),new Bit(false),new Bit(false),new Bit(false)}));

//		functions.put("bne", new MipsFunc("bne","I",
//				new Bit[]{new Bit(false),new Bit(false),new Bit(false),new Bit(true),new Bit(false),new Bit(true)},
//				new Bit[]{new Bit(false),new Bit(false),new Bit(false),new Bit(false),new Bit(false),new Bit(false)}));

		functions.put("addi", new MipsFunc("addi","I",
				new Bit[]{new Bit(false),new Bit(false),new Bit(true),new Bit(false),new Bit(false),new Bit(false)},
				new Bit[]{new Bit(false),new Bit(false),new Bit(false),new Bit(false),new Bit(false),new Bit(false)}));
		
		functions.put("slt", new MipsFunc("slt","R",
				new Bit[]{new Bit(false),new Bit(false),new Bit(false),new Bit(false),new Bit(false),new Bit(false)},
				new Bit[]{new Bit(true),new Bit(false),new Bit(true),new Bit(false),new Bit(true),new Bit(false)}));
	}

	public static void initRegs() {
		regs.put("$zero", 0);	regs.put("$at", 1);		regs.put("$v0", 2);		regs.put("$v1", 3);
		regs.put("$a0", 4);		regs.put("$a1", 5);		regs.put("$a2", 6);		regs.put("$a3", 7);
		regs.put("$t0", 8);		regs.put("$t1", 9);		regs.put("$t2", 10);	regs.put("$t3", 11);
		regs.put("$t4", 12);	regs.put("$t5", 13);	regs.put("$t6", 14);	regs.put("$t7", 15);
		regs.put("$s0", 16);	regs.put("$s1", 17);	regs.put("$s2", 18);	regs.put("$s3", 19);
		regs.put("$s4", 20);	regs.put("$s5", 21);	regs.put("$s6", 22);	regs.put("$s7", 23);
		regs.put("$t8", 24);	regs.put("$t9", 25);	regs.put("$k0", 26);	regs.put("$k1", 27);
		regs.put("$gp", 28);	regs.put("$sp", 29);	regs.put("$fp", 30);	regs.put("$ra", 31);
	}

	public static Bit[] parseLine(String input, int lineNum) {
		Bit[] ret = new Bit[32];

		String[] tok = input.split(", *| +");

		MipsFunc func = parseFunc(tok[0]);

		String type = func.getFType();
		Bit[][] operand = new Bit[3][];

		if (type.equals("R") || type.equals("S")) {
			operand[0] = parseOp(tok[1]);
			operand[1] = parseOp(tok[2]);
			operand[2] = parseOp(tok[3]);

		} else if (type.equals("I")) {
			String funcName = func.getName();
			if (funcName.equals("lw") || funcName.equals("sw")) {
				operand[0] = parseOp(tok[1]);
				operand[1] = parseOp(tok[2].substring(tok[2].indexOf("(")+1, (tok[2].length())-1));
				operand[2] = parseNum(tok[2].substring(0, tok[2].indexOf("(")));
			} else if (funcName.equals("beq") || funcName.equals("bne")){
				operand[0] = parseOp(tok[1]);
				operand[1] = parseOp(tok[2]);
				operand[2] = parseLabel(tok[3], lineNum, 16);
			} else if (funcName.equals("addi")) {
				operand[0] = parseOp(tok[1]);
				operand[1] = parseOp(tok[2]);
				operand[2] = BitConverter.signedConvert(Integer.valueOf(tok[3]),16);
			}
		} else {
			operand[0] = parseLabel(tok[1], lineNum, 26);
		}

		Bit[] op = func.getOp();
		Bit[] fun = func.getFunc();
		
		if (type.equals("R")) {
			ret = copyTo(ret,0,op);
			ret = copyTo(ret,6,operand[1]);
			ret = copyTo(ret,11,operand[2]);
			ret = copyTo(ret,16,operand[0]);
			ret = copyTo(ret,21,new Bit[]{new Bit(false),new Bit(false),new Bit(false),new Bit(false),new Bit(false)});
			ret = copyTo(ret,26,fun);

		} else if (type.equals("S")) {
			ret = copyTo(ret,0,op);
			ret = copyTo(ret,6,operand[1]);
			ret = copyTo(ret,11,operand[0]);
			ret = copyTo(ret,16,new Bit[]{new Bit(false),new Bit(false),new Bit(false),new Bit(false),new Bit(false)});
			ret = copyTo(ret,21,operand[2]);
			ret = copyTo(ret,26,fun);

		} else if (type.equals("I")) {
			
			ret = copyTo(ret,0,op);
			ret = copyTo(ret,6,operand[1]);
			ret = copyTo(ret,11,operand[0]);
			ret = copyTo(ret,16,operand[2]);

		} else {
			ret = copyTo(ret,0,op);
			//System.out.println("J TYPE JUMP SIZE IS " + BitPrinter.toBinary32(operand[0]));
			ret = copyTo(ret,6,operand[0]);
		}
		
		return ret;
	}


	public static Bit[][] parseFile(File in) {
		Bit[][] ret;
		inFile = in;
		Scanner sc;
		labels = new HashMap<String, Integer>();
		findLabels();
		
		int i = 0;
		
		try {
			sc = new Scanner(inFile);
			System.out.println("Opened file");
		} catch (Exception e) {
			System.out.println("Failed");
			return null;
		}
		
		while(sc.hasNext()) {
			String line = sc.nextLine();
			i++;
		}
		
		ret = new Bit[i][32];

		try {
			sc = new Scanner(inFile);
			// System.out.println("Opened file");
		} catch (Exception e) {
			// System.out.println("Failed");
			return null;
		}
		
		i = 0;
		
		while(sc.hasNext()) {
			String line = sc.nextLine();
			String[] tok = line.split(": +|: +\t+|:\t+|:|\t+");
			// System.out.println(tok[0] + "||" + tok[1]);
			ret[i] = parseLine(tok[1], i); 
			System.out.println(tok[1] + " is " + BitPrinter.toHex8(ret[i]));
			i++;
		}
		
		return ret;
	}
	
	public static void findLabels() {
		int i = 0;
		Scanner sc;
		
		try {
			sc = new Scanner(inFile);
		} catch (Exception e) {
			return;
		}
		
		while(sc.hasNext()) {
			String line = sc.nextLine();
			if (line.contains(":")) {
				String[] tok = line.split(":");
				System.out.println("Label " + tok[0]+ " found at " + i);
				labels.put(tok[0],i);
			}
			i++;
		}
	}
	
	public static Bit[] parseLabel(String s, int lineNum, int resultLength) {
		try {
			return BitConverter.signedConvert(Integer.valueOf(s),resultLength);
		} catch (Exception e){
			int labelLine = labels.get(s), offset = 0;
			//if (labelLine > lineNum) 
				offset = (labelLine - lineNum);
			//else offset = (lineNum - labelLine)*4;
			//System.out.println("Offset for instruction at line " + lineNum + " is " + offset);
			Bit[] tmp = BitConverter.signedConvert(offset,resultLength);
			//System.out.println("In bits, that's " + BitPrinter.toBinary32(tmp));
			return tmp;
		}
	}

	public static MipsFunc parseFunc(String s) {
		return functions.get(s);
	}
	
	public static Bit[] parseOp(String s) {
		try {
			int ret = Integer.parseInt(s.substring(1, s.length()));
			return BitConverter.convert(ret, 5);
		} catch (Exception e) {
			return BitConverter.convert(regs.get(s), 5);
		}
	}
	
	public static Bit[] parseNum(String s) {
		return BitConverter.convert(Integer.parseInt(s), 16);
	}

	public static Bit[] copyTo(Bit[] in, int index, Bit[] copy) {
		// System.out.println(">>" + copy.length);
		for(int i = 0; i < copy.length; i++) {
			in[i+index] = copy[i];
		}
		return in;
	}
	
	public static class MipsFunc {
		Bit[] opCode;
		Bit[] funCode;
		String mips;
		String type;

		public MipsFunc(String s, String t, Bit[] op, Bit[] func) {
			mips = s;
			type = t;
			opCode = op;
			funCode = func;
		}

		public boolean contains (String s) { return s.contains(mips); }

		public String getName() {return mips; }
		
		public Bit[] getOp() { return opCode; }

		public Bit[] getFunc() { return funCode; }

		public String getFType() { return type; }
	}
}
