package com.cooksys.airline.models;

import java.util.ArrayList;
import java.util.List;

import com.cooksys.core.models.Flight;

public class Route implements Comparable<Route>
{
	private List<Flight> flights = new ArrayList<Flight>();

	public Route()
	{
		
	}
	
	public Route(List<Flight> flights)
	{
		this.flights.addAll(flights);
	}

	public List<Flight> getFlights()
	{
		return flights;
	}

	public void setFlights(List<Flight> flights)
	{
		this.flights = flights;
	}

	@Override
	public int compareTo(Route route)
	{
		Flight thisLastFlight = this.getFlights()
				.get(this.getFlights().size() - 1);
		
		int thisArrivalTime = 
				thisLastFlight.getDeparture() + thisLastFlight.getEta();
		
		Flight otherLastFlight = route.getFlights()
				.get(route.getFlights().size() - 1);
		
		int otherArrivalTime = 
				otherLastFlight.getDeparture() + otherLastFlight.getEta();
		
		return thisArrivalTime - otherArrivalTime;
	}
	
	public int earliestDeparture()
	{
		int earliest = 10000;
		
		if(!this.flights.isEmpty())
		{
			for(Flight f : flights)
				if(f.getDeparture() < earliest)
					earliest = f.getDeparture();
		}
		
		return earliest;
	}
}
