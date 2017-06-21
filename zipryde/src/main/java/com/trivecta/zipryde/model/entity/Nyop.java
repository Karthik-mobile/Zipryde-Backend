package com.trivecta.zipryde.model.entity;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the nyop database table.
 * 
 */
@Entity
@Table(name="NYOP")
@NamedQuery(name="Nyop.findAll", query="SELECT n FROM Nyop n")
public class Nyop implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;

	private Integer percentage;

	public Nyop() {
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getPercentage() {
		return this.percentage;
	}

	public void setPercentage(Integer percentage) {
		this.percentage = percentage;
	}

}