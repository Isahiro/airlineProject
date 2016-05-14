package com.cooksys.airline.dao;

import java.util.List;

import com.cooksys.airline.models.User;

public interface UserDao 
{

	public List<User> index();
	public User add(User user);
	public User get(Integer id);
	public User get(String username);

}
