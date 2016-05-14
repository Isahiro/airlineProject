package com.cooksys.airline.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import com.cooksys.core.models.Flight;

public class Route implements Comparable<Route>
{
	private List<Flight> flights;

	public Route()
	{
		this.flights = new ArrayList<Flight>();
	}
	
	public Route(Stack<Flight> flights)
	{
		while(!flights.isEmpty())
		{
			this.flights.add(flights.pop());
		}
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
}
