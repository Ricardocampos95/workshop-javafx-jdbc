package model.services;

import java.util.ArrayList;
import java.util.List;

import model.entites.Department;

public class DepartmentService {
	
	public List<Department> findAll(){
		
		List<Department> list = new ArrayList<>();
		
		list.add(new Department(1, "Books"));
		list.add(new Department(2, "Computers"));
		list.add(new Department(3, "Electronics"));
		list.add(new Department(4, "Home"));
		list.add(new Department(5, "Car"));
		return list;
		
	}

}
