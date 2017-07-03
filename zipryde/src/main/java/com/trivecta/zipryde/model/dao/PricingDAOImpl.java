package com.trivecta.zipryde.model.dao;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.trivecta.zipryde.constants.ZipRydeConstants;
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
			System.out.println(" Pricing Mstr List Size : "+pricingMstrList.size());
			for(PricingMstr pricingMstr : pricingMstrList) {
				System.out.println(" Pricing Mstr Type : "+pricingMstr.getPricingType().getType());
				System.out.println(" Price Per Unit "+pricingMstr.getPricePerUnit());
				System.out.println(" Price "+pricingMstr.getPrice());
				if(ZipRydeConstants.PRICINGTYPE.DISTANCE.equalsIgnoreCase(pricingMstr.getPricingType().getType())){
					BigDecimal distancePrice = pricingMstr.getPricePerUnit().multiply(new BigDecimal(NoOfMiles));
					System.out.println(" distancePrice : "+distancePrice);
					price = price.add(distancePrice);
					System.out.println(" Price (1)  : "+price);

				}
				else if(ZipRydeConstants.PRICINGTYPE.PERSON.equalsIgnoreCase(pricingMstr.getPricingType().getType())){
					BigDecimal personPrice = pricingMstr.getPrice().multiply(new BigDecimal(noOfPerson-1));
					System.out.println(" distancePrice : "+personPrice);
					price = price.add(personPrice);
					System.out.println(" Price (2)  : "+price);
				}
				else {
					price = price.add(pricingMstr.getPrice());
					System.out.println(" Price (3)  : "+price);
				}
			}
		}
		System.out.println(" Price (4)  : "+price);
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
}
