package com.sxy.transcoder;

import it.sauronsoftware.jave.AudioAttributes;
import it.sauronsoftware.jave.Encoder;
import it.sauronsoftware.jave.EncodingAttributes;

import java.io.File;

public class Transcoder {
	/** 
     * ִ��ת������ 
     *  
     * @param source 
     *            Դ�ļ� 
     * @param desFileName 
     *            Ŀ���ļ��� 
     * @return ת������ļ� 
     */  
    public static File executeTranscode(File source, String desFileName)  
            throws Exception {  
        File target = new File(desFileName);  
        AudioAttributes audio = new AudioAttributes();  
        audio.setCodec("libmp3lame");  
        audio.setBitRate(new Integer(1280000)); //��Ƶ���� MP3Ĭ����1280000  
        audio.setChannels(new Integer(2));  
        audio.setSamplingRate(new Integer(44100));  
        EncodingAttributes attrs = new EncodingAttributes();  
        attrs.setFormat("mp3");  
        attrs.setAudioAttributes(audio);  
        Encoder encoder = new Encoder();  
        encoder.encode(source, target, attrs);  
        return target;  
    }  
    
    public static void transcode(File source) throws Exception{
    	System.out.println("transcoding:"+source.getName());
    	String desFileName=source.getAbsolutePath().replace(".wav", ".mp3");
    	executeTranscode(source,desFileName);
    }
}
