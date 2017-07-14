package com.trivecta.zipryde.model.dao;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.NoResultException;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.trivecta.zipryde.constants.ZipRydeConstants;
import com.trivecta.zipryde.constants.ZipRydeConstants.PRICINGTYPE;
import com.trivecta.zipryde.model.entity.CabType;
import com.trivecta.zipryde.model.entity.Nyop;
import com.trivecta.zipryde.model.entity.PricingMstr;
import com.trivecta.zipryde.model.entity.PricingType;

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
	
	/*public BigDecimal calculatePricingByTypeAndDistance(int NoOfMiles, int cabTypeId) {
		Session session = this.sessionFactory.getCurrentSession();

		PricingMstr pricingMstr = (PricingMstr) session.getNamedQuery("PricingMstr.findByCabTypeId")
				.setParameter("cabTypeId", cabTypeId).getSingleResult();

		BigDecimal price = pricingMstr.getPricePerUnit().multiply(new BigDecimal(NoOfMiles));

		return price;
	}*/
	
	public BigDecimal calculatePricingByTypeDistanceAndPerson(int NoOfMiles, int cabTypeId,int noOfPerson) {
		Session session = this.sessionFactory.getCurrentSession();

		List<PricingMstr> pricingMstrList = session.getNamedQuery("PricingMstr.findByCabTypeIdAndIsEnable")
				.setParameter("cabTypeId", cabTypeId).setParameter("isEnable",1).getResultList();
		
		BigDecimal price  = new BigDecimal("0.0");
		if(pricingMstrList != null && pricingMstrList.size() > 0) {
			for(PricingMstr pricingMstr : pricingMstrList) {
				if(ZipRydeConstants.PRICINGTYPE.DISTANCE.equalsIgnoreCase(pricingMstr.getPricingType().getType())){
					BigDecimal distancePrice = pricingMstr.getPricePerUnit().multiply(new BigDecimal(NoOfMiles));
					price = price.add(distancePrice);
				}
				else if(ZipRydeConstants.PRICINGTYPE.PERSON.equalsIgnoreCase(pricingMstr.getPricingType().getType())){
					BigDecimal personPrice = pricingMstr.getPrice().multiply(new BigDecimal(noOfPerson-1));
					price = price.add(personPrice);
				}
				else {
					price = price.add(pricingMstr.getPrice());
				}
			}
		}
		return price;
	}
	
	public Map<Integer,BigDecimal> getAllNYOPByCabTypeDistanceAndPerson(int NoOfMiles, int cabTypeId,int noOfPerson) {
		
		Map<Integer,BigDecimal> priceMap = new HashMap<Integer, BigDecimal>();
		
		List<Nyop> nyopList = adminDAO.getAllNyopList();
		
		BigDecimal calculatedFare = calculatePricingByTypeDistanceAndPerson(NoOfMiles,cabTypeId,noOfPerson);
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
	
	public List<PricingType> getAllEnabledPricingType() {
		Session session = this.sessionFactory.getCurrentSession();
		List<PricingType> pricingTypeList = session.getNamedQuery("PricingType.findAllByIsEnable").getResultList();
		return pricingTypeList;
	}
	
	public List<PricingMstr> getAllPricingMstrByCabType(int cabTypeId) {
		Session session = this.sessionFactory.getCurrentSession();
		List<PricingMstr> pricingMstrList = session.getNamedQuery("PricingMstr.findByCabTypeId")
				.setParameter("cabTypeId", cabTypeId).getResultList();
		if(pricingMstrList != null && pricingMstrList.size() > 0) {
			for(PricingMstr pricingMstr : pricingMstrList) {
				fetchLazyPrcingMstr(pricingMstr);
			}
		}
		return pricingMstrList;
	}
	
	public void savePricingMstrs(List<PricingMstr> pricingMstrList) {
		Session session = this.sessionFactory.getCurrentSession();
		for(PricingMstr pricingMstr : pricingMstrList) {
			PricingMstr existingPricingMstr = null;
			if(pricingMstr.getId() != null) {				
				existingPricingMstr = session.find(PricingMstr.class, pricingMstr.getId());
			}
			else{
				existingPricingMstr = 
						getPricingMstrByPricingTypeCabType(pricingMstr.getCabType().getId(),pricingMstr.getPricingType().getId());
			}
			
			if(existingPricingMstr != null) {
				existingPricingMstr.setIsEnable(pricingMstr.getIsEnable());
				if(existingPricingMstr.getPrice() != null){	
					existingPricingMstr.setPrice(pricingMstr.getPrice());
				}
				else{
					//From Request, PricePerUnit also will set in Price
					existingPricingMstr.setPricePerUnit(pricingMstr.getPrice());
				}
				session.merge(existingPricingMstr);
			}
			else {
				pricingMstr.setUnit("Miles");
				CabType cabType = session.find(CabType.class, pricingMstr.getCabType().getId());
				pricingMstr.setCabType(cabType);
				PricingType pricingType = session.find(PricingType.class, pricingMstr.getPricingType().getId()); 
				pricingMstr.setPricingType(pricingType);
				
				if(PRICINGTYPE.DISTANCE.equalsIgnoreCase(pricingType.getType())) {
					pricingMstr.setPricePerUnit(pricingMstr.getPrice());
					pricingMstr.setPrice(null);
				}
				session.save(pricingMstr);
			}
		}
	}
	
	private PricingMstr getPricingMstrByPricingTypeCabType(int cabTypeId,int pricingTypeId){
		Session session = this.sessionFactory.getCurrentSession();
		PricingMstr pricingMstr = null;
		try {
			pricingMstr = (PricingMstr) session.getNamedQuery("PricingMstr.findByCabTypeIdPricingTypeId").
					setParameter("cabTypeId", cabTypeId).setParameter("pricingTypeId", pricingTypeId).getSingleResult();
		}
		catch(Exception e){
			// No Data
			e.printStackTrace();
		}
		return pricingMstr;
	}
	
	private void fetchLazyPrcingMstr(PricingMstr pricingMstr) {
		pricingMstr.getCabType();
		pricingMstr.getPricingType();
	}
}
