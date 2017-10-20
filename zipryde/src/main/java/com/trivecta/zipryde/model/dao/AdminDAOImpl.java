package com.trivecta.zipryde.model.dao;

import java.util.List;

import javax.persistence.NoResultException;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.trivecta.zipryde.constants.ErrorMessages;
import com.trivecta.zipryde.framework.exception.UserValidationException;
import com.trivecta.zipryde.model.entity.CabType;
import com.trivecta.zipryde.model.entity.Make;
import com.trivecta.zipryde.model.entity.Model;
import com.trivecta.zipryde.model.entity.Nyop;
import com.trivecta.zipryde.model.entity.PricingMstr;
import com.trivecta.zipryde.model.entity.Status;
import com.trivecta.zipryde.model.entity.UserType;

@Repository
public class AdminDAOImpl implements AdminDAO {

	@Autowired
	private SessionFactory sessionFactory;
		
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	public List<Make> getAllMake() {
		Session session = this.sessionFactory.getCurrentSession();
		List<Make> makeList = session.getNamedQuery("Make.findAll").getResultList();
		return makeList;
	}
	
	public List<Make> getAllEnabledMake() {
		Session session = this.sessionFactory.getCurrentSession();
		List<Make> makeList = session.getNamedQuery("Make.findAllEnabled").getResultList();
		return makeList;
	}
	
	public List<Model> getAllModelByMakeId(int makeId) {
		Session session = this.sessionFactory.getCurrentSession();
		List<Model> modelList = session.getNamedQuery("Model.findByMakeId").
				setParameter("makeId", makeId).getResultList();
		return modelList;
	}
	
	public List<CabType> getAllCabTypes() {
		Session session = this.sessionFactory.getCurrentSession();
		List<CabType> cabTypeList = session.getNamedQuery("CabType.findAll").getResultList();
		
		for(CabType cabType : cabTypeList){
			if(cabType.getPricingMstrs() != null) {
				cabType.getPricingMstrs().size();	
			}
		}
		return cabTypeList;
	}
	
	public List<UserType> getAllUserTypes() {
		Session session = this.sessionFactory.getCurrentSession();
		List<UserType> userTypeList = session.getNamedQuery("UserType.findAll").getResultList();
		return userTypeList;
	}
	
	public List<Nyop> getAllNyopList() {
		Session session = this.sessionFactory.getCurrentSession();
		List<Nyop> pricePercentageList = session.getNamedQuery("Nyop.findAll").getResultList();
		return pricePercentageList;
	}
	
	public Status findByStatus(String status){
		Session session = this.sessionFactory.getCurrentSession();
		Status statusEntity = (Status)
				session.getNamedQuery("Status.findByStatus").
				setParameter("status", status).getSingleResult();
		return statusEntity;		
	}
	public Make getMakeByMakeId(Integer makeId) {
		Session session = this.sessionFactory.getCurrentSession();
		Make origMake = session.find(Make.class, makeId);
		return origMake;
	}
	
	public Model getModelByModelId(Integer modelId) {
		Session session = this.sessionFactory.getCurrentSession();
		Model origModel = session.find(Model.class, modelId);		
		origModel.getMake();
		return origModel;
	}
	
	public Make saveMake(Make make) throws UserValidationException {
		Session session = this.sessionFactory.getCurrentSession();
		if(make.getId() != null) {
			Make existMake = getMakeByMakeName(make.getMake());
			if( existMake == null || (make.getId() == existMake.getId())) {
				Make origMake = session.find(Make.class, make.getId());
				origMake.setMake(make.getMake());
				origMake.setIsEnable(make.getIsEnable());
				session.merge(origMake);
				return origMake;
			}
			else {
				throw new UserValidationException(ErrorMessages.MAKE_ALREADY_EXISTS);
			}			
		}
		else {
			session.save(make);
			return make;
		}
	}
	
	private Make getMakeByMakeName(String make) {
		Session session = this.sessionFactory.getCurrentSession();
		Make origMake = null;
		try {
			origMake = (Make)session.getNamedQuery("Make.findByMake").setParameter("make", make).getSingleResult();
		}
		catch(NoResultException e){
			// no result
		}
		return origMake;
	}
	
	private Model getModelByModelNameAndMakeId(String model,Integer makeId) {
		Session session = this.sessionFactory.getCurrentSession();
		Model origModel = null;
		try {
			origModel = (Model)session.getNamedQuery("Model.findByMakeIdAndModelName").setParameter("makeId", makeId).setParameter("model", model).getSingleResult();
		}
		catch(NoResultException e){
			// no result
		}
		return origModel;
	}
	
	public Model saveModel(Model model) throws UserValidationException {
		Session session = this.sessionFactory.getCurrentSession();
		if(model.getId() != null) {
			Model existModel = getModelByModelNameAndMakeId(model.getModel(),model.getMake().getId());
			if( existModel == null || (existModel.getId() == model.getId())) {
				Model origModel = session.find(Model.class, model.getId());			
				origModel.setIsEnable(model.getIsEnable());
				origModel.setModel(model.getModel());
				if(origModel.getMake().getId() != model.getMake().getId()) {
					Make origMake = session.find(Make.class, model.getMake().getId());
					origModel.setMake(origMake);
				}
				session.merge(origModel);
				return origModel;
			}
			else {
				throw new UserValidationException(ErrorMessages.MODEL_ALREADY_EXISTS);
			}					
		}
		else {
			Make origMake = session.find(Make.class, model.getMake().getId());
			model.setMake(origMake);
			session.save(model);
			return model;
		}
	}

	public List<Model> getAllModel() {
		Session session = this.sessionFactory.getCurrentSession();
		List<Model> modelList = session.getNamedQuery("Model.findAll").getResultList();
		return modelList;
	}
}
