package db.dao;

public class DAOException extends Exception  {

	public DAOException(String e)
	{
		super(e);
	}
	
	public DAOException(Throwable e) {
		super(e);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
