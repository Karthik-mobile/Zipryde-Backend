package com.trivecta.zipryde.model.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;


/**
 * The persistent class for the commission_mstr database table.
 * 
 */
@Entity
@Table(name="COMMISSION_MSTR")
@NamedQuery(name="CommissionMstr.findAll", query="SELECT c FROM CommissionMstr c")
public class CommissionMstr implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;

	private BigDecimal commisionAmount;

	@Temporal(TemporalType.TIMESTAMP)
	private Date fromDate;

	private Integer noOfMiles;

	private Integer noOfTrips;

	@Temporal(TemporalType.TIMESTAMP)
	private Date toDate;

	public CommissionMstr() {
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public BigDecimal getCommisionAmount() {
		return this.commisionAmount;
	}

	public void setCommisionAmount(BigDecimal commisionAmount) {
		this.commisionAmount = commisionAmount;
	}

	public Date getFromDate() {
		return this.fromDate;
	}

	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}

	public Integer getNoOfMiles() {
		return this.noOfMiles;
	}

	public void setNoOfMiles(Integer noOfMiles) {
		this.noOfMiles = noOfMiles;
	}

	public Integer getNoOfTrips() {
		return this.noOfTrips;
	}

	public void setNoOfTrips(Integer noOfTrips) {
		this.noOfTrips = noOfTrips;
	}

	public Date getToDate() {
		return this.toDate;
	}

	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}

}