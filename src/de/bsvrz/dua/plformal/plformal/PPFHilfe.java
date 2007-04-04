package de.bsvrz.dua.plformal.plformal;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import stauma.dav.clientside.ClientReceiverInterface;
import stauma.dav.clientside.Data;
import stauma.dav.clientside.DataDescription;
import stauma.dav.clientside.ReceiveOptions;
import stauma.dav.clientside.ReceiverRole;
import stauma.dav.clientside.ResultData;
import stauma.dav.configuration.interfaces.Aspect;
import stauma.dav.configuration.interfaces.AttributeGroup;
import stauma.dav.configuration.interfaces.SystemObject;
import stauma.dav.configuration.interfaces.SystemObjectType;
import sys.funclib.debug.Debug;
import de.bsvrz.dua.plformal.allgemein.DUAHilfe;
import de.bsvrz.dua.plformal.allgemein.DUAKonstanten;
import de.bsvrz.dua.plformal.allgemein.DUAInitialisierungsException;
import de.bsvrz.dua.plformal.allgemein.schnittstellen.IVerwaltung;
import de.bsvrz.dua.plformal.av.DAVDatenAnmeldung;

/**
 * Diese Klasse meldet sich auf die Attributgruppe <code>atg.plausibilitätsPrüfungFormal</code> 
 * eines Objekts vom Typ <code>typ.plausibilitätsPrüfungFormal</code> an. Es stellt die darin
 * enthaltenen Informationen über die Schnittstelle <code>IPPFHilfe</code> allen
 * angemeldeten Instanzen von <code>IPPFHilfeListener</code> zur Verfügung.
 * 
 * @author Thierfelder
 *
 */
public class PPFHilfe
implements IPPFHilfe, ClientReceiverInterface{
		
	/**
	 * der Logger
	 */
	private static final Debug LOGGER = Debug.getLogger();
	
	/**
	 * die statische Instanz dieser Klasse
	 */
	private static PPFHilfe INSTANZ = null;
		
	/**
	 * Verbindung zum Verwaltungsmodul
	 */
	private IVerwaltung verwaltung;
	
	/**
	 * die Informationen der Attributgruppe <code>atg.plausibilitätsPrüfungFormal</code> 
	 */
	private PPFDatenBeschreibungen plBeschreibungen = null;
		
	/**
	 * Alle Datenidentifikationen, die z.Z. für eine formale Plausibilisierung vorgesehen sind.
	 */
	private List<DAVDatenAnmeldung> aktuelleAnmeldungen = new ArrayList<DAVDatenAnmeldung>();
	
	/**
	 * Alle Listener dieses Objektes
	 */
	private Collection<IPPFHilfeListener> listenerListe = new HashSet<IPPFHilfeListener>();
	
	
	/**
	 * Standardkonstruktor
	 * 
	 * @param verwaltung Verbindung zum Verwaltungsmodul
	 * @param plausbibilisierungsObjekt das Objekt vom Typ <code>typ.plausibilitätsPrüfungFormal</code>
	 * @throws DUAInitialisierungsException falls die Datenanmeldung schief geht 
	 */
	private PPFHilfe(final IVerwaltung verwaltung,
					 final SystemObject plausbibilisierungsObjekt)
	throws DUAInitialisierungsException{
		if(verwaltung == null){
			throw new DUAInitialisierungsException("Keine Verbindung zur Verwaltung."); //$NON-NLS-1$
		}
		if(verwaltung.getVerbindung() == null){
			throw new DUAInitialisierungsException("Keine Verbindung zum Datenverteiler."); //$NON-NLS-1$
		}
		if(plausbibilisierungsObjekt == null){
			throw new DUAInitialisierungsException("Ungültiges Plausibilisierungsobjekt."); //$NON-NLS-1$
		}
		this.verwaltung = verwaltung;
		final AttributeGroup atg = verwaltung.getVerbindung().getDataModel().
									getAttributeGroup(DUAKonstanten.ATG_PL_FORMAL);
		final Aspect asp = verwaltung.getVerbindung().getDataModel().
							getAspect(DUAKonstanten.ASP_PARA_SOLL);
		final DataDescription dd = new DataDescription(atg, asp, (short)0);
		verwaltung.getVerbindung().subscribeReceiver(this, plausbibilisierungsObjekt, dd,
					ReceiveOptions.normal(), ReceiverRole.receiver());
		
		LOGGER.info("Initialisierung erfolgreich."); //$NON-NLS-1$
	}
	
	/**
	 * Erfragt die statische Instanz dieser Klasse
	 * 
	 * @param verwaltung Verbindung zum Verwaltungsmodul
	 * @return die statische Instanz dieser Klasse
	 * @throws DUAInitialisierungsException falls die Datenanmeldung schief geht
	 */
	public static final PPFHilfe getInstanz(final IVerwaltung verwaltung)
	throws DUAInitialisierungsException{
		if(INSTANZ == null){
			/**
			 * Ermittlung des Objektes, das die formale Plausibilisierung beschreibt
			 */
			final SystemObjectType typPPF = (SystemObjectType)verwaltung.getVerbindung().getDataModel().getObject(DUAKonstanten.TYP_PL_FORMAL);
 			SystemObject[] plausibilisierungsObjekte = DUAHilfe
								.getAlleObjekteVomTypImKonfigBereich(verwaltung, typPPF,
									verwaltung.getKonfigurationsBereiche()).toArray(
											new SystemObject[0]);
			 			
			if(plausibilisierungsObjekte.length > 0){
				INSTANZ = new PPFHilfe(verwaltung, plausibilisierungsObjekte[0]);
			}else{
				throw new DUAInitialisierungsException("Es liegt kein Objekt vom Typ " + //$NON-NLS-1$
						DUAKonstanten.TYP_PL_FORMAL + " vor.");  //$NON-NLS-1$
			}
		}

		return INSTANZ;
	}

	/**
	 * {@inheritDoc}
	 */
	public void update(ResultData[] resultate) {
		List<DAVDatenAnmeldung> neueAnmeldungen = new ArrayList<DAVDatenAnmeldung>();
		PPFDatenBeschreibungen neuePlBeschreibungen = new PPFDatenBeschreibungen();
		boolean fehler = false;
		LOGGER.info("Neue Parameter empfangen."); //$NON-NLS-1$
		
		if(resultate != null && resultate.length > 0){
			/**
			 * nur ein Objekt wird hier behandelt, d.h. dass 
			 * nur ein Datensatz (der letzte) interessiert
			 */
			final ResultData resultat = resultate[resultate.length - 1];
			
			if(resultat != null && resultat.isSourceAvailable() &&
					!resultat.isNoDataAvailable() && resultat.hasData() &&
					 resultat.getData() != null){
				
				final Data.Array parameterSaetze = resultat.getData().getArray(DUAKonstanten.ATL_PL_FORMAL_PARA_SATZ);
				
				for(int i = 0; i<parameterSaetze.getLength(); i++){
					final Data parameterSatz = parameterSaetze.getItem(i);
					try {
						neuePlBeschreibungen.addParameterSatz(parameterSatz);
						LOGGER.fine(parameterSatz.toString());
					
						final AttributeGroup atg = (AttributeGroup)parameterSatz.getReferenceValue
														(DUAKonstanten.ATT_PL_FORMAL_PARA_SATZ_ATG).getSystemObject();
						final Aspect asp = (Aspect)parameterSatz.getReferenceValue
												(DUAKonstanten.ATT_PL_FORMAL_PARA_SATZ_ASP).getSystemObject();
						final DataDescription dd = new DataDescription(atg, asp, (short)0);
						final Collection<SystemObject> objListe = new HashSet<SystemObject>();
						final Data.Array objArray = parameterSatz.getArray(DUAKonstanten.ATL_PL_FORMAL_PARA_SATZ_OBJ);
						for(int j = 0; j<objArray.getLength(); j++){
							objListe.add(objArray.getItem(j).asReferenceValue().getSystemObject());
						}
						final DAVDatenAnmeldung neueAnmeldung = new DAVDatenAnmeldung(objListe.toArray(new SystemObject[0]), dd,
																	verwaltung.getKonfigurationsBereiche(),
																	verwaltung.getVerbindung());
						neueAnmeldungen.add(neueAnmeldung);
					} catch (Throwable e) {
						LOGGER.error("Parameterdatensatz für die formale" + //$NON-NLS-1$
								" Plausibilisierung konnte nicht ausgelesen werden.", e); //$NON-NLS-1$
						fehler = true;
						break;
					}
				}
			}
		}
		
		/**
		 * alle informieren
		 */
		if(!fehler){
			synchronized (this) {
				plBeschreibungen = neuePlBeschreibungen;
				aktuelleAnmeldungen = neueAnmeldungen;
				for(IPPFHilfeListener listener:listenerListe){
					listener.aktualisiereParameter(this);
				}			
			}
		}			
		
		LOGGER.info(this.toString());
	}
	
	/**
	 * {@inheritDoc}
	 */
	public SystemObject[] getBetrachteteObjekte() {
		Collection<SystemObject> objekte = new HashSet<SystemObject>();
		
		for(DAVDatenAnmeldung anmeldung:this.getDatenAnmeldungen()){
			objekte.addAll(anmeldung.getObjekte());
		}
		
		return objekte.toArray(new SystemObject[0]);
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isZurPausibilisierungVorgesehen(final ResultData resultat) {
		boolean ergebnis = false;
		
		if(resultat != null && resultat.hasData() &&
		   resultat.getData() != null && this.plBeschreibungen != null){
			ergebnis = this.plBeschreibungen.getAttributSpezifikationenFuer(resultat) != null;
		}
		
		return ergebnis;
	}

	/**
	 * {@inheritDoc}
	 */
	public List<DAVDatenAnmeldung> getDatenAnmeldungen() {
		return this.aktuelleAnmeldungen;
	}

	/**
	 * {@inheritDoc}
	 */
	public Data plausibilisiere(ResultData resultat) {
		Data ergebnis = null;
		
		if(resultat != null && resultat.hasData() && resultat.getData() != null &&			
		   this.plBeschreibungen != null){
			Collection<PPFAttributSpezifikation> attSpezifikationen = 
					this.plBeschreibungen.getAttributSpezifikationenFuer(resultat);

			if(attSpezifikationen != null){
				Data data = resultat.getData();
				boolean echtModifiziert = false;
				
				for(PPFAttributSpezifikation attSpez:attSpezifikationen){
					Data dummy = plausibilisiereAttribut(data, attSpez);
					if(dummy != null){
						data = dummy;
						echtModifiziert = true;
					}
				}
				
				if(echtModifiziert){
					ergebnis = data;
				}
			}else{
				LOGGER.info("ResultData " + resultat + //$NON-NLS-1$
				" ist nicht zur formalen Plausibilisierung vorgesehen."); //$NON-NLS-1$
			}

		}
		
		return ergebnis;
	}
	
	/**
	 * Führt die formale Plausibilisierung für ein bestimmtes Attribut innerhalb
	 * eines Datensatzes durch und gibt für dieses Attribut plausibilisierten
	 * Datensatz zurück.
	 * 
	 * @param datum der Datensatz
	 * @param attSpez die Spezifikation des Attributs und die Parameter der formalen
	 * Plausibilisierung desselben
	 * @return für dieses Attribut plausibilisierten Datensatz, so er plausibilisiert
	 * werden konnte, oder <code>null</code> sonst.
	 */
	private final Data plausibilisiereAttribut(final Data datum,
									  		   final PPFAttributSpezifikation attSpez){
		Data ergebnis = null;
		
		if(attSpez != null){
			final String attPfad = attSpez.getAttributPfad();
			final double min = attSpez.getMin();
			final double max = attSpez.getMax();
			if(max < min){
				LOGGER.warning("Max-Wert (" + max + ") ist kleiner" +//$NON-NLS-1$ //$NON-NLS-2$
						" als Min-Wert (" + min + ")");   //$NON-NLS-1$//$NON-NLS-2$
			}
			
			final int methode = (int)attSpez.getMethode();
			if(attPfad != null){
				try{
					Data dummy = datum.createModifiableCopy();
					Data plausDatum = DUAHilfe.getAttributDatum(attPfad, dummy);
					if(plausDatum != null && 
						(plausDatum.getAttributeType().isOfType(DUAKonstanten.TYP_GANZ_ZAHL) || 
						 plausDatum.getAttributeType().isOfType(DUAKonstanten.TYP_KOMMA_ZAHL))){
						
						/**
						 * Eine Plausibilisierung wird nur durchgeführt, wenn das Attribut sich nicht in einem
						 * vordefinierten Zustand befindet. Sollte ein vordefinierter Zustand vorliegen, so wird
						 * das Datum zwar nicht plausibilisiert, diese Methode gibt jedoch trotzdem einen
						 * Wert zurück (damit nachgelagerte Funktionen z.B. dieses Datum publizieren können)
						 */
						if(!plausDatum.asUnscaledValue().isState()){
							double alterWert = plausDatum.asUnscaledValue().doubleValue();
							double neuerWert = plausDatum.asUnscaledValue().doubleValue();
							boolean implausibelMin = alterWert < min;
							boolean implausibelMax = alterWert > max;

							/**
							 * Plausibilisierung
							 */
							switch(methode){
							case (int)DUAKonstanten.ATT_PL_FORMAL_WERT_NUR_PRUEFUNG:
								if(implausibelMin || implausibelMax){
									setStatusPlFormalWert(dummy, true, attPfad, DUAKonstanten.ATT_PL_FORMAL_STATUS_IMPLAUSIBEL);
								}else{
									setStatusPlFormalWert(dummy, false, attPfad, DUAKonstanten.ATT_PL_FORMAL_STATUS_IMPLAUSIBEL);
								}								
								break;
							case (int)DUAKonstanten.ATT_PL_FORMAL_WERT_SETZE_MINMAX:
								if(implausibelMax){
									neuerWert = max;
									setStatusPlFormalWert(dummy, false, attPfad,
											DUAKonstanten.ATT_PL_FORMAL_STATUS_MIN);
									setStatusPlFormalWert(dummy, true, attPfad,
											DUAKonstanten.ATT_PL_FORMAL_STATUS_MAX);
								}else if(implausibelMin){
									neuerWert = min;
									setStatusPlFormalWert(dummy, true, attPfad,
											DUAKonstanten.ATT_PL_FORMAL_STATUS_MIN);
									setStatusPlFormalWert(dummy, false, attPfad,
											DUAKonstanten.ATT_PL_FORMAL_STATUS_MAX);
								}
							break;
							case (int)DUAKonstanten.ATT_PL_FORMAL_WERT_SETZE_MIN:
								if(implausibelMin){
									neuerWert = min;
									setStatusPlFormalWert(dummy, true, attPfad,
											DUAKonstanten.ATT_PL_FORMAL_STATUS_MIN);
									setStatusPlFormalWert(dummy, false, attPfad,
											DUAKonstanten.ATT_PL_FORMAL_STATUS_MAX);
								}							
							break;
							case (int)DUAKonstanten.ATT_PL_FORMAL_WERT_SETZE_MAX:
								if(implausibelMax){
									neuerWert = max;
									setStatusPlFormalWert(dummy, false, attPfad,
											DUAKonstanten.ATT_PL_FORMAL_STATUS_MIN);
									setStatusPlFormalWert(dummy, true, attPfad,
											DUAKonstanten.ATT_PL_FORMAL_STATUS_MAX);
								}
							break;
							case (int)DUAKonstanten.ATT_PL_FORMAL_WERT_KEINE_PRUEFUNG:
								// mache nichts
							}

							if(neuerWert != alterWert){
								if(plausDatum.getAttributeType().isOfType(DUAKonstanten.TYP_GANZ_ZAHL)){
									plausDatum.asUnscaledValue().set((long)neuerWert);	
								}else{
									plausDatum.asUnscaledValue().set(neuerWert);	
								}
							}
						}
						
						/**
						 * Veränderung des Rückgabedatums
						 */
						ergebnis = dummy;
					}
				}catch(Exception ex){
					LOGGER.error("Unerwarteter Fehler beim formalen Plausibilisieren von " + datum, ex); //$NON-NLS-1$
				}
			}else{
				LOGGER.warning("Für Datum " + datum + " ist die Attributspezifikation "//$NON-NLS-1$ //$NON-NLS-2$
						+ attSpez + " unvollständig.");  //$NON-NLS-1$
			}
		}
		
		return ergebnis;
	}

	/**
	 * Fügt diesem Element einen Listener hinzu
	 * 
	 * @param listener der neue Listener
	 */
	public final void addListener(final IPPFHilfeListener listener){
		if(this.listenerListe.add(listener)){
			synchronized (this) {
				listener.aktualisiereParameter(this);
			}	
		}
	}
	
	/**
	 * Entfernt einen Listener von diesem Element
	 * 
	 * @param listener der zu entfernende Listener
	 */
	public final void removeListener(final IPPFHilfeListener listener){
		synchronized (this) {
			this.listenerListe.remove(listener);	
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		String s = DUAKonstanten.STR_UNDEFINIERT;
		
		synchronized (this) {
			if(this.plBeschreibungen != null){
				s = "Datenanmeldungen: \n"; //$NON-NLS-1$
				for(DAVDatenAnmeldung da:this.aktuelleAnmeldungen){
					s += da + "\n"; //$NON-NLS-1$
				}
				s += "Plausibilisierungsbeschreibung: \n" + this.plBeschreibungen; //$NON-NLS-1$
			}						
		}
		
		return s;
	}

	/**
	 * Setzt den Wert eines bestimmten Status-Flags (MIN/MAX)
	 * für ein bistimmtes Attribut innerhalb einer bestimmten
	 * Attributgruppe. (So dieser vorhanden ist!)
	 * 
	 * @param datum der Datensatz, in dem sich der Statuswert
	 * befindet 
	 * @param wert der neue Status-Wert
	 * @param attPfad der Attributpfad bis zum dem Wert, der vom
	 * bewussten Status-Flag flankiert wird.
	 * @param attPfadErsetzung der Attributpfad bis zum Status-
	 * Flag (vom Wert aus gesehen)
	 */
	private final void setStatusPlFormalWert(final Data datum,
 											 final boolean wert,
											 final String attPfad,
											 final String attPfadErsetzung){
		if(datum != null && attPfad != null && attPfadErsetzung != null){
			final String neuerAttPfad = DUAHilfe.
				ersetzeLetztesElemInAttPfad(attPfad, attPfadErsetzung);
			
			if(neuerAttPfad != null){
				Data status = DUAHilfe.getAttributDatum(neuerAttPfad, datum);
				if(status != null){
					try{
						status.asUnscaledValue().set(wert?1:0);
					}catch(Exception ex){
						LOGGER.error("Unerwarteter Fehler beim " + //$NON-NLS-1$
								"Setzen des Statusflags", ex); //$NON-NLS-1$
					}
				}else{
					LOGGER.warning("Statuswert konnte nicht" + //$NON-NLS-1$
							" ausgelesen werden:\n" + //$NON-NLS-1$
							"Datum: " + datum + "\nAttr.-Pfad: " + attPfad + //$NON-NLS-1$ //$NON-NLS-2$
							"\nErsetzung: " + attPfadErsetzung); //$NON-NLS-1$					
				}
			}else{
				LOGGER.warning("Attributpfad zum Statuswert konnte nicht" + //$NON-NLS-1$
						" erstellt werden:\n" + //$NON-NLS-1$
						"Datum: " + datum + "\nAttr.-Pfad: " + attPfad + //$NON-NLS-1$ //$NON-NLS-2$
						"\nErsetzung: " + attPfadErsetzung); //$NON-NLS-1$
			}
		}
	}
}

