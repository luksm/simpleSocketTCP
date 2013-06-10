package cgt.bo;

import java.util.ArrayList;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import cdp.Cliente;
import cdp.Produto;

import db.dao.BaseDAO;
import db.dao.DAO;
import db.dao.DAOException;
import db.dao.ProdutoDAO;
import db.util.DBUtil;

public class ProdutoBO {
	private SessionFactory sessionFactory;
	private ProdutoDAO dao;
	
	public ProdutoBO() {
		sessionFactory = DBUtil.getSessionFactory();
		this.dao = new ProdutoDAO();
	}

	public ProdutoBO(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
		this.dao = new ProdutoDAO();
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

	public void delete(Produto object) throws BOException {
		try {
			dao.delete(object);
		} catch (DAOException e) {
			e.printStackTrace();
			throw new BOException(e);
		}
	}
	
	public void save(Produto object) throws BOException {
		try {
			if(object.getEstoque() < 10) throw new BOException("O estoque mínimo para cadastro é de 10 itens!");
			dao.save(object);
		} catch (DAOException e) {
			e.printStackTrace();
			throw new BOException(e);
		}
	}

	public Produto saveOrUpdate(Produto object) throws BOException {
		try {
			return dao.saveOrUpdate(object);
		} catch (DAOException e) {
			e.printStackTrace();
			throw new BOException(e);
		}
	}

	public Produto selectById(long id) throws BOException {
		try {
			return dao.selectById(id);
		} catch (DAOException e) {
			e.printStackTrace();
			throw new BOException(e);
		}
	}

	public void update(Produto object) throws BOException {
		try {
			dao.update(object);
		} catch (DAOException e) {
			e.printStackTrace();
			throw new BOException(e);
		}
	}
	
	public ArrayList<Produto> buscarPelaDescricao(String descricao)
	{
		return dao.buscarPelaDescricao(descricao);
	}
}