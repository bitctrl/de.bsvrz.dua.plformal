package de.bsvrz.dua.plformal.allgemein;

/**
 * Exception, die geworfen wird, wenn ein Modul innerhalb einer SWE nicht initialisiert
 * werden konnte. Also, wenn z.B. keine Anmeldung zum Empfangen oder Versenden von Daten
 * durchgeführt werden konnte.
 * 
 * @author Thierfelder
 *
 */
public class DUAInitialisierungsException
extends Exception {

	private String meldung = null;
	
	public DUAInitialisierungsException(final String meldung){
		super();
		this.meldung = meldung;
	}

	public DUAInitialisierungsException(final String meldung, final Throwable t){
		super(t);
		this.meldung = meldung;
	}

	@Override
	public String getMessage() {
		return this.meldung == null?super.getMessage():this.meldung;
	}
}
