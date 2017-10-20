package com.trivecta.zipryde.model.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the make database table.
 * 
 */
@Entity
@Table(name="MAKE")
@NamedQueries({
	@NamedQuery(name="Make.findAll", query="SELECT m FROM Make m"),
	@NamedQuery(name="Make.findAllEnabled", query="SELECT m FROM Make m where m.isEnable =  1"),
	@NamedQuery(name="Make.findByMake", query="SELECT m FROM Make m where m.make = :make")
})
public class Make implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;

	private Integer isEnable;

	private String make;

	//bi-directional many-to-one association to Model
	@OneToMany(mappedBy="make")
	private List<Model> models;

	public Make() {
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getIsEnable() {
		return this.isEnable;
	}

	public void setIsEnable(Integer isEnable) {
		this.isEnable = isEnable;
	}

	public String getMake() {
		return this.make;
	}

	public void setMake(String make) {
		this.make = make;
	}

	public List<Model> getModels() {
		return this.models;
	}

	public void setModels(List<Model> models) {
		this.models = models;
	}

	public Model addModel(Model model) {
		getModels().add(model);
		model.setMake(this);

		return model;
	}

	public Model removeModel(Model model) {
		getModels().remove(model);
		model.setMake(null);

		return model;
	}

}