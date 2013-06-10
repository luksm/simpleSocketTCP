package db.dao;

import java.util.ArrayList;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;

import cdp.Cliente;
import cdp.Pedido;

public class PedidoDAO extends BaseDAO<Pedido> {
	
	public PedidoDAO()
	{
		super(Pedido.class);
	}
	
	@SuppressWarnings("unchecked")
	public ArrayList<Pedido> buscarPeloCliente(Cliente cliente) throws DAOException
	{
		ArrayList<Pedido> lista = null;
		try
		{
			Session session = getSession();
			String sql = "SELECT * FROM TB_PEDIDO WHERE ID_CLIENTE=" + cliente.getId();
			SQLQuery query = session.createSQLQuery(sql);
			lista = new ArrayList<Pedido>(query.list());
		}
		catch(HibernateException e) { throw new DAOException(e); }
		return lista;
	}
	
	public boolean pedidoProcessado(Pedido pedido) throws DAOException
	{
		ArrayList<Pedido> lista = null;
		try
		{
			Session session = getSession();
			String sql = "SELECT DATA_PROC FROM TB_PEDIDO WHERE ID_PEDIDO=" + pedido.getId();
			SQLQuery query = session.createSQLQuery(sql);
			lista = new ArrayList<Pedido>(query.list());
			if(lista != null && lista.size() == 1)
			{
				return !(lista.get(0).getDataProcessamento() == null);
			}
			else
			{
				throw new DAOException("Pedido não existe!");
			}
		}
		catch(HibernateException e) { throw new DAOException(e); }
	}
}
