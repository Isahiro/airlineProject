package com.cooksys.airline.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cooksys.airline.dao.UserDao;
import com.cooksys.airline.models.User;

@RestController
@RequestMapping(value = "/users")
public class UserController 
{
	@Autowired
	private UserDao userDao;
	
	// airline/api/users/
	@RequestMapping(method = RequestMethod.GET)
	public List<User> index()
	{
		return userDao.index();
	}
	
	@RequestMapping(method = RequestMethod.POST)
	public User addUser(@RequestBody User user)
	{
		return userDao.add(user);
	}
	
	// airline/api/users/{id}
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public User getUser(@PathVariable Integer id)
	{
		return userDao.get(id);
	}
	
	// airline/api/users/login
	@RequestMapping(value ="/login", method = RequestMethod.POST)
	public User login(@RequestBody String username)
	{
		return userDao.get(username);
	}
}
