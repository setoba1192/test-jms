package com.prueba;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jms.annotation.EnableJms;

import com.prueba.client.SendPetition;

@SpringBootApplication
@EnableJms
public class Application {

	public static void main(String[] args) {

		if (args.length > 0) {

			int ammount = Integer.parseInt(args[1]);

			if (ammount > 0) {

				SendPetition enviarPeticion = new SendPetition();
				enviarPeticion.createWidgets(ammount);

			}

			return;

		}

		SpringApplication.run(Application.class, args);
	}

}