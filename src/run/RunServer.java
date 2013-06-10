package run;

import server.SimpleServer;

public class RunServer {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		int port = 6000;
		
		SimpleServer server = new SimpleServer(port);
		server.run();
	}

}
