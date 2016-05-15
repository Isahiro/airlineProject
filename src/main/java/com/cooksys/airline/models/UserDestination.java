package com.cooksys.airline.models;

// Generated May 14, 2016 4:09:48 PM by Hibernate Tools 4.3.1

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * UserDestination generated by hbm2java
 */
@Entity
@Table(name = "user_destination", catalog = "airline")
public class UserDestination implements java.io.Serializable {

	private Integer id;
	private Location location;
	private User user;

	public UserDestination()
	{
	}

	public UserDestination(Location location, User user)
	{
		this.location = location;
		this.user = user;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	public Integer getId()
	{
		return this.id;
	}

	public void setId(Integer id)
	{
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "destination_id", nullable = false)
	public Location getLocation()
	{
		return this.location;
	}

	public void setLocation(Location location)
	{
		this.location = location;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	public User getUser()
	{
		return this.user;
	}

	public void setUser(User user)
	{
		this.user = user;
	}

}