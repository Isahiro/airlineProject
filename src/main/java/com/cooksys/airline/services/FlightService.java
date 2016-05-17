package com.cooksys.airline.services;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.cooksys.airline.controllers.WebSocketController;
import com.cooksys.airline.dao.TicketDao;
import com.cooksys.airline.models.Notification;
import com.cooksys.airline.models.Trip;
import com.cooksys.core.models.Flight;
import com.google.gson.Gson;

@Repository
public class FlightService 
{
	private static final String ARRIVED = "Flight Arrived";
	private static final String DELAYED = "Flights Delayed";
	
	@Autowired TicketDao ticketDao;
	
	public void handleFlightMessage(String text, String status)
	{
		switch (status)
		{
			case ARRIVED:
				flightArrived(text);
				break;
			
			case DELAYED:
				flightsDelayed(text);
				break;
				
			default:
				System.out.println("Unknown flight status received: " + status);
				break;
		}
	}
	
	private void flightArrived(String text)
	{
		Gson gson = new Gson();
		
		Flight flight = gson.fromJson(text, Flight.class);
		
		try
		{
			System.out.println("Flight " + flight.getFlightId() + " arrived");
			
			List<Trip> relatedTrips = getRelatedTrips(flight.getFlightId());
			
			if(!relatedTrips.isEmpty())
			{
				ticketDao.removeFlightFromTrips(relatedTrips, flight.getFlightId());
			}
			
			Notification notification = new Notification(ARRIVED, flight, relatedTrips);
			
			WebSocketController.broadcastNotification(notification);
		} 
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	private void flightsDelayed(String text)
	{
		Gson gson = new Gson();
		
		List<Flight> flightsDelayed = new ArrayList<Flight>();
		
		try
		{
			JSONArray jsonArray = new JSONArray(text); 
			
			if (jsonArray != null) 
			{ 
			   for (int i = 0; i < jsonArray.length(); i++)
			   { 
				   Flight flight = gson.fromJson(jsonArray.get(i).toString(), Flight.class);
				   System.out.println("Flight " + flight.getFlightId() + " delayed");
				   flightsDelayed.add(flight);
			   } 
			}
			
			for(Flight flight : flightsDelayed)
			{
				System.out.println("Testing flight " + flight.getFlightId());
				
				List<Trip> relatedTrips = getRelatedTrips(flight.getFlightId());
				
				if(!relatedTrips.isEmpty())
				{
					for(Trip trip : relatedTrips)
					{
						if(!ticketDao.tripValid(trip))
						{
							System.out.println("Trip invalidated: " + trip.getId());
							ticketDao.holdTrip(trip, flight.getDestination());
						}
					}
					
					Notification notification = new Notification(DELAYED, relatedTrips);
					
					try
					{
						System.out.println("Notifying users of invalid trips");
						WebSocketController.broadcastNotification(notification);
					} 
					catch (Exception e)
					{
						e.printStackTrace();
					}
				}
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public List<Trip> getRelatedTrips(Integer flightId)
	{
		List<Trip> relatedTrips = new ArrayList<Trip>();
		
		List<Trip> results = ticketDao.getTripsByFlightId(flightId);
		
		if(results != null)
		{
			relatedTrips.addAll(results);
		}
		
		return relatedTrips;
	}
}
