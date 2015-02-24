import gate.AndGate;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;

import util.BitConverter;
import util.BitPrinter;
import util.CodeParser;
import circuit.ALU;
import circuit.BitUtil;
import circuit.Control;
import circuit.Multiplexor;
import circuit.ZeroALU;
import data.Bit;
import data.Memory;
import data.Registers;


public class Machine {
	
	private final JFrame main;
	private File inFile;
	private Memory mem = new Memory();
	private Memory instrMem = new Memory();
	private CodeParser parser;
	private Registers regs = new Registers();
	private JTextField[] regBox = new JTextField[32];
	private String[] regLabels = {
			"$zero","$at","$v0","$v1","$a0","$a1","$a2","$a3",
			"$t0","$t1","$t2","$t3","$t4","$t5","$t6","$t7",
			"$s0","$s1","$s2","$s3","$s4","$s5","$s6","$s7",
			"$t8","$t9","$k0","$k1","$gp","$sp","$fp","$ra"};
	
	private JTextPane memPane;
	private final JTextField loadFile = new JTextField(25);;
	private final JTextField pcDisp = new JTextField(10);
	private final JFileChooser fc = new JFileChooser();
	private int inLength;
	private Bit[] PCCounter;
	
	public Machine() {
		parser.initFuncs();
		parser.initRegs();
		
		for(int i = 0; i < 32; i++) {
			regBox[i] = new JTextField(10);
			regBox[i].setEditable(false);
		}
		
		PCCounter = BitConverter.convert(1000, 32);
		main = makeGUI();
		setRegs();
		
	}
	
	public void loadFile() {
		PCCounter = BitConverter.convert(1000, 32);
		
		Bit[][] file = parser.parseFile(inFile);
	
		instrMem.setControl(new Bit(false), new Bit(true));
		
		for(int i = 0; i < inLength; i++) {
			instrMem.setMem(1000+4*i, BitConverter.convert(0, 32));
		}
		
		inLength = file.length;
		
		for(int i = 0; i < file.length; i++) {
			instrMem.setMem(1000+4*i, file[i]);
		}
		
		instrMem.setControl(new Bit(true), new Bit(false));
		
		memPane.setText("");
		
		for(int i = 0; i < inLength; i++) {
//			System.out.println(1000+4*i);
//			System.out.println(BitPrinter.toBinary32(BitConverter.convert(1000+4*i,32)));
//			System.out.println(BitPrinter.toHex8(mem.getMem(1000+4*i)));
			memPane.setText(memPane.getText() + BitPrinter.toHex8(BitConverter.convert(1000+4*i,32)) + ": " + BitPrinter.toHex8(instrMem.getMem(1000+4*i)) + "\n");
		}
		
		setRegs();
	}
	
	public void step() {
		Bit[] pcOp = new Bit[]{new Bit(false), new Bit(true), new Bit(false)};
		
		Bit regDest, ALUSrc, memToReg, regWrite, memRead, memWrite, branch, jump;
		Bit[] ALUOp;
		
		Bit[] toRun = instrMem.getMem(PCCounter);
		
		if(BitConverter.convert(instrMem.getMem(PCCounter)) == 0) return;
		
		System.out.println("Running line " + BitPrinter.toHex8(PCCounter) + ": " + BitPrinter.toHex8(toRun));
		
		Bit[] control = Control.getControls(toRun);
		regDest = control[0];
		ALUSrc = control[1];
		memToReg = control[2];
		regWrite = control[3];
		memRead = control[4];
		memWrite = control[5];
		branch = control[6];
		ALUOp = new Bit[]{control[7], control[8]};
		jump = control[9];
		
		printControl(regDest, ALUSrc, memToReg, regWrite, memRead, memWrite, branch, jump);
		
		mem.setControl(memRead, memWrite);
		regs.setControl(regWrite);
		// System.out.println("Reg control set to " + regWrite);

		Bit[] ALUControl = Control.ALUControl(ALUOp, BitUtil.getRange(toRun, 26, 32));
		
		Bit[] regData1 = regs.getReg(BitUtil.getRange(toRun, 6, 11));
		Bit[] regData2 = regs.getReg(BitUtil.getRange(toRun, 11, 16));
		Bit[] ALUData2 = Multiplexor.select(new Bit[][]{regData2,
				BitUtil.signExt(BitUtil.getRange(toRun, 16, 32))},ALUSrc);
		
		System.out.println("ALU in:" + BitPrinter.toHex8(regData1) + " " + BitPrinter.toHex8(regData2));
		
		Bit[] ALUResult = ALU.compute(regData1,ALUData2,ALUControl);
		
		Bit[] memResult = mem.update(ALUResult,regData2);
		
		Bit[] ALUMult = Multiplexor.select(new Bit[][]{ALUResult,memResult},memToReg);

/*		System.out.println(BitPrinter.toBinary32(memResult));
		
		System.out.println(BitPrinter.toHex8(ALUResult) + "/" + BitPrinter.toHex(new Bit[]{memToReg}) + "=" +BitPrinter.toHex8(ALUMult));
		
		System.out.println("MEM:");
		mem.printMem(BitConverter.convert(ALUResult), BitConverter.convert(ALUResult)+4);
		System.out.println("----");*/
		
		Bit[] regAddr = Multiplexor.select(new Bit[][]{BitUtil.getRange(toRun,11,16),BitUtil.getRange(toRun,16,21)},regDest);
		// System.out.println(BitPrinter.toHex8(regAddr) + "=" +BitPrinter.toHex8(ALUMult)); 

		regs.setReg(regAddr, ALUMult);
		
		Bit zero = ZeroALU.zero(ALUResult);
			
		Bit[] PCSet = ALU.compute(PCCounter, BitConverter.convert(4,32), pcOp);
		Bit[] PCBranch = ALU.compute(PCCounter, BitUtil.sll(BitUtil.signExt(BitUtil.getRange(toRun, 16, 32)), 2), pcOp);
		Bit[] PCJump = ALU.compute(PCCounter, BitUtil.sll(BitUtil.jumpExt(BitUtil.getRange(toRun, 6, 32)), 2), pcOp);
		Bit[][] PCAdd = new Bit[][]{PCSet,PCBranch};
		
		// System.out.println("beq jump is " + BitPrinter.toBinary32(BitUtil.sll(BitUtil.signExt(BitUtil.getRange(toRun, 16, 32)), 2)));
		
		Bit[] PCCount = Multiplexor.select(PCAdd, AndGate.and(branch, zero));
		
		PCCounter = Multiplexor.select(new Bit[][]{PCCount,PCJump},jump);
		
		setRegs();
		System.out.println("--------------------------------------------------");
	}
	
