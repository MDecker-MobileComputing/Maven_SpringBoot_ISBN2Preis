package de.eldecker.spring.isbn2preis.rest;

import static java.lang.Double.NEGATIVE_INFINITY;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.OK;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


/**
 * RestController-Klasse mit REST-Endpunkt für Abfrage von Preis
 * von Buch anhand ISBN-13.
 */
@RestController
@RequestMapping( "/api/v1" )
public class Isbn2PreisRestController {
	
	private static Logger LOG = LoggerFactory.getLogger( Isbn2PreisRestController.class );


	/**
	 * Methode für HTTP-GET-REST-Endpunkt um Preis für ein Buch anhand der
	 * ISBN13 abzufragen.
	 * <br><br>
	 * 
	 * Beispiel-URL für lokalen Aufruf an Port 8010 (Instanz 1): 
	 * <pre>
	 * http://localhost:8010/api/v1/isbn2preis?isbn13=9783836290494
	 * </pre>
	 * 
	 * @param isbn13 ISBN13 für das Buch, dessen Preis abgefragt werden soll.
	 *               Beispiel: {@code 9783446481220}
	 * 
	 * @return Preis in Euro; wird aus Hash-Code der von Bindestrichen 
	 *         bereinigen ISBN13 berechnet. Wenn eine Zahl übergeben wird,
	 *         die nicht genau 13 Stellen hat, dann wird {@code -1}
	 *         zurückgegeben.
	 */
	@GetMapping( "/isbn2preis" )
	public ResponseEntity<Double> getPreis( @RequestParam("isbn13") Long isbn13 ) {
		
		if ( isbn13.toString().length() != 13 ) {
		
			LOG.error( "Aufruf mit ISBN={}, hat aber nicht genau 13 Stellen.", isbn13 );
			return ResponseEntity.status( BAD_REQUEST ).body( NEGATIVE_INFINITY );
		}
				
		final int    hashCodeQuadrat = Math.abs( isbn13.hashCode() * isbn13.hashCode() );
		final int    preisInEuroCent = hashCodeQuadrat % 10_000;
		final double preisInEuro     = preisInEuroCent / 100.0;
		
		LOG.info( "Antwort für ISBN13={}: {} Euro", isbn13, preisInEuro );
		
		return ResponseEntity.status( OK ).body( preisInEuro );
	}
	
}
