package client;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.concurrent.TimeoutException;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import util.ServerUtil;
import cdp.Cliente;
import cdp.Pedido;
import cdp.Produto;

public class ServerRequest<T> {
	private SimpleClient client;
	private final String timeoutError = "Não foi possível obter a resposta do servidor...";
	private final String generalError = "Não foi possível finalizar a operação devido o erro: ";
	
	public ServerRequest(SimpleClient client)
	{
		this.client = client;
	}
	
	private String sendAndWait(String command) throws InterruptedException, TimeoutException
	{
		//envia o comando solicitado
		client.sendCommand(command);
		//aguarda a resposta do servidor por até 2s
		if(!ServerUtil.waitForMessage(client, (short)2)) { throw new TimeoutException(timeoutError); };
		//se o servidor responder, retorna a mensagem lida
		return client.readMessage();
	}
	
	public void incluir(T obj) throws InterruptedException, TimeoutException, NotImplementedException, Exception
	{
		incluirAlterarExcluir(obj, "incluir");
	}
	
	public void alterar(T obj) throws InterruptedException, TimeoutException, NotImplementedException, Exception
	{
		incluirAlterarExcluir(obj, "alterar");
	}
	
	public void excluir(T obj) throws InterruptedException, TimeoutException, NotImplementedException, Exception
	{
		incluirAlterarExcluir(obj, "excluir");
	}
	
	private void incluirAlterarExcluir(T obj,  String command) throws InterruptedException, TimeoutException, NotImplementedException, Exception
	{
		String resposta = null;
		if(obj instanceof Cliente)
		{
			//envia o comando para o servidor informando que o objeto que será trabalhado é um cliente
			//e pega a resposta do servidor
			resposta = sendAndWait("cliente");
			if(!resposta.contains("ok")) { throw new Exception(generalError + resposta); };
		}
		else if(obj instanceof Produto)
		{
			//envia o comando para o servidor informando que o objeto que será trabalhado é um produto
			//e pega a resposta do servidor
			resposta = sendAndWait("produto");
			if(!resposta.contains("ok")) { throw new Exception(generalError + resposta); };
		}
		else if(obj instanceof Pedido)
		{
			//envia o comando para o servidor informando que o objeto que será trabalhado é um produto
			//e pega a resposta do servidor
			resposta = sendAndWait("pedido");
			if(!resposta.contains("ok")) { throw new Exception(generalError + resposta); };
		}
		else
		{
			throw new NotImplementedException();
		}
		//envia o comando de inclusão
		//e pega a resposta do servidor
		resposta = sendAndWait(command);
		if(!resposta.contains("ok")) { throw new Exception(generalError + resposta); };
		//envia o objeto para o servidor
		client.sendObject(obj);
		if(!ServerUtil.waitForMessage(client, (short)2)) { throw new TimeoutException(timeoutError); };
		//verifica se o servidor confirmou o recebimento do objeto
		resposta = client.readMessage();
		if(!resposta.contains("ok")) { throw new Exception(generalError + resposta); };
		//verifica se o servidor confirmou a inclusão
		if(!ServerUtil.waitForMessage(client, (short)5)) { throw new TimeoutException(timeoutError); };
		resposta = client.readMessage();
		if(!resposta.contains("fim")) { throw new Exception(generalError + resposta); };
	}
	
	@SuppressWarnings("unchecked")
	public T buscarPeloId(long id, String tipoObjeto) throws InterruptedException, TimeoutException, NotImplementedException, Exception
	{
		String resposta = null;
		//envia o comando para o servidor informando que o objeto que será trabalhado
		//e pega a resposta do servidor
		resposta = sendAndWait(tipoObjeto);
		if(!resposta.contains("ok")) { throw new Exception(generalError + resposta); };
		//envia o comando de busca pelo id
		//e pega a resposta do servidor
		resposta = sendAndWait("buscarPeloId");
		if(!resposta.contains("ok")) { throw new Exception(generalError + resposta); };
		//envia o id para o servidor e pega a resposta do servidor
		resposta = sendAndWait(String.valueOf(id));
		if(!resposta.contains("ok")) { throw new Exception(generalError + resposta); };
		//avisa o servidor que ele já pode enviar o objeto
		client.sendCommand("ok");
		//recebe o objeto do servidor
		if(!ServerUtil.waitForMessage(client, (short)10)) { throw new TimeoutException(timeoutError); };
		T obj = (T) client.readObject();
		return obj;
	}
	
	@SuppressWarnings("unchecked")
	public HashSet<T> buscarPorString(String str, String tipoObjeto, String propriedade) throws InterruptedException, TimeoutException, NotImplementedException, Exception
	{
		String resposta = null;
		//envia o comando para o servidor informando que o objeto que será trabalhado
		//e pega a resposta do servidor
		resposta = sendAndWait(tipoObjeto);
		if(!resposta.contains("ok")) { throw new Exception(generalError + resposta); };
		//envia o comando de busca por uma string
		//e pega a resposta do servidor
		resposta = sendAndWait("buscarPorString");
		if(!resposta.contains("ok")) { throw new Exception(generalError + resposta); };
		//envia a string para o servidor e pega a resposta do servidor
		resposta = sendAndWait(str);
		if(!resposta.contains("ok")) { throw new Exception(generalError + resposta); };
		//envia a propriedade que será utilizada para o servidor e pega a resposta do servidor
		resposta = sendAndWait(propriedade);
		if(!resposta.contains("ok")) { throw new Exception(generalError + resposta); };
		//avisa o servidor que ele já pode enviar o resultado da busca
		client.sendCommand("ok");
		//recebe os objetos do servidor
		return receiveObjectSet();
	}
	
	//envia um arraylist do objeto tipo T para o client
	@SuppressWarnings("unchecked")
	private HashSet<T> receiveObjectSet() throws IOException, InterruptedException, TimeoutException, ClassNotFoundException
	{
		HashSet<T> set = new HashSet<T>();
		T obj = null;
		short tries;
		
		while(true)
		{
			tries = 0;
			while(tries < 5)
			{
				if(ServerUtil.waitForMessage(client, 50, (short)100))
				{
					obj = (T) client.readObject();
					if(obj == null) return set;
					set.add(obj);
					client.sendCommand("ok");
					break;
				}
				client.sendCommand("ok");
				tries++;
			}
			if(tries == 5) throw new TimeoutException("Não foi possível enviar todos os objetos, o servidor não obteve resposta do client!");
		}
	}
}
