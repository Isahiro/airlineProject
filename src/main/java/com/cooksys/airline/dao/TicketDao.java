package com.cooksys.airline.dao;

import java.util.List;

import com.cooksys.airline.models.Route;
import com.cooksys.airline.models.Trip;
import com.cooksys.core.models.Flight;
import com.cooksys.core.models.Location;

public interface TicketDao 
{

	Trip bookRoute(Route route, Integer userId, Integer originId, Integer destinationId);
	List<Route> planRoutes(Integer originId, Integer destinationId);
	List<com.cooksys.airline.models.Location> getLocations();
	List<Trip> getTrips(Integer id); 
	boolean tripValid(Trip trip);
	List<Trip> getTripsByFlightId(Integer id);
	List<Trip> cancelTrip(Integer userId, Integer tripId);
	void holdTrip(Trip trip, Location location);
	void removeFlightFromTrips(List<Trip> trips, Integer flightId);
	List<Trip> updateTrip(Integer userId, Integer tripId, Route route);
	Flight getFlightFromModel(Integer flightId);

}
