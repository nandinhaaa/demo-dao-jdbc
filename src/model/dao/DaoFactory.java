package model.dao;

import model.dao.impl.SellerDaoJDBC;

public class DaoFactory { //método que retorna o tipo da interface

	public static SellerDao createSellerDao() {
		return new SellerDaoJDBC(); //mas internamente instancia uma implementacao
		
	}
}
