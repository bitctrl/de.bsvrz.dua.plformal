package de.bsvrz.dua.plformal.vew;

import stauma.dav.clientside.ReceiveOptions;
import stauma.dav.clientside.ReceiverRole;
import stauma.dav.clientside.ResultData;
import sys.funclib.application.StandardApplicationRunner;
import sys.funclib.debug.Debug;
import de.bsvrz.dua.plformal.adapter.VerwaltungsAdapterEinfach;
import de.bsvrz.dua.plformal.allgemein.DUAInitialisierungsException;
import de.bsvrz.dua.plformal.allgemein.StandardAspekteVersorger;
import de.bsvrz.dua.plformal.av.DAVEmpfangsAnmeldungsVerwaltung;
import de.bsvrz.dua.plformal.dfs.typen.SWETyp;
import de.bsvrz.dua.plformal.plformal.IPPFHilfe;
import de.bsvrz.dua.plformal.plformal.IPPFHilfeListener;
import de.bsvrz.dua.plformal.plformal.PPFHilfe;
import de.bsvrz.dua.plformal.plformal.PlPruefungFormal;

/**
 * Implementierung des Moduls Verwaltung der SWE PL-Prüfung formal.
 * Dieses Modul erfragt die zu überprüfenden Daten aus der Parametrierung
 * und initialisiert damit das Modul PL-Prüfung formal, das dann die 
 * eigentliche Prüfung durchführt.
 * 
 * @author Thierfelder
 *
 */
public class VerwaltungPlPruefungFormal
extends VerwaltungsAdapterEinfach
implements IPPFHilfeListener{
	
	/**
	 * der Logger
	 */
	protected static final Debug LOGGER = Debug.getLogger();

	/**
	 * Instanz des Moduls PL-Prüfung formal
	 */
	private PlPruefungFormal plPruefungFormal = new PlPruefungFormal();
		
	/**
	 * Verwaltung für alle Empfangsanmeldungen dieses Moduls
	 */
	private DAVEmpfangsAnmeldungsVerwaltung empfangsVerwaltung = null; 
	
	
	/**
	 * {@inheritDoc}
	 */
	public SWETyp getSWETyp() {
		return SWETyp.PL_PRUEFUNG_FORMAL;
	}
		
	/**
	 * {@inheritDoc}
	 */
	public void aktualisiereParameter(IPPFHilfe parameter) {
		if(parameter != null){
			this.empfangsVerwaltung.modifiziereDatenAnmeldung(parameter.getDatenAnmeldungen());
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void initialisiere() throws DUAInitialisierungsException {
		super.initialisiere();
		this.empfangsVerwaltung = new DAVEmpfangsAnmeldungsVerwaltung(this.verbindung,
																	  ReceiverRole.receiver(), 
																	  ReceiveOptions.delayed(),
																	  this);
		this.plPruefungFormal = new PlPruefungFormal();
		this.plPruefungFormal.setPublikation(true);
		this.plPruefungFormal.initialisiere(this);
		
		/**
		 * An dieser Stelle werden die Parameter der formalen Plausibilisierung 
		 * auch ausgewertet, da sich hier aus Gründen der Systemarchitektur auf
		 * die Daten angemeldet werden muss, die innerhalb der Untermodule 
		 * plausibilisiert werden sollen.
		 */
		PPFHilfe.getInstanz(this).addListener(this);			
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void update(ResultData[] resultate) {
		this.plPruefungFormal.aktualisiereDaten(resultate);
	}
	
	/**
	 * Startet diese Applikation
	 * 
	 * @param args Argumente der Kommandozeile
	 */
	public static void main(String argumente[]){
        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler(){
            public void uncaughtException(@SuppressWarnings("unused")
			Thread t, Throwable e) {
                LOGGER.error("Applikation wird wegen unerwartetem Fehler beendet", e);  //$NON-NLS-1$
                Runtime.getRuntime().exit(0);
            }
        });
		StandardApplicationRunner.run(new VerwaltungPlPruefungFormal(), argumente);
	}
	
}
