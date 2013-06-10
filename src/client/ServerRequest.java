package client;
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
		resposta = sendAndWait("incluir");
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
		//recebe o objeto do servidor
		if(!ServerUtil.waitForMessage(client, (short)5)) { throw new TimeoutException(timeoutError); };
		T obj = (T) client.readObject();
		return obj;
	}
}
