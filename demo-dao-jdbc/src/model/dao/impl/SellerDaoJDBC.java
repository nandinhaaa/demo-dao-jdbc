package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import db.DB;
import db.DbException;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

public class SellerDaoJDBC implements SellerDao {

	private Connection conn;
	
	public SellerDaoJDBC (Connection conn) {
		this.conn = conn;
	}
	
	@Override
	public void insert(Seller obj) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(
					"INSERT INTO seller "
			+ "(Name, Email, BirthDate, BaseSalary, DepartmentId) "
			+ "VALUES " 
			+"(?, ?, ?, ?, ?)", 
			Statement.RETURN_GENERATED_KEYS);
			
			st.setString(1, obj.getName());
			st.setString(2, obj.getEmail());
			st.setDate(3, new java.sql.Date(obj.getBirthDate().getTime()));
			st.setDouble(4, obj.getBaseSalary());
			st.setInt(5, obj.getDepartment().getId());
			
			int rownsAffected = st.executeUpdate();
			
			if (rownsAffected >0) {
				ResultSet rs = st.getGeneratedKeys();
				if(rs.next()) {
					int id = rs.getInt(1);
					obj.setId(id);
				}
				DB.closeResultSet(rs);
			}
			else {
				throw new DbException("Unexpected error! No rowns afefected!");
			}
		}
	 catch (SQLException e) {
		 throw new DbException(e.getMessage());
	 }
		finally {
			DB.closeStatement(st);
	 }
	}

	@Override
	public void update(Seller obj) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteById(Integer id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Seller findById(Integer id) {
		PreparedStatement st =null;
		ResultSet rs= null;
		try {
			st = conn.prepareStatement(

					"SELECT seller.*,department.Name as DepName"
					+ " FROM seller INNER JOIN department "
					+ "	ON seller.DepartmentId = department.Id "
					+ "WHERE seller.Id = ?");
					
					st.setInt(1, id);
					rs = st.executeQuery();
					
					//Nosso resultSet traz os dados em tabela (nossa classe Dao transforma em 
					//obj associados/ instanciados em memória
					//apenas na pos 1 tem itens por isso do if abaixo 
					if(rs.next()) {
						Department dep = instantiateDepartment(rs);
						//com isso instanciamos um departamento e setamos os valores deles 
						Seller obj = instantiateSeller(rs,dep);
						return obj; 
						//met para retornar um vendedor por id. 
					}
					return null;
					
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}

	private Seller instantiateSeller(ResultSet rs, Department dep) throws SQLException {
		Seller obj=	new Seller();
			obj.setId(rs.getInt("Id"));
			obj.setName(rs.getString("Name"));
			obj.setEmail(rs.getString("Email"));
			obj.setBaseSalary(rs.getDouble("BaseSalary"));
			obj.setBirthDate(rs.getDate("BirthDate"));
			obj.setDepartment(dep);
			return obj;
	}//tratamento de erro ja foi feito acima, apenas propaguei a correcao;

	private Department instantiateDepartment(ResultSet rs) throws SQLException {
		Department dep=  new Department();
			dep.setId(rs.getInt("DepartmentId"));
			dep.setName(rs.getString("DepName"));
			return dep;
	} //tratamento de erro ja foi feito acima, apenas propaguei a correcao;

	@Override
	public List<Seller> findAll() {
		
		PreparedStatement st =null;
		ResultSet rs= null;
		try {
			st = conn.prepareStatement(

					"SELECT seller.*,department.Name as DepName "
					+ "FROM seller INNER JOIN department " 
					+ "	ON seller.DepartmentId = department.Id "
					+ "	ORDER BY Name " );
					
					rs = st.executeQuery();
					//percorrer enq tiver um next 
					List<Seller> list = new ArrayList<>();
					//controlar a nao repeticao do departamento com o Map 
					Map<Integer, Department> map = new HashMap<>();
					//map vazio acima para guardar qqlq depart que eu instanciar
					//cada vez que passar no while ele vai no map
					
					while (rs.next()) {
						//testando se o departamento já existe  com o mesmo ID  
						Department dep = map.get(rs.getInt("DepartmentId"));
						
						if (dep == null) {// se entrar aq significa que ele n existia ainda 
							dep = instantiateDepartment(rs);
							map.put(rs.getInt("DepartmentId"), dep);
						} // faz com que um mesmo departamento seja instanciando para 2 ou mais vendedores sem criar mais Departamentos 
						Seller obj = instantiateSeller(rs,dep);
						list.add(obj); 
			
					}
					return list;
					
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}
	

	@Override
	public List<Seller> findByDepartment(Department department) {
		PreparedStatement st =null;
		ResultSet rs= null;
		try {
			st = conn.prepareStatement(

					"SELECT seller.*,department.Name as DepName "
					+ "FROM seller INNER JOIN department " 
					+ "	ON seller.DepartmentId = department.Id "
					+ "WHERE DepartmentId = ? " 
					+ "	ORDER BY Name " );
					
					st.setInt(1, department.getId());
					
					rs = st.executeQuery();
					//percorrer enq tiver um next 
					List<Seller> list = new ArrayList<>();
					//controlar a nao repeticao do departamento com o Map 
					Map<Integer, Department> map = new HashMap<>();
					//map vazio acima para guardar qqlq depart que eu instanciar
					//cada vez que passar no while ele vai no map
					
					while (rs.next()) {
						//testando se o departamento já existe  com o mesmo ID  
						Department dep = map.get(rs.getInt("DepartmentId"));
						
						if (dep == null) {// se entrar aq significa que ele n existia ainda 
							dep = instantiateDepartment(rs);
							map.put(rs.getInt("DepartmentId"), dep);
						} // faz com que um mesmo departamento seja instanciando para 2 ou mais vendedores sem criar mais Departamentos 
						Seller obj = instantiateSeller(rs,dep);
						list.add(obj); 
			
					}
					return list;
					
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}
	}

