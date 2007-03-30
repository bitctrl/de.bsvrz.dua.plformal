package de.bsvrz.dua.plformal.allgemein;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.TreeMap;

import stauma.dav.clientside.ClientDavInterface;
import stauma.dav.clientside.DataDescription;
import stauma.dav.clientside.ResultData;
import stauma.dav.configuration.interfaces.Aspect;
import stauma.dav.configuration.interfaces.AttributeGroup;
import stauma.dav.configuration.interfaces.ConfigurationArea;
import stauma.dav.configuration.interfaces.SystemObject;
import stauma.dav.configuration.interfaces.SystemObjectType;
import sys.funclib.debug.Debug;
import de.bsvrz.dua.plformal.av.DAVDatenAnmeldung;
import de.bsvrz.dua.plformal.schnittstellen.IStandardAspekte;
import de.bsvrz.dua.plformal.schnittstellen.IVerwaltung;

/**
 * Diese Klasse stellt die Standard-Publikationsinformationen für alle
 * SWE innerhalb der DUA zur Verfügung. Diese Informationen können im
 * Konstruktor dieser Klasse für je eine SWE-Modultyp-Kombination
 * als Instanz der Klasse <code>StandardAspekteAdapter</code> angelegt
 * werden.
 * 
 * @author Thierfelder
 * 
 */
public class StandardAspekteVersorger {

	/**
	 * Debug-Logger
	 */
	protected static final Debug LOGGER = Debug.getLogger();

	/**
	 * staische Instanz dieser Klasse
	 */
	protected static StandardAspekteVersorger INSTANZ = null;
	
	/**
	 * Dummy-Schnittstelle vom Typ <code>IStandardAspekte</code> ohne
	 * Informationen
	 */
	private static final IStandardAspekte LEERES_OBJEKT = new IStandardAspekte() {

		/**
		 * {@inheritDoc}
		 */
		public Collection<DAVDatenAnmeldung> getStandardAnmeldungen() {
			return null;
		}

		/**
		 * {@inheritDoc}
		 */
		public Aspect getStandardAspekt(ResultData originalDatum) {
			return null;
		}

	};
	
	/**
	 * Konfigurationsbereichsfilter
	 */
	protected Collection<ConfigurationArea> filter = null;

	/**
	 * Menge aller Standard-Publikationsinformations-Objekte
	 * für alle SWE-Modultyp-Kombinationen
	 */
	protected Collection<StandardAspekteAdapter> adapter =
				new ArrayList<StandardAspekteAdapter>();


	/**
	 * Erfragt die statische Instanz dieser Klasse
	 * 
	 * @param verwaltung
	 *            Verbindung zur Verwaltung
	 * @return die statische Instanz dieser Klasse
	 * @throws DUAInitialisierungsException falls es Probleme bei der Initialisierung
	 * gegeben hat
	 */
	public static final StandardAspekteVersorger getInstanz(
			IVerwaltung verwaltung)
	throws DUAInitialisierungsException{
		if (INSTANZ == null) {
			INSTANZ = new StandardAspekteVersorger(verwaltung);
		}
		return INSTANZ;
	}

	/**
	 * Standardkonstrauktor. Hier werden die einzelnen Informationen zu den
	 * Standardpulikationsaspekten für bestimmte SWE-Modultyp-Kombinationen
	 * angelegt
	 * 
	 * @param verwaltung
	 *            Verbindung zum Verwaltungsmodul
	 * @throws DUAInitialisierungsException falls es Probleme bei der Initialisierung
	 * gegeben hat
	 */
	private StandardAspekteVersorger(IVerwaltung verwaltung) 
	throws DUAInitialisierungsException{
		filter = verwaltung.getKonfigurationsBereiche();
		
		adapter.add(new StandardAspekteAdapter(
				DAVKonstanten.CONST_SWE_PL_Pruefung_formal,
				DAVKonstanten.CONST_MODUL_TYP_PLPruefungFormal,
				new StandardPublikationsZuordnung[] {
						new StandardPublikationsZuordnung("typ.fahrStreifen", //$NON-NLS-1$
								"atg.verkehrsDatenKurzZeitIntervall", //$NON-NLS-1$
								"asp.externeErfassung", //$NON-NLS-1$
								"asp.plausibilitätsPrüfungFormal", //$NON-NLS-1$
								verwaltung.getVerbindung()),
						new StandardPublikationsZuordnung("typ.fahrStreifen", //$NON-NLS-1$
								"atg.verkehrsDatenLangZeitIntervall", //$NON-NLS-1$
								"asp.externeErfassung", //$NON-NLS-1$
								"asp.plausibilitätsPrüfungFormal", //$NON-NLS-1$
								verwaltung.getVerbindung()) }));
	}

	/**
	 * Erfragt die Standardpublikationsinformationen für die übergebenen
	 * SWE-Modultyp-Kombination
	 * 
	 * @param swe
	 *            die DAV-ID der SWE
	 * @param modulTyp
	 *            die DAV-ID des Modultyps
	 * @return eine <code>IStandardAspekte</code>-Schnittstelle zu den
	 *         Standardpublikationsinformationen für die übergebenen
	 *         SWE-Modultyp- Kombination
	 */
	public final IStandardAspekte getStandardPubInfos(final String swe,
			final String modulTyp) {
		IStandardAspekte ergebnis = LEERES_OBJEKT;

		for (StandardAspekteAdapter ad : adapter) {
			if (ad.swe.equals(swe) && ad.modulTyp.equals(modulTyp)) {
				ergebnis = ad;
				break;
			}
		}
		
		if(ergebnis.equals(LEERES_OBJEKT)){
			LOGGER.warning("Es wurden keine Standardpublikationsaspekte für die SWE " + //$NON-NLS-1$
					swe + " und den Modultyp " + modulTyp + " bereitgestellt."); //$NON-NLS-1$ //$NON-NLS-2$
		}

		return ergebnis;
	}
	

	/**
	 * In diesen Objekten werden alle Informationen über das standardmäßige
	 * Publikationsverhalten von SWE-Modultyp-Kombinationen festgehalten. Diese
	 * Objekte werden innerhalb von <code>StandardAspekteVersorger</code>
	 * statisch erstellt und können über die statische Methode <code>
	 * getStandardPubInfos(..)</code>
	 * ausgelesen werden.
	 * 
	 * @author Thierfelder
	 * 
	 */
	protected class StandardAspekteAdapter
	implements IStandardAspekte {

		/**
		 * DAV-ID der SWE
		 */
		protected String swe = null;

		/**
		 * DAV-ID des Modulstyps
		 */
		protected String modulTyp = null;

		/**
		 * Zuordnungen von bestimmten
		 * <code>SystemObjectType-AttributeGroup-Aspect</code>-Kombination zu
		 * einem Standardpublikationsaspekt
		 */
		private StandardPublikationsZuordnung[] zuordnungen = null;

		/**
		 * Notwendige Datenanmeldungen ohne Filterung
		 */
		private Collection<DAVDatenAnmeldung> anmeldungenGlobal = 
					new ArrayList<DAVDatenAnmeldung>();

		/**
		 * Ein Baum für den schnellen Zugriff auf den Standardapplikationsaspekt.
		 * Originalobjekt-->Originalattributgruppe-->Originalaspekt-->Publikationsaspekt
		 */
		private TreeMap<SystemObject, TreeMap<AttributeGroup, TreeMap<Aspect, Aspect>>>
				aspektBaum = new TreeMap<SystemObject, TreeMap<AttributeGroup, TreeMap<Aspect, Aspect>>>();

		/**
		 * Standardkonstruktor
		 * 
		 * @param swe
		 *            DAV-ID der SWE
		 * @param modulTyp
		 *            DAV-ID des Modultyps
		 * @param zuordnungen
		 *            Liste mit Standardpublikationszurodnungen
		 */
		public StandardAspekteAdapter(final String swe, final String modulTyp,
				final StandardPublikationsZuordnung[] zuordnungen) {
			this.swe = swe;
			this.modulTyp = modulTyp;
			this.zuordnungen = zuordnungen;
			erstelleAspektInfos();
		}

		/**
		 * Erstellt die Datenstruktur <code>aspektBaum</code> auf Basis aller
		 * Datenanmeldungen
		 */
		private final void erstelleAspektInfos() {
			aspektBaum.clear();
			anmeldungenGlobal.clear();

			for (StandardPublikationsZuordnung zuordnung : zuordnungen) {
				DAVDatenAnmeldung anmeldung = null;
				
				try {
					anmeldung = new DAVDatenAnmeldung(
							new SystemObject[] { zuordnung.typ },
							new DataDescription(zuordnung.atg, zuordnung.aspAusgang, (short)0), filter);
				} catch (Exception e) {
					LOGGER.error("--------------", e);
				}
				
				if(!anmeldungenGlobal.contains(anmeldung)){
					anmeldungenGlobal.add(anmeldung);
				}
				
				for (SystemObject obj : anmeldung.getObjekte()) {
					add(obj, anmeldung.getDatenBeschreibung()
							.getAttributeGroup(), zuordnung.aspEingang,
							zuordnung.aspAusgang);
				}
			}
		}

		/**
		 * Fügt die Parameter in den Aspektbaum ein
		 * 
		 * @param obj
		 *            das Originalobjekt
		 * @param atg
		 *            die Originalattributgruppe
		 * @param aspEingang
		 *            der Orriginalaspekt
		 * @param aspAusgang
		 *            der Publikationsaspekt
		 */
		public void add(SystemObject obj, AttributeGroup atg, Aspect aspEingang,
				Aspect aspAusgang) {
			TreeMap<AttributeGroup, TreeMap<Aspect, Aspect>> atgZuAspZuAsp = aspektBaum
					.get(obj);
			TreeMap<Aspect, Aspect> aspZuAsp = null;

			if (atgZuAspZuAsp != null) {
				aspZuAsp = atgZuAspZuAsp.get(atg);
				if (aspZuAsp != null) {
					aspZuAsp.put(aspEingang, aspAusgang);
				} else {
					aspZuAsp = new TreeMap<Aspect, Aspect>();
					aspZuAsp.put(aspEingang, aspAusgang);
					atgZuAspZuAsp.put(atg, aspZuAsp);
				}
			} else {
				atgZuAspZuAsp = new TreeMap<AttributeGroup, TreeMap<Aspect, Aspect>>();
				aspZuAsp = new TreeMap<Aspect, Aspect>();
				aspZuAsp.put(aspEingang, aspAusgang);
				atgZuAspZuAsp.put(atg, aspZuAsp);
				aspektBaum.put(obj, atgZuAspZuAsp);
			}
		}

		/**
		 * {@inheritDoc}
		 */
		public Aspect getStandardAspekt(ResultData originalDatum) {
			Aspect ergebnis = null;

			try {
				final SystemObject obj = originalDatum.getObject();
				TreeMap<AttributeGroup, TreeMap<Aspect, Aspect>> atgZuAspekt = aspektBaum
						.get(obj);
				if (atgZuAspekt != null) {
					final AttributeGroup atg = originalDatum
							.getDataDescription().getAttributeGroup();
					TreeMap<Aspect, Aspect> aspZuAsp = atgZuAspekt.get(atg);
					if (aspZuAsp != null) {
						final Aspect asp = originalDatum.getDataDescription()
								.getAspect();
						ergebnis = aspZuAsp.get(asp);
					} else {
						LOGGER
								.info("Keine Informationen zu Quell-Attributgruppe für " + originalDatum); //$NON-NLS-1$
					}
				} else {
					LOGGER
							.info("Keine Informationen zu Quell-Objekt für " + originalDatum); //$NON-NLS-1$
				}
			} catch (NullPointerException ex) {
				LOGGER.error("Standardaspekt für " //$NON-NLS-1$
						+ (originalDatum == null ? DAVKonstanten.NULL
								: originalDatum)
						+ " konnte nicht ermittelt werden", ex); //$NON-NLS-1$
			}

			return ergebnis;
		}

		/**
		 * {@inheritDoc}
		 */
		public Collection<DAVDatenAnmeldung> getStandardAnmeldungen() {
			return anmeldungenGlobal;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean equals(Object obj) {
			boolean ergebnis = false;

			if (obj != null && obj instanceof StandardAspekteVersorger) {
				StandardAspekteAdapter that = (StandardAspekteAdapter) obj;

				ergebnis = this.swe.equals(that.swe)
						&& that.modulTyp.equals(that.modulTyp);
			}

			return ergebnis;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public String toString() {
			String s = "SWE: " + swe + "\nTodul-Typ: " + modulTyp + "\nAnmeldungen:\n"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

			for (DAVDatenAnmeldung anmeldung : anmeldungenGlobal) {
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
	 * @author Thierfelder
	 * 
	 */
	private class StandardPublikationsZuordnung {

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
		 * @param dav
		 *            Verbindung zum Datenverteiler
		 * @throws DUAInitialisierungsException falls eines der übergebenen
		 * DV-Elemente nicht ausgelesen werden konnte
		 */
		public StandardPublikationsZuordnung(String typ, String atg,
				String aspEingang, String aspAusgang, ClientDavInterface dav)
		throws DUAInitialisierungsException{
			try{
				this.typ = dav.getDataModel().getType(typ);
				this.atg = dav.getDataModel().getAttributeGroup(atg);
				this.aspEingang = dav.getDataModel().getAspect(aspEingang);
				this.aspAusgang = dav.getDataModel().getAspect(aspAusgang);
			}catch(Exception ex){
				throw new DUAInitialisierungsException("Standardpublikationsaspekt" + //$NON-NLS-1$
						" konnte" + //$NON-NLS-1$
						" nicht eingerichtet werden ", ex); //$NON-NLS-1$
			}
		}

	}

}
