package de.eldecker.spring.isbn2preis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


/**
 * Klasse mit Einstiegsmethode der Anwendung.
 */
@SpringBootApplication
public class IsbnZuPreisApplication {

	
	/**
	 * Einstiegsmethode der Anwendung.
	 * 
	 * @param args Kommandozeilenargumente, werden an Spring durchgereicht
	 */
	public static void main( String[] args ) {
		
		SpringApplication.run( IsbnZuPreisApplication.class, args );
	}

}
