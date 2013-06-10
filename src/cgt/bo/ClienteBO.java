package cgt.bo;

import java.util.ArrayList;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import cdp.Cliente;

import db.dao.ClienteDAO;
import db.dao.DAOException;
import db.util.DBUtil;

public class ClienteBO {
	private SessionFactory sessionFactory;
	private ClienteDAO dao;
	
	public ClienteBO() {
		sessionFactory = DBUtil.getSessionFactory();
		this.dao = new ClienteDAO();
	}
	
	public ClienteBO(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
		this.dao = new ClienteDAO();
	}

	public Session getCleanSession() {
		Session session = sessionFactory.getCurrentSession();
		if (!session.isOpen())
			session = sessionFactory.openSession();
		return session;
	}

	public void beginTransaction() throws BOException {
		try {
			getCleanSession().beginTransaction();
		} catch (HibernateException e) {
			throw new BOException(e);
		}
	}

	public void commit() throws BOException {
		try {
			sessionFactory.getCurrentSession().flush();
			sessionFactory.getCurrentSession().getTransaction().commit();
		} catch (HibernateException e) {
			throw new BOException(e);
		}
	}

	public void rollback() throws BOException {
		try {
			sessionFactory.getCurrentSession().getTransaction().rollback();
		} catch (HibernateException e) {
			throw new BOException(e);
		}
	}

	public void delete(Cliente object) throws BOException {
		try {
			dao.delete(object);
		} catch (DAOException e) {
			e.printStackTrace();
			throw new BOException(e);
		}
	}
	
	public void save(Cliente object) throws BOException {
		try {
			ArrayList<Cliente> clientes = buscarPeloCpf(object.getCpf());
			//valida se já existe um cliente com o cpf informado
			if(clientes.size() > 0) throw new BOException("Já existe um cliente com este CPF!");
			dao.save(object);
		} catch (DAOException e) {
			e.printStackTrace();
			throw new BOException(e);
		}
	}

	public Cliente selectById(long id) throws BOException {
		try {
			return dao.selectById(id);
		} catch (DAOException e) {
			e.printStackTrace();
			throw new BOException(e);
		}
	}

	public void update(Cliente object) throws BOException {
		try {
			ArrayList<Cliente> clientes = buscarPeloCpf(object.getCpf());
			//valida se já existe um cliente com o cpf informado
			if(clientes.size() > 0) throw new BOException("Já existe um cliente com este CPF!");
			dao.update(object);
		} catch (DAOException e) {
			e.printStackTrace();
			throw new BOException(e);
		}
	}
	
	public ArrayList<Cliente> buscarPeloNome(String nome)
	{
		return dao.buscarPeloNome(nome);
	}
	
	public ArrayList<Cliente> buscarPeloCpf(String cpf)
	{
		return dao.buscarPeloCpf(cpf);
	}
}
