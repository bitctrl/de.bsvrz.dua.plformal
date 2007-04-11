/**
 * Segment 4 Datenübernahme und Aufbereitung (DUA), SWE 4.x 
 * Copyright (C) 2007 BitCtrl Systems GmbH 
 * 
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 51
 * Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA.
 *
 * Contact Information:<br>
 * BitCtrl Systems GmbH<br>
 * Weißenfelser Straße 67<br>
 * 04229 Leipzig<br>
 * Phone: +49 341-490670<br>
 * mailto: info@bitctrl.de
 */

package de.bsvrz.dua.plformal.allgemein;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.TreeSet;

import stauma.dav.clientside.ClientDavInterface;
import stauma.dav.clientside.Data;
import stauma.dav.clientside.DataDescription;
import stauma.dav.configuration.interfaces.Aspect;
import stauma.dav.configuration.interfaces.AttributeGroup;
import stauma.dav.configuration.interfaces.ConfigurationArea;
import stauma.dav.configuration.interfaces.SystemObject;
import stauma.dav.configuration.interfaces.SystemObjectType;
import sys.funclib.debug.Debug;
import de.bsvrz.dua.plformal.allgemein.schnittstellen.IVerwaltung;
import de.bsvrz.dua.plformal.av.DAVObjektAnmeldung;


/**
 * Einige hilfreiche Methoden, die an verschiedenen Stellen
 * innerhalb der DUA Verwendung finden.
 * 
 * @author BitCtrl Systems GmbH, Thierfelder
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
		List<String> resultListe = new ArrayList<String>();
		
		if(kbString != null){
			String[] s = kbString.split(","); //$NON-NLS-1$
			for(String dummy:s){
				if(dummy != null && dummy.length() > 0){
					resultListe.add(dummy);
				}
			}
		}
		
		return resultListe.toArray(new String[0]);
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
//				benutzteBereiche.add(verwaltung.getVerbindung().
//						getDataModel().getConfigurationAuthority().
//						getConfigurationArea());
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
				if(area != null)kbListe.add(area);
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
	 * <code>"a.b.c.Status.PlFormal.WertMax" = ersetzeLetztesElemInAttPfad(
	 * "a.b.c.Wert", "Status.PlFormal.WertMax")</code><br>
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
	 * oder <code>null</code>, wenn der Schlüssel nicht gefunden wurde
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
	 * Kombination gültig bzw. kompatibel ist.
	 * 
	 * @param obj das (finale) Systemobjekt
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
		}else
		if(! (obj.getClass().equals(stauma.dav.configuration.meta.ConfigurationObject.class) ||
			  obj.getClass().equals(stauma.dav.configuration.meta.DynamicObject.class)) ){
			result = "Es handelt sich weder um ein Konfigurationsobjekt " + //$NON-NLS-1$
					"noch ein dynamisches Objekt: " + obj; //$NON-NLS-1$
		}
			
		return result;
	}
	
	/**
	 * Erfragt die Menge aller Konfigurationsobjekte bzw. Dynamischen
	 * Objekte (finale Objekte), die unter Umständen im Parameter
	 * <code>obj</code> 'versteckt' sind. 
	 * 
	 * @param obj ein Systemobjekt (finales Objekt oder Typ)
	 * @param dav Verbindung zum Datenverteiler
	 * @return eine Menge von finalen Systemobjekten
	 */
	public static final Collection<SystemObject>
				getFinaleObjekte(final SystemObject obj,
								 final ClientDavInterface dav){
		Collection<SystemObject> finaleObjekte =
			new HashSet<SystemObject>();
		
		if(obj == null || obj.getPid().equals(DUAKonstanten.TYP_TYP)){
			SystemObjectType typTyp = dav.getDataModel().getType(DUAKonstanten.TYP_TYP);
			for(SystemObject typ:typTyp.getElements()){
				for(SystemObject elem:((SystemObjectType)typ).getElements()){
					if( elem.getClass().equals(stauma.dav.configuration.meta.ConfigurationObject.class) ||
						elem.getClass().equals(stauma.dav.configuration.meta.DynamicObject.class) ){
						finaleObjekte.add(elem);
					}
				}
			}
		}else
		if(obj instanceof SystemObjectType){
			SystemObjectType typ = (SystemObjectType)obj;
			for(SystemObject elem:typ.getElements()){
				finaleObjekte.addAll(getFinaleObjekte(elem, dav));
			}			
		}else
		if( obj.getClass().equals(stauma.dav.configuration.meta.ConfigurationObject.class) ||
			obj.getClass().equals(stauma.dav.configuration.meta.DynamicObject.class) ){
			finaleObjekte.add(obj);
		}else{
			LOGGER.info("Das übergebene Objekt ist weder ein Typ," + //$NON-NLS-1$
					" ein Konfigurationsobjekt noch ein dynamisches Objekt"); //$NON-NLS-1$
		}
		
		return finaleObjekte;
	}

	/**
	 * Erfragt die Menge aller Konfigurationsobjekte bzw. Dynamischen
	 * Objekte (finale Objekte), die unter Umständen im Argument
	 * <code>obj</code> 'versteckt' sind <b>und außerdem innerhalb der
	 * übergebenen Konfigurationsbereiche liegen</b>. 
	 * 
	 * @param obj ein Systemobjekt (finales Objekt oder Typ)
	 * @param dav Verbindung zum Datenverteiler
	 * @param kBereichsFilter eine Menge von Konfigurationsbereichen
	 * @return eine Menge von finalen Systemobjekten, die innerhalb der 
	 * übergebenen Konfigurationsbereiche (bzw. im Standardkonfigurationsbereich)
	 * definiert sind.
	 */
	public static final Collection<SystemObject>
				getFinaleObjekte(final SystemObject obj,
								 final ClientDavInterface dav, 
								 final Collection<ConfigurationArea> kBereichsFilter){
		Collection<SystemObject> finaleObjekte =
			getFinaleObjekte(obj, dav);
		Collection<SystemObject> finaleObjekteMitCheck = new HashSet<SystemObject>();
		
		if(finaleObjekte.size() > 0){
			Collection<ConfigurationArea> benutzteBereiche = 
				new HashSet<ConfigurationArea>();	
			
			if(kBereichsFilter != null && kBereichsFilter.size() > 0){
				benutzteBereiche = kBereichsFilter;				
			}else{
				/**
				 * Es wurden keine Konfigurationsbereiche übergeben:
				 * Standardkonfigurationsbereich wird verwendet
				 */
//				benutzteBereiche.add(dav.
//						getDataModel().getConfigurationAuthority().
//						getConfigurationArea());
			}
			
			for(ConfigurationArea ca:benutzteBereiche){
//				System.out.println("Be: " + ca);
			}
			
			for(SystemObject finObj:finaleObjekte){
//				System.out.println(finObj + ", " + finObj.getConfigurationArea());
				if(benutzteBereiche.contains(finObj.getConfigurationArea())){
					finaleObjekteMitCheck.add(finObj);
				}
			}
		}
		
		return finaleObjekteMitCheck;
	}
	
	/**
	 * Erfragt die Menge von <code>DAVObjektAnmeldung</code>-Objekten,
	 * die alle Anmeldungen unter der übergebenen Datenbeschreibung für
	 * das übergebene Objekt enthält.<br>
	 * <b>Achtung:</b> Das Objekt wird in seine finalen Instanzen
	 * aufgelöst. Sollte für das Objekt <code>null</code> übergeben
	 * worden sein, so wird 'Alle Objekte' angenommen. Gleiches gilt
	 * für die Elemente der Datenbeschreibung.
	 * 
	 * @param obj ein Systemobjekt (auch Typ)
	 * @param datenBeschreibung eine Datenbeschreibung
	 * @param dav Verbindung zum Datenverteiler
	 * @return eine Menge von <code>DAVObjektAnmeldung</code>-Objekten
	 */
	public static final Collection<DAVObjektAnmeldung>
				getAlleObjektAnmeldungen(final SystemObject obj, 
						final DataDescription datenBeschreibung,
						final ClientDavInterface dav){
		Collection<DAVObjektAnmeldung> anmeldungen =
			new TreeSet<DAVObjektAnmeldung>();
		
		Collection<SystemObject> finObjekte = getFinaleObjekte(obj, dav);
		
		for(SystemObject finObj:finObjekte){
			try{
				if(datenBeschreibung == null || 
				   (datenBeschreibung.getAttributeGroup() == null &&
					datenBeschreibung.getAspect() == null)){
					for(AttributeGroup atg:finObj.getType().getAttributeGroups()){
						for(Aspect asp:atg.getAspects()){
							anmeldungen.add(new DAVObjektAnmeldung(
								finObj, new DataDescription(atg, asp, (short)0)));
						}					
					}					
				}else
				if(datenBeschreibung.getAttributeGroup() == null){
					for(AttributeGroup atg:finObj.getType().getAttributeGroups()){
						try{
							anmeldungen.add(new DAVObjektAnmeldung(
								finObj, new DataDescription(atg, datenBeschreibung.
										getAspect(), (short)0)));
						}catch(Exception ex){
							LOGGER.warning(DUAKonstanten.EMPTY_STR, ex);
						}
					}
				}else
				if(datenBeschreibung.getAspect() == null){
					for(Aspect asp:datenBeschreibung.getAttributeGroup().getAspects()){
						anmeldungen.add(new DAVObjektAnmeldung(
								finObj, new DataDescription(datenBeschreibung.
										getAttributeGroup(), asp, (short)0)));
					}					
				}else{
					anmeldungen.add(new DAVObjektAnmeldung(finObj, 
							datenBeschreibung));
				}
			}catch(Exception ex){
				LOGGER.warning(DUAKonstanten.EMPTY_STR, ex);
			}
		}
		
		return anmeldungen;
	}
}
