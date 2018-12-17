package kr.co.lcalmsky.macro.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import kr.co.lcalmsky.macro.vo.Settings;

public class FileManager {

	private static final String FILE_PATH = "test.dat";

	public static Settings readSettingsFromFile() {
		File f = new File(FILE_PATH);

		try {
			if (!f.exists()) {
				System.out.println("Create new file...");
				f.createNewFile();
			}
		} catch (IOException e) {
			throw new RuntimeException("Failed to create file");
		}

		byte[] bytes = null;

		try {
			bytes = Files.readAllBytes(Paths.get(FILE_PATH));
		} catch (IOException e) {
			throw new RuntimeException("Failed to read file");
		}

		try {
			return (bytes == null || bytes.length == 0) ? new Settings() : new ObjectMapper().readValue(bytes, Settings.class);
		} catch (IOException e) {
			System.out.println("Failed to read json to settings");
			return new Settings();
		}
	}

	public static void write(Settings setting) throws JsonProcessingException, IOException {
		Files.write(Paths.get(FILE_PATH), new ObjectMapper().writeValueAsBytes(setting), StandardOpenOption.WRITE);
	}
}