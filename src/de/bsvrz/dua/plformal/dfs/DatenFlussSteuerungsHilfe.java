package de.bsvrz.dua.plformal.dfs;

import java.util.Collection;
import java.util.HashSet;

import stauma.dav.clientside.ClientReceiverInterface;
import stauma.dav.clientside.Data;
import stauma.dav.clientside.DataDescription;
import stauma.dav.clientside.ReceiveOptions;
import stauma.dav.clientside.ReceiverRole;
import stauma.dav.clientside.ResultData;
import stauma.dav.configuration.interfaces.ConfigurationArea;
import stauma.dav.configuration.interfaces.SystemObject;
import stauma.dav.configuration.interfaces.SystemObjectType;
import sys.funclib.debug.Debug;
import de.bsvrz.dua.plformal.allgemein.DUAHilfe;
import de.bsvrz.dua.plformal.allgemein.DUAInitialisierungsException;
import de.bsvrz.dua.plformal.allgemein.DUAKonstanten;
import de.bsvrz.dua.plformal.allgemein.schnittstellen.IVerwaltung;
import de.bsvrz.dua.plformal.dfs.schnittstellen.IDatenFlussSteuerungsListener;
import de.bsvrz.dua.plformal.dfs.typen.SWETyp;

/**
 * Diese Klasse liest die Parameter der Datenflusssteuerung aus 
 * und meldet Änderungen formatiert an andere Module des Typs
 * <code>IDatenFlussSteuerungsListener</code> weiter.
 * 
 * @author BitCtrl Systems GmbH, Thierfelder
 * 
 */
