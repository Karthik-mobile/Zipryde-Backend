package com.trivecta.zipryde.model.dao;

import java.util.List;

import com.trivecta.zipryde.framework.exception.UserValidationException;
import com.trivecta.zipryde.model.entity.CabType;
import com.trivecta.zipryde.model.entity.Make;
import com.trivecta.zipryde.model.entity.Model;
import com.trivecta.zipryde.model.entity.Nyop;
import com.trivecta.zipryde.model.entity.Status;
import com.trivecta.zipryde.model.entity.UserType;

public interface AdminDAO {

	public List<Make> getAllMake();
	
	public List<Make> getAllEnabledMake();
	
	public List<Model> getAllModelByMakeId(int makeId);
	
	public List<CabType> getAllCabTypes();
	
	public List<UserType> getAllUserTypes();
	
	public List<Nyop> getAllNyopList();
	
	public Status findByStatus(String status);	

	public Make saveMake(Make make) throws UserValidationException;
	
	public Model saveModel(Model model) throws UserValidationException;
	
	public Make getMakeByMakeId(Integer makeId);
	
	public Model getModelByModelId(Integer modelId);
	
	public List<Model> getAllModel();
}
