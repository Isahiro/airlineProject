package com.cooksys.airline.models;

import java.util.Stack;

import com.cooksys.core.models.Flight;

public class RouteBuilder 
{
	private Stack<Flight> flights;
	
	public RouteBuilder()
	{
		this.flights = new Stack<Flight>();
	}
	
	public RouteBuilder(Stack<Flight> flights)
	{
		this.flights = flights;
	}

	public Stack<Flight> getFlights()
	{
		return flights;
	}

	public void setFlights(Stack<Flight> flights)
	{
		this.flights = flights;
	}
	
	
}
