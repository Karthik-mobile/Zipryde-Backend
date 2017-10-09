package com.trivecta.zipryde.model.dao;

import java.util.Date;
import java.util.List;

import javax.persistence.NoResultException;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.trivecta.zipryde.constants.ErrorMessages;
import com.trivecta.zipryde.constants.ZipRydeConstants;
import com.trivecta.zipryde.framework.exception.UserValidationException;
import com.trivecta.zipryde.model.entity.AppVersion;
import com.trivecta.zipryde.model.entity.ZiprydeConfiguration;
import com.trivecta.zipryde.model.entity.ZiprydeMstr;

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
	
	public ZiprydeConfiguration saveZiprydeConfiguration(ZiprydeConfiguration ziprydeConfiguration) throws UserValidationException {
		Session session = this.sessionFactory.getCurrentSession();
		if(ziprydeConfiguration.getId() == null) {
			ZiprydeConfiguration origConfiguration = getZiprydeConfigurationByType(ziprydeConfiguration.getType());
			if(origConfiguration != null) {
				throw new UserValidationException(ErrorMessages.CONFIGURATION_TYPE_ALREADY_EXISTS);
			}
			ziprydeConfiguration.setCreationDate(new Date());
			session.save(ziprydeConfiguration);
			return ziprydeConfiguration;
		}
		else {
			ZiprydeConfiguration origConfig = session.find(ZiprydeConfiguration.class, ziprydeConfiguration.getId());
			origConfig.setAccessKey(ziprydeConfiguration.getAccessKey());
			origConfig.setUrl(ziprydeConfiguration.getUrl());
			if(ZipRydeConstants.NOTIFICATION_CONFIG_TYPE.TWILIO_SMS.equalsIgnoreCase(ziprydeConfiguration.getType())) {
				origConfig.setAccoutSID(ziprydeConfiguration.getAccoutSID());
				origConfig.setTwilioNo(ziprydeConfiguration.getTwilioNo());
			}
			origConfig.setModifiedDate(new Date());
			origConfig = (ZiprydeConfiguration) session.merge(origConfig);
			return origConfig;
		}
	}
	
	public List<ZiprydeConfiguration> getAllZiprydeConfigurations() {
		Session session = this.sessionFactory.getCurrentSession();
		List<ZiprydeConfiguration> ziprydeConfigList = 
				session.getNamedQuery("ZiprydeConfiguration.findAll").getResultList();
		return ziprydeConfigList;
	}
	
	public ZiprydeMstr  getZiprydeMstrByType(String type){
		Session session = this.sessionFactory.getCurrentSession();
		ZiprydeMstr ziprydeMstr = null;
		try{
			ziprydeMstr = (ZiprydeMstr) session.getNamedQuery("ZiprydeMstr.findByType").
					setParameter("type", type).getSingleResult();
		}
		catch(NoResultException e){
			//No Result
		}
		return ziprydeMstr;		
	}
	
	public String  getZiprydeMstrValueByType(String type){
		Session session = this.sessionFactory.getCurrentSession();
		String zipMstrValue = null;
		try{
			ZiprydeMstr ziprydeMstr = (ZiprydeMstr) session.getNamedQuery("ZiprydeMstr.findByType").
					setParameter("type", type).getSingleResult();
			zipMstrValue = ziprydeMstr.getValue();					
		}
		catch(NoResultException e){
			//No Result
		}
		return zipMstrValue;		
	}
	
	public AppVersion getAppVersionByMobileOS(String appMobileOs) {
		Session session = this.sessionFactory.getCurrentSession();
		AppVersion appVersion = null;
		try {
			appVersion =  (AppVersion)session.getNamedQuery("AppVersion.findByMobileOS")
					.setParameter("appMobileOS", appMobileOs).getSingleResult();
		}
		catch(NoResultException e){
			//No Result
		}
		return appVersion;
	}
	
	public AppVersion getAppVersionByMobileOSVersionName(String appMobileOs,String versionName) {
		Session session = this.sessionFactory.getCurrentSession();
		AppVersion appVersion = null;
		try {
			appVersion =  (AppVersion)session.getNamedQuery("AppVersion.findByMobileOSVersionName")
					.setParameter("appMobileOS", appMobileOs).setParameter("versionName", versionName).getSingleResult();
		}
		catch(NoResultException e){
			//No Result
		}
		return appVersion;
	}
}
