package kr.co.lcalmsky.macro.command;

import kr.co.lcalmsky.macro.util.FileManager;
import kr.co.lcalmsky.macro.vo.Result;
import kr.co.lcalmsky.macro.vo.Settings;

public abstract class AbstractCommand implements Command {

	@Override
	public void execute() {
		Settings settings;
		try {
			settings = FileManager.readSettingsFromFile();
			execute(settings).doPost(settings);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected abstract Result execute(Settings settings) throws Exception;
}