package de.bsvrz.dua.plformal.allgemein;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import stauma.dav.clientside.ClientDavInterface;
import stauma.dav.clientside.Data;
import stauma.dav.clientside.DataDescription;
import stauma.dav.configuration.interfaces.ConfigurationArea;
import stauma.dav.configuration.interfaces.SystemObject;
import stauma.dav.configuration.interfaces.SystemObjectType;
import sys.funclib.debug.Debug;
import de.bsvrz.dua.plformal.allgemein.schnittstellen.IVerwaltung;


/**
 * Einige hilfreiche Methoden, die an verschiedenen Stellen
 * innerhalb der DUA Verwendung finden.
 * 
 * @author Thierfelder
 *
 */
public class DUAHilfe {
	
	/**
	 * Debug-Logger
	 */
	private static final Debug LOGGER = Debug.getLogger();
	
	/**
	 * Schablone für eine ganze positive Zahl
	 */
	private static final String NATUERLICHE_ZAHL = "\\d+"; //$NON-NLS-1$
	
	
	/**
	 * Extrahiert aus einer Zeichekette alle über Kommata getrennten 
	 * Konfigurationsbereiche.
	 * 
	 * @param kbString Zeichenkette mit den Konfigurationsbereichen.
	 * @return (ggf. leerer) <code>String</code>-Array mit allen
	 * extrahierten Konfigurationsbereichen. 
	 */
	public static final String[] getKonfigurationsBereiche(
			final String kbString){
		String[] s = new String[0];
		
		if(kbString != null){
			s = kbString.split(","); //$NON-NLS-1$
		}
		
		return s;
	}
	
	/**
	 * Erfragt alle Objekte des Typs <code>typ</code>, die
	 * innerhalb der übergebenen Konfigurationsbereiche
	 * definiert sind.<br>
	 * <b>Achtung:</b> Sollte die Menge der übergebenen
	 * Konfigurationsbereiche leer oder <code>null</code>
	 * sein, so werden die Objekte zurückgegeben, die im
	 * Standardkonfigurationsbereich definiert sind (Der
	 * Standardkonfigurationsbereich ist der Bereich, in
	 * dem die aktuelle autarke Organisationseinheit
	 * definiert ist).
	 * 
	 * @param verwaltung Verbindung zum Verwaltungsmodul
	 * @param typ der Systemobjekt-Typ
	 * @param konfigurationsBereiche eine Menge von 
	 * Konfigurationsbereichen
	 * @return Objekte des Typs <code>typ</code> oder eine leere
	 * <code>Collection</code>, wenn keine Objekte vom übergebenen
	 * Typ identifiziert werden konnten.
	 */
	public static final Collection<SystemObject> getAlleObjekteVomTypImKonfigBereich(
			final IVerwaltung verwaltung,
			final SystemObjectType typ,
			final Collection<ConfigurationArea> konfigurationsBereiche){
		Collection<SystemObject> objListe = new HashSet<SystemObject>();
		
		
		if(typ != null){
			Collection<ConfigurationArea> benutzteBereiche = 
				new HashSet<ConfigurationArea>();
			
			if(konfigurationsBereiche != null &&
			   konfigurationsBereiche.size() > 0){
				benutzteBereiche = konfigurationsBereiche;				
			}else{
				/**
				 * Es wurden keine Konfigurationsbereiche übergeben:
				 * Standardkonfigurationsbereich wird verwendet
				 */
				benutzteBereiche.add(verwaltung.getVerbindung().
						getDataModel().getConfigurationAuthority().
						getConfigurationArea());
			}
			
			String bereicheStr = DUAKonstanten.EMPTY_STR;
			for(ConfigurationArea bereich:benutzteBereiche){
				bereicheStr += bereich + "\n"; //$NON-NLS-1$
			}
			LOGGER.info("Es wird in folgenden Bereichen nach Objekten" + //$NON-NLS-1$
					" vom Typ " + typ + " gesucht:\n" + bereicheStr);  //$NON-NLS-1$//$NON-NLS-2$
			
			for(SystemObject obj:typ.getObjects()){
				if(benutzteBereiche.contains(obj.getConfigurationArea())){
					objListe.add(obj);	
				}
			}
		}
		
		return objListe;
	}

	/**
	 * Extrahiert aus einer Zeichenkette alle über Kommata getrennten 
	 * Konfigurationsbereiche und gibt deren Systemobjekte zurück.
	 * 
	 * @param kbString Zeichenkette mit den Konfigurationsbereichen
	 * @param dav Verbindung zum Datenverteiler
	 * @return (ggf. leere) <code>ConfigurationArea-Collection</code>
	 * mit allen extrahierten Konfigurationsbereichen.
	 */
	public static final Collection<ConfigurationArea> 
				getKonfigurationsBereicheAlsObjekte(final String kbString,
													final ClientDavInterface dav){
		final String[] kbStr = getKonfigurationsBereiche(kbString);
		Collection<ConfigurationArea> kbListe = new HashSet<ConfigurationArea>();
		
		for(String kb:kbStr){
			try{
				ConfigurationArea area = dav.getDataModel().getConfigurationArea(kb);
				kbListe.add(area);
			}catch(UnsupportedOperationException ex){
				LOGGER.error("Konfigurationsbereich " + kb +  //$NON-NLS-1$
						" konnte nicht identifiziert werden.", ex); //$NON-NLS-1$
			}
		}
		
		return kbListe;
	}
	
	/**
	 * Ersetzt den letzten Teil eines Attribuspfades durch eine bestimmte
	 * Zeichenkette. Der Aufruf<br>
	 * <code>"a.b.c.Status.PlFormal.WertMax" = ersetzeLetztesElemInAttPfad("a.b.c.Wert", "Status.PlFormal.WertMax")</code><br>
	 * bewirkt bspw., dass auf den Statuswert <code>max</code> der formalen
	 * Plausibilisierung des Elements <code>a.b.c</code> zugegriffen werden kann
	 * 
	 * @param attPfad der orginale Attributpfad
	 * @param ersetzung die Zeichenkette, durch die der letzte Teil des
	 * Attributpfades ersetzt werden soll
	 * @return einen veränderten Attributpfad oder <code>null</code>, wenn die
	 * Ersetzung nicht durchgeführt werden konnte
	 */
	public static final String ersetzeLetztesElemInAttPfad(final String attPfad,
														   final String ersetzung){
		String ergebnis = null;
		
		if(attPfad != null && attPfad.length() > 0 &&
		   ersetzung != null && ersetzung.length() > 0){
			int letzterPunkt = attPfad.lastIndexOf("."); //$NON-NLS-1$
			if(letzterPunkt != -1){
				ergebnis = attPfad.substring(0, letzterPunkt + 1) + ersetzung;
			}else{
				ergebnis = ersetzung;
			}
		}
		
		return ergebnis;
	}
	
	/**
	 * Liest eine Argument aus der ArgumentListe der Kommandozeile aus
	 * 
	 * @param schluessel der Schlüssel
	 * @param argumentListe alle Argumente der Kommandozeile
	 * @return das Wert des DAV-Arguments mit dem übergebenen Schlüssel
	 */
	public static final String getArgument(final String schluessel,
										   final List<String> argumentListe){
		String ergebnis = null;
		
		if(schluessel != null && argumentListe != null){
			for(String argument:argumentListe){
				String[] teile = argument.split("="); //$NON-NLS-1$
				if(teile != null && teile.length > 1){
					if(teile[0].equals("-" + schluessel)){ //$NON-NLS-1$
						ergebnis = teile[1];
						break;
					}
				}
			}
		}
			
		return ergebnis;
	}
	
	/**
	 * Vergleicht die beiden übergebenen Objekte miteinander.
	 * 
	 * @param obj1 Objekt 1
	 * @param obj2 Objekt 2
	 * @return eine Zeichenkette <code>!= null</code>, wenn die beiden
	 * Objekte eine gemeinsame Schnittmenge (an Objekten) haben (könnten).
	 * Dies ist der Fall, wenn sie voneinander abgeleitet, Instanzen
	 * voneinander, oder einfach identisch sind.<br>
	 * <b>Achtung</b>: Wenn beide Objekte <code>null</code> sind, so
	 * haben sie hier per Definition eine leere Schnittmenge (keine)
	 */
	public static final String hasSchnittMenge(final SystemObject obj1,
											   final SystemObject obj2){
		String ergebnis = null;
		
		if(obj1 != null && obj2 != null){
			if(obj1.isOfType(DUAKonstanten.TYP_TYP) && 
			   obj2.isOfType(DUAKonstanten.TYP_TYP)){
				SystemObjectType typ1 = (SystemObjectType)obj1;
				SystemObjectType typ2 = (SystemObjectType)obj2;
				
				if(typ1.equals(typ2)){
					ergebnis = "Die beiden Typen sind identisch: " + typ1;  //$NON-NLS-1$
				}else if(typ1.inheritsFrom(typ2)){
					ergebnis = typ1 + " erweitert " + typ2;  //$NON-NLS-1$
				}else if(typ2.inheritsFrom(typ1)){
					ergebnis = typ2 + " erweitert " + typ1;  //$NON-NLS-1$
				}

			}else if(obj1.isOfType(DUAKonstanten.TYP_TYP) && 
			   !obj2.isOfType(DUAKonstanten.TYP_TYP)){
				SystemObjectType typ = (SystemObjectType)obj1;
				
				if(obj2.isOfType(typ)){
					ergebnis = obj2 + " ist Instanz von " + typ; //$NON-NLS-1$
				}
			}else if(!obj1.isOfType(DUAKonstanten.TYP_TYP) &&
					obj2.isOfType(DUAKonstanten.TYP_TYP)){
				SystemObjectType typ = (SystemObjectType)obj2;
				
				if(obj1.isOfType(typ)){
					ergebnis = obj1 + " ist Instanz von " + typ; //$NON-NLS-1$
				}
			}else{
				if(obj1.equals(obj2)){
					ergebnis = "Die beiden Objekte sind identisch: " + obj1; //$NON-NLS-1$
				}
			}
		}
		
		return ergebnis;
	}
	
