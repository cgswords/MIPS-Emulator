import java.io.File;

import util.BitConverter;
import util.BitPrinter;
import util.CodeParser;
import circuit.ALU;
import circuit.BitALU;
import circuit.BitAdder;
import circuit.Control;
import circuit.HalfBitAdder;
import circuit.Multiplexor;
import circuit.BitUtil;
import data.Bit;
import data.Memory;
import data.Registers;


public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		utilTest();
		adderTest();
		multiTest();
		ALUTest();
		RegTest();
		MemTest();
		ParseTest();
		ControlTest();
		
		// GUIMain main = new GUIMain();
		// main.show();
	}
	
	public static void utilTest() {
		System.out.println("\n+---------------------------+");
		System.out.println("+------ Util Testing -------+");
		System.out.println("+---------------------------+");
		Bit[] in = new Bit[5];
		int a = 31, b = 5, c = -10;
		
		for(int i = 0; i < 5; i++) {
			in[i] = new Bit(true);
		}
		
		System.out.println(BitConverter.convert(in) + " should be 31");
		
		in[2].setValue(false);
		
		System.out.println(BitConverter.convert(in)  + " should be 27");
		
		in[1].setValue(false);
		in[3].setValue(false);
		
		System.out.println(BitConverter.convert(in)  + " should be 17");
		
		in = new Bit[32];
		for(int i = 0; i < 32; i++) {
			in[i] = new Bit(true);
		}
		// Should Actually be a Negative Number - FIX THIS
		System.out.println(BitConverter.convert(in)  + " should be Max Int " + Integer.MAX_VALUE);
		// System.out.println(BitConverter.convert(in)  + " should be Min Int " + (Integer.MIN_VALUE-1) + " or " + Integer.toBinaryString(Integer.MIN_VALUE-1));
		
		Bit[] aBit = BitConverter.convert(a);
		Bit[] bBit = BitConverter.convert(b);
		Bit[] cBit = BitConverter.convert(c);
		
		System.out.println(BitPrinter.toBinary32(aBit) + "\n" + BitPrinter.toBinary32(bBit) + "\n" + BitPrinter.toBinary32(cBit));
		
		System.out.println(BitPrinter.toBinary32(BitConverter.signedConvert(-10))  + " should be " + Integer.toBinaryString(-10));
	}

	public static void adderTest() {
		System.out.println("\n+---------------------------+");
		System.out.println("+------ Adder Testing ------+");
		System.out.println("+---------------------------+");
		System.out.println("Truth Table for HB Adder:\na\tb\tfunc\treal");
		System.out.println("0\t0\t " + HalfBitAdder.HalfAdd(new Bit(false), new Bit(false)) + "\t 0");
		System.out.println("0\t1\t " + HalfBitAdder.HalfAdd(new Bit(false), new Bit(true)) + "\t 1");
		System.out.println("1\t0\t " + HalfBitAdder.HalfAdd(new Bit(true), new Bit(false)) + "\t 1");
		System.out.println("1\t1\t " + HalfBitAdder.HalfAdd(new Bit(true), new Bit(true)) + "\t 0");
		System.out.println();
		System.out.println("Truth Table for Bit Adder:\n" +
							"a\tb\tc\tfcout\tfsum\trcout\trsum");
		System.out.println("0\t0\t0\t " + BitAdder.carryout(new Bit(false), new Bit(false), new Bit(false)) + "\t " + BitAdder.add(new Bit(false), new Bit(false), new Bit(false)) + "\t 0\t 0");
		System.out.println("0\t0\t1\t " + BitAdder.carryout(new Bit(false), new Bit(false), new Bit(true)) + "\t " + BitAdder.add(new Bit(false), new Bit(false), new Bit(true)) + "\t 0\t 1");
		System.out.println("0\t1\t0\t " + BitAdder.carryout(new Bit(false), new Bit(true), new Bit(false)) + "\t " + BitAdder.add(new Bit(false), new Bit(true), new Bit(false)) + "\t 0\t 1");
		System.out.println("0\t1\t1\t " + BitAdder.carryout(new Bit(false), new Bit(true), new Bit(true)) + "\t " + BitAdder.add(new Bit(false), new Bit(true), new Bit(true)) + "\t 1\t 0");
		System.out.println("1\t0\t0\t " + BitAdder.carryout(new Bit(true), new Bit(false), new Bit(false)) + "\t " + BitAdder.add(new Bit(true), new Bit(false), new Bit(false)) + "\t 0\t 1");
		System.out.println("1\t0\t1\t " + BitAdder.carryout(new Bit(true), new Bit(false), new Bit(true)) + "\t " + BitAdder.add(new Bit(true), new Bit(false), new Bit(true)) + "\t 1\t 0");
		System.out.println("1\t1\t0\t " + BitAdder.carryout(new Bit(true), new Bit(true), new Bit(false)) + "\t " + BitAdder.add(new Bit(true), new Bit(true), new Bit(false)) + "\t 1\t 0");
		System.out.println("1\t1\t1\t " + BitAdder.carryout(new Bit(true), new Bit(true), new Bit(true)) + "\t " + BitAdder.add(new Bit(true), new Bit(true), new Bit(true)) + "\t 1\t 1");
		
	
	}

	public static void multiTest() {
		System.out.println("\n+---------------------------+");
		System.out.println("+--- Multiplexor Testing ---+");
		System.out.println("+---------------------------+");

		Bit[] arr = new Bit[32];
		Bit[] brr = new Bit[32];
		Bit[] crr = new Bit[32];
		for (int i = 0; i < 32; i++) {
			arr[i] = new Bit(true);
			brr[i] = new Bit(false);
			crr[i] = new Bit((i%2 == 1));
		}
				
		Bit[][] lol = {arr,brr,crr};
		Bit[] choice = {new Bit(false), new Bit(false), new Bit(false)};
				
		System.out.println("Picking all true string: " + BitPrinter.toBinary32(Multiplexor.select(lol, choice)));
		choice[2].setValue(true);
		System.out.println("Picking all false string: " + BitPrinter.toBinary32(Multiplexor.select(lol, choice)));
		choice[1].setValue(true);
		choice[2].setValue(false);
		System.out.println("Picking alternating string: " + BitPrinter.toBinary32(Multiplexor.select(lol, choice)));
	}
	
	public static void ALUTest() {
		System.out.println("\n+---------------------------+");
		System.out.println("+--- One-Bit ALU Testing ---+");
		System.out.println("+---------------------------+");
		
		Bit a = new Bit(true), b = new Bit(false), c = new Bit(true);
		Bit[] op = {new Bit(false), new Bit(false), new Bit(false)};
		Bit[] arr = new Bit[32];
		Bit[] brr = new Bit[32];
		Bit[] crr = new Bit[32];
		Bit[] drr = new Bit[32];
		for (int i = 0; i < 32; i++) {
			arr[i] = new Bit(true);
			brr[i] = new Bit(false);
			crr[i] = new Bit((i%2 == 1));
			drr[i] = new Bit(false);
		}
		drr[31].setValue(true);
		drr[29].setValue(true);
		
		System.out.println("Func\tA\tB\tC\tBinv\tALUOp\tResult\tReal");
		System.out.println("And\t"+a+"\t"+b+"\t"+c+"\t0\t " + op[2] + op[1] + op[0] + "\t  "+ 
							BitALU.operate(a, b, c, new Bit(false), new Bit(false), op )[0] + "\t 0");
		
		op[2].setValue(true);
		System.out.println("Or\t"+a+"\t"+b+"\t"+c+"\t0\t " + op[2] + op[1] + op[0] + "\t  "+ 
				BitALU.operate(a, b, c, new Bit(false), new Bit(false), op )[0] + "\t 1");
		
		op[2].setValue(false);
		op[1].setValue(true);
		System.out.println("Add\t"+a+"\t"+b+"\t"+c+"\t0\t " + op[2] + op[1] + op[0] + "\t "+ 
				BitALU.operate(a, b, c, new Bit(false), new Bit(false), op )[0] + "|"+BitALU.operate(a, b, c, new Bit(false), new Bit(false), op )[1]+"\t 0|1");
		
		
		System.out.println("\n+---------------------------+");
		System.out.println("+--- 32-Bit ALU Testing ----+");
		System.out.println("+---------------------------+");
		System.out.println("Func Op\t\t\tA\t\t\t\t\tB\t\t\t\tResult\t\t\t\t\tReal");
		
		op[1].setValue(false);
		Bit[] and = ALU.compute(arr, crr, op);
		System.out.println("And:"+op[0]+op[1]+op[2]+"\t"+BitPrinter.toBinary32(arr)+"\t"+BitPrinter.toBinary32(crr)+"\t"+
						BitPrinter.toBinary32(and) + "\t" + BitPrinter.toBinary32(crr));
		
		and = ALU.compute(BitConverter.convert(15), BitConverter.convert(16), op);
		System.out.println("Or :"+op[0]+op[1]+op[2]+"\t"+BitPrinter.toBinary32(BitConverter.convert(15))+"\t"+BitPrinter.toBinary32(BitConverter.convert(16))+"\t"+
						BitPrinter.toBinary32(and) + "\t" + BitPrinter.toBinary32(BitConverter.convert(0)));
		
		op[2].setValue(true);
		Bit[] or = ALU.compute(arr, crr, op);
		System.out.println("Or :"+op[0]+op[1]+op[2]+"\t"+BitPrinter.toBinary32(arr)+"\t"+BitPrinter.toBinary32(crr)+"\t"+
						BitPrinter.toBinary32(or) + "\t" + BitPrinter.toBinary32(arr));
		
		or = ALU.compute(BitConverter.convert(15), BitConverter.convert(16), op);
		System.out.println("Or :"+op[0]+op[1]+op[2]+"\t"+BitPrinter.toBinary32(BitConverter.convert(15))+"\t"+BitPrinter.toBinary32(BitConverter.convert(16))+"\t"+
						BitPrinter.toBinary32(or) + "\t" + BitPrinter.toBinary32(BitConverter.convert(31)));
		
		op[0].setValue(false);
		op[1].setValue(true);
		op[2].setValue(false);
		Bit[] sum = ALU.compute(brr, crr, op);
		System.out.println("Sum:"+op[0]+op[1]+op[2]+"\t"+BitPrinter.toBinary32(brr)+"\t"+BitPrinter.toBinary32(crr)+"\t"+
						BitPrinter.toBinary32(sum) + "\t" + BitPrinter.toBinary32(crr));
		

		sum = ALU.compute(drr, drr, op);
		System.out.println("Sum:"+op[0]+op[1]+op[2]+"\t"+BitPrinter.toBinary32(drr)+"\t"+BitPrinter.toBinary32(drr)+"\t"+
						BitPrinter.toBinary32(sum) + "\t" + BitPrinter.toBinary32(BitConverter.convert(10)));
		
		sum = ALU.compute(BitConverter.convert(-5), drr, op);
		System.out.println("Sum:"+op[0]+op[1]+op[2]+"\t"+BitPrinter.toBinary32(drr)+"\t"+BitPrinter.toBinary32(BitConverter.convert(-5))
						+"\t"+BitPrinter.toBinary32(sum) + "\t" + BitPrinter.toBinary32(BitConverter.convert(0)));
		
		sum = ALU.compute(BitConverter.convert(26), BitConverter.convert(50), op);
		System.out.println("Sub:"+op[0]+op[1]+op[2]+"\t"+BitPrinter.toBinary32(BitConverter.convert(26))+"\t"+BitPrinter.toBinary32(BitConverter.convert(50))
						+"\t"+BitPrinter.toBinary32(sum) + "\t" + BitPrinter.toBinary32(BitConverter.convert(76)));
		
		op[0].setValue(true);
		sum = ALU.compute(BitConverter.convert(10), BitConverter.convert(5), op);
		System.out.println("Sub:"+op[0]+op[1]+op[2]+"\t"+BitPrinter.toBinary32(BitConverter.convert(10))+"\t"+BitPrinter.toBinary32(BitConverter.convert(5))
						+"\t"+BitPrinter.toBinary32(sum) + "\t" + BitPrinter.toBinary32(BitConverter.convert(5)));
		
		sum = ALU.compute(BitConverter.convert(5), BitConverter.convert(10), op);
		System.out.println("Sub:"+op[0]+op[1]+op[2]+"\t"+BitPrinter.toBinary32(BitConverter.convert(10))+"\t"+BitPrinter.toBinary32(BitConverter.convert(5))
						+"\t"+BitPrinter.toBinary32(sum) + "\t" + BitPrinter.toBinary32(BitConverter.convert(-5)));
		
		op[2].setValue(true);
		Bit[] slt = ALU.compute(BitConverter.convert(10), BitConverter.convert(5), op);
		System.out.println("Slt:"+op[0]+op[1]+op[2]+"\t"+BitPrinter.toBinary32(BitConverter.convert(10))+"\t"+BitPrinter.toBinary32(BitConverter.convert(5))
						+"\t"+BitPrinter.toBinary32(slt) + "\t" + BitPrinter.toBinary32(BitConverter.convert(0)));
		
		slt = ALU.compute(BitConverter.convert(10), BitConverter.convert(10), op);
		System.out.println("Slt:"+op[0]+op[1]+op[2]+"\t"+BitPrinter.toBinary32(BitConverter.convert(10))+"\t"+BitPrinter.toBinary32(BitConverter.convert(10))
						+"\t"+BitPrinter.toBinary32(slt) + "\t" + BitPrinter.toBinary32(BitConverter.convert(0)));
		
		slt = ALU.compute(BitConverter.convert(10), BitConverter.convert(15), op);
		System.out.println("Slt:"+op[0]+op[1]+op[2]+"\t"+BitPrinter.toBinary32(BitConverter.convert(10))+"\t"+BitPrinter.toBinary32(BitConverter.convert(15))
						+"\t"+BitPrinter.toBinary32(slt) + "\t" + BitPrinter.toBinary32(BitConverter.convert(1)));
		
		slt = ALU.compute(BitConverter.convert(10), BitConverter.convert(150), op);
		System.out.println("Slt:"+op[0]+op[1]+op[2]+"\t"+BitPrinter.toBinary32(BitConverter.convert(10))+"\t"+BitPrinter.toBinary32(BitConverter.convert(150))
						+"\t"+BitPrinter.toBinary32(slt) + "\t" + BitPrinter.toBinary32(BitConverter.convert(1)));
	}

	public static void RegTest() {
		System.out.println("\n+---------------------------+");
		System.out.println("+----- Register Testing ----+");
		System.out.println("+---------------------------+");
		Registers regs = new Registers();
		regs.printRegsHex();
		System.out.println();
		Bit[] arr = new Bit[32];
		Bit[] brr = new Bit[32];
		Bit[] crr = new Bit[32];
		Bit[] drr = new Bit[32];
		for (int i = 0; i < 32; i++) {
			arr[i] = new Bit(true);
			brr[i] = new Bit(false);
			crr[i] = new Bit((i%2 == 1));
			drr[i] = new Bit(false);
		}
		
		regs.setReg(5, arr);
		
		regs.setReg(7, brr);
		
		regs.setReg(12, crr);
		
		regs.setReg(18, drr);
		
		regs.printRegsHex();
		System.out.println();
		drr = regs.getReg(12);
		
		regs.setReg(18, drr);
		
		regs.printRegsHex();
		
	}

	public static void MemTest() {
		System.out.println("\n+---------------------------+");
		System.out.println("+------ Memory Testing -----+");
		System.out.println("+---------------------------+");
		Memory mem = new Memory();
		mem.printMemHex(0,32);
		
		System.out.println();
		Bit[] arr = new Bit[32];
		Bit[] brr = new Bit[32];
		Bit[] crr = new Bit[32];
		Bit[] drr = new Bit[32];
		for (int i = 0; i < 32; i++) {
			arr[i] = new Bit(true);
			brr[i] = new Bit(false);
			crr[i] = new Bit((i%2 == 1));
			drr[i] = new Bit(false);
		}
		
		mem.setMem(0, arr);
		
		mem.setMem(4, brr);
		
		mem.setMem(12, crr);
		
		mem.setMem(16, drr);
		
		// mem.printMemHex(0,32);
		mem.printMem(0,32);
		System.out.println();
		drr = mem.getMem(12);
		
		mem.setMem(24, drr);
		
		mem.printMemHex(0,32);
	}

	public static void ParseTest() {
		System.out.println("\n+---------------------------+");
		System.out.println("+------ Parser Testing -----+");
		System.out.println("+---------------------------+");
		
		CodeParser.initFuncs();
		CodeParser.initRegs();
		
		System.out.println(BitPrinter.toBinary32(CodeParser.parseLine("sw $s2, 32($t1)",0)) + " should be 10101101001100100000000000100000");
		System.out.println(BitPrinter.toHex8(CodeParser.parseLine("lw $t0,0($s2)",0)) + " should be 10001110010010000000000000000000");
		System.out.println(BitPrinter.toBinary32(CodeParser.parseLine("sub $s0, $t1, $t2",0)) + " should be 00000001001010101000000000100010");
		System.out.println(BitPrinter.toBinary32(CodeParser.parseLine("and $t6,$t1,$s2",0)) + " should be 00000001001100100111000000100100");
		System.out.println(BitPrinter.toBinary32(CodeParser.parseLine("add $t1,$t1,$t0",0)) + " should be 00000001001010010100000000100000");
		
		Bit[][] input = CodeParser.parseFile(new File("mipsin"));

		System.out.println();
		for(int i = 0; i < input.length; i++) {
			System.out.println(BitPrinter.toBinary32(input[i]));
		}
	}
	
	public static void ControlTest() {
		System.out.println("\n+---------------------------+");
		System.out.println("+----- Control Testing -----+");
		System.out.println("+---------------------------+");
		
		CodeParser.initFuncs();
		CodeParser.initRegs();
		
		Control.ALUControl(new Bit[]{new Bit(false),new Bit(false)},
				new Bit[]{new Bit(false),new Bit(false),new Bit(false),new Bit(false),new Bit(false),new Bit(false)});
		
		Control.ALUControl(new Bit[]{new Bit(false),new Bit(true)},
				new Bit[]{new Bit(false),new Bit(false),new Bit(false),new Bit(false),new Bit(false),new Bit(false)});
		
		Control.ALUControl(new Bit[]{new Bit(true),new Bit(false)},
				new Bit[]{new Bit(false),new Bit(false),new Bit(false),new Bit(false),new Bit(false),new Bit(false)});
		
		Control.ALUControl(new Bit[]{new Bit(true),new Bit(false)},
				new Bit[]{new Bit(false),new Bit(false),new Bit(false),new Bit(false),new Bit(true),new Bit(false)});
		
		Control.ALUControl(new Bit[]{new Bit(true),new Bit(false)},
				new Bit[]{new Bit(false),new Bit(false),new Bit(false),new Bit(true),new Bit(false),new Bit(false)});
		
		Control.ALUControl(new Bit[]{new Bit(true),new Bit(false)},
				new Bit[]{new Bit(false),new Bit(false),new Bit(false),new Bit(true),new Bit(false),new Bit(true)});
		
		Control.ALUControl(new Bit[]{new Bit(true),new Bit(false)},
				new Bit[]{new Bit(false),new Bit(false),new Bit(true),new Bit(false),new Bit(true),new Bit(false)});
		
		Bit[] ins = CodeParser.parseLine("sw $s2, 32($t1)",0);
		Bit[] opTest = Control.getControls(ins);
		
		System.out.println(BitPrinter.toBinary(opTest,9));
		
		ins = CodeParser.parseLine("lw $s2, 32($t1)",0);
		opTest = Control.getControls(ins);
		
		System.out.println(BitPrinter.toBinary(opTest,9));
		
		ins = CodeParser.parseLine("add $s2, $t1, $s3",0);
		opTest = Control.getControls(ins);
		
		System.out.println(BitPrinter.toBinary(opTest,9));
		
		ins = CodeParser.parseLine("beq $t1,$t2,15",0);
		opTest = Control.getControls(ins);
		
		System.out.println(BitPrinter.toBinary(opTest,9));
		
		System.out.println("\n+---------------------------+");
		System.out.println("+----- SignExt Testing -----+");
		System.out.println("+---------------------------+");
		
		System.out.println(BitPrinter.toBinary32(BitUtil.signExt(new Bit[]{new Bit(false),new Bit(false),new Bit(false),new Bit(false),
														new Bit(false),new Bit(false),new Bit(false),new Bit(false),
														new Bit(false),new Bit(false),new Bit(false),new Bit(false),
														new Bit(false),new Bit(false),new Bit(false),new Bit(false)}
		)));
		
		System.out.println(BitPrinter.toBinary32(BitUtil.signExt(new Bit[]{new Bit(true),new Bit(false),new Bit(true),new Bit(false),
				new Bit(true),new Bit(false),new Bit(false),new Bit(true),
				new Bit(false),new Bit(true),new Bit(false),new Bit(false),
				new Bit(false),new Bit(false),new Bit(false),new Bit(false)}
)));
		
		System.out.println("\n+---------------------------+");
		System.out.println("+------- SLL Testing -------+");
		System.out.println("+---------------------------+");
		
		System.out.print(BitPrinter.toBinary32(BitConverter.convert(5)) + " << 2 = ");
		System.out.println(BitPrinter.toBinary32(BitUtil.sll(BitConverter.convert(5),2)));
	}
}

