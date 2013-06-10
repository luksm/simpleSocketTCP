package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.TimeoutException;

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
	
	public void tratarRequisicao() throws IOException, InterruptedException, ClassNotFoundException, BOException, TimeoutException
	{
		String command;
		//aguarda por até 2s a resposta da operação que será feita
		if(!ServerUtil.waitForMessage(in, (short)2)) return;
		command = in.readLine();
		//informa o client que a operação foi recebida
		out.println("ok");
		
		//trata a inclusão, alteração ou exclusão
		if(command.equalsIgnoreCase("incluir") ||
		   command.equalsIgnoreCase("alterar") ||
		   command.equalsIgnoreCase("excluir"))
		{
			T obj = receiveObject();
			if(obj == null) return;
			
			incluirAlterarExcluir(obj, command);
		}
		else if(command.equalsIgnoreCase("buscarPeloId"))
		{
			//recebe o id que será consultado
			if(!ServerUtil.waitForMessage(in, (short)2)) return;
			String id = in.readLine();
			//informa o client que o id foi recebido
			out.println("ok");
			//verifica se o client está pronto para receber o objeto
			if(!ServerUtil.waitForMessage(in, (short)2)) return;
			command = in.readLine();
			if(!command.contains("ok")) return;
			//consulta o objeto e envia para o client
			T obj = buscarPeloId(Long.parseLong(id));
			outObject.writeObject(obj);
			outObject.flush();
		}
		else if(command.equalsIgnoreCase("buscarPorString"))
		{
			//recebe a string que será consultada
			if(!ServerUtil.waitForMessage(in, (short)2)) return;
			String str = in.readLine();
			//informa o client que a string foi recebida
			out.println("ok");
			//recebe a propriedade que será consultada
			if(!ServerUtil.waitForMessage(in, (short)2)) return;
			String prop = in.readLine();
			//informa o client que a propriedade foi recebida
			out.println("ok");
			//verifica se o client está pronto para receber o resultado da busca
			if(!ServerUtil.waitForMessage(in, (short)2)) return;
			command = in.readLine();
			if(!command.contains("ok")) return;
			//busca os objetos e envia para o client
			sendObjectList(buscarPorString(str, prop));
		}
	}
	
	private void incluirAlterarExcluir(T obj, String command) throws InterruptedException, IOException, BOException, ClassNotFoundException
	{
		
		//verifica o tipo do objeto
		if(obj instanceof Cliente)
		{
			//faz a inclusão do cliente
			Cliente c = (Cliente) obj;
			ClienteBO bo = new ClienteBO();
			bo.beginTransaction();
			if(command.equals("incluir"))
				bo.save(c);
			else if(command.equals("alterar"))
				bo.update(c);
			else if(command.equals("excluir"))
				bo.delete(c);
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
			if(command.equals("incluir"))
				bo.save(p);
			else if(command.equals("alterar"))
				bo.update(p);
			else if(command.equals("excluir"))
				bo.delete(p);
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
			if(command.equals("incluir"))
				bo.save(p);
			else if(command.equals("alterar"))
				bo.update(p);
			else if(command.equals("excluir"))
				bo.delete(p);
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
	
	@SuppressWarnings("unchecked")
	private ArrayList<T> buscarPorString(String str, String prop) throws BOException, ClassNotFoundException
	{
		//verifica o tipo do objeto
		if(tipoObjeto.equals("cliente"))
		{
			//faz a consulta do cliente
			ArrayList<Cliente> clientes = null;
			ClienteBO bo = new ClienteBO();
			bo.beginTransaction();
			if(prop.equals("nome"))
				clientes = bo.buscarPeloNome(str);
			else if(prop.equals("cpf"))
				clientes = bo.buscarPeloCpf(str);
			bo.commit();
			return (ArrayList<T>) clientes;
		}
		else if(tipoObjeto.equals("produto"))
		{
			//faz a consulta do produto
			ArrayList<Produto> produtos = null;
			ProdutoBO bo = new ProdutoBO();
			bo.beginTransaction();
			if(prop.equals("descricao"))
				produtos = bo.buscarPelaDescricao(str);
			bo.commit();
			return (ArrayList<T>) produtos;
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
	
	//envia um arraylist do objeto tipo T para o client
	private void sendObjectList(ArrayList<T> list) throws IOException, InterruptedException, TimeoutException
	{
		String response = "";
		short tries;
		if(list == null) 
		{
			outObject.writeObject(null);
			return;
		}	
		for(T obj : list)
		{
			tries = 0;
			do
			{
				outObject.writeObject(obj);
				out.flush();
				if(ServerUtil.waitForMessage(in, 50, (short)40))
				{
					response = in.readLine();
				}
				tries++;
			}while(!response.contains("ok") && tries < 5);
			if(tries == 5) throw new TimeoutException("Não foi possível enviar todos os objetos, o servidor não obteve resposta do client!");
		}
		
		outObject.writeObject(null);
	}
}
