package com.sxy.recorder;

import java.util.Date;

public class MainRecorder {
	private static AudioRecorder Audiu_Recorder=null;
	private static Date Start_Time;
	private static Date End_Time;
	private static String Path;
	
	public static void main(String[] args){
		Audiu_Recorder=new AudioRecorder();
		Start_Time=new Date();
		
	}
}