	/**
	 * Extrahiert aus einem übergebenen Datum ein darin enthaltenes Datum.
	 *  
	 * @param attributPfad gibt den kompletten Pfad zu einem Attribut
	 * innerhalb einer Attributgruppe an. Die einzelnen Pfadbestandteile
	 * sind jeweils durch einen Punkt '.' separiert. Um z. B. ein Attribut
	 * mit dem Namen "maxSichtweite", welches Bestandteil einer variablen
	 * Liste (Array) mit dem Namen "ListeDerSichtweiten" zu spezifizieren,
	 * ist folgendes einzutragen: "ListeDerSichtweiten.2.maxSichtweite",
	 * wobei hier das dritte Arrayelement der Liste angesprochen wird.
	 * @param datum das Datum, aus dem ein eingebettetes Datum extrahiert
	 * werden soll.
	 * @return das extrahierte Datum oder <code>null</code> wenn keine Extraktion möglich war
	 */
	public static final Data getAttributDatum(final String attributPfad,
											  final Data datum){
		Data ergebnis = null;

		if(datum != null){
			if(attributPfad != null){
				final String[] elemente = attributPfad.split("[.]"); //$NON-NLS-1$
				ergebnis = datum;
				for(String element:elemente){
					if(ergebnis != null){
						if(element.length() == 0){
							LOGGER.warning("Syntaxfehler in Attributpfad: \"" + attributPfad + "\"");  //$NON-NLS-1$//$NON-NLS-2$
							return null;
						}

						try{
							if(element.matches(NATUERLICHE_ZAHL)){
								ergebnis = ergebnis.asArray().getItem(Integer.parseInt(element));
							}else{
								ergebnis = ergebnis.getItem(element);	
							}
						}catch(Exception ex){
							LOGGER.error("Fehler bei Exploration von Datum " + datum + //$NON-NLS-1$
									" mit \"" + attributPfad + "\"", ex);  //$NON-NLS-1$//$NON-NLS-2$
							return null;
						}

					}else{
						LOGGER.info("Datensatz " + datum + " kann nicht bis \"" + //$NON-NLS-1$ //$NON-NLS-2$
								attributPfad + "\" exploriert werden."); //$NON-NLS-1$
					}
				}				
			}else{
				LOGGER.warning("Übergebener Attributpfad ist " + DUAKonstanten.NULL); //$NON-NLS-1$
			}			
		}else{
			LOGGER.warning("Übergebenes Datum ist " + DUAKonstanten.NULL); //$NON-NLS-1$
		}
		
		return ergebnis;
	}

	/**
	 * Erfragt, ob die übergebene Systemobjekt-Attributgruppen-Aspekt-
	 * Kombination gültig ist.
	 * 
	 * @param obj das Systemobjekt
	 * @param datenBeschreibung die Datenbeschreibung
	 * @return <code>null</code>, wenn die übergebene Systemobjekt-
	 * Attributgruppen-Aspekt-Kombination gültig ist, entweder.
	 * Oder eine die Inkombatibilität beschreibende Fehlermeldung
	 * sonst.
	 */
	public static final String isKombinationOk(final SystemObject obj,
			final DataDescription datenBeschreibung){
		String result = null;
		
		if(obj == null){
			result = "Objekt ist " + DUAKonstanten.NULL; //$NON-NLS-1$
		}else
		if(datenBeschreibung == null){
			result = "Datenbeschreibung ist " + DUAKonstanten.NULL; //$NON-NLS-1$
		}else
		if(datenBeschreibung.getAttributeGroup() == null){
			result = "Attributgruppe ist " + DUAKonstanten.NULL;  //$NON-NLS-1$
		}else
		if(datenBeschreibung.getAspect() == null){
			result = "Aspekt ist " + DUAKonstanten.NULL;  //$NON-NLS-1$
		}else
		if(!obj.getType().getAttributeGroups().contains(
					datenBeschreibung.getAttributeGroup())){
			result = "Attributgruppe " + datenBeschreibung.getAttributeGroup() +  //$NON-NLS-1$
					" ist für Objekt " + obj +  //$NON-NLS-1$ 
					" nicht definiert";  //$NON-NLS-1$
		}else
		if(!datenBeschreibung.getAttributeGroup().getAspects().
				contains(datenBeschreibung.getAspect())){
			result = "Aspekt " + datenBeschreibung.getAspect() +  //$NON-NLS-1$
					" ist für Attributgruppe " + datenBeschreibung.getAttributeGroup() +  //$NON-NLS-1$ 
					" nicht definiert";  //$NON-NLS-1$)
		}
			
		return result;
	}
}
