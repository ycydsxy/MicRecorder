package com.sxy.recorder;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.sound.sampled.*;

public class CopyOfAudioRecorder extends JFrame {

	private static final long serialVersionUID = 1L;

	AudioFormat audioFormat;
	TargetDataLine targetDataLine;

	final JButton captureBtn = new JButton("Capture");
	final JButton stopBtn = new JButton("Stop");

	final JPanel btnPanel = new JPanel();
	final ButtonGroup btnGroup = new ButtonGroup();
	final JRadioButton aifcBtn = new JRadioButton("AIFC");
	final JRadioButton aiffBtn = new JRadioButton("AIFF");
	final JRadioButton auBtn = // selected at startup
	new JRadioButton("AU", true);
	final JRadioButton sndBtn = new JRadioButton("SND");
	final JRadioButton waveBtn = new JRadioButton("WAVE");

	public static void main(String args[]) {
		new CopyOfAudioRecorder();
	}// end main

	public CopyOfAudioRecorder() {// constructor
		captureBtn.setEnabled(true);
		stopBtn.setEnabled(false);

		// 注册匿名的听众
		captureBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				captureBtn.setEnabled(false);
				stopBtn.setEnabled(true);
				// 从捕捉输入数据
				// 麦克风，直到停止按钮
				// 点击。
				captureAudio();
			}// end actionPerformed
		}// end ActionListener
				);// end addActionListener()

		stopBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				captureBtn.setEnabled(true);
				stopBtn.setEnabled(false);
				// 终止输入数据的捕获
				// 从麦克风。
				targetDataLine.stop();
				targetDataLine.close();
			}// end actionPerformed
		}// end ActionListener
		);// end addActionListener()

		// 把按钮放在JFrame中
		getContentPane().add(captureBtn);
		getContentPane().add(stopBtn);

		// 包括在组中的单选按钮
		btnGroup.add(aifcBtn);
		btnGroup.add(aiffBtn);
		btnGroup.add(auBtn);
		btnGroup.add(sndBtn);
		btnGroup.add(waveBtn);

		// 添加单选按钮到JPanel中
		btnPanel.add(aifcBtn);
		btnPanel.add(aiffBtn);
		btnPanel.add(auBtn);
		btnPanel.add(sndBtn);
		btnPanel.add(waveBtn);

		// 把JPanel中在JFrame中
		getContentPane().add(btnPanel);

		// 完成图形用户界面，使可见
		getContentPane().setLayout(new FlowLayout());
		setTitle("Copyright 2003, R.G.Baldwin");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(300, 120);
		setVisible(true);
	}// end constructor

	// 此方法捕获从一个音频输入
	// 麦克风，并将其保存在一个音频文件。
	private void captureAudio() {
		try {
			// 得到的东西设定为捕捉
			audioFormat = getAudioFormat();
			DataLine.Info dataLineInfo = new DataLine.Info(
					TargetDataLine.class, audioFormat);
			targetDataLine = (TargetDataLine) AudioSystem.getLine(dataLineInfo);

			// 创建一个线程来捕捉麦克风
			// 数据转换为音频文件并启动
			// 线程运行。它会一直运行，直到
			// 停止按钮被点击。该方法
			// 将启动线程后返回。
			new CaptureThread().start();
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}// end catch
	}// end captureAudio method

	// 此方法创建并返回一个
	// 对于一组给定的格式AudioFormat对象
	// 参数。如果这些参数不工作
	// 很适合你，尝试一些其他的
	// 允许的参数值，这显示
	// 以下的声明的评论。
	private AudioFormat getAudioFormat() {
		float sampleRate = 8000.0F;
		// 8000,11025,16000,22050,44100
		int sampleSizeInBits = 16;
		// 8,16
		int channels = 1;
		// 1,2
		boolean signed = true;
		// true,false
		boolean bigEndian = false;
		// true,false
		return new AudioFormat(sampleRate, sampleSizeInBits, channels, signed,
				bigEndian);
	}// end getAudioFormat

	// =============================================//

	// 内部类从麦克风中捕获数据
	// 并将其写入到输出的音频文件。
	class CaptureThread extends Thread {
		public void run() {
			AudioFileFormat.Type fileType = null;
			File audioFile = null;

			// 设置文件类型和文件扩展名
			// 根据选择的单选按钮。
			if (aifcBtn.isSelected()) {
				fileType = AudioFileFormat.Type.AIFC;
				audioFile = new File("junk.aifc");
			} else if (aiffBtn.isSelected()) {
				fileType = AudioFileFormat.Type.AIFF;
				audioFile = new File("junk.aif");
			} else if (auBtn.isSelected()) {
				fileType = AudioFileFormat.Type.AU;
				audioFile = new File("junk.au");
			} else if (sndBtn.isSelected()) {
				fileType = AudioFileFormat.Type.SND;
				audioFile = new File("junk.snd");
			} else if (waveBtn.isSelected()) {
				fileType = AudioFileFormat.Type.WAVE;
				audioFile = new File("junk.wav");
			}// end if

			try {
				targetDataLine.open(audioFormat);
				targetDataLine.start();
				AudioSystem.write(new AudioInputStream(targetDataLine),
						fileType, audioFile);
			} catch (Exception e) {
				e.printStackTrace();
			}// end catch

		}// end run
	}// end inner class CaptureThread
	// =============================================//

}// end outer class AudioRecorder.java