package com.prueba.server;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Session;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.prueba.model.Widget;

@Component
public class Listener {

	private int id = 1;
	private long lastMessageTime = 0;
	private int widgetsByMinute = 5;
	private int widgetsProduced = 0;

	@JmsListener(destination = "companyA.queue")
	@SendTo("companyB.queue")
	public Widget receiveMessage(final Message message) throws JMSException, InterruptedException {

		if (secondsBetweenLastWork() > 60) {
			widgetsProduced = 0;
		}

		if (widgetsProduced == widgetsByMinute) {
			widgetsProduced = 0;

			delay();

		}

		Widget widget = createWidget();

		lastMessageTime = new Date().getTime();

		System.out.println(widget);

		++id;
		++widgetsProduced;

		return widget;

	}

	public void delay() {
		try {
			TimeUnit.MINUTES.sleep(1);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public long secondsBetweenLastWork() {

		long diffMs = new Date().getTime() - lastMessageTime;
		long diffSec = diffMs / 1000;

		return diffSec;

	}

	public Widget createWidget() {

		Date date = new Date();

		Widget widget = new Widget();
		widget.setId(id);
		widget.setTime(date.getTime());

		return widget;

	}

	void returnWidget(Widget widget) {

		try {

			ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://localhost:61616");

			Connection connection = connectionFactory.createConnection();
			connection.start();

			Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

			Destination destination = session.createTopic("companyB.topic");

			MessageProducer messageProducer = session.createProducer(destination);

			ObjectMessage message = session.createObjectMessage();
			message.setObject(new Gson().toJson(widget));

			messageProducer.send(message);

			session.close();
			connection.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
