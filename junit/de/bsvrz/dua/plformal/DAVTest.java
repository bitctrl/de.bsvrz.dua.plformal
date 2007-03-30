package de.bsvrz.dua.plformal;

import stauma.dav.clientside.ClientDavInterface;
import sys.funclib.ArgumentList;
import sys.funclib.application.StandardApplication;
import sys.funclib.application.StandardApplicationRunner;

/**
 * Stellt eine Datenverteiler-Verbindung
 * zur Verfügung.
 * 
 * @author Thierfelder
 * 
 */
public class DAVTest {

	/**
	 * Verbindungsdaten
	 */
	private static final String[] CON_DATA = new String[] {
			"-datenverteiler=192.168.1.191:8083", //$NON-NLS-1$ 
			"-benutzer=Tester", //$NON-NLS-1$
			"-authentifizierung=c:\\passwd" }; //$NON-NLS-1$

	/**
	 * Verbindung zum Datenverteiler
	 */
	protected static ClientDavInterface VERBINDUNG = null;

	
	/**
	 * Erfragt bzw. initialisiert eine
	 * Datenverteiler-Verbindung
	 * 
	 * @return die Datenverteiler-Verbindung
	 * @throws Exception falls die Verbindung nicht
	 * hergestellt werden konnte
	 */
	public static final ClientDavInterface getDav()
	throws Exception {
		
		if(VERBINDUNG == null) {
			StandardApplicationRunner.run(new StandardApplication() {
	
				public void initialize(ClientDavInterface connection)
						throws Exception {
					DAVTest.VERBINDUNG = connection;
				}
	
				public void parseArguments(ArgumentList argumentList)
						throws Exception {
					//
				}
	
			}, CON_DATA);
		}
		
		return VERBINDUNG;
	}

}
