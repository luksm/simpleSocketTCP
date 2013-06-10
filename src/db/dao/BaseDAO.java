package db.dao;

import java.io.Serializable;

import org.hibernate.HibernateException;
import org.hibernate.Session;

import db.util.DBUtil;

public class BaseDAO<T> implements DAO<T> {
	
	@SuppressWarnings("rawtypes")
	private Class clazz;
	
	public BaseDAO() {
		
	}
	
	/**
	 * Construtor para instanciar um BaseDAO
	 * ex.: BaseDAO<Cliente> dao = new BaseDAO<Cliente>(Cliente.class);
	 * @param clazz
	 */
	public BaseDAO(@SuppressWarnings("rawtypes") Class clazz) {
		this.clazz = clazz;	
	}
	protected Session getSession() {
		return DBUtil.getCurrentSession();
	}

	public Serializable save(T object) throws DAOException {
		Serializable id = null;
		try {
			id = getSession().save(object);
		} catch (HibernateException e) {
			throw new DAOException(e);
		}
		return id;
	}

	@SuppressWarnings("unchecked")
	public T saveOrUpdate(T object) throws DAOException {
		T result = null;
		try {
			result = (T) getSession().merge(object);
		} catch (HibernateException e) {
			throw new DAOException(e);
		}
		return result;
	}

	public void update(T object) throws DAOException {
		try {
			getSession().update(object);
		} catch (HibernateException e) {
			throw new DAOException(e);
		}
	}

	public void delete(T object) throws DAOException {
		try {
			getSession().delete(object);
		} catch (HibernateException e) {
			throw new DAOException(e);
		}
	}

	@SuppressWarnings("unchecked")
	public T selectById(Serializable id) throws DAOException {
		T result = null;
		try {
			Session session = getSession();
			result = (T) session.get(clazz, id);
		} catch (HibernateException e) {
			throw new DAOException(e);
		}
		return result;
	}
}
