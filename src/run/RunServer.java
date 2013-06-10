package run;

import server.SimpleServer;

public class RunServer {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		int port = 6666;
		if (args.length>0) {
			String sPort = args[0];
			try {
				port = Integer.parseInt(sPort);
				if (port<1024) {
					System.out.println("Porta informada < 1024. Assumindo porta 9009.");
					port = 6666;
				}
			} catch (NumberFormatException e) {
				System.out.println("Erro na porta informada: "+sPort);
				System.out.println("Assumindo porta 9009.");
				port = 6666;
			}
		} else {
			System.out.println("Assumindo porta "+port+".");
		}
		SimpleServer server = new SimpleServer(port);
		server.run();
	}

}
