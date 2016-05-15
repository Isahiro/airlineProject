package com.cooksys.airline.dao;

import java.util.List;

import com.cooksys.airline.models.Route;
import com.cooksys.airline.models.Trip;

public interface TicketDao 
{

	Trip bookRoute(Route route, Integer id);
	List<Route> planRoutes(Integer originId, Integer destinationId);
	String getLocations();
	boolean tripValid(Trip trip);

}
