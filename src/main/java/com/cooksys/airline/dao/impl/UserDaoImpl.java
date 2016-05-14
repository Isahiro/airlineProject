package com.cooksys.airline.dao.impl;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.cooksys.airline.dao.UserDao;
import com.cooksys.airline.models.User;

@Repository
@Transactional
public class UserDaoImpl implements UserDao
{
	@Autowired SessionFactory sf;

	@SuppressWarnings("unchecked")
	@Override
	public List<User> index()
	{
		Session session = sf.getCurrentSession();
		
		return session
				.createQuery("from User").list();
	}

	@Override
	public User add(User user)
	{
		Session session = sf.getCurrentSession();
		
		if(get(user.getUsername()) != null)
			return null;
		
		session.save(user);
		
		return get(user.getUsername());
	}

	@Override
	public User get(Integer id)
	{
		Session session = sf.getCurrentSession();
		
		String hql = "select u.username, u.id "
				+ "from User u where u.id = :id";
		
		return (User) session
				.createQuery(hql)
				.setInteger("id", id)
				.uniqueResult();
	}

	@Override
	public User get(String username)
	{
		Session session = sf.getCurrentSession();
		
		String hql = "select u.username, u.id, u.password "
				+ "from User u where u.username = :username";
		
		return (User) session
				.createQuery(hql)
				.setString("username", username)
				.uniqueResult();
	}

}
