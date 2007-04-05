package de.bsvrz.dua.plformal.allgemein;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

import stauma.dav.clientside.DataDescription;
import stauma.dav.clientside.ResultData;
import stauma.dav.configuration.interfaces.Aspect;
import stauma.dav.configuration.interfaces.AttributeGroup;
import stauma.dav.configuration.interfaces.DataModel;
import stauma.dav.configuration.interfaces.SystemObject;
import stauma.dav.configuration.interfaces.SystemObjectType;
import sys.funclib.debug.Debug;
import de.bsvrz.dua.plformal.allgemein.schnittstellen.IStandardAspekte;
import de.bsvrz.dua.plformal.allgemein.schnittstellen.IVerwaltung;
import de.bsvrz.dua.plformal.av.DAVDatenAnmeldung;
import de.bsvrz.dua.plformal.av.DAVObjektAnmeldung;
import de.bsvrz.dua.plformal.dfs.typen.SWETyp;

/**
 * Instanzen dieser Klasse stellen die Standard-Publikationsinformationen
 * für <b>eine</b> SWE und <b>einen</b> Modul-Typ innerhalb der DUA zur
 * Verfügung.<br> Diese Informationen können im Konstruktor dieser Klasse
 * für je eine SWE-Modultyp-Kombination als Instanz der Klasse <code>
 * StandardAspekteAdapter</code> angelegt
 * werden.
 * 
 * @author BitCtrl Systems GmbH, Thierfelder
 * 
 */
public abstract class StandardAspekteVersorger {

	/**
	 * Debug-Logger
	 */
	protected static final Debug LOGGER = Debug.getLogger();
	
	/**
	 * Verbindung zum Verwaltungsmodul
	 */
	protected IVerwaltung verwaltung = null;
	
	/**
	 * Die Informationen über die Standardaspekte für die
	 * Publikation einer bestimmten SWE-Modultyp-Kombinationen
	 */
	protected IStandardAspekte standardAspekte = new IStandardAspekte(){

		/**
		 * {@inheritDoc}
		 */
		public final Collection<DAVObjektAnmeldung> getStandardAnmeldungen(
				final SystemObject[] objektFilter) {
			return new ArrayList<DAVObjektAnmeldung>();
		}

		/**
		 * {@inheritDoc}
		 */
		public Aspect getStandardAspekt(ResultData originalDatum) {
			return null;
		}
		
	};
	
	
	/**
	 * Standardkonstruktor
	 * 
	 * @param verwaltung Verbindung zum Verwaltungsmodul
	 */
	public StandardAspekteVersorger(final IVerwaltung verwaltung)
	throws DUAInitialisierungsException{
		this.verwaltung = verwaltung;
		init(verwaltung.getSWETyp());
	}
	
	/**
	 * Initialisiert die Standard-Publikationsinformationen für einen
	 * kompleten Modul-Typ unter der Vorraussetzung, dass das Modul 
	 * unter der übergebenen SWE läuft.
	 *   
	 * @param swe die SWE unter der das Modul läuft
	 * @throws DUAInitialisierungsException wenn es Probleme beim
	 * Initialisieren der Standard-Publikationsinformationen gab
	 */
	protected abstract void init(final SWETyp swe)
	throws DUAInitialisierungsException;
	
	/**
	 * Erfragt die Standardpublikations-Informationen
	 * 
	 * @return eine <code>IStandardAspekte</code>-Schnittstelle
	 * zu den Standardpublikations-Informationen
	 */
	public final IStandardAspekte getStandardPubInfos() {
		return this.standardAspekte;
	}
	
	
	/**
	 * In diesen Objekten werden alle Informationen über das standardmäßige
	 * Publikationsverhalten von SWE-Modultyp-Kombinationen festgehalten. Diese
	 * Objekte werden innerhalb von <code>StandardAspekteVersorger</code>
	 * statisch erstellt und können über die statische Methode <code>
	 * getStandardPubInfos(..)</code>
	 * ausgelesen werden.
	 * 
	 * @author BitCtrl Systems GmbH, Thierfelder
	 * 
	 */
	protected class StandardAspekteAdapter
	implements IStandardAspekte {

		/**
		 * Notwendige Datenanmeldungen ohne Filterung
		 */
		private Collection<DAVObjektAnmeldung> anmeldungenGlobal = 
					new ArrayList<DAVObjektAnmeldung>();
		
		/**
		 * 
		 */
		private Map<DAVObjektAnmeldung, Aspect> publikationsMap =
			new TreeMap<DAVObjektAnmeldung, Aspect>();

		/**
		 * Standardkonstruktor
		 * 
		 * @param zuordnungen
		 *            Liste mit Standardpublikationszurodnungen
		 * @throws DUAInitialisierungsException wenn Standard-
		 * Publikaionsinformationen nicht angelegt werden konnten
		 */
		public StandardAspekteAdapter(final StandardPublikationsZuordnung[] zuordnungen)
		throws DUAInitialisierungsException{
			if(zuordnungen != null){
				for (StandardPublikationsZuordnung zuordnung : zuordnungen) {
					DAVDatenAnmeldung anmeldung = null;
					
					try {
						anmeldung = new DAVDatenAnmeldung(
								new SystemObject[] { zuordnung.typ },
								new DataDescription(zuordnung.atg, zuordnung.aspAusgang, (short)0),
								StandardAspekteVersorger.this.verwaltung.getKonfigurationsBereiche(),
								verwaltung.getVerbindung());
					
						anmeldungenGlobal.addAll(anmeldung.getObjektAnmeldungen());
						
						DataDescription originalDesc = new DataDescription(zuordnung.atg,
								zuordnung.aspEingang);
						for(SystemObject obj:zuordnung.typ.getElements()){
							DAVObjektAnmeldung objektAnmeldung =
								new DAVObjektAnmeldung(obj, originalDesc);
							this.publikationsMap.put(objektAnmeldung, zuordnung.aspAusgang);
						}
					} catch (Exception e) {
						throw new DUAInitialisierungsException("Standard-" + //$NON-NLS-1$
								"Publikaionsinformationen konnten nicht angelegt werden"); //$NON-NLS-1$
					}
				}				
			}
		}

		/**
		 * {@inheritDoc}
		 */
		public Aspect getStandardAspekt(ResultData originalDatum) {
			Aspect ergebnis = null;

			if(originalDatum != null){
				try{
					DAVObjektAnmeldung objektAnmeldung = 
						new DAVObjektAnmeldung(originalDatum.getObject(), 
								originalDatum.getDataDescription());
					
					ergebnis = this.publikationsMap.get(objektAnmeldung);
				}catch(Exception e){
					LOGGER.error("Der Standard-Publikationsaspekt konnte" + //$NON-NLS-1$
							"nicht ermittelt werden: " + originalDatum, e); //$NON-NLS-1$
				}
			}

			return ergebnis;
		}

		/**
		 * {@inheritDoc}
		 */
		public final Collection<DAVObjektAnmeldung> getStandardAnmeldungen(
				final SystemObject[] objektFilter) {
			Collection<DAVObjektAnmeldung> anmeldungen = 
							new TreeSet<DAVObjektAnmeldung>();
			
			if(objektFilter == null || objektFilter.length == 0){
				anmeldungen = anmeldungenGlobal;	
			}else{	
				HashSet<SystemObject> objekte = new HashSet<SystemObject>();
				for(SystemObject obj:objektFilter){
					objekte.add(obj);
				}
				
				for(DAVObjektAnmeldung anmeldung:anmeldungenGlobal){
					if(objekte.contains(anmeldung.getObjekt())){
						anmeldungen.add(anmeldung);
					}
				}
			}
			
			return anmeldungen;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public String toString() {
			String s = "Objekt-Anmeldungen:\n"; //$NON-NLS-1$

			for (DAVObjektAnmeldung anmeldung : anmeldungenGlobal) {
				s += anmeldung;
			}

			return s;
		}

	}
	

	/**
	 * Zuordnung einer bestimmten
	 * <code>SystemObjectType-AttributeGroup-Aspect</code>-Kombination zu
	 * einem Standardpublikationsaspekt
	 * 
	 * @author BitCtrl Systems GmbH, Thierfelder
	 * 
	 */
	protected class StandardPublikationsZuordnung {

		/**
		 * Objekttyp eines Originaldatums
		 */
		protected SystemObjectType typ = null;

		/**
		 * Attributgruppe eines Originadatums
		 */
		protected AttributeGroup atg = null;

		/**
		 * Aspekt eines Originaldatums
		 */
		protected Aspect aspEingang = null;

		/**
		 * Standardpublikationsaspekt
		 */
		protected Aspect aspAusgang = null;
		
		/**
		 * Standardkonstruktor
		 * 
		 * @param typ
		 *            Objekttyp des Originaldatums
		 * @param atg
		 *            Attributgruppe des Originadatums
		 * @param aspEingang
		 *            Aspekt des Originaldatums
		 * @param aspAusgang
		 *            Standardpublikationsaspekt für die <code>SystemObjectType-
		 *            AttributeGroup-Aspect</code>-Kombination
		 * @throws DUAInitialisierungsException falls eines der übergebenen
		 * DAV-Elemente nicht ausgelesen werden konnte
		 */
		public StandardPublikationsZuordnung(String typ, String atg,
				String aspEingang, String aspAusgang)
		throws DUAInitialisierungsException{
			try{
				DataModel dataModel = StandardAspekteVersorger.this.
									verwaltung.getVerbindung().getDataModel();
				this.typ = dataModel.getType(typ);
				this.atg = dataModel.getAttributeGroup(atg);
				this.aspEingang = dataModel.getAspect(aspEingang);
				this.aspAusgang = dataModel.getAspect(aspAusgang);
			}catch(Exception ex){
				throw new DUAInitialisierungsException(
						"Standardpublikationsaspekt" + //$NON-NLS-1$
						" konnte" + //$NON-NLS-1$
						" nicht eingerichtet werden ", ex); //$NON-NLS-1$
			}
		}
	}
}
