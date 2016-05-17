package com.cooksys.airline.dao.impl;

import java.util.ArrayList;
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
	
	private FlightModel flightModel;
	
	public void updateFlightModel()
	{
		// Get flights from bc-webservice
		Gson gson = new Gson();
		final String flightService = "http://localhost:8080/bc-final-webservice/getFlightModel";
		RestTemplate restTemplate = new RestTemplate();
		
		this.flightModel = gson.fromJson(
				restTemplate.getForObject(flightService, String.class), 
				FlightModel.class);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<com.cooksys.airline.models.Location> getLocations()
	{
		Session session = sf.getCurrentSession();
		
		String hql = "from Location";
		
		return session
				.createQuery(hql)
				.list();
	}
	
	public List<Flight> getFlights()
	{
		updateFlightModel();
		// Sort flights by departure time
	    List<Flight> flights = new ArrayList<Flight>();
	    flights.addAll(flightModel.getFlights());
	    Collections.sort(flights);
	    
	    System.out.println("Found " + flights.size() + " flights");
	    	    
	    return flights;
	}

	@Override
	public Trip bookRoute(Route route, Integer userId, 
			Integer originId, Integer destinationId)
	{
		Session session = sf.getCurrentSession();
		
		if(!routeValid(route))
			return null;
		
		User user = userDao.get(userId);
		List<Ticket> tickets = new ArrayList<Ticket>();
		
		for(Flight f : route.getFlights())
		{
			Ticket ticket = new Ticket(user, f.getFlightId());
			session.save(ticket);
			tickets.add(ticket);
		}
		
		com.cooksys.airline.models.Location origin = getLocation(originId);
		com.cooksys.airline.models.Location destination = getLocation(destinationId);
		
		Trip trip = new Trip(origin, destination, true);
		
		trip.getTickets().addAll(tickets);		
		trip.getUsers().add(user);
		trip.setLocationByCurrentLocation(origin);
		
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
					routeFlight = flight;
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
		
		if(route.getFlights().size() > 1)
		{
			for(int i = 0; i < route.getFlights().size() - 1; i++)
			{
				Flight f = route.getFlights().get(i);
				
				if(f.getDeparture() + f.getEta() > route.getFlights().get(i + 1).getDeparture())
					return false;
			}
		}
		
		return true;
	}

	@Override
	public List<Route> planRoutes(Integer originId, Integer destinationId)
	{
		Session session = sf.getCurrentSession();
		
		String hql = "select new com.cooksys.core.models.Location"
				+ "(l.state, l.city) "
				+ "from Location l where l.id = :id";
		
		Location origin = (Location) session
				.createQuery(hql)
				.setParameter("id", originId)
				.uniqueResult();
		
		Location destination = (Location) session
				.createQuery(hql)
				.setParameter("id", destinationId)
				.uniqueResult();
		
		System.out.println("Planning a route from " 
				+ origin.getCity() + ", " + origin.getState() + " to "
				+ destination.getCity() + ", " + destination.getState());
		
		List<Route> plannedRoutes = new ArrayList<Route>();
		
		findRoutes(getAvailableFlights(), origin, destination, new Route(), plannedRoutes);
		
		Collections.sort(plannedRoutes);
		
		return plannedRoutes;
	}
	
	// Finds routes to get to the destination and works back to origin
	public void findRoutes(List<Flight> flights, Location origin, 
			Location destination, Route routeBuilder, List<Route> plannedRoutes)
	{
		// If a flight was found to reach a destination 
		// set the new destination to that flight's origin
		if(!routeBuilder.getFlights().isEmpty())
		{
			destination = routeBuilder
					.getFlights()
					.get(routeBuilder.getFlights().size() - 1)
					.getOrigin();
		}
		
		System.out.println("Searching for flights to " 
				+ destination.getCity() + ", " + destination.getState());
		
		for(int i = 0; i < flights.size(); i++)
		{
			Flight flight = flights.get(i);
			
			// Does the origin match the destination
			if(flight.getDestination().equals(flight.getOrigin()))
			{
				System.out.println("Ignoring circular flight");
				continue;
			}
			
			// Does the flight go to the destination
			if(flight.getDestination().getCity()
					.equals(destination.getCity())
				&& flight.getDestination().getState()
					.equals(destination.getState()))
			{
				System.out.println("Found flight to "
						+ flight.getOrigin().getCity() + ", " + flight.getOrigin().getState() 
						+ " from "
						+ destination.getCity() + ", " + destination.getState());
				
				// Add the flight to the RouteBuilder's List
				Route newrouteBuilder = 
						new Route(routeBuilder.getFlights());
				
				newrouteBuilder.getFlights().add(flight);
				
				// Does the flight origin match the user's origin
				if(flight.getOrigin().equals(origin))
				{
					System.out.println("Found a flight originating from " 
							+ origin.getCity() + ", " + origin.getState());
					
					Route route = new Route(newrouteBuilder.getFlights());
					Collections.sort(route.getFlights());
					
					System.out.println("Route Established:");
					for(Flight f : route.getFlights())
					{
						System.out.println("Leg goes from " 
								+ f.getOrigin().getCity() + ", " + f.getOrigin().getState()
								+ " to " + 
								f.getDestination().getCity() + ", " + f.getDestination().getState());
					}
					// Add the route to the planned routes
					plannedRoutes.add(route);
				}
				else if(i == 0)
				{
					System.out.println("No previous flights to check leading to this flight");
					continue;
				}
				else
				{
					List<Flight> newFlights = removeUnavailableFlights(flights, newrouteBuilder);
					findRoutes(newFlights, origin, destination, newrouteBuilder, plannedRoutes);
				}
			}
		}
		
	}

	private List<Flight> removeUnavailableFlights(List<Flight> flights, Route route)
	{
		System.out.println("Starting with " + flights.size() + " flights");
		
		if(route.getFlights().isEmpty())
		{
			return flights;
		}
		
		List<Flight> newFlights = new ArrayList<Flight>();
		
		for(int i = 0; i < flights.size(); i++)
		{
			Flight f = flights.get(i);
			
			if(f.getDeparture() + f.getEta() <= route.earliestDeparture() 
					&& f.getDeparture() < route.earliestDeparture())
			{
				newFlights.add(f);
			}
		}
		
		System.out.println(newFlights.size() + " flights remain");
		
		return newFlights;
	}

	@Override
	public List<Trip> getTripsByFlightId(Integer id)
	{
		System.out.println("Searching for trips related to a flight");
		
		Session session = sf.getCurrentSession();
		
		String hql = "from Trip";
		
		@SuppressWarnings("unchecked")
		List<Trip> trips = session
				.createQuery(hql)
				.list();
		
		List<Trip> tripsWithFlight = new ArrayList<Trip>();
		
		if(!trips.isEmpty())
		{
			System.out.println("Checking trips for flight " + id);
			
			for(Trip trip : trips)
			{
				System.out.println("Checking " + trip.getTickets().size() + " for flight");
				for(Ticket ticket : trip.getTickets())
				{
					if(ticket.getFlightId() == id)
					{
						tripsWithFlight.add(trip);
						System.out.println("Trip contained flight " + id);
						break;
					}
					
					System.out.println("Flight not found in this trip");
				}
			}
			
			System.out.println("Found " + tripsWithFlight.size() + "trips with flight " + id);
		}
		else
		{
			System.out.println("No trips to check");
			return null;
		}
		
		return tripsWithFlight;
	}
	
	@Override
	public boolean tripValid(Trip trip)
	{
		if(trip.getTickets().isEmpty())
			return true;
		
		List<Flight> tripFlights = new ArrayList<Flight>();
		
		for(Ticket t : trip.getTickets())
		{
			Flight flight = getFlightFromModel(t.getFlightId());
			
			if(flight != null)
				tripFlights.add(flight);
			else
				trip.setValid(false);
		}
		
		if(trip.isValid())
		{
			Collections.sort(tripFlights);
			Route route = new Route(tripFlights);
			
			trip.setValid(routeValid(route));
		}
		
		return trip.isValid();
	}

	@Override
	public void holdTrip(Trip trip, Location location)
	{
		Session session = sf.getCurrentSession();
		
		trip.setLocationByCurrentLocation(getLocationByName(location));
		
		for(Ticket t : trip.getTickets())
		{
			session.delete(t);
		}
		
		trip.getTickets().clear();
		
		session.update(trip);
	}
	
	public List<Trip> getTrips(Integer id)
	{
		Session session = sf.getCurrentSession();
		
		String hql = "select u.trips from User u where u.id = :id";
		
		@SuppressWarnings("unchecked")
		List<Trip> userTrips = session
				.createQuery(hql)
				.setParameter("id", id)
				.list();
		
		List<Trip> activeTrips = new ArrayList<Trip>();
		
		for(Trip t : userTrips)
		{
			if(!t.getTickets().isEmpty())
				activeTrips.add(t);
		}
		
		return activeTrips;
	}

	public List<Trip> cancelTrip(Integer userId, Integer tripId)
	{
		Session session = sf.getCurrentSession();
		
		String hql = "from Trip t where t.id = :id";
		
		Trip trip = (Trip) session
				.createQuery(hql)
				.setParameter("id", tripId)
				.uniqueResult();
		
		for(Ticket t : trip.getTickets())
		{
			session.delete(t);
		}
		
		trip.getUsers().clear();
		session.delete(trip);
		
		return getTrips(userId);
	}
	
	private Flight getFlightFromModel(int flightId)
	{
		for(Flight f : getFlights())
		{
			if(f.getFlightId() == flightId)
				return f;
		}
		
		return null;
	}

	private List<Flight> removeDepartedFlights(List<Flight> flights)
	{
		List<Flight> newFlights = new ArrayList<Flight>();
		
		for(Flight f : flights)
	    	if(f.getDeparture() >= 0)
	    		newFlights.add(f);
		
		System.out.println(newFlights.size() + 
				" flights remain after removing departed flights");
		
		return newFlights;
	}
	
	@SuppressWarnings("unchecked")
	private List<Flight> removeFullFlights(List<Flight> flights)
	{
		Session session = sf.getCurrentSession();
		
		String hql = "select new com.cooksys.airline.models.SeatsTaken"
				+ "(t.flightId, count (t.flightId)) "
				+ "from Ticket t group by t.flightId";
		
		List<SeatsTaken> occupied = session.createQuery(hql).list();
		
		List<Flight> newFlights = new ArrayList<Flight>();
		
		for(Flight f : flights)
		{
			boolean atCapacity = false;
			
			for(SeatsTaken taken : occupied)
			{
				if(f.getFlightId() == taken.getFlightId())
				{
					if(taken.getNumberOfTickets() > 4)
					{
						atCapacity = true;
						break;
					}
				}
			}
			
			if(!atCapacity)
				newFlights.add(f);
		}
		
		System.out.println(newFlights.size() + 
				" flights remain after removing full flights");
		
		return newFlights;
	}
	
	private List<Flight> getAvailableFlights()
	{
		return removeFullFlights(removeDepartedFlights(getFlights()));
	}
	
	public com.cooksys.airline.models.Location getLocation(Integer id)
	{
		Session session = sf.getCurrentSession();
		
		String hql = "from Location l where l.id = :id";
		
		return (com.cooksys.airline.models.Location) session
				.createQuery(hql)
				.setParameter("id", id)
				.uniqueResult();
	}
	
	public com.cooksys.airline.models.Location getLocationByName(Location location)
	{
		Session session = sf.getCurrentSession();
		
		String hql = "from Location l "
				+ "where l.city = :city "
				+ "and where l.state = :state";
		
		return (com.cooksys.airline.models.Location) session
				.createQuery(hql)
				.setParameter("city", location.getCity())
				.setParameter("state", location.getState())
				.uniqueResult();
	}

	@Override
	public void removeFlightFromTrips(List<Trip> trips, Integer flightId)
	{
		Session session = sf.getCurrentSession();
		
		for(Trip trip : trips)
		{
			for(Ticket ticket : trip.getTickets())
			{
				if(ticket.getFlightId() == flightId)
				{
					trip.getTickets().remove(ticket);
					session.update(trip);
					break;
				}
			}
		}
	}
}
