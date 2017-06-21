package com.trivecta.zipryde.model.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.trivecta.zipryde.model.entity.CabType;
import com.trivecta.zipryde.model.entity.Make;
import com.trivecta.zipryde.model.entity.Model;
import com.trivecta.zipryde.model.entity.Nyop;
import com.trivecta.zipryde.model.entity.PricingMstr;
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
	
}
