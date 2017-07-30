package com.trivecta.zipryde.model.dao;

import java.util.Date;

import javax.persistence.NoResultException;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.trivecta.zipryde.model.entity.ZiprydeConfiguration;

@Repository
public class ZiprydeConfigurationDAOImpl implements ZiprydeConfigurationDAO{

	@Autowired
	private SessionFactory sessionFactory;
	
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	public ZiprydeConfiguration getZiprydeConfigurationByType(String type) {
		Session session = this.sessionFactory.getCurrentSession();
		ZiprydeConfiguration ziprydeConfig = null;
		try {
			ziprydeConfig = (ZiprydeConfiguration) session.getNamedQuery("ZiprydeConfiguration.findByType").setParameter("type", type).getSingleResult();
		}
		catch(NoResultException e){
			//No data found
			e.printStackTrace();
		}
		return ziprydeConfig;
	}
	
	public ZiprydeConfiguration saveZiprydeConfiguration(ZiprydeConfiguration ziprydeConfiguration) {
		Session session = this.sessionFactory.getCurrentSession();
		if(ziprydeConfiguration.getId() == null) {
			ziprydeConfiguration.setCreationDate(new Date());
			session.save(ziprydeConfiguration);
			return ziprydeConfiguration;
		}
		else {
			ZiprydeConfiguration origConfig = session.find(ZiprydeConfiguration.class, ziprydeConfiguration.getId());
			origConfig.setAccessKey(ziprydeConfiguration.getAccessKey());
			origConfig.setUrl(ziprydeConfiguration.getUrl());
			origConfig.setModifiedDate(new Date());
			origConfig = (ZiprydeConfiguration) session.merge(origConfig);
			return origConfig;
		}
	}
}