	public void run() {
	
		while(BitConverter.convert(instrMem.getMem(PCCounter)) != 0) {
			step();
		}
	}
	
	public JFrame makeGUI() {
		JFrame mainFrame = new JFrame("Mips Simulation by Cameron Swords");
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel mainPanel = new JPanel(new GridLayout(2,1));

		// REGISTER BOXES
		JPanel regPanel = new JPanel(new GridLayout(8,8));
		
		for(int i = 0; i < 32; i++) {
			regPanel.add(new JLabel(regLabels[i], JLabel.CENTER));
			regPanel.add(regBox[i]);
		}
		
		// PC Display
		JPanel pcPanel = new JPanel(new FlowLayout());
		pcPanel.add(new JLabel("  PC Counter (Next Line to Execute):"));
		pcPanel.add(pcDisp);
		
		// MEMORY DISPLAY
		JPanel memPanel = new JPanel(new BorderLayout());
		memPane = new JTextPane();
		memPane.setAutoscrolls(true);
		memPane.setPreferredSize(new Dimension(600,200));
		memPane.setEditable(false);
		memPane.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
		
		JScrollPane memScroll = new JScrollPane();
		memScroll.add(memPane);
		memScroll.setViewportView(memPane);
		
		memPanel.add(memScroll,BorderLayout.CENTER);
		
		// RUNNING BUTTONS
		JPanel buttonPanel = new JPanel(new FlowLayout());
		
		JButton load = new JButton("Load File");
		
		load.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				 int returnVal = fc.showOpenDialog(main);

			        if (returnVal == JFileChooser.APPROVE_OPTION) {
			            inFile = fc.getSelectedFile();
			            loadFile.setText(fc.getName(inFile));
			            loadFile();
			        }
			}
		} );
		
			
		JButton step = new JButton("Step");
		step.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				step();
			}
		} );
		
		JButton run = new JButton("Run");
		run.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				run();
			}
		} );
		
		JButton exit = new JButton("Exit");
		
		exit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		} );
		
		buttonPanel.add(loadFile);
		buttonPanel.add(load);
		buttonPanel.add(step);
		buttonPanel.add(run);
		buttonPanel.add(exit);
		
		memPanel.add(pcPanel,BorderLayout.NORTH);
		memPanel.add(buttonPanel,BorderLayout.SOUTH);
		
		mainPanel.add(regPanel);
		mainPanel.add(memPanel);
		
		mainFrame.setSize(605,400);
		mainFrame.add(mainPanel);
		return mainFrame;
	}
	
	public void setRegs() {
		Bit[][] reg = regs.getRegs();
		for(int i = 0; i < 32; i++) {
			// System.out.println("Setting reg" + i + " to " + BitPrinter.toHex8(reg[i]));
			regBox[i].setText(BitPrinter.toHex8(reg[i]));
		}
		pcDisp.setText(BitPrinter.toHex8(PCCounter));
	}
	
	public void showGUI() {
		main.setVisible(true);
	}
	
	public void printControl(Bit regDest, Bit ALUSrc, Bit memToReg, Bit regWrite, Bit memRead, Bit memWrite, Bit branch, Bit jump) {
		System.out.print("RegDest=" + regDest);
		System.out.print("\tALUSrc=" + ALUSrc);
		System.out.print("\tMemToReg=" + memToReg);
		System.out.println("\tRegWrite=" + regWrite);
		System.out.print("MemRead=" + memRead);
		System.out.print("\tMemWrite=" + memWrite);
		System.out.print("\tBranch=" + branch);
		System.out.println("\tJump=" + jump);
		
	}
}
