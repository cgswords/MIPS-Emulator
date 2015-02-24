package data;

import util.BitConverter;
import util.BitPrinter;

public class Registers {
	Bit[][] regs = new Bit[32][32];
	Bit regWrite = new Bit(false);
	
	public Registers() {
		for(int i = 0; i < 32; i++) {
			for (int j = 0; j < 32; j++) {
				regs[i][j] = new Bit(false);
			}
		}
	}
	
	public Bit[][] getRegs() {
		return regs;
	}
	
	public void printRegs() {
		for (int i = 0; i < 10; i++) {
			if (i % 4 == 0) System.out.println();
			System.out.print(" 0" + i + ": " + BitPrinter.toBinary32(regs[i]));
			
		}
		for (int i = 10; i < 32; i++) {
			if (i % 4 == 0) System.out.println();
			System.out.print(" " + i + ": " + BitPrinter.toBinary32(regs[i]));
		}
	}
	
	public void printRegsHex() {
		for (int i = 0; i < 10; i++) {
			if (i % 4 == 0) System.out.println();
			System.out.print(" 0" + i + ": " + BitPrinter.toHex8(regs[i]));
			
		}
		for (int i = 10; i < 32; i++) {
			if (i % 4 == 0) System.out.println();
			System.out.print(" " + i + ": " + BitPrinter.toHex8(regs[i]));
		}
	}
	
	public void setReg(int addr, Bit[] in) {
		if (addr < 0 || addr > 31 || in.length != 32)
			return;
		
		if (!regWrite.value()) return;
		
		for (int i = 0; i < 32; i++) {
			regs[addr][i] = in[i];
		}
	}

	public void setReg(Bit[] addr, Bit[] in) {
		setReg(BitConverter.convert(addr),in);
	}
	
	public Bit[] getReg(int addr) {
		return regs[addr];
	}
	
	public Bit[] getReg(Bit[] addr) {
		return regs[BitConverter.convert(addr)];
	}
	
	public void setControl(Bit w) {
		regWrite = w;
	}
	
	public Bit getControl() {
		return regWrite;
	}
}
