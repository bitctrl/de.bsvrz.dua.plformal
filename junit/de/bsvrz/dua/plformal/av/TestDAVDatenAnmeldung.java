package de.bsvrz.dua.plformal.av;


import org.junit.Before;
import org.junit.Test;

import stauma.dav.clientside.ClientDavInterface;
import de.bsvrz.dua.plformal.DAVTest;

/**
 * Tests für die Klasse <code>DAVDatenAnmeldung</code>
 * 
 * @author Thierfelder
 *
 */
public class TestDAVDatenAnmeldung {

	/**
	 * Datenverteiler-Verbindung
	 */
	private static ClientDavInterface VERBINDUNG = null;
	
	/**
	 * {@inheritDoc}
	 */
	@Before
	public void setUp() throws Exception {
		VERBINDUNG = DAVTest.getDav();
	}
	
	/**
	 * Testet den Konstruktor Nr.1
	 */
	@Test
	public void testKonstruktor1(){
		
	}
	
	/**
	 * Testet den Konstruktor Nr.2
	 */
	@Test
	public void testKonstruktor2(){
		
	}

}
