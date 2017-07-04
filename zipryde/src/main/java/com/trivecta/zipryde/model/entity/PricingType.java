package com.trivecta.zipryde.model.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the pricing_type database table.
 * 
 */
@Entity
@Table(name="PRICING_TYPE")
@NamedQuery(name="PricingType.findAll", query="SELECT p FROM PricingType p")
public class PricingType implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;

	private int isEnable;

	private String type;

	//bi-directional many-to-one association to PricingMstr
	@OneToMany(mappedBy="pricingType")
	private List<PricingMstr> pricingMstrs;

	public PricingType() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getIsEnable() {
		return this.isEnable;
	}

	public void setIsEnable(int isEnable) {
		this.isEnable = isEnable;
	}

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public List<PricingMstr> getPricingMstrs() {
		return this.pricingMstrs;
	}

	public void setPricingMstrs(List<PricingMstr> pricingMstrs) {
		this.pricingMstrs = pricingMstrs;
	}

	public PricingMstr addPricingMstr(PricingMstr pricingMstr) {
		getPricingMstrs().add(pricingMstr);
		pricingMstr.setPricingType(this);

		return pricingMstr;
	}

	public PricingMstr removePricingMstr(PricingMstr pricingMstr) {
		getPricingMstrs().remove(pricingMstr);
		pricingMstr.setPricingType(null);

		return pricingMstr;
	}

}