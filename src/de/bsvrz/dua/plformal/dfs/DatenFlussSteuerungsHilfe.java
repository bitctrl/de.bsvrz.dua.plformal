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
import de.bsvrz.dua.plformal.allgemein.DAVHilfe;
import de.bsvrz.dua.plformal.allgemein.DAVKonstanten;
import de.bsvrz.dua.plformal.allgemein.DUAInitialisierungsException;
import de.bsvrz.dua.plformal.schnittstellen.IVerwaltung;

/**
 * Diese Klasse liest die Parameter der Datenflusssteuerung aus 
 * und meldet Änderungen formatiert an andere Module des Typs
 * <code>IDatenFlussSteuerungsListener</code> weiter.
 * 
 * @author Thierfelder
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
				"Anmeldung auf Datenflusssteuerung fehlgeschlagen. "; //$NON-NLS-1$

	/**
	 * die statische Instanz dieser Klasse
	 */
	private static DatenFlussSteuerungsHilfe INSTANZ = null;

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
					+ "\nKeine Verbindung zum Datenverteiler."); //$NON-NLS-1$
		}

		if(dfsObjekt != null){
			try {
				DataDescription dd = new DataDescription(verwaltung.getVerbindung()
						.getDataModel().getAttributeGroup(DAVKonstanten.ATG_DFS),
						verwaltung.getVerbindung().getDataModel().getAspect(
								DAVKonstanten.ASP_PARA_SOLL), (short) 0);
	
				verwaltung.getVerbindung().subscribeReceiver(this, dfsObjekt, dd,
						ReceiveOptions.normal(), ReceiverRole.receiver());
	
				LOGGER.config("Für die Datenflusssteuerung" + //$NON-NLS-1$
						" wird das Objekt " + dfsObjekt + " verwendet."); //$NON-NLS-1$//$NON-NLS-2$
			} catch (Exception ex) {
				throw new DUAInitialisierungsException(STD_FEHLER, ex);
			}
		}else{
			LOGGER.warning("Die Datenflusssteuerung ist nicht zur Laufzeit steuerbar.\n" + //$NON-NLS-1$
					"Es wurde kein Objekt vom Typ " + DAVKonstanten.TYP_DFS + //$NON-NLS-1$
					" identifiziert."); //$NON-NLS-1$
		}
	}

	/**
	 * Erfragt die statische Instanz dieser Klasse
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
			 * Ermittlung des Objektes, das die Datenflusssteuerung für das
			 * übergebene Verwaltungsmodul beschreibt
			 */
			final SystemObjectType typDFS = (SystemObjectType) verwaltung
					.getVerbindung().getDataModel().getObject(
							DAVKonstanten.TYP_DFS);
			
			Collection<ConfigurationArea> kBereiche = 
				verwaltung.getKonfigurationsBereiche(); 
			SystemObject[] dfsObjekte = DAVHilfe.getAlleObjekteVomTypImKonfigBereich(
											typDFS, kBereiche).toArray(new SystemObject[0]);

			SystemObject dfsObjekt = (dfsObjekte.length > 0?dfsObjekte[0]:null);
			
			if(dfsObjekte.length > 1){
				LOGGER.warning("Es liegen mehrere Objekte vom Typ " + //$NON-NLS-1$
						DAVKonstanten.TYP_DFS + " vor"); //$NON-NLS-1$
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
			 * nur ein Objekt wird hier behandelt, d.h. dass nur ein Datensatz
			 * (der letzte) interessiert
			 */
			final ResultData resultat = resultate[resultate.length - 1];

			if (resultat != null && resultat.isSourceAvailable()
					&& !resultat.isNoDataAvailable() && resultat.hasData()
					&& resultat.getData() != null) {

				Data.Array ps = resultat.getData().getArray(
						DAVKonstanten.ATL_DFS_PARA_SATZ);

				for (int i = 0; i < ps.getLength(); i++) {
					Data satz = ps.getItem(i);
					if (satz != null) {
						ParameterSatz dfPs = new ParameterSatz();
						
						final String swe = satz.getUnscaledValue(
								DAVKonstanten.ATT_DFS_SWE).getState().getName()
								.toString();
						
						dfPs.setSwe(swe);

						for (int j = 0; j < satz.getArray(
								DAVKonstanten.ATT_DFS_PUB_ZUORDNUNG).getLength(); j++) {
							Data pz = satz.getArray(
									DAVKonstanten.ATT_DFS_PUB_ZUORDNUNG).getItem(j);
							PublikationsZuordung dfPz;
							try {
								dfPz = new PublikationsZuordung(pz);
								dfPs.add(dfPz);
							} catch (Exception e) {
								LOGGER.error("Eine Publikationszuordnung (" +  //$NON-NLS-1$
										ps + ") konnte nicht korrekt" +  //$NON-NLS-1$
												" ausgelesen werden", e);  //$NON-NLS-1$
							}
						}

						ParameterSatz dummy = letzteDfs
								.getParameterSatzFuerSWE(swe);
						
						if (dummy != null) {
							for (PublikationsZuordung neuePz : dfPs
									.getPubZuordnung()) {
								dummy.add(neuePz);
							}
						} else {
							letzteDfs.add(dfPs);
						}
					}
				}
			}
		}

		synchronized (this.listenerListe) {
			for (IDatenFlussSteuerungsListener listener : this.listenerListe) {
				listener.aktualisierePublikation(letzteDfs);
			}
		}
	}

	/**
	 * Fügt diesem Element einen neuen Beobachter hinzu. Jedes neue
	 * Beobachterobjekt wird sofort nach der Anmeldung mit den aktuellen Daten
	 * versorgt
	 * 
	 * @param listener
	 *            der neue Listener
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
					" werden. Er ist " + DAVKonstanten.NULL + "."); //$NON-NLS-1$ //$NON-NLS-2$
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