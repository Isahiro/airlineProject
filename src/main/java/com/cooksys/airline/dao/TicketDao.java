package com.cooksys.airline.dao;

import java.util.List;

import com.cooksys.airline.models.Route;
import com.cooksys.airline.models.Trip;
import com.cooksys.core.models.Location;

public interface TicketDao 
{

	Trip bookRoute(Route route, Integer id);
	List<Route> planRoutes(Location origin, Location destination);
	String getLocations();
	boolean tripValid(Trip trip);

}
