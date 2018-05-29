package com.sxy.recorder;

import java.io.File;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.sxy.transcoder.Transcoder;

public class MainRecorder {
	private static AudioRecorder Audio_Recorder = null;
	private static String Path = "./";
	private static int Interval = 1;
	private static int Transcode_Interval = 24;

	public static void main(String[] args) throws Exception {
		// 改写默认值
		if (args.length == 1) {
			Path = args[0];
		} else if (args.length == 2) {
			Path = args[0];
			Interval = Integer.parseInt(args[1]);
		} else if (args.length == 3) {
			Path = args[0];
			Interval = Integer.parseInt(args[1]);
			Transcode_Interval = Integer.parseInt(args[2]);
		}

		final File saveDirectory = new File(Path);
		System.out.println("Save path:" + saveDirectory.getAbsolutePath());

		if (!saveDirectory.isDirectory()) {
			throw new Exception("ERROR:NOT A PATH!");
		}

		if (!saveDirectory.exists()) {
			System.out
					.println("MAKING DIRS:" + saveDirectory.getAbsolutePath());
			saveDirectory.mkdirs();
		}

		Audio_Recorder = new AudioRecorder(saveDirectory);
		Audio_Recorder.captureAudio();
		Runnable runnable = new Runnable() {
			public void run() {
				System.out.println("run!");
				Audio_Recorder.stopCapture();
				Audio_Recorder.captureAudio();
			}
		};
		ScheduledExecutorService service = Executors
				.newSingleThreadScheduledExecutor();
		// 参数：1、任务体 2、首次执行的延时时间
		// 3、任务执行间隔 4、间隔时间单位
		service.scheduleAtFixedRate(runnable, Interval, Interval,
				TimeUnit.HOURS);

		Runnable runnable2 = new Runnable() {
			public void run() {
				System.out.println("transcode!");
				for (File file : saveDirectory.listFiles()) {
					if (file.getName().endsWith(".wav")) {
						try {
							Transcoder.transcode(file);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						file.delete();
					}
				}
			}
		};

		service.scheduleAtFixedRate(runnable2, Transcode_Interval,
				Transcode_Interval, TimeUnit.HOURS);

	}
}
