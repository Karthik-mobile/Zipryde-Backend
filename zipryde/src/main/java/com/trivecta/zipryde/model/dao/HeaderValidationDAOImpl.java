package com.trivecta.zipryde.model.dao;

import javax.persistence.NoResultException;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.trivecta.zipryde.constants.ErrorMessages;
import com.trivecta.zipryde.framework.exception.SessionExpiredException;
import com.trivecta.zipryde.model.entity.UserSession;

@Repository
public class HeaderValidationDAOImpl implements HeaderValidationDAO{

	@Autowired
	private SessionFactory sessionFactory;
	
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	public Boolean validateHeaderAccessToken(String accessToken) throws SessionExpiredException{
		Session session = this.sessionFactory.getCurrentSession();		
		try {
			Integer userId = (Integer) session.getNamedQuery("UserSession.findBySessionToken")
					.setParameter("sessionToken", accessToken).getSingleResult();
			return true;
		}
		catch(NoResultException e){
			throw new SessionExpiredException(ErrorMessages.SESSION_EXPIRED);
		}		
	}
}
