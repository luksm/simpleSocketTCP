package db.dao;

import java.util.ArrayList;

import org.hibernate.Query;
import org.hibernate.Session;

import cdp.Cliente;

public class ClienteDAO extends BaseDAO<Cliente> {
	
	public ClienteDAO()
	{
		super(Cliente.class);
	}
	
	@SuppressWarnings("unchecked")
	public ArrayList<Cliente> buscarPeloNome(String nome)
	{
		Session session = getSession();
		String sql = "FROM Cliente WHERE nome LIKE '" + nome + "%' ORDER BY nome ASC";
		Query query = session.createQuery(sql);
		ArrayList<Cliente> lista = new ArrayList<Cliente>(query.list());
		return lista;
	}
	
	@SuppressWarnings("unchecked")
	public ArrayList<Cliente> buscarPeloCpf(String cpf)
	{
		Session session = getSession();
		String sql = "FROM Cliente WHERE cpf LIKE '" + cpf + "%' ORDER BY nome ASC";
		Query query = session.createQuery(sql);
		ArrayList<Cliente> lista = new ArrayList<Cliente>(query.list());
		return lista;
	}
}
