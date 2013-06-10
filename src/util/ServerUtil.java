package util;

import java.io.BufferedReader;
import java.io.IOException;

import client.SimpleClient;

public class ServerUtil {
	public static boolean waitForMessage(SimpleClient client, short seconds) throws InterruptedException
	{
		int aux = 0;
		while(!client.hasMessage() && aux < 10){ aux++; Thread.sleep(seconds * 100); }
		if(aux == 10) return false; 
		else return true;
	}
	
	public static boolean waitForMessage(SimpleClient client, int milliseconds, short loop) throws InterruptedException
	{
		int aux = 0;
		while(!client.hasMessage() && aux < loop){ aux++; Thread.sleep(milliseconds); }
		if(aux == 10) return false; 
		else return true;
	}
	
	public static boolean waitForMessage(BufferedReader in, short seconds) throws InterruptedException, IOException
	{
		int aux = 0;
		while(!in.ready() && aux < 10){ aux++; Thread.sleep(seconds * 100); }
		if(aux == 10) return false; 
		else return true;
	}
	
	public static boolean waitForMessage(BufferedReader in, int milliseconds, short loop) throws InterruptedException, IOException
	{
		int aux = 0;
		while(!in.ready() && aux < loop){ aux++; Thread.sleep(milliseconds); }
		if(aux == 10) return false; 
		else return true;
	}
}
