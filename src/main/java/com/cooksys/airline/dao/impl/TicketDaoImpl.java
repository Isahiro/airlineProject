package com.cooksys.airline.dao.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.cooksys.airline.dao.TicketDao;
import com.cooksys.airline.dao.UserDao;
import com.cooksys.airline.models.Route;
import com.cooksys.airline.models.RouteBuilder;
import com.cooksys.airline.models.SeatsTaken;
import com.cooksys.airline.models.Ticket;
import com.cooksys.airline.models.Trip;
import com.cooksys.airline.models.User;
import com.cooksys.core.models.Flight;
import com.cooksys.core.models.FlightModel;
import com.cooksys.core.models.Location;
import com.google.gson.Gson;

@Repository
@Transactional
public class TicketDaoImpl implements TicketDao 
{
	@Autowired SessionFactory sf;
	
	@Autowired UserDao userDao;
	
	private FlightModel flightModel = updateFlightModel();
	
	private static List<Route> plannedRoutes = new ArrayList<Route>();
	
	public FlightModel updateFlightModel()
	{
		// Get flights from bc-webservice
		Gson gson = new Gson();
		final String flightService = "http://localhost:8080/bc-final-webservice/getFlightModel";
		RestTemplate restTemplate = new RestTemplate();
		
		return gson.fromJson(
				restTemplate.getForObject(flightService, String.class), 
				FlightModel.class);
	}
	
	@Override
	public String getLocations()
	{
		// Get serviced areas from bc-webservice
		final String locationService = "http://localhost:8080/bc-final-webservice/getLocations";
		RestTemplate restTemplate = new RestTemplate();
		
		return restTemplate
				.getForObject(locationService, String.class);
	}
	
	public List<Flight> getFlights()
	{
		updateFlightModel();
		// Sort flights by departure time
	    List<Flight> flights = flightModel.getFlights();
	    Collections.sort(flights);
	    	    
	    return flights;
	}

	@Override
	public Trip bookRoute(Route route, Integer id)
	{
		Session session = sf.getCurrentSession();
		
		if(!routeValid(route))
			return null;
		
		User user = userDao.get(id);
		List<Ticket> tickets = new ArrayList<Ticket>();
		
		for(Flight f : route.getFlights())
		{
			Ticket ticket = new Ticket(user, f.getFlightId());
			session.save(ticket);
			tickets.add(ticket);
		}
		
		Trip trip = new Trip();
		
		trip.getTickets().addAll(tickets);		
		trip.getUsers().add(user);
		
		session.save(trip);
		return trip;
	}
	
	public boolean routeValid(Route route)
	{
		List<Flight> flights = getAvailableFlights();
		
		boolean routePossible = true;
		
		for(Flight routeFlight : route.getFlights())
		{
			boolean hasFlightId = false;
			
			for(Flight flight : flights)
			{
				if(flight.getFlightId().intValue() == routeFlight.getFlightId().intValue())
				{
					hasFlightId = true;
					break;
				}
			}
			
			if(!hasFlightId)
			{
				routePossible = false;
				break;
			}
		}
		
		if(!routePossible)
			return false;
		else
			return true;
	}

	@Override
	public List<Route> planRoutes(Location origin, Location destination)
	{
		List<Flight> flights = getAvailableFlights();
		
		findRoutes(flights, origin, destination, new RouteBuilder());
		
		List<Route> routes = getPlannedRoutes();
		setPlannedRoutes(new ArrayList<Route>());
		
		Collections.sort(routes);
		
		return routes;
	}
	
	// Finds routes to get to the destination and works back to origin
	public void findRoutes(List<Flight> flights, Location origin, 
			Location destination, RouteBuilder routeBuilder)
	{
		// If a flight was found to reach a destination 
		// set the new destination to the flight's origin
		if(!routeBuilder.getFlights().isEmpty())
		{
			destination = routeBuilder
					.getFlights()
					.lastElement()
					.getOrigin();
		}
		
		for(int i = 0; i < flights.size(); i++)
		{
			// Does the flight go to the destination
			if(flights.get(i).getDestination().equals(destination))
			{
				// Add the flight to the RouteBuilder's Stack
				routeBuilder.getFlights().push(flights.get(i));
				
				// Does the flight origin match the user's origin
				if(flights.get(i).getOrigin().equals(origin))
				{
					// Add the route to the planned routes
					getPlannedRoutes()
						.add(new Route(
								routeBuilder.getFlights(
					)));
				}
				else if(i == 0)
				{
					return;
				}
				else
				{
					findRoutes(flights.subList(0, i), origin, destination, routeBuilder);
					routeBuilder.getFlights().pop();
				}
			}
		}
		
	}

	@Override
	public boolean tripValid(Trip trip)
	{
		// TODO Auto-generated method stub
		return false;
	}

	public List<Flight> removeDepartedFlights(List<Flight> flights)
	{
		for(Flight f : flights)
	    	if(f.getDeparture() < 0)
	    		flights.remove(f);
		
		return flights;
	}
	
	@SuppressWarnings("unchecked")
	public List<Flight> removeFullFlights(List<Flight> flights)
	{
		Session session = sf.getCurrentSession();
		
		String hql = "select new com.cooksys.airline.models.SeatsTaken"
				+ "(t.flightId, count t.flightId) "
				+ "from Ticket t group by t.flightId";
		
		List<SeatsTaken> occupied = session.createQuery(hql).list();
		
		for(SeatsTaken taken : occupied)
			if(taken.getNumberOfTickets() > 4)
				for(Flight f : flights)
					if(f.getFlightId() == taken.getFlightId())
						flights.remove(f);
		
		return flights;
	}
	
	public List<Flight> getAvailableFlights()
	{
		return removeFullFlights(removeDepartedFlights(getFlights()));
	}

	private static List<Route> getPlannedRoutes()
	{
		return plannedRoutes;
	}

	private static void setPlannedRoutes(List<Route> plannedRoutes)
	{
		TicketDaoImpl.plannedRoutes = plannedRoutes;
	}
	
	
}
