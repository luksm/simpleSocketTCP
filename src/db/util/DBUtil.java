package db.util;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;

/**
 * O papel desta classe é vincular a aplicação à camada de persistência.
 * 
 * @author fabianmartins
 * 
 */
public class DBUtil {

	private static final SessionFactory sessionFactory = buildSessionFactory();

	/**
	 * Este método inicializa o Hibernate, fornecendo a ele o arquivo de
	 * configuração 'resource/hibernate.cfg.xml' que inicializara' a
	 * SessionFactory
	 * 
	 * @return
	 */
	private static SessionFactory buildSessionFactory() {
		try {
			Configuration configuration = new Configuration();
			configuration.configure("resource/hibernate.cfg.xml");
			ServiceRegistry serviceRegistry = new ServiceRegistryBuilder().applySettings(configuration.getProperties()).buildServiceRegistry();        
			return configuration.buildSessionFactory(serviceRegistry);
		} catch (Throwable ex) {
			System.err.println("Erro na inicializacao da SessionFactory" + ex);
			throw new ExceptionInInitializerError(ex);
		}
	}

	public static SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	/**
	 * Solicita à SessionFactory que produza uma sessão, solicitando-a ao
	 * ConnectionProvider. A classe ConnectionProvider, que conhece as
	 * configurações as características do SGBD cuida de gerar uma nova conexão
	 * ou buscar uma conexão no pool, entregando-a dentro do escopo de uma
	 * sessão.
	 * 
	 * O trabalho da SessionFactory e da ConnectionProvider é transparente para
	 * as classes que conversam com elas.
	 * 
	 * É importante lembrar que sempre que for realizado um commit ou rollback,
	 * a sessão é fechada.
	 * 
	 * @return
	 */
	public static Session getCurrentSession() {
		Session session = sessionFactory.getCurrentSession();
		if (!session.isOpen())
			session = sessionFactory.openSession();
		return session;
	}

}