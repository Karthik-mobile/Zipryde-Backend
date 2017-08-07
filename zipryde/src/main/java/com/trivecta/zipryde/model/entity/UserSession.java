package com.trivecta.zipryde.model.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;

/**
 * The persistent class for the user_session database table.
 * 
 */
@Entity
@Table(name="USER_SESSION")
@NamedQueries({
	@NamedQuery(name="UserSession.findAll", query="SELECT u FROM UserSession u"),
	@NamedQuery(name="UserSession.findByUserId", query="SELECT u FROM UserSession u where u.userId = :userId"),
	@NamedQuery(name="UserSession.findByActiveUserIds", query="SELECT u FROM UserSession u where u.isActive = 1 and u.userId in :userIds")
})
public class UserSession implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;

	private Integer isActive;

	@Temporal(TemporalType.TIMESTAMP)
	private Date logInDateTime;

	@Temporal(TemporalType.TIMESTAMP)
	private Date logOutDateTime;

	private String status;

	private Integer userId;

	public UserSession() {
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getIsActive() {
		return this.isActive;
	}

	public void setIsActive(Integer isActive) {
		this.isActive = isActive;
	}

	public Date getLogInDateTime() {
		return this.logInDateTime;
	}

	public void setLogInDateTime(Date logInDateTime) {
		this.logInDateTime = logInDateTime;
	}

	public Date getLogOutDateTime() {
		return this.logOutDateTime;
	}

	public void setLogOutDateTime(Date logOutDateTime) {
		this.logOutDateTime = logOutDateTime;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Integer getUserId() {
		return this.userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

}