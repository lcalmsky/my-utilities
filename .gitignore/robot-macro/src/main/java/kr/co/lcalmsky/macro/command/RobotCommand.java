package kr.co.lcalmsky.macro.command;

import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileInputStream;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.StringTokenizer;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import kr.co.lcalmsky.macro.vo.Result;
import kr.co.lcalmsky.macro.vo.Settings;

public class RobotCommand extends AbstractCommand {

	private static final File EXCEL_FILE = new File("D:/Utilities/Drivers/Mainboard/haveSeen/Book1.xlsx");

	@Override
	protected Result execute(Settings settings) throws Exception {
		FileInputStream in = new FileInputStream(EXCEL_FILE);
		XSSFWorkbook workbook = new XSSFWorkbook(in);
		in.close();
		XSSFSheet sheet = workbook.getSheetAt(0);

		Robot robot = new Robot();
		robot.mouseMove(settings.getEmptyX(), settings.getEmptyY());
		robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
		robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
		robot.delay(300);

		Map<String, String> time = new LinkedHashMap<>();
		int delay = 450, startRow = 0, endRow = 0;
		for (Iterator<Row> iterator = sheet.rowIterator(); iterator.hasNext();) {
			XSSFRow titleRow = (XSSFRow) iterator.next();
			XSSFCell titleCell = (XSSFCell) titleRow.getCell(0);
			if (titleCell == null) continue;
			String title = titleCell.toString();
			if (title == null || title.isEmpty()) continue;
			if (title.startsWith("_")) {
				XSSFCell infoCell = titleRow.getCell(1);
				String info = infoCell.toString();
				StringTokenizer st = new StringTokenizer(info);
				startRow = Integer.valueOf(st.nextToken()) - 1;
				endRow = Integer.valueOf(st.nextToken());
				System.out.println("START: " + startRow + ", END: " + endRow);
				delay = st.hasMoreTokens() ? Integer.valueOf(st.nextToken()) : 450;

				for (int i = startRow; i < endRow; i++) {
					XSSFRow temp = sheet.getRow(i);
					String start = String.format("%06d", Integer.parseInt(temp.getCell(1).toString()));
					String end = String.format("%06d", Integer.parseInt(temp.getCell(2).toString()));
					time.put(start, end);
				}

				System.out.println("TOTAL: " + time.size());
				System.out.println(time.toString());

				Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
				StringSelection stringSelection = new StringSelection(title.substring(1));
				clipboard.setContents(stringSelection, stringSelection);
				break;
			}
		}

		int cnt = 0;
		for (String start : time.keySet()) {

			robot.mouseMove(settings.getTimeX(), settings.getTimeY());
			robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
			robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
			robot.delay(100);
			robot.keyPress(KeyEvent.VK_TAB);
			robot.keyRelease(KeyEvent.VK_TAB);
			robot.delay(100);

			for (int i = 0; i < start.length(); i++) {
				robot.keyPress(start.charAt(i));
				robot.keyRelease(start.charAt(i));
			}

			robot.keyPress(KeyEvent.VK_SHIFT);
			robot.keyPress(KeyEvent.VK_TAB);
			robot.keyRelease(KeyEvent.VK_SHIFT);
			robot.keyRelease(KeyEvent.VK_TAB);

			String end = time.get(start);

			for (int i = 0; i < end.length(); i++) {
				robot.keyPress(end.charAt(i));
				robot.keyRelease(end.charAt(i));
			}

			robot.keyPress(KeyEvent.VK_TAB);
			robot.keyRelease(KeyEvent.VK_TAB);
			robot.keyPress(KeyEvent.VK_TAB);
			robot.keyRelease(KeyEvent.VK_TAB);
			robot.keyPress(KeyEvent.VK_TAB);
			robot.keyRelease(KeyEvent.VK_TAB);
			robot.keyPress(KeyEvent.VK_SPACE);
			robot.keyRelease(KeyEvent.VK_SPACE);

			if (cnt == time.keySet().size()) break;

			robot.delay(delay);
		}

		robot.delay(300);
		robot.mouseMove(settings.getStartX(), settings.getStartY());
		robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
		robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);

		workbook.close();

		return Result.OK;
	}
}