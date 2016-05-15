package com.cooksys.airline.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cooksys.airline.dao.TicketDao;
import com.cooksys.airline.models.Route;
import com.cooksys.airline.models.Trip;

@RestController
@RequestMapping(value = "/tickets")
public class TicketController 
{
	@Autowired TicketDao ticketDao;
	
	@RequestMapping(value = "/locations", method = RequestMethod.GET)
	public String getLocations()
	{
		return ticketDao.getLocations();
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.POST)
	public Trip bookRoute(@RequestBody Route route, @PathVariable Integer id)
	{
		return ticketDao.bookRoute(route, id);
	}
	
	@RequestMapping(value = "/{originId}/{destinationId}", method = RequestMethod.GET)
	public List<Route> findRoute(@PathVariable Integer originId, @PathVariable Integer destinationId)
	{
		return ticketDao.planRoutes(originId, destinationId);
	}
	
	@RequestMapping(value = "/trips", method = RequestMethod.POST)
	
	public boolean tripValid(@RequestBody Trip trip)
	{
		return ticketDao.tripValid(trip);
	}
}
