package cdp;

import java.io.Serializable;
import java.math.BigDecimal;

public class ItemPedido implements Serializable {
	private ItemPedidoPK id;
	private Pedido pedido;
	private Produto produto;
	private Integer quantidade;
	private BigDecimal precoUnitario;
	private BigDecimal desconto;

	
	public ItemPedidoPK getId() {
		return id;
	}
	public void setId(ItemPedidoPK id) {
		this.id = id;
	}
	
	private transient BigDecimal totalDoItem;
	
	public ItemPedido() { }

	public ItemPedido(Produto produto, Integer quantidade) {
		setProduto(produto);
		this.quantidade = quantidade;
		setPrecoUnitario(produto.getPreco());
	}
	public ItemPedido(Pedido pedido, Produto produto, Integer quantidade) {
		setPedido(pedido);
		setProduto(produto);
		this.quantidade = quantidade;
		setPrecoUnitario(produto.getPreco());
	}

	public void setPedido(Pedido pedido) {
		if (this.id==null) this.id = new ItemPedidoPK();
		id.setPedido(pedido);
		this.pedido = pedido;
	}
	public Pedido getPedido() {
		return this.pedido;
	}
	
	public void setProduto(Produto produto) {
		if (this.id==null) this.id = new ItemPedidoPK();
		id.setProduto(produto);
		this.produto = produto;
		this.precoUnitario = produto.getPreco();
	}
	public Produto getProduto() {
		return produto;
	}		

	public Integer getQuantidade() {
		return quantidade;
	}
	public void setQuantidade(Integer quantidade) {
		this.quantidade = quantidade;
	}

	public void setPrecoUnitario(BigDecimal precoUnitario) {
		this.precoUnitario = precoUnitario.setScale(2, BigDecimal.ROUND_HALF_UP);
	}
	public void setPrecoUnitario(Double precoUnitario) {
		this.precoUnitario = new BigDecimal(precoUnitario).setScale(2, BigDecimal.ROUND_HALF_UP);
	}
	public void setPrecoUnitario(double precoUnitario) {
		this.setPrecoUnitario(new Double(precoUnitario));
	}
	public BigDecimal getPrecoUnitario() {
		return precoUnitario;
	}

	public BigDecimal getDesconto() {
		return desconto;
	}
	public void setDesconto(BigDecimal desconto) {
		this.desconto = desconto;
	}	
	
	public BigDecimal getTotalItem() {
		totalDoItem = this.getPrecoUnitario()
				.multiply(new BigDecimal(quantidade).setScale(2, BigDecimal.ROUND_HALF_UP))
				.multiply(new BigDecimal(100L).subtract(this.getDesconto()).divide(new BigDecimal(100L)).setScale(2, BigDecimal.ROUND_HALF_UP));
		return totalDoItem;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ItemPedido other = (ItemPedido) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ItemPedido [pedido=" + pedido.getId() + ", produto=" + produto +  ", precoUnitario=" + precoUnitario
				+ ", quantidade=" + quantidade + ", desconto=" + desconto + "]";
	}
}
