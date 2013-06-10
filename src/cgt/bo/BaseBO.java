package cgt.bo;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import db.dao.BaseDAO;
import db.dao.DAO;
import db.dao.DAOException;
import db.util.DBUtil;

public class BaseBO<T> {

	private SessionFactory sessionFactory;
	private DAO<T> dao;
	
	public BaseBO(@SuppressWarnings("rawtypes") Class clazz) {
		sessionFactory = DBUtil.getSessionFactory();
		this.dao = new BaseDAO<T>(clazz);
	}

	public BaseBO(SessionFactory sessionFactory, @SuppressWarnings("rawtypes") Class clazz) {
		this.sessionFactory = sessionFactory;
		this.dao = new BaseDAO<T>(clazz);
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

	public void delete(T object) throws BOException {
		try {
			dao.delete(object);
		} catch (DAOException e) {
			e.printStackTrace();
			throw new BOException(e);
		}
	}
	
	public void save(T object) throws BOException {
		try {
			dao.save(object);
		} catch (DAOException e) {
			e.printStackTrace();
			throw new BOException(e);
		}
	}

	public T saveOrUpdate(T object) throws BOException {
		try {
			return dao.saveOrUpdate(object);
		} catch (DAOException e) {
			e.printStackTrace();
			throw new BOException(e);
		}
	}

	public T selectById(long id) throws BOException {
		try {
			return dao.selectById(id);
		} catch (DAOException e) {
			e.printStackTrace();
			throw new BOException(e);
		}
	}

	public void update(T object) throws BOException {
		try {
			dao.update(object);
		} catch (DAOException e) {
			e.printStackTrace();
			throw new BOException(e);
		}
	}

}
