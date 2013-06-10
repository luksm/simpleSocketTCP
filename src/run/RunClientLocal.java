package run;

import cdp.Cliente;
import cdp.ItemPedido;
import cdp.Pedido;
import cdp.Produto;
import client.ServerRequest;
import client.SimpleClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.Calendar;

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
		int port = 6666;
		String host = "LOCALHOST";
		boolean gotMessage;
		String resposta = null;
		String comando = null;
		
		// A linha a seguir define um leitor de teclado
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		
		// Crio o SimpleClient que conversa com o nosso servidor
		SimpleClient client = new SimpleClient(host,port);
		client.Connect();
		if (client.isConnected()) {
			do {
				try {
					ServerRequest<Cliente> reqCli = new ServerRequest<Cliente>(client);
					ServerRequest<Produto> reqPro = new ServerRequest<Produto>(client);
					ServerRequest<Pedido> reqPed = new ServerRequest<Pedido>(client);
					System.out.println("consultando cliente");
					Cliente c = reqCli.buscarPeloId(1L, "cliente");
					System.out.println("cliente: " + c.getNome());
					Produto p = reqPro.buscarPeloId(5L, "produto");
					System.out.println("produto: " + p.getDescricao());
					Pedido ped = new Pedido();
					ped.setCliente(c);
					ped.setDataEmissao(Calendar.getInstance().getTime());
					ped.setDataProcessamento(Calendar.getInstance().getTime());
					ped.setTotalPedido(new BigDecimal(30));
					ItemPedido iped = new ItemPedido();
					iped.setProduto(p);
					iped.setQuantidade(2);
					iped.setPrecoUnitario(new BigDecimal(3));
					ped.getItens().add(iped);
					System.out.println("incluindo pedido..");
					reqPed.incluir(ped);
					System.out.println("pedido incluido..");
				} catch (Exception e) {
					e.printStackTrace();
				}
				finally
				{
					comando = "CLOSE";
				}
			} while (!comando.trim().equalsIgnoreCase("CLOSE"));
			client.close();
		} else
			System.out.println("Falha - Cliente nao conectado.");
	}
}
