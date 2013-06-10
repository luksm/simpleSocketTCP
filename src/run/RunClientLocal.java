package run;

import cdp.Cliente;
import cdp.ItemPedido;
import cdp.Pedido;
import cdp.Produto;
import cgt.bo.BOException;
import cgt.bo.ClienteBO;
import client.ServerRequest;
import client.SimpleClient;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.concurrent.TimeoutException;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import util.ServerUtil;

/**
 * Roda o cliente apontando para o servidor local.
 * 
 * @author fabianmartins
 *
 */
public class RunClientLocal {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		int port = 6000;
		String host = "LOCALHOST";
		if(args.length > 0) host = args[0];
		boolean gotMessage;
		String resposta = null;
		String comando = null;
		short opcao = 0;
		
		// A linha a seguir define um leitor de teclado
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		// Crio o SimpleClient que conversa com o nosso servidor
		SimpleClient client = new SimpleClient(host,port);
		client.Connect();
		if (client.isConnected()) {
			do {
				montarMenuPrincipal();
				try
				{
					opcao = Short.parseShort(in.readLine());
				}
				catch(Exception ex) { System.out.println("Comando inv�lido!"); continue; }
				try {
					switch(opcao)
					{
						case 1:
							tratarMenuClientes(client, in);
						break;
						
						case 2:
							tratarMenuProdutos(client, in);
						break;
					
						case 3:
							
						break;
						
						case 9:
							client.close();
							System.out.println("Voc� saiu do sistema...");
						break;
						
						default:
							System.out.println("Comando inv�lido!");
						break;
					}
 				} catch (Exception e) {
					e.printStackTrace();
				}
			} while (opcao != 9);
		} else
			System.out.println("Falha - Cliente nao conectado.");
	}
	
	private static void montarMenuPrincipal()
	{
		System.out.println("Selecione um menu:");
		System.out.println("1. Clientes");
		System.out.println("2. Produtos");
		System.out.println("3. Pedidos");
		System.out.println("9. Sair");
	}
	
	private static void tratarMenuClientes(SimpleClient client, BufferedReader in) throws NotImplementedException, InterruptedException, TimeoutException, Exception
	{
		ServerRequest<Cliente> req = new ServerRequest<Cliente>(client);
		int opcao = 0;
		Cliente c = null;
		HashSet<Cliente> clientes = null;
		
		do
		{
			System.out.println("Selecione uma opera��o:");
			System.out.println("1. Novo Cliente");
			System.out.println("2. Alterar Cliente");
			System.out.println("3. Excluir Cliente");
			System.out.println("4. Consultar cliente pelo id");
			System.out.println("5. Consultar cliente pelo nome");
			System.out.println("6. Consultar cliente pelo cpf");
			System.out.println("9. Voltar");
			try
			{
				opcao = Short.parseShort(in.readLine());
			}
			catch(Exception ex) { System.out.println("Comando inv�lido!"); continue; }
			
			switch(opcao)
			{
				case 1:
					System.out.println("Informe os dados do cliente:");
					c = new Cliente();
					System.out.println("Nome: ");
					c.setNome(in.readLine());
					System.out.println("CPF: ");
					c.setCpf(in.readLine());
					req.incluir(c);
					System.out.println("Cliente inclu�do com sucesso!");
				break;
				
				case 2:
					System.out.println("Informe o c�digo do cliente (-1 para sair):");
					long id = 0;
					while(id == 0)
					{
						try
						{
							id = Integer.parseInt(in.readLine());
						}
						catch(Exception ex) { System.out.println("Digite um n�mero v�lido!"); }
					}
					if (id == -1) { continue; }
					c = req.buscarPeloId(id, "cliente");
					if(c == null)
					{
						 System.out.println("Cliente n�o encontrado no banco de dados!");
						 continue;
					}
					System.out.println("Informe os novos dados do cliente:");
					System.out.println("Nome (" + c.getNome() + "): ");
					c.setNome(in.readLine());
					System.out.println("CPF (" + c.getCpf() + "): ");
					c.setCpf(in.readLine());
					req.alterar(c);
					System.out.println("Cliente alterado com sucesso!");
				break;
			
				case 3:
					System.out.println("Informe o c�digo do cliente (-1 para sair):");
					id = 0;
					while(id == 0)
					{
						try
						{
							id = Integer.parseInt(in.readLine());
						}
						catch(Exception ex) { System.out.println("Digite um n�mero v�lido!"); }
					}
					if(id == -1) { continue; }
					c = req.buscarPeloId(id, "cliente");
					if(c == null)
					{
						 System.out.println("Cliente n�o encontrado no banco de dados!");
						 continue;
					}
					req.excluir(c);
					System.out.println("Cliente exclu�do com sucesso!");
				break;
				
				case 4:
					System.out.println("Informe o c�digo do cliente (-1 para sair):");
					id = 0;
					while(id == 0)
					{
						try
						{
							id = Integer.parseInt(in.readLine());
						}
						catch(Exception ex) { System.out.println("Digite um n�mero v�lido!"); }
					}
					if(id == -1) { continue; }
					c = req.buscarPeloId(id, "cliente");
					if(c == null)
					{
						 System.out.println("Cliente n�o encontrado no banco de dados!");
						 continue;
					}
					System.out.println("ID\tNOME\t\t\t\t\tCPF");
					System.out.println(c.toString());
				break;
					
				case 5:
					System.out.println("Informe o nome do cliente:");
					String nome = in.readLine();
					clientes = req.buscarPorString(nome, "cliente", "nome");
					if(clientes == null) continue;
					System.out.println("ID\tNOME\t\t\t\t\tCPF");
					for(Cliente cli : clientes)
					{
						System.out.println(cli.toString());
					}
				break;
					
				case 6:
					System.out.println("Informe o cpf do cliente:");
					String cpf = in.readLine();
					clientes = req.buscarPorString(cpf, "cliente", "cpf");
					if(clientes == null) continue;
					System.out.println("ID\tNOME\t\t\t\t\tCPF");
					for(Cliente cli : clientes)
					{
						System.out.println(cli.toString());
					}
				break;
				
				case 9:
					
				break;
				
				default:
					System.out.println("Comando inv�lido!");
				break;
			}
		}while(opcao != 9);
	}
	
	private static void tratarMenuProdutos(SimpleClient client, BufferedReader in) throws NotImplementedException, InterruptedException, TimeoutException, Exception
	{
		ServerRequest<Produto> req = new ServerRequest<Produto>(client);
		int opcao = 0;
		Produto p = null;
		HashSet<Produto> produtos = null;
		
		do
		{
			System.out.println("Selecione uma opera��o:");
			System.out.println("1. Novo Produto");
			System.out.println("2. Alterar Produto");
			System.out.println("3. Excluir Produto");
			System.out.println("4. Consultar produto pelo id");
			System.out.println("5. Consultar produto pela descri��o");
			System.out.println("9. Voltar");
			try
			{
				opcao = Short.parseShort(in.readLine());
			}
			catch(Exception ex) { System.out.println("Comando inv�lido!"); continue; }
			
			switch(opcao)
			{
				//inclus�o
				case 1:
					System.out.println("Informe os dados do produto:");
					p = new Produto();
					System.out.println("Descri��o: ");
					p.setDescricao(in.readLine());
					//pre�o
					BigDecimal preco = null;
					while(preco == null)
					{
						System.out.println("Pre�o: ");
						try
						{
							preco = new BigDecimal(in.readLine());
						}
						catch(Exception ex) { System.out.println("Digite um valor v�lido!"); }
					}
					p.setPreco(preco);
					//estoque
					Integer estoque = null;
					while(estoque == null)
					{
						System.out.println("Estoque: ");
						try
						{
							estoque = Integer.parseInt(in.readLine());
						}
						catch(Exception ex) { System.out.println("Digite um valor v�lido!"); }
					}
					p.setEstoque(estoque);
					req.incluir(p);
					System.out.println("Produto inclu�do com sucesso!");
				break;
				//altera��o
				case 2:
					System.out.println("Informe o c�digo do produto (-1 para sair):");
					long id = 0;
					while(id == 0)
					{
						try
						{
							id = Integer.parseInt(in.readLine());
						}
						catch(Exception ex) { System.out.println("Digite um n�mero v�lido!"); }
					}
					if(id == -1) { continue; }
					p = req.buscarPeloId(id, "produto");
					if(p == null)
					{
						 System.out.println("Produto n�o encontrado no banco de dados!");
						 continue;
					}
					System.out.println("Informe os novos dados do produto:");
					System.out.println("Descri��o (" + p.getDescricao() + "): ");
					p.setDescricao(in.readLine());
					//pre�o
					preco = null;
					while(preco == null)
					{
						System.out.println("Pre�o (" + p.getPreco() + "): ");
						try
						{
							preco = new BigDecimal(in.readLine());
						}
						catch(Exception ex) { System.out.println("Digite um valor v�lido!"); }
					}
					p.setPreco(preco);
					//estoque
					estoque = null;
					while(estoque == null)
					{
						System.out.println("Estoque (" + p.getEstoque() + "): ");
						try
						{
							estoque = Integer.parseInt(in.readLine());
						}
						catch(Exception ex) { System.out.println("Digite um valor v�lido!"); }
					}
					p.setEstoque(estoque);
					req.alterar(p);
					System.out.println("Produto alterado com sucesso!");
				break;
			
				case 3:
					System.out.println("Informe o c�digo do produto (-1 para sair):");
					id = 0;
					while(id == 0)
					{
						try
						{
							id = Integer.parseInt(in.readLine());
						}
						catch(Exception ex) { System.out.println("Digite um n�mero v�lido!"); }
					}
					if(id == -1) { continue; }
					p = req.buscarPeloId(id, "produto");
					if(p == null)
					{
						 System.out.println("Produto n�o encontrado no banco de dados!");
						 continue;
					}
					req.excluir(p);
					System.out.println("Produto exclu�do com sucesso!");
				break;
				
				case 4:
					System.out.println("Informe o c�digo do produto (-1 para sair):");
					id = 0;
					while(id == 0)
					{
						try
						{
							id = Integer.parseInt(in.readLine());
						}
						catch(Exception ex) { System.out.println("Digite um n�mero v�lido!"); }
					}
					if(id == -1) { continue; }
					p = req.buscarPeloId(id, "produto");
					if(p == null)
					{
						 System.out.println("Produto n�o encontrado no banco de dados!");
						 continue;
					}
					System.out.println("ID\tDESCRI��O\t\t\t\tPRE�O\t\t\tESTOQUE");
					System.out.println(p.toString());
				break;
					
				case 5:
					System.out.println("Informe a descri��o do produto:");
					String descricao = in.readLine();
					produtos = req.buscarPorString(descricao, "produto", "descricao");
					if(produtos == null) continue;
					System.out.println("ID\tDESCRI��O\t\t\t\tPRE�O\t\t\tESTOQUE");
					for(Produto prod : produtos)
					{
						System.out.println(prod.toString());
					}
				break;
				
				case 9:
					
				break;
				
				default:
					System.out.println("Comando inv�lido!");
				break;
			}
		}while(opcao != 9);
	}
}
