package data;

import util.BitConverter;
import util.BitPrinter;

public class Memory {
	Bit[][] mem = new Bit[65536][8];
	Bit read, write;
	
	public Memory() {
		for(int i = 0; i < 65536; i++) {
			for (int j = 0; j < 8; j++) {
				mem[i][j] = new Bit(false);
			}
		}
		read = new Bit(false);
		write = new Bit(true);
	}

	public Bit[][] getMem() {
		return mem;
	}

	public void setMem(int addr, Bit[] in) {
		if (addr < 0 || addr > 65535 || in.length != 32)
			return;

		if (!write.value()) {
			// System.out.println("Couldn't write");
			return;
		}
		
		// System.out.println("Setting " + addr + " to " + BitPrinter.toBinary32(in));
		for (int i = 0; i < 4; i++) {
			for(int j = 0; j < 8; j++) {
				// System.out.println("[" + (addr+4*i) + "][" + j + "] = " + in[4*i+j]);
				mem[addr+i][j] = in[8*i+j];
			}
		}
	}

	public void setMem(int addr, Bit[][] in) {
		if (addr < 0 || addr > 65535)
			return;

		if (!write.value())
			return;

		for (int i = 0; i < in.length; i++) {
			setMem(addr+4*i,in[i]);
		}
	}

	public void setMem(Bit[] addr, Bit[] in) {
		setMem(BitConverter.convert(addr),in);
	}

	public Bit[] getMem(int addr) {
		
		Bit[] ret = new Bit[32];
		
		for(int i = 0; i < 32; i++) {
			ret[i] = new Bit(false);
		}

		if (!read.value()) return ret;
		
		int retPos = 0;
		for (int i = 0; i < 4; i++) {
			for(int j = 0; j < 8; j++) {
				// System.out.println((retPos) + "/[" + (addr+i) + "][" + j + "]");
				ret[retPos] = mem[addr+i][j];
				retPos++;
			}

		}
		return ret;
	}

	public Bit[] getMem(Bit[] addr) {
		return getMem(BitConverter.convert(addr));
	}

	public void printMem(int addr) {
		System.out.print("Mem at " + addr + ":");
		for (int i = 0; i < 4; i++)
			for(int j = 0; j < 8; j++)
				System.out.print(mem[addr+i][j]);
		
		System.out.println();
	}
	
	public void printMem(int start, int end) {
		for (int i = start; i <= end; i+=4) {
			printMem(i);
		}
	}

	public void printMemHex(int start, int end) {
		for (int i = start; i <= end; i+=4) {
			System.out.println(BitPrinter.toHex8(getMem(i)));
		}
	}
	
	public void setControl(Bit r, Bit w) {
		read = r;
		write = w;
	}
	
	public Bit[] update(Bit[] addr, Bit[] data) {
		Bit[] ret = new Bit[32];
		for(int i =0; i < 32; i++)
			ret[i] = new Bit(false);
		
		if (write.value()) {
			// System.out.println("Writing data");
			setMem(addr,data);
			return ret; 
		}
		
		// System.out.println("Reading data");
		return getMem(addr);
	}
}
