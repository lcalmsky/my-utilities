package kr.co.lcalmsky.macro.vo;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonProcessingException;

import kr.co.lcalmsky.macro.util.FileManager;

public enum Result {
	OK {
		@Override
		public void doPost(Settings setting) {}
	},
	CHANGED {
		@Override
		public void doPost(Settings setting) throws JsonProcessingException, IOException {
			FileManager.write(setting);
		}
	};

	public abstract void doPost(Settings setting) throws JsonProcessingException, IOException;
}