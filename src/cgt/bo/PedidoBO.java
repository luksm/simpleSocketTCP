package cgt.bo;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import cdp.Cliente;
import cdp.ItemPedido;
import cdp.Pedido;

import db.dao.BaseDAO;
import db.dao.DAO;
import db.dao.DAOException;
import db.dao.PedidoDAO;
import db.util.DBUtil;

public class PedidoBO {
	private SessionFactory sessionFactory;
	private PedidoDAO dao;
	
	public PedidoBO() {
		sessionFactory = DBUtil.getSessionFactory();
		this.dao = new PedidoDAO();
	}

	public PedidoBO(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
		this.dao = new PedidoDAO();
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

	public void delete(Pedido object) throws BOException {
		try {
			if(dao.pedidoProcessado(object))
			{
				throw new BOException("O pedido não pode ser EXCLUÍDO pois ele já foi processado!");
			}
			dao.delete(object);
		} catch (DAOException e) {
			e.printStackTrace();
			throw new BOException(e);
		}
	}
	
	public void save(Pedido object) throws BOException {
		try {
			dao.save(object);
		} catch (DAOException e) {
			e.printStackTrace();
			throw new BOException(e);
		}
	}
	
	public void Update(Pedido object) throws BOException {
		try {
			Pedido p = selectById(object.getId());
			if(!p.getItens().equals(object))
			{
				if(dao.pedidoProcessado(object))
				{
					throw new BOException("Os itens do pedido não podem ser alterados pois ele já foi processado!");
				}
			}
			if(!p.getCliente().equals(object.getCliente()))
			{
				throw new BOException("O cliente do pedido não pode ser alterado!");
			}
			dao.update(object);
		} catch (DAOException e) {
			e.printStackTrace();
			throw new BOException(e);
		}
	}

	public Pedido selectById(long id) throws BOException {
		try {
			return dao.selectById(id);
		} catch (DAOException e) {
			e.printStackTrace();
			throw new BOException(e);
		}
	}

	public void update(Pedido object) throws BOException {
		try {
			dao.update(object);
		} catch (DAOException e) {
			e.printStackTrace();
			throw new BOException(e);
		}
	}
	
	public ArrayList<Pedido> buscarPeloCliente(Cliente cliente) throws BOException
	{
		ArrayList<Pedido> pedidos = null;
		try {
			pedidos = dao.buscarPeloCliente(cliente);
		} catch (DAOException e) {
			e.printStackTrace();
			throw new BOException(e);
		}
		return pedidos;
	}
	
	public void processarPedido(Pedido p) throws BOException
	{
		try {
			p.setDataProcessamento(Calendar.getInstance().getTime());
			dao.save(p);
		} catch (DAOException e) {
			e.printStackTrace();
			throw new BOException(e);
		}
	}
}
