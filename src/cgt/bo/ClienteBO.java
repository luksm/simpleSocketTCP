package cgt.bo;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import cdp.Cliente;

import db.dao.BaseDAO;
import db.dao.DAO;
import db.dao.DAOException;
import db.util.DBUtil;

public class ClienteBO {
	private SessionFactory sessionFactory;
	private DAO<Cliente> dao;
	
	public ClienteBO() {
		sessionFactory = DBUtil.getSessionFactory();
		this.dao = new BaseDAO<Cliente>(Cliente.class);
	}

	public ClienteBO(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
		this.dao = new BaseDAO<Cliente>(Cliente.class);
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
			dao.save(object);
		} catch (DAOException e) {
			e.printStackTrace();
			throw new BOException(e);
		}
	}

	public Cliente saveOrUpdate(Cliente object) throws BOException {
		try {
			return dao.saveOrUpdate(object);
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
			dao.update(object);
		} catch (DAOException e) {
			e.printStackTrace();
			throw new BOException(e);
		}
	}
}
