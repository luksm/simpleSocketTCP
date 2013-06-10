package server;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.Date;

import cdp.Cliente;
import cdp.Pedido;
import cdp.Produto;
import cgt.bo.BaseBO;
import cgt.bo.ClienteBO;
import cgt.bo.ProdutoBO;

/**
 * SimpleServer Implementacao de um servidor TCP simples para ilustrar os
 * fundamentos de sistemas distribuidos para os alunos da FIAP.
 * 
 * Trata-se de uma aplicação simples de ECHO, implementando a palavra-chave CLOSE para
 * fechar o servidor.
 * 
 * @author Fabian Martins
 **/
public class SimpleServer {

	private ServerSocket socket;
	private int port;

	public SimpleServer() {
		this.port = 6666;
	}

	public SimpleServer(int port) {
		this.port = port;
	}

	/**
	 * Abre o socket e define o timeout.
	 */
	private void open() {
		try {
			socket = new ServerSocket(port);
			socket.setSoTimeout(40000);
			System.out.println("Servidor ativo e aguardando conexao.");

		} catch (IOException e) {
			System.out.println("Erro criando o socket na porta:" + port);
			e.printStackTrace();
		}
	}
	
	/**
	 * Abre o servidor e o coloca em espera de requisições.
	 */
	public void run() {
		this.open();
		this.handleRequest();
	}

	/**
	 * Metodo que determina o inicio de atendimento a uma requisicao.
	 */
	private void handleRequest() {
		/**
		 *  Verifica se o socket existe o que est� vinculado a um par IP/porta
		 */
		
		if ((socket != null) && (socket.isBound())) {
			try {
				Socket clientSocket;
				clientSocket = socket.accept();
				if (clientSocket != null) {
					System.out.println("Cliente conectado:"
							+ clientSocket.getInetAddress().getHostAddress()
							+ ":" + clientSocket.getPort());
					handleClient(clientSocket);
				}
			} catch (SocketTimeoutException timeoutException) {
				System.out.println("Terminando por time-out");
				close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Gerencia uma requisicao atendida.
	 * 
	 * @param clientSocket
	 *            - Socket produzido no accept.
	 */
	private void handleClient(Socket clientSocket) {
		BufferedReader in;
		PrintWriter out;
		ObjectInputStream inObject;
		ObjectOutputStream outObject;
		String command = "";
		ClientRequest req;
		try {
			in = new BufferedReader(new InputStreamReader(clientSocket
					.getInputStream()));
			out = new PrintWriter(clientSocket.getOutputStream(), true);
			inObject = new ObjectInputStream(clientSocket.getInputStream());
			outObject = new ObjectOutputStream(clientSocket.getOutputStream());
			do {
				if (in.ready()) {
					command = in.readLine();
					//informa o client que o comando com o tipo de objeto foi recebido
					out.println("ok");
					try{
						//verifica qual tipo de objeto ser� trabalhado
						if(command.equalsIgnoreCase("cliente"))
						{
							req = new ClientRequest<Cliente>(in, out, inObject, outObject);
							req.setTipoObjeto(command);
							req.tratarRequisicao();
						}
						else if(command.equalsIgnoreCase("produto"))
						{
							req = new ClientRequest<Produto>(in, out, inObject, outObject);
							req.setTipoObjeto(command);
							req.tratarRequisicao();
						}
						else if(command.equalsIgnoreCase("pedido"))
						{
							req = new ClientRequest<Pedido>(in, out, inObject, outObject);
							req.setTipoObjeto(command);
							req.tratarRequisicao();
						}
						else
						{
							out.println("Tipo " + command + " desconhecido pelo servidor.");
						}
					}
					catch(Exception e)
					{
						System.out.println(e.getMessage());
					}
				}
			} while (!command.equalsIgnoreCase("CLOSE"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		close();
	}
	
	
	
	/**
	 * Fecha a conexao.
	 */
	private void close() {
		try {
			System.out.println("Solicitacao de termino. Fechando o servidor.");
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
