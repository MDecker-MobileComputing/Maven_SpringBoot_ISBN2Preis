package de.eldecker.spring.isbn2preis.rest;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.OK;

import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * RestController-Klasse mit REST-Endpunkt für Abfrage von zufälllige erzeugtem Preis
 * von Buch anhand ISBN.
 */
@RestController
@RequestMapping( "/api/v1" )
public class Isbn2PreisRestController {
	
	private static Logger LOG = LoggerFactory.getLogger( Isbn2PreisRestController.class );
		
	/** Regex-Pattern für ISBN-13: genau 13 Ziffern */
	private static final Pattern ISBN13_PATTERN = Pattern.compile( "^\\d{13}$" );

    
	/**
	 * Methode für HTTP-GET-REST-Endpunkt um Preis für ein Buch anhand der
	 * ISBN abzufragen.
	 * <br><br>
	 * 
	 * Beispiel-URL für lokalen Aufruf: 
	 * <pre>
	 * http://localhost:8080/api/v1/isbn2preis?isbn=9783836290494
	 * </pre>
	 * 
	 * @param isbn ISBN für das Buch, dessen Preis abgefragt werden soll.
	 *             Darf keine Bindestriche oder Leerzeichen enthalten:
	 *             Beispiel für ISBN13: {@code 9783446481220} 
	 * 
	 * @return Preis in Euro; wird aus Hash-Code der von Bindestrichen 
	 *         bereinigen ISBN berechnet; wenn ISBN eine ungültiges
	 *         Format hat, dann wird {@code -1.0} zurückgegeben.
	 */
	@GetMapping( "/isbn2preis" )
	public ResponseEntity<Double> getPreis( String isbn ) {
		
		isbn = isbn.trim();		
		
		boolean istGueltig = ISBN13_PATTERN.matcher( isbn ).matches();
		
		if ( istGueltig ) {

			final int    hashCodeQuadrat = Math.abs( isbn.hashCode() * isbn.hashCode() );
			final int    preisInEuroCent = hashCodeQuadrat % 10_000;
			final double preisInEuro     = preisInEuroCent / 100.0;
			
			LOG.info( "Antwort für ISBN={}: {} Euro", isbn, preisInEuro );
			
			return ResponseEntity.status( OK ).body( preisInEuro );
						
		} else {
			
			LOG.warn( "ISBN={} hat unerlaubtes Format.", isbn );			
			return ResponseEntity.status( BAD_REQUEST ).body( -1.0 );
		}				
	}
	
}
