package com.trivecta.zipryde.model.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;


/**
 * The persistent class for the pricing_mstr database table.
 * 
 */
@Entity
@Table(name="PRICING_MSTR")
@NamedQueries ({
	@NamedQuery(name="PricingMstr.findAll", query="SELECT p FROM PricingMstr p"),
	@NamedQuery(name="PricingMstr.findByCabTypeId", query="SELECT p FROM PricingMstr p where p.cabType.id = :cabTypeId"),
	@NamedQuery(name="PricingMstr.findByCabTypeIdPricingTypeId", query="SELECT p FROM PricingMstr p where p.cabType.id = :cabTypeId and p.pricingType.id = :pricingTypeId"),
	@NamedQuery(name="PricingMstr.findByCabTypeIdAndIsEnable", query="SELECT p FROM PricingMstr p where p.cabType.id = :cabTypeId and "
			+ "p.isEnable = :isEnable")
})

public class PricingMstr implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "`from`") 
	private Integer from;

	private BigDecimal price;

	private BigDecimal pricePerUnit;

	@Column(name = "`to`") 
	private Integer to;

	private String unit;

	//bi-directional many-to-one association to CabType
	@ManyToOne
	@JoinColumn(name="cabTypeId")
	private CabType cabType;
	
	//bi-directional many-to-one association to PricingType
	@ManyToOne
	@JoinColumn(name="pricingTypeId")
	private PricingType pricingType;
	
	private Integer isEnable;

	public PricingMstr() {
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getFrom() {
		return this.from;
	}

	public void setFrom(Integer from) {
		this.from = from;
	}

	public BigDecimal getPrice() {
		return this.price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public BigDecimal getPricePerUnit() {
		return this.pricePerUnit;
	}

	public void setPricePerUnit(BigDecimal pricePerUnit) {
		this.pricePerUnit = pricePerUnit;
	}

	public Integer getTo() {
		return this.to;
	}

	public void setTo(Integer to) {
		this.to = to;
	}

	public String getUnit() {
		return this.unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public CabType getCabType() {
		return this.cabType;
	}

	public void setCabType(CabType cabType) {
		this.cabType = cabType;
	}

	public PricingType getPricingType() {
		return pricingType;
	}

	public void setPricingType(PricingType pricingType) {
		this.pricingType = pricingType;
	}

	public Integer getIsEnable() {
		return isEnable;
	}

	public void setIsEnable(Integer isEnable) {
		this.isEnable = isEnable;
	}

	@Override
	public String toString() {
		return "PricingMstr [id=" + id + ", from=" + from + ", price=" + price + ", pricePerUnit=" + pricePerUnit
				+ ", to=" + to + ", unit=" + unit + ", cabType=" + cabType + ", pricingType=" + pricingType
				+ ", isEnable=" + isEnable + "]";
	}
	
	

}