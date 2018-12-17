package kr.co.lcalmsky.macro.command;

public class CommandFactory {

	public static Command newCommand(String[] args) {
		return args.length > 0 ? new SettingCommand() : new RobotCommand();
	}
}