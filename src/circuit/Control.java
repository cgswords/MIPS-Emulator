package circuit;

import util.BitPrinter;
import gate.AndGate;
import gate.NotGate;
import gate.OrGate;
import data.Bit;

public class Control {
	// 0 1 0 1 2 3 4 5
	// 1 x x x 1 0 1 0
	
	public static Bit[] ALUControl(Bit[] ALUOp, Bit[] func) {
		Bit[] ret = new Bit[3];

		ret[2] = AndGate.and(OrGate.or(func[5], func[2]),ALUOp[0]);
		    
		ret[1] = OrGate.or(NotGate.not(func[5-2]),NotGate.not(ALUOp[0]));
		
		ret[0] = OrGate.or(ALUOp[1],AndGate.and(ALUOp[0],func[5-1]));
		
		// System.out.println(BitPrinter.toBinary(ret,3));
		
		return ret;
	}
	
	public static Bit[] getControls(Bit[] inst) {
		Bit[] op = new Bit[6];	
		Bit[] ret = new Bit[10];
		Bit[] in = new Bit[7];
		Bit rtype, lw, sw, beq, addi, jump, bne;
		
		op[0] = inst[0];
		op[1] = inst[1];
		op[2] = inst[2];
		op[3] = inst[3];
		op[4] = inst[4];
		op[5] = inst[5];
		
		rtype = AndGate.and(NotGate.not(op[0]),NotGate.not(op[1]),NotGate.not(op[2]),NotGate.not(op[3]),NotGate.not(op[4]),NotGate.not(op[5]));
		
		lw = AndGate.and(op[0],NotGate.not(op[1]),NotGate.not(op[2]),NotGate.not(op[3]),op[4],op[5]); 

		sw = AndGate.and(op[0],NotGate.not(op[1]),op[2],NotGate.not(op[3]),op[4],op[5]);
		
		beq = AndGate.and(NotGate.not(op[0]),NotGate.not(op[1]),NotGate.not(op[2]),op[3],NotGate.not(op[4]),NotGate.not(op[5]));
		
		addi = AndGate.and(NotGate.not(op[0]),NotGate.not(op[1]),op[2],NotGate.not(op[3]),NotGate.not(op[4]),NotGate.not(op[5]));
		
		jump = AndGate.and(NotGate.not(op[0]),NotGate.not(op[1]),NotGate.not(op[2]),NotGate.not(op[3]),op[4],NotGate.not(op[5]));
		
		// bne = AndGate.and(NotGate.not(op[0]),NotGate.not(op[1]),NotGate.not(op[2]),op[3],NotGate.not(op[4]),op[5]);
		
		// Running line 0x000003fc: 0x1549000c
//		RegDest=0	ALUSrc=0	MemToReg=0	RegWrite=0
//		MemRead=0	MemWrite=0	Branch=1	Jump=0
//		ALU in:0x00000014 0x0000000a
//		------------------------------------------
		ret[0] = rtype;
		ret[1] = OrGate.or(lw,sw, addi); 
		ret[2] = lw;
		ret[3] = OrGate.or(rtype, lw, addi);
		ret[4] = lw;
		ret[5] = sw;
		ret[6] = beq; //OrGate.or(beq,bne);
		ret[7] = rtype;
		ret[8] = beq; //OrGate.or(beq,bne);
		ret[9] = jump;
		
		// System.out.println("ALUOp is " + ret[7] + ret[8]);
		
		return ret;
	}
}
