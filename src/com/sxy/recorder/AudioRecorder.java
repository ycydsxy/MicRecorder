package com.sxy.recorder;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.TargetDataLine;

/**
 * 
 * @author  R.G.Baldwin
 * 
 * Modified by Kevin Su
 *
 */
public class AudioRecorder {

	AudioFormat audioFormat;
	TargetDataLine targetDataLine;
	private Date Start_Time = new Date();
	private Date End_Time;
	private File saveDirectory;

	// 此方法捕获从一个音频输入
	// 麦克风，并将其保存在一个音频文件。
	public void captureAudio() {
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

	public void stopCapture() {
		targetDataLine.stop();
		targetDataLine.close();
	}

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
			AudioFileFormat.Type fileType = AudioFileFormat.Type.WAVE;
			// String absoluteFilePath = saveDirectory.getAbsolutePath()
			// + "\\"
			// + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
			// .format(Start_Time) + "_junk.wav";
			
			String absoluteFilePath = new SimpleDateFormat(
					"yyyy-MM-dd HH:mm:ss").format(Start_Time) + "_junk.wav";
			File audioFile = new File(absoluteFilePath);

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