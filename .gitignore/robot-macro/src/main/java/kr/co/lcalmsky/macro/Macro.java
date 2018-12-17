package kr.co.lcalmsky.macro;

import kr.co.lcalmsky.macro.command.Command;
import kr.co.lcalmsky.macro.command.CommandFactory;

public class Macro {
	public static void main(String args[]) {
		Command command = CommandFactory.newCommand(args);
		command.execute();
	}
}