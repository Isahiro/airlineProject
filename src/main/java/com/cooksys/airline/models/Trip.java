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
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Trip generated by hbm2java
 */
@Entity
@Table(name = "trip", catalog = "airline")
public class Trip implements java.io.Serializable {

	private Integer id;
	private Location locationByStartLocation;
	private Location locationByCurrentLocation;
	private Location locationByDestinationLocation;
	private boolean valid;
	
	private Set<Ticket> tickets = new HashSet<Ticket>(0);
	private Set<User> users = new HashSet<User>(0);

	public Trip()
	{
	}

	public Trip(Location locationByStartLocation,
			Location locationByDestinationLocation, boolean valid)
	{
		this.locationByStartLocation = locationByStartLocation;
		this.locationByDestinationLocation = locationByDestinationLocation;
		this.valid = valid;
	}

	public Trip(Location locationByStartLocation,
			Location locationByCurrentLocation,
			Location locationByDestinationLocation, boolean valid,
			Set<Ticket> tickets, Set<User> users)
	{
		this.locationByStartLocation = locationByStartLocation;
		this.locationByCurrentLocation = locationByCurrentLocation;
		this.locationByDestinationLocation = locationByDestinationLocation;
		this.valid = valid;
		this.tickets = tickets;
		this.users = users;
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

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "start_location", nullable = false)
	public Location getLocationByStartLocation()
	{
		return this.locationByStartLocation;
	}

	public void setLocationByStartLocation(Location locationByStartLocation)
	{
		this.locationByStartLocation = locationByStartLocation;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "current_location")
	public Location getLocationByCurrentLocation()
	{
		return this.locationByCurrentLocation;
	}

	public void setLocationByCurrentLocation(Location locationByCurrentLocation)
	{
		this.locationByCurrentLocation = locationByCurrentLocation;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "destination_location", nullable = false)
	public Location getLocationByDestinationLocation()
	{
		return this.locationByDestinationLocation;
	}

	public void setLocationByDestinationLocation(
			Location locationByDestinationLocation)
	{
		this.locationByDestinationLocation = locationByDestinationLocation;
	}

	@Column(name = "valid", nullable = false)
	public boolean isValid()
	{
		return this.valid;
	}

	public void setValid(boolean valid)
	{
		this.valid = valid;
	}

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "trip_ticket", catalog = "airline", joinColumns = { @JoinColumn(name = "trip_id", nullable = false, updatable = false) }, inverseJoinColumns = { @JoinColumn(name = "ticket_id", nullable = false, updatable = false) })
	public Set<Ticket> getTickets()
	{
		return this.tickets;
	}

	public void setTickets(Set<Ticket> tickets)
	{
		this.tickets = tickets;
	}

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "trip_user", catalog = "airline", joinColumns = { @JoinColumn(name = "trip_id", nullable = false, updatable = false) }, inverseJoinColumns = { @JoinColumn(name = "user_id", nullable = false, updatable = false) })
	public Set<User> getUsers()
	{
		return this.users;
	}

	public void setUsers(Set<User> users)
	{
		this.users = users;
	}

}
