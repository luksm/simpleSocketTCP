package testeLocal;

import java.math.BigDecimal;
import java.util.Calendar;

import cdp.Cliente;
import cdp.ItemPedido;
import cdp.Pedido;
import cdp.Produto;
import cgt.bo.BOException;
import cgt.bo.BaseBO;

public class Run {
	public static void main(String args[])
	{
		Cliente c = testeInclusaoCliente();
		Produto p = testeInclusaoProduto();
		Pedido ped = testeInclusaoPedido(c, p);
	}
	
	private static Cliente testeInclusaoCliente()
	{
		BaseBO<Cliente> bo = new BaseBO<Cliente>(Cliente.class);
		Cliente c = new Cliente();
		c.setNome("rafael");
		c.setCpf("12345");
		try {
			bo.beginTransaction();
			bo.save(c);
			bo.commit();
		} catch (BOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return c;
	}
	
	private static Produto testeInclusaoProduto()
	{
		BaseBO<Produto> bo = new BaseBO<Produto>(Produto.class);
		Produto p = new Produto();
		p.setPreco(new BigDecimal(10));
		p.setEstoque(4);
		p.setDescricao("Coca-Cola");
		try {
			bo.beginTransaction();
			bo.save(p);
			bo.commit();
		} catch (BOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return p;
	}
	
	private static Pedido testeInclusaoPedido(Cliente c, Produto p)
	{
		BaseBO<Pedido> bo = new BaseBO<Pedido>(Pedido.class);
		Pedido ped = new Pedido();
		ItemPedido ip = new ItemPedido();
		ped.setCliente(c);
		ped.setDataEmissao(Calendar.getInstance().getTime());
		ped.setDataProcessamento(ped.getDataEmissao());
		ped.setTotalPedido(new BigDecimal(10));
		ip.setProduto(p);
		ip.setPedido(ped);
		ip.setPrecoUnitario(new BigDecimal(10));
		ip.setQuantidade(1);
		ped.getItens().add(ip);
		
		try {
			bo.beginTransaction();
			bo.save(ped);
			bo.commit();
		} catch (BOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ped;
	}
}
