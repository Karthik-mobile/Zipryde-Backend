package com.trivecta.zipryde.model.entity;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the user_prefered_location database table.
 * 
 */
@Entity
@Table(name="USER_PREFERED_LOCATION")
@NamedQuery(name="UserPreferedLocation.findAll", query="SELECT u FROM UserPreferedLocation u")
public class UserPreferedLocation implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;

	private String address;

	private String type;

	//bi-directional many-to-one association to User
	@ManyToOne
	@JoinColumn(name="userId")
	private User user;

	public UserPreferedLocation() {
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getAddress() {
		return this.address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public User getUser() {
		return this.user;
	}

	public void setUser(User user) {
		this.user = user;
	}

}