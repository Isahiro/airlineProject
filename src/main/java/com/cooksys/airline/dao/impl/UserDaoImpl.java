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
		
		String hql = "select new com.cooksys.airline.models.User(u.username) "
				+ "from User u where u.id = :id";
		
		User user = (User) session
				.createQuery(hql)
				.setInteger("id", id)
				.uniqueResult();
		
		user.setId(id);
		
		return user;
	}

	@Override
	public User get(String username)
	{
		Session session = sf.getCurrentSession();
		
		String hql = "select new com.cooksys.airline.models.User("
				+ "u.id, u.username, u.password) "
				+ "from User u where u.username = :username";
		
		return (User) session
				.createQuery(hql)
				.setParameter("username", username)
				.uniqueResult();
	}

}
