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
import model.dao.DepartmentDao;
import model.entites.Department;
import model.entites.Seller;

public class DepartmentDaoJDBC implements DepartmentDao {

	private Connection conn;

	public DepartmentDaoJDBC(Connection conn) {
		this.conn = conn;
	}

	@Override
	public void insert(Department obj) {

		ResultSet rs = null;
		PreparedStatement st = null;

		try {

			st = conn.prepareStatement("INSERT INTO Department (Name) VALUES (?)",
					Statement.RETURN_GENERATED_KEYS);

			st.setString(1, obj.getName());

			int rowsAffected = st.executeUpdate();

			if (rowsAffected > 0) {
				rs = st.getGeneratedKeys();
				if (rs.next()) {
					int id = rs.getInt(1);
					obj.setId(id);
				}
			} else {
				throw new DbException("Unexpected error: No rows affected!");
			}

		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}

		finally {
			DB.closeResultSet(rs);
			DB.closeResultSet(rs);
		}

	}

	@Override
	public void update(Department obj) {
		ResultSet rs = null;
		PreparedStatement st = null;

		try {
			st = conn.prepareStatement("UPDATE Department SET Name = ? WHERE id = ?");

			st.setString(1, obj.getName());
			st.setInt(2, obj.getId());

			int rowsAffected = st.executeUpdate();

			if (rowsAffected > 0) {
				System.out.println("Success!");
			} else {
				throw new SQLException("No rows affected!");
			}

		} catch (SQLException e) {
			e.getMessage();
		}

		finally {
			DB.closeResultSet(rs);
			DB.closeStatement(st);
		}

	}

	@Override
	public void deleteByid(Integer id) {
		ResultSet rs = null;
		PreparedStatement st = null;

		try {
			st = conn.prepareStatement("DELETE FROM department WHERE Id = ?");

			st.setInt(1, id);

			int rowsAffected = st.executeUpdate();

			if (rowsAffected > 0) {
				System.out.println("Success! Removed id: " + id);
			} else {
				throw new DbException("Id does not exist on DataBase!");
			}

		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}

		finally {
			DB.closeResultSet(rs);
			DB.closeStatement(st);
		}

	}

	@Override
	public Department findById(Integer id) {

		ResultSet rs = null;
		PreparedStatement st = null;

		try {
			st = conn.prepareStatement("SELECT * FROM Department WHERE Id = ?");

			st.setInt(1, id);

			rs = st.executeQuery();

			if (rs.next()) {
				Department dep = instantiateDepartment(rs);
				return dep;
			}
			return null;

		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		}

		finally {
			DB.closeResultSet(rs);
			DB.closeStatement(st);
		}
	}

	@Override
	public List<Department> findAll() {
		ResultSet rs = null;
		PreparedStatement st = null;

		List<Department> list = new ArrayList<>();

		try {
			st = conn.prepareStatement("SELECT * FROM Department");

			rs = st.executeQuery();

			while (rs.next()) {
				Department dep = instantiateDepartment(rs);
				list.add(dep);
			}

		}

		catch (SQLException e) {
			throw new DbException("Error picking Departments: " + e.getMessage());
		}

		finally {
			DB.closeResultSet(rs);
			DB.closeStatement(st);
		}
		return list;
	}

	private Department instantiateDepartment(ResultSet rs) throws SQLException {
		Department dep = new Department();
		dep.setId(rs.getInt("Id"));
		dep.setName(rs.getString("Name"));
		return dep;
	}

}
