# MIPS-Emulator
A Java emulator written for a class a long time ago.

## Execution Instructions
Built it and run  run the command:

    java GUIMain

A GUI will launch that displays current instruction memory, all 32 registers, and control buttons.

To load an assembly file, simply click `load file`. A file open dialog will
appear. Select your assembly file and hit `okay`. The instructions will be parsed
and their hex will appear in the pane.

Note: the PC counter always starts at `1000`

Once loaded, to execute the assembly, click `run`. This will iteratively run
each instruction through the machine, updating registers and reading / writing
from system memory as necessary.

If, instead, you'd like to see a step-by-step execution of the assembly program,
click `step` repeatedly. Each time you click `step`, the machine will run the
assembly line at the PC counter. In this way, you can control the speed and
watch register evolution over the course of the program.

If the program ever arrives at a line which is `0x00000000` in instruction memory,
it will halt execution.

At any point in the machine's execution, you may elect to load a different file.
Loading a new file will clear instruction memory and reset the PC counter. In
this way, you can run multiple programs on the same data, as registers and
system memory are preserved.

## Sample Input Files

In classical fashion, sample input has been provided. In the root directory,
there are five files: 
- mipsin
- mipsin2
- mipsin3
- mipsin4
- mipsin5
Each of these four files contain small assembly programs that, overall, utilize
each of the commands recommended for implementation. You may load these files.

## Source Code Files

All source code is in the folder labeled `src`. You may explore it at your
leisure - everything is named fairly intuitively, and the subfolders contain
code according to their name (for example, the ALU code is in the `circuit`
folder).


## Working Instructions

Currently working instructions are:

    and   or    slt
    add   sub   addi
    lw    sw
    beq   j

Support for labels is also present.

## Input File Instructions

Each instruction line that does not contain a label must be tabbed in. Each
instruction line that does contain a label must not be tabbed in. Without this
whitespace, the parser will fail because it uses the whitespace for label
detection.

Example:

    <tab><instruction>

or

    <label>:<instruction>

Again, see the input files for more examples.

