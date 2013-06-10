package client;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class SimpleClient {
	private Socket clientSocket;
	private static final String DEFAULT_HOST = "127.0.0.1";
	private static final int DEFAULT_PORT = 6666;
	private String host;
	private int port;
	private PrintWriter out = null;
	private ObjectOutputStream outObject = null;
	private BufferedReader in = null;
	private ObjectInputStream inObject = null;

	public SimpleClient() {
		this.host = SimpleClient.DEFAULT_HOST;
		this.port = SimpleClient.DEFAULT_PORT;
	}

	public SimpleClient(String host) {
		this.host = host;
		this.port = SimpleClient.DEFAULT_PORT;
	}

	public SimpleClient(int port) {
		this.host = SimpleClient.DEFAULT_HOST;
		this.port = port;
	}

	public SimpleClient(String host, int port) {
		this.host = host;
		this.port = port;
	}

	public void Connect() {
		try {
			clientSocket = new Socket(host,port);
			out = new PrintWriter(clientSocket.getOutputStream(), true);
			outObject = new ObjectOutputStream(clientSocket.getOutputStream());
			in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			inObject = new ObjectInputStream(clientSocket.getInputStream());
		} catch (UnknownHostException e) {
			System.out.println("Servidor nao encontrado.");
		} catch (IOException e) {
			System.out.println("Erro na preparacao para ler o socket. O servidor deve estar desconectado.");
		}
	}
	
	public boolean isConnected() {
		boolean result = false;
		if (clientSocket!=null) result = clientSocket.isConnected();
		return result;
	}

	public void sendObject(Object obj) throws IOException {
		outObject.writeObject(obj);
		outObject.flush();
	}
	
	public Object readObject() throws IOException, ClassNotFoundException {
		if(inObject.available() > 0)
			return inObject.readObject();
		else
			return null;
	}
	
	public void sendCommand(String command)
	{
		out.println(command);
	}
	
	public String readMessage() {
		try {
			return in.readLine();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public boolean hasMessage()
	{
		try {
			return in.ready();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	public void close() {
		try {
			clientSocket.shutdownOutput();
			clientSocket.close();
		} catch (IOException e) {
			System.out.println("Erro no fechamento do socket");
		}
	}
}