public class DatenFlussSteuerungsHilfe
implements ClientReceiverInterface {

	/**
	 * Debug-Logger
	 */
	private static final Debug LOGGER = Debug.getLogger();

	/**
	 * Fehlermeldungstext
	 */
	private static final String STD_FEHLER = 
				"Anmeldung auf Datenflusssteuerung fehlgeschlagen"; //$NON-NLS-1$

	/**
	 * die statische Instanz dieser Klasse
	 */
	protected static DatenFlussSteuerungsHilfe INSTANZ = null;

	/**
	 * Menge aller Beobachter dieser Instanz
	 */
	private Collection<IDatenFlussSteuerungsListener> 
			listenerListe = new HashSet<IDatenFlussSteuerungsListener>();

	/**
	 * die aktuellen Parameter der Datenflusssteuerung
	 */
	private DatenFlussSteuerung letzteDfs = null;
	
	/**
	 * Verbindung zum Verwaltungsmodul
	 */
	private IVerwaltung verwaltung = null;

	
	/**
	 * Standardkonstruktor
	 * 
	 * @param verwaltung
	 *            Verbindung zum Verwaltungsmodul
	 * @param dfsObjekt
	 *            das des Objektes, das die Datenflusssteuerung für das
	 *            übergebene Verwaltungsmodul beschreibt
	 * @throws DUAInitialisierungsException
	 *             wird geworfen, wenn die übergebene Verbindung fehlerhaft ist
	 *             (nicht die geforderten Informationen bereit hält), bzw. keine
	 *             Datenanmeldungen durchgeführt werden konnten
	 */
	private DatenFlussSteuerungsHilfe(final IVerwaltung verwaltung,
			final SystemObject dfsObjekt)
	throws DUAInitialisierungsException {
		if (verwaltung == null) {
			throw new DUAInitialisierungsException(STD_FEHLER
					+ "\nKeine Verbindung zum Datenverteiler"); //$NON-NLS-1$
		}
		this.verwaltung = verwaltung; 

		if(dfsObjekt != null){
			try {
				DataDescription dd = new DataDescription(verwaltung.getVerbindung()
						.getDataModel().getAttributeGroup(DUAKonstanten.ATG_DFS),
						verwaltung.getVerbindung().getDataModel().getAspect(
								DUAKonstanten.ASP_PARA_SOLL), (short) 0);
	
				verwaltung.getVerbindung().subscribeReceiver(this, dfsObjekt, dd,
						ReceiveOptions.normal(), ReceiverRole.receiver());
	
				LOGGER.config("Für die Datenflusssteuerung" + //$NON-NLS-1$
						" wird das Objekt " + dfsObjekt + " verwendet."); //$NON-NLS-1$//$NON-NLS-2$
			} catch (Exception ex) {
				throw new DUAInitialisierungsException(STD_FEHLER, ex);
			}
		}else{
			LOGGER.warning("Die Datenflusssteuerung ist nicht zur Laufzeit steuerbar.\n" + //$NON-NLS-1$
					"Es wurde kein Objekt vom Typ " + DUAKonstanten.TYP_DFS + //$NON-NLS-1$
					" identifiziert."); //$NON-NLS-1$
		}
	}

	/**
	 * Erfragt die statische Instanz dieser Klasse. Diese
	 * liest die Parameter der Datenflusssteuerung aus
	 * und meldet Änderungen formatiert an angemeldete Module
	 * des Typs <code>IDatenFlussSteuerungsListener</code>
	 * weiter.
	 * 
	 * @param verwaltung
	 *            Verbindung zum Verwaltungsmodul
	 * @return die statische Instanz dieser Klasse
	 * @throws DUAInitialisierungsException
	 *             wird geworfen, wenn die übergebene Verbindung fehlerhaft ist
	 *             (nicht die geforderten Informationen bereit hält), bzw. keine
	 *             Datenanmeldungen durchgeführt werden konnten
	 */
	public static final DatenFlussSteuerungsHilfe getInstanz(
			final IVerwaltung verwaltung)
	throws DUAInitialisierungsException {
		if (INSTANZ == null) {
			/**
			 * Ermittlung des Objektes, das die Datenflusssteuerung
			 * für das übergebene Verwaltungsmodul beschreibt
			 */
			final SystemObjectType typDFS = (SystemObjectType) verwaltung
					.getVerbindung().getDataModel().getObject(
							DUAKonstanten.TYP_DFS);
			
			Collection<ConfigurationArea> kBereiche = 
				verwaltung.getKonfigurationsBereiche(); 
			SystemObject[] dfsObjekte = DUAHilfe.getAlleObjekteVomTypImKonfigBereich(
											verwaltung, typDFS, kBereiche).
											toArray(new SystemObject[0]);

			SystemObject dfsObjekt = (dfsObjekte.length > 0?dfsObjekte[0]:null);
			
			if(dfsObjekte.length == 1){
				LOGGER.info("Es wurde genau ein Objekt vom Typ " + //$NON-NLS-1$
						DUAKonstanten.TYP_DFS + " identifiziert"); //$NON-NLS-1$		
			}else if(dfsObjekte.length > 1){
				LOGGER.warning("Es liegen mehrere Objekte vom Typ " + //$NON-NLS-1$
						DUAKonstanten.TYP_DFS + " vor"); //$NON-NLS-1$
			}
			
			INSTANZ = new DatenFlussSteuerungsHilfe(verwaltung, dfsObjekt);
		}
		return INSTANZ;
	}

	/**
	 * {@inheritDoc}
	 */
	public void update(ResultData[] resultate) {
		letzteDfs = new DatenFlussSteuerung();

		if (resultate != null && resultate.length > 0) {
			/**
			 * nur ein Objekt wird hier behandelt, d.h. dass nur ein
			 * Datensatz (der Letzte) interessiert
			 */
			final ResultData resultat = resultate[resultate.length - 1];

			if (resultat != null && resultat.isSourceAvailable()
					&& !resultat.isNoDataAvailable() && resultat.hasData()
					&& resultat.getData() != null) {

				Data.Array ps = resultat.getData().getArray(
						DUAKonstanten.ATL_DFS_PARA_SATZ);

				for (int i = 0; i < ps.getLength(); i++) {
					Data satz = ps.getItem(i);
					if (satz != null) {
						ParameterSatz dfParameterSatz = new ParameterSatz();
						
						final SWETyp swe = SWETyp.getZustand((int)satz.getUnscaledValue(
								DUAKonstanten.ATT_DFS_SWE).getState().getValue());
						dfParameterSatz.setSwe(swe);

						/**
						 * Iteriere über alle Publikationszuordnungen
						 * innerhalb dieses Parametersatzes
						 */
						for (int j = 0; j < satz.getArray(
								DUAKonstanten.ATT_DFS_PUB_ZUORDNUNG).getLength(); j++) {
							Data paraZuordnung = satz.getArray(
									DUAKonstanten.ATT_DFS_PUB_ZUORDNUNG).getItem(j);
							PublikationsZuordung dfParaZuordnung;
							try {
								dfParaZuordnung = new PublikationsZuordung(
										paraZuordnung, verwaltung);
								dfParameterSatz.add(dfParaZuordnung);
							} catch (Exception e) {
								LOGGER.error("Eine Publikationszuordnung " +  //$NON-NLS-1$
										"konnte nicht korrekt" +  //$NON-NLS-1$
										" ausgelesen werden: " + paraZuordnung, e);  //$NON-NLS-1$
							}
						}

						ParameterSatz dummy = letzteDfs
								.getParameterSatzFuerSWE(swe);
						
						if (dummy != null) {
							for (PublikationsZuordung neuePz : dfParameterSatz
									.getPubZuordnung()) {
								dummy.add(neuePz);
							}
						} else {
							letzteDfs.add(dfParameterSatz);
						}
					}
				}
			}
		}

		if(letzteDfs != null){
			synchronized (this.listenerListe) {
				for (IDatenFlussSteuerungsListener listener : this.listenerListe) {
					listener.aktualisierePublikation(letzteDfs);
				}
			}
		}
	}

	/**
	 * Fügt diesem Element einen neuen Beobachter hinzu. Jedes neue
	 * Beobachterobjekt wird sofort nach der Anmeldung mit den aktuellen
	 * Daten versorgt.
	 * 
	 * @param listener
	 *            der neue Beobachter
	 */
	public final void addListener(final IDatenFlussSteuerungsListener listener) {
		if (listener != null) {
			synchronized (this.listenerListe) {
				this.listenerListe.add(listener);
				if (letzteDfs != null) {
					listener.aktualisierePublikation(letzteDfs);
				}
			}
		} else {
			LOGGER.warning("Listener kann nicht eingefügt" + //$NON-NLS-1$
					" werden. Er ist " + DUAKonstanten.NULL); //$NON-NLS-1$
		}
	}

	/**
	 * Löscht ein Beobachterobjekt.
	 * 
	 * @param listener
	 *            das zu löschende Beobachterobjekt
	 */
	public final void removeListener(
			final IDatenFlussSteuerungsListener listener) {
		if (listener != null) {
			synchronized (this.listenerListe) {
				this.listenerListe.remove(listener);
			}
		}
	}
}