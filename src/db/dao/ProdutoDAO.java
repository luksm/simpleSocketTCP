package db.dao;

import java.util.ArrayList;

import org.hibernate.Query;
import org.hibernate.Session;

import cdp.Produto;

public class ProdutoDAO extends BaseDAO<Produto> {
	
	public ProdutoDAO()
	{
		super(Produto.class);
	}
	
	@SuppressWarnings("unchecked")
	public ArrayList<Produto> buscarPelaDescricao(String descricao)
	{
		Session session = getSession();
		String sql = "FROM Produto WHERE descricao LIKE '" + descricao + "%' ORDER BY descricao ASC";
		Query query = session.createQuery(sql);
		ArrayList<Produto> lista = new ArrayList<Produto>(query.list());
		return lista;
	}
}
