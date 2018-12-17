package kr.co.lcalmsky.macro.command;

import java.awt.Color;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.CountDownLatch;
import java.util.function.Consumer;

import javax.swing.JFrame;

import kr.co.lcalmsky.macro.vo.Result;
import kr.co.lcalmsky.macro.vo.Settings;

public class SettingCommand extends AbstractCommand {

	private CountDownLatch countdown = null;

	@Override
	protected Result execute(Settings settings) throws InvocationTargetException, InterruptedException {

		System.out.println("Set the location to click to switch to the app.");
		TransparentFrame frame = new TransparentFrame(countdown = new CountDownLatch(1), e -> {
			settings.setEmptyX(e.getX());
			settings.setEmptyY(e.getY());
		});
		frame.setVisible(true);
		countdown.await();
		frame.dispose();
		frame = null;

		System.out.println("Set the location to click to run the time setting dialog.");
		frame = new TransparentFrame(countdown = new CountDownLatch(1), e -> {
			settings.setTimeX(e.getX());
			settings.setTimeY(e.getY());
		});
		frame.setVisible(true);
		countdown.await();
		frame.dispose();
		frame = null;

		System.out.println("Set the location of the execution button.");
		frame = new TransparentFrame(countdown = new CountDownLatch(1), e -> {
			settings.setStartX(e.getX());
			settings.setStartY(e.getY());
		});
		frame.setVisible(true);
		countdown.await();
		frame.dispose();
		frame = null;

		return Result.CHANGED;
	}
}

class TransparentFrame extends JFrame {

	private static final long		serialVersionUID	= 7842260193391286345L;
	private Consumer<MouseEvent>	consumer;

	public TransparentFrame(CountDownLatch countdown, Consumer<MouseEvent> consumer) {
		this.consumer = consumer;
		setSize(Toolkit.getDefaultToolkit().getScreenSize());
		setUndecorated(true);
		setBackground(new Color(1.0f, 1.0f, 1.0f, 0.2f));
		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				TransparentFrame.this.consumer.accept(e);
				countdown.countDown();
			}
		});
	}

	public void setConsumer(Consumer<MouseEvent> consumer) {
		this.consumer = consumer;
	}
}