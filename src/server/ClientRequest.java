package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.util.Arrays;

import com.sun.xml.internal.bind.v2.runtime.unmarshaller.XsiNilLoader.Array;


import util.ServerUtil;

import cdp.Cliente;
import cdp.Pedido;
import cdp.Produto;
import cgt.bo.BOException;
import cgt.bo.BaseBO;
import cgt.bo.ClienteBO;
import cgt.bo.PedidoBO;
import cgt.bo.ProdutoBO;

public class ClientRequest<T> {
	private BufferedReader in;
	private PrintWriter out;
	private ObjectInputStream inObject;
	private ObjectOutputStream outObject;
	private String tipoObjeto;
	
	public String getTipoObjeto() {
		return tipoObjeto;
	}

	public void setTipoObjeto(String tipoObjeto) {
		this.tipoObjeto = tipoObjeto;
	}

	public ClientRequest(BufferedReader in, PrintWriter out, ObjectInputStream inObject, ObjectOutputStream outObject)
	{
		this.in = in;
		this.out = out;
		this.inObject = inObject;
		this.outObject = outObject;
	}
	
	public void tratarRequisicao() throws IOException, InterruptedException, ClassNotFoundException, BOException
	{
		String command;
		//aguarda por até 2s a resposta da operação que será feita
		if(!ServerUtil.waitForMessage(in, (short)2)) return;
		command = in.readLine();
		//informa o client que a operação foi recebida
		out.println("ok");
		
		//trata a inclusão
		if(command.equalsIgnoreCase("incluir"))
		{
			T obj = receiveObject();
			if(obj == null) return;
			
			incluir(obj);
		}
		//trata a alteração
		else if(command.equalsIgnoreCase("alterar"))
		{
			
		}
		else if(command.equalsIgnoreCase("buscarPeloId"))
		{
			//recebe o id que será consultado
			if(!ServerUtil.waitForMessage(in, (short)2)) return;
			command = in.readLine();
			//informa o client que o id foi recebido
			out.println("ok");
			//consulta o objeto e envia par ao client
			T obj = buscarPeloId(Long.parseLong(command));
			outObject.writeObject(obj);
			outObject.flush();
		}
	}
	
	private void incluir(T obj) throws InterruptedException, IOException, BOException, ClassNotFoundException
	{
		//verifica o tipo do objeto
		if(obj instanceof Cliente)
		{
			//faz a inclusão do cliente
			Cliente c = (Cliente) obj;
			ClienteBO bo = new ClienteBO();
			bo.beginTransaction();
			bo.save(c);
			bo.commit();
			//informa ao client que a operação foi finalizada com sucesso
			out.println("fim");
		}
		else if(obj instanceof Produto)
		{
			//faz a inclusão do produto
			Produto p = (Produto) obj;
			ProdutoBO bo = new ProdutoBO();
			bo.beginTransaction();
			bo.save(p);
			bo.commit();
			//informa ao client que a operação foi finalizada com sucesso
			out.println("fim");
		}
		else if(obj instanceof Pedido)
		{
			//faz a inclusão do pedido
			Pedido p = (Pedido) obj;
			PedidoBO bo = new PedidoBO();
			bo.beginTransaction();
			bo.save(p);
			bo.commit();
			//informa ao client que a operação foi finalizada com sucesso
			out.println("fim");
		}
	}
	
	@SuppressWarnings("unchecked")
	private T buscarPeloId(long id) throws BOException, ClassNotFoundException
	{
		//verifica o tipo do objeto
		if(tipoObjeto.equals("cliente"))
		{
			//faz a consulta do cliente
			Cliente c = null;
			ClienteBO bo = new ClienteBO();
			bo.beginTransaction();
			c = bo.selectById(id);
			bo.commit();
			return (T) c;
		}
		else if(tipoObjeto.equals("produto"))
		{
			//faz a consulta do produto
			Produto p = null;
			ProdutoBO bo = new ProdutoBO();
			bo.beginTransaction();
			p = bo.selectById(id);
			bo.commit();
			return (T) p;
		}
		else if(tipoObjeto.equals("pedido"))
		{
			//faz a inclusão do pedido
			Pedido p = null;
			PedidoBO bo = new PedidoBO();
			bo.beginTransaction();
			bo.selectById(id);
			bo.commit();
			return (T) p;
		}
		else
		{
			throw new ClassNotFoundException("O tipo de objeto " + tipoObjeto + " não é tratado pelo servidor.");
		}
	}
	
	//retorna um objeto enviado pelo client
	private T receiveObject() throws InterruptedException, IOException, ClassNotFoundException
	{
		//aguarda por até 2s a resposta do client com o objeto
		if(!ServerUtil.waitForMessage(in, (short)2)) return null;
		//lê o objeto enviado pelo client
		T obj = (T) inObject.readObject();
		//informa ao client que o objeto foi recebido
		out.println("ok");
		return obj;
	}
}
