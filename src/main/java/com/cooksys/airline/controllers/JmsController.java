package com.cooksys.airline.controllers;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cooksys.airline.services.FlightService;

@RestController
@RequestMapping(value = "/initialize")
public class JmsController 
{
	private Connection jmsConnection;
	private Session jmsSession;
	private Destination destination;
	
	private static final String FLIGHT_STATUS_PROPERTY = "FlightStatus";
	private static final String JMS_URL = "tcp://localhost:61616";
	private static final String TOPIC_NAME = "FlightUpdate";
	
	@Autowired FlightService flightController;
	
	@RequestMapping(method = RequestMethod.GET)
	public String startJmsController()
	{
		return "JmsController initiallized";
	}
	
	public JmsController()
	{
		System.out.println("JmsController initiallizing...");
		try
		{
			jmsConnection = 
				new ActiveMQConnectionFactory(JMS_URL)
					.createConnection();
			
			jmsConnection.start();
			jmsSession = jmsConnection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			
			destination = jmsSession.createTopic(TOPIC_NAME);			
			MessageConsumer consumer = jmsSession.createConsumer(destination);
			
			consumer.setMessageListener(new MessageListener()
			{
				@Override
				public void onMessage(Message msg)
				{
					try
					{
						if (msg instanceof TextMessage)
						{
							TextMessage message = (TextMessage) msg;
							
							String text = message.getText();							
							String flightStatus = message.getStringProperty(FLIGHT_STATUS_PROPERTY);
							
							System.out.println("Received notification: " + flightStatus);
							flightController.handleFlightMessage(text, flightStatus);
						}
						else
						{
							System.out.println("JmsController received a message");
							System.out.println("Received: " + msg);
						}
					}
					catch (Exception e)
					{

					}
				}
			});
		} 
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
}
