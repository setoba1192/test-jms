package com.prueba.client;

import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;

import com.prueba.model.Widget;

public class SendPetition {

	public static void main(String[] args) {

		// SendPetition sendPetition = new SendPetition();
		// sendPetition.initializeWidgetreceiver();
		// sendPetition.createWidgets(7);

	}

	public void createWidgets(int amount) {

		Logger.getLogger("log4j.logger.org.apache.activemq").setLevel(Level.WARNING);
		initializeWidgetreceiver();
		try {

			ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://localhost:61616");

			Connection connection = connectionFactory.createConnection();
			connection.start();

			Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

			Destination destination = session.createQueue("companyA.queue");

			MessageProducer messageProducer = session.createProducer(destination);

			for (int i = 0; i < amount; i++) {

				String text = "Create Widget number #" + i;

				System.out.println(text);

				TextMessage message = session.createTextMessage(text);
				messageProducer.send(message);

			}

			session.close();
			connection.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void initializeWidgetreceiver() {

		try {

			ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://localhost:61616");
			connectionFactory.setTrustedPackages(Arrays.asList("com.prueba.model"));
			Connection connection = connectionFactory.createConnection();

			Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

			Queue destination = session.createQueue("companyB.queue");

			MessageConsumer consumer = session.createConsumer(destination);

			consumer.setMessageListener(messageListener);

			connection.start();

		} catch (Exception e) {

			e.printStackTrace();
		}

	}

	MessageListener messageListener = new MessageListener() {

		@Override
		public void onMessage(Message message) {

			System.out.println("Recibido");

			if (message instanceof ObjectMessage) {
				ObjectMessage objectMessage = (ObjectMessage) message;
				try {
					Widget widget = (Widget) objectMessage.getObject();

					System.out.println(widget.toString());
				} catch (JMSException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}
	};

}
