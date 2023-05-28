package model.dao;

import db.DB;
import model.dao.impl.SellerDaoJDBC;

public class DaoFactory { //método que retorna o tipo da interface

	public static SellerDao createSellerDao() {
		return new SellerDaoJDBC(DB.getConnection()); //mas internamente instancia uma implementacao
		
	}
}
