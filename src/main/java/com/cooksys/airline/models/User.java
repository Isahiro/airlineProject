package com.cooksys.airline.models;

// Generated May 15, 2016 12:47:06 PM by Hibernate Tools 4.3.1

import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * User generated by hbm2java
 */
@Entity
@Table(name = "user", catalog = "airline", uniqueConstraints = @UniqueConstraint(columnNames = "username"))
public class User implements java.io.Serializable {

	private Integer id;
	private String firstName;
	private String lastName;
	private String username;
	private String password;
	private Set<Trip> trips = new HashSet<Trip>(0);
	private Set<Ticket> tickets = new HashSet<Ticket>(0);

	public User()
	{
	}

	public User(String firstName, String lastName, String username,
			String password)
	{
		this.firstName = firstName;
		this.lastName = lastName;
		this.username = username;
		this.password = password;
	}

	public User(String firstName, String lastName, String username,
			String password, Set<Trip> trips, Set<Ticket> tickets)
	{
		this.firstName = firstName;
		this.lastName = lastName;
		this.username = username;
		this.password = password;
		this.trips = trips;
		this.tickets = tickets;
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

	@Column(name = "first_name", nullable = false, length = 50)
	public String getFirstName()
	{
		return this.firstName;
	}

	public void setFirstName(String firstName)
	{
		this.firstName = firstName;
	}

	@Column(name = "last_name", nullable = false, length = 50)
	public String getLastName()
	{
		return this.lastName;
	}

	public void setLastName(String lastName)
	{
		this.lastName = lastName;
	}

	@Column(name = "username", unique = true, nullable = false, length = 75)
	public String getUsername()
	{
		return this.username;
	}

	public void setUsername(String username)
	{
		this.username = username;
	}

	@Column(name = "password", nullable = false, length = 60)
	public String getPassword()
	{
		return this.password;
	}

	public void setPassword(String password)
	{
		this.password = password;
	}

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "trip_user", catalog = "airline", joinColumns = { @JoinColumn(name = "user_id", nullable = false, updatable = false) }, inverseJoinColumns = { @JoinColumn(name = "trip_id", nullable = false, updatable = false) })
	public Set<Trip> getTrips()
	{
		return this.trips;
	}

	public void setTrips(Set<Trip> trips)
	{
		this.trips = trips;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
	public Set<Ticket> getTickets()
	{
		return this.tickets;
	}

	public void setTickets(Set<Ticket> tickets)
	{
		this.tickets = tickets;
	}

}
