package com.trivecta.zipryde.model.dao;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.trivecta.zipryde.model.entity.Nyop;
import com.trivecta.zipryde.model.entity.PricingMstr;

@Repository
public class PricingDAOImpl implements PricingDAO {

	@Autowired
	private SessionFactory sessionFactory;
	
	@Autowired
	private AdminDAO adminDAO;
	
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	public List<PricingMstr> getAllPricingMstr() {
		Session session = this.sessionFactory.getCurrentSession();

		List<PricingMstr> pricingMstrList = session.getNamedQuery("PricingMstr.findAll").getResultList();
		return pricingMstrList;
	}
	
	public BigDecimal calculatePricingByTypeAndDistance(int NoOfMiles, int cabTypeId) {
		Session session = this.sessionFactory.getCurrentSession();

		PricingMstr pricingMstr = (PricingMstr) session.getNamedQuery("PricingMstr.findByCabTypeId")
				.setParameter("cabTypeId", cabTypeId).getSingleResult();

		BigDecimal price = pricingMstr.getPricePerUnit().multiply(new BigDecimal(NoOfMiles));

		return price;
	}
	
	public Map<Integer,BigDecimal> getAllNYOPByCabTypeAndDistance(int NoOfMiles, int cabTypeId) {
		
		Map<Integer,BigDecimal> priceMap = new HashMap<Integer, BigDecimal>();
		
		List<Nyop> nyopList = adminDAO.getAllNyopList();
		
		BigDecimal calculatedFare = calculatePricingByTypeAndDistance(NoOfMiles,cabTypeId);
		priceMap.put(0,calculatedFare );
		
		if(nyopList != null && nyopList.size() > 0) {
			for(Nyop nyop : nyopList) {
				BigDecimal percentage = new BigDecimal(nyop.getPercentage()).divide(new BigDecimal(100));
				BigDecimal percentageFare = calculatedFare.subtract(calculatedFare.multiply(percentage));
				priceMap.put(nyop.getPercentage(), percentageFare);
			}
		}
		return priceMap;
	}
}
