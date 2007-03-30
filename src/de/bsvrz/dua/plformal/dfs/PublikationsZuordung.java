package de.bsvrz.dua.plformal.dfs;

import java.util.ArrayList;
import java.util.List;

import stauma.dav.clientside.Data;
import stauma.dav.configuration.interfaces.Aspect;
import stauma.dav.configuration.interfaces.AttributeGroup;
import stauma.dav.configuration.interfaces.SystemObject;
import stauma.dav.configuration.interfaces.SystemObjectType;
import de.bsvrz.dua.plformal.allgemein.DAVKonstanten;

/**
 * In dieser Klasse sind alle Informationen zusammengefasst, die das
 * Publikationsverhalten bezüglich <b>einer</b> bestimmten SWE, <b>einem</b>
 * bestimmten Modul-Typ und <b>einem</b> Publikationsaspekt beschreiben.
 * 
 * @author Thierfelder
 * 
 */
public class PublikationsZuordung {

	/**
	 * Die Modul-ID
	 */
	private String modulTyp = null;

	/**
	 * Der Publikationsaspekt
	 */
	private Aspect aspekt = null;

	/**
	 * die Objekte, für die ein Publikationsverhalten beschrieben ist
	 */
	private List<SystemObject> objekte = new ArrayList<SystemObject>();

	/**
	 * die Attributgruppen, für die ein Publikationsverhalten vorgesehen ist
	 */
	private List<AttributeGroup> atgs = new ArrayList<AttributeGroup>();

	/**
	 * soll publiziert werden
	 */
	private boolean publizieren = false;

	
	/**
	 * Standardkonstruktor
	 * 
	 * @param data ein Datenverteiler-Datum mit den
	 * mit einem Parametersatz assoziierten Daten
	 * @throws Exception wenn einer der Parameter nicht
	 * ausgelesen werden konnte
	 */
	public PublikationsZuordung(Data data)
	throws Exception{
		this.aspekt = (Aspect)data.getReferenceValue(
				DAVKonstanten.ATT_DFS_ASP).getSystemObject();
		this.modulTyp = data.getUnscaledValue(
				DAVKonstanten.ATT_DFS_MODUL_TYP).getState()
				.getName().toString();
		this.publizieren = data.getTextValue(
				DAVKonstanten.ATT_DFS_PUBLIZIEREN).getText()
				.toLowerCase().equals("ja"); //$NON-NLS-1$

		for (int iObj = 0; iObj < data.getArray(
				DAVKonstanten.ATT_DFS_OBJ).getLength(); iObj++) {
			this.objekte.add(data.getArray(DAVKonstanten.ATT_DFS_OBJ)
					.getReferenceValue(iObj)
					.getSystemObject());
		}
		for (int iAtg = 0; iAtg < data.getArray(
				DAVKonstanten.ATT_DFS_ATG).getLength(); iAtg++) {
			this.atgs.add((AttributeGroup) data.getArray(
					DAVKonstanten.ATT_DFS_ATG)
					.getReferenceValue(iAtg)
					.getSystemObject());
		}
	}
	
	/**
	 * Erfragt den Aspekt
	 * 
	 * @return den Aspekt
	 */
	public Aspect getAspekt() {
		return aspekt;
	}

	/**
	 * Erfragt die DAV-ID des Modul-Typs
	 * 
	 * @return die DAV-ID des Modul-Typs
	 */
	public final String getModulTyp() {
		return modulTyp;
	}

	/**
	 * Erfragt das Publikations-FLAG
	 * 
	 * @return das Publikations-FLAG
	 */
	public final boolean isPublizieren() {
		return publizieren;
	}

	/**
	 * Erfragt alle hier definierten Attributgruppen
	 * 
	 * @return alle hier definierten Attributgruppen
	 */
	public final List<AttributeGroup> getAtgs() {
		return atgs;
	}

	/**
	 * Erfragt die Liste aller hier definierten Objekte
	 * 
	 * @return die Liste aller hier definierten Objekte
	 */
	public final List<SystemObject> getObjekte() {
		return objekte;
	}

	/**
	 * Diese Methode wird nur aufgerufen, wenn die Bedingungen 1-4 aus
	 * <code>isKompatibelMit</code> erfüllt sind. Sie überprüft lediglich die
	 * letzte Bedingung 5. Sollte diese Überprüfung positiv ausfallen, dann gibt
	 * sie eine vollständige Fehlernachricht zurück. Sonst <code>null</code>
	 * 
	 * @param fehler1
	 *            die Beschreibung des Fehlers bis dahin
	 * @param vergleichsObj
	 *            das Objekt, mit dem dieses (<code>this</code>) verglichen
	 *            werden soll
	 * @return <code>null</code> wenn kein Widerspruch vorliegt und eine den
	 *         Widerspruch illustrierende Fehlermeldung sonst.
	 */
	private final String getInkompatibilitätsFehler(final String fehler1,
			final PublikationsZuordung vergleichsObj) {
		if (this.getAtgs() != null && vergleichsObj.getAtgs() != null) {
			for (AttributeGroup thisAtg : this.getAtgs()) {
				for (AttributeGroup thatAtg : vergleichsObj.getAtgs()) {
					if (thisAtg.equals(thatAtg)) {
						return fehler1
								+ "\nDie Attributgruppe " + thisAtg +  //$NON-NLS-1$
								" ist in beiden Publikationszuordnungen" + //$NON-NLS-1$
								" enthalten.\n(1)\n" + this //$NON-NLS-1$
								+ "\n(2)\n" + vergleichsObj; //$NON-NLS-1$
					}
				}
			}
		}

		return null;
	}

	/**
	 * Fragt, ob eine bestimmte Publikationszuordnung mit dieser hier kompatibel
	 * ist. Ob sie sich also widersprechen. Ein Widerspruch liegt vor, wenn:<br>
	 * 1. der Modul-Typ identisch ist UND<br>
	 * 2. für <b>beide</b> Objekte die Publikation eingeschalten ist UND<br>
	 * 3. der Publikationsaspekt der beiden Objekte <code>this</code> und
	 * <code>vergleichsObj</code> nicht identisch ist UND<br>
	 * 4. eine Objekt-Überschneidung innerhalb der Member-SystemObjekte von
	 * <code>this</code> und <code>vergleichsObj</code> besteht UND<br>
	 * 5. die Schnittmenge der Member-Attributgruppen nicht leer ist.<br>
	 * 
	 * @param vergleichsObj
	 *            das Objekt, mit dem dieses (<code>this</code>) verglichen
	 *            werden soll
	 * @return <code>null</code> wenn kein Widerspruch vorliegt und eine den
	 *         Widerspruch illustrierende Fehlermeldung sonst.
	 */
	public final String isKompatibelMit(final PublikationsZuordung vergleichsObj) {
		if (this.modulTyp.equals(vergleichsObj.getModulTyp()) && // 1.
				this.isPublizieren() && vergleichsObj.isPublizieren() && // 2.
				!this.getAspekt().equals(vergleichsObj.getAspekt())) { // 3.

			for (SystemObject thisObj : this.getObjekte()) { // 4. + 5.
				if (thisObj.isOfType(DAVKonstanten.TYP_TYP)) {
					SystemObjectType thisTyp = (SystemObjectType) thisObj;

					for (SystemObject thatObj : vergleichsObj.getObjekte()) {
						if (thatObj.isOfType(DAVKonstanten.TYP_TYP)) {
							SystemObjectType thatTyp = (SystemObjectType) thatObj;

							if (thatObj.equals(thisObj)
									|| thisTyp.inheritsFrom(thatTyp)
									|| thatTyp.inheritsFrom(thisTyp)) {
								return this
										.getInkompatibilitätsFehler(
											"Die Objekte " + thisObj + "(1) und " + //$NON-NLS-1$ //$NON-NLS-2$
											thatObj
											+ "(2) sind entweder " + //$NON-NLS-1$
											"gleich oder voneinander abgeleitet.", vergleichsObj); //$NON-NLS-1$
							}
						} else {
							if (thatObj.isOfType(thisTyp)) {
								return this
									.getInkompatibilitätsFehler(
									"Das Objekt " + thatObj + "(2) ist schon " + //$NON-NLS-1$ //$NON-NLS-2$
									"durch " + thisObj//$NON-NLS-1$
									+ "(1) beschrieben.", vergleichsObj); //$NON-NLS-1$
							}
						}
					}
				} else {
					for (SystemObject thatObj : vergleichsObj.getObjekte()) {
						if (thatObj.isOfType(DAVKonstanten.TYP_TYP)) {
							SystemObjectType thatTyp = (SystemObjectType) thatObj;

							if (thisObj.isOfType(thatTyp)) {
								return this
										.getInkompatibilitätsFehler(
										"Das Objekt " + thisObj + "(1) ist schon " + //$NON-NLS-1$ //$NON-NLS-2$
										"durch " + thatObj//$NON-NLS-1$ 
										+ "(2) beschrieben.", vergleichsObj); //$NON-NLS-1$
							}
						} else {
							if (thisObj.equals(thatObj)) {
								return this
										.getInkompatibilitätsFehler(
										"Die Objekte " + thisObj + //$NON-NLS-1$
										"(1) und " + thatObj //$NON-NLS-1$
										+ "(2) sind gleich.", vergleichsObj); //$NON-NLS-1$
							}
						}
					}
				}
			}
		}

		return null; // keine Widersprüche
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		String s = "Modul-Typ: " + modulTyp + "\n"; //$NON-NLS-1$ //$NON-NLS-2$
		s += "Aspekt: " + aspekt + "\n"; //$NON-NLS-1$ //$NON-NLS-2$
		s += "Publizieren: " + (publizieren ? "ja" : "nein") + "\n"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		for (SystemObject obj : objekte) {
			s += "Objekt: " + obj + "\n"; //$NON-NLS-1$ //$NON-NLS-2$
		}
		for (AttributeGroup atg : atgs) {
			s += "Atg: " + atg + "\n"; //$NON-NLS-1$//$NON-NLS-2$
		}

		return s;
	}
}
