/*
 * Segment 4 Datenübernahme und Aufbereitung (DUA), SWE 4.1 Plausibilitätsprüfung formal
 * Copyright (C) 2007-2015 BitCtrl Systems GmbH
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

package de.bsvrz.dua.plformal.plformal;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

import de.bsvrz.dav.daf.main.ClientReceiverInterface;
import de.bsvrz.dav.daf.main.Data;
import de.bsvrz.dav.daf.main.DataDescription;
import de.bsvrz.dav.daf.main.ReceiveOptions;
import de.bsvrz.dav.daf.main.ReceiverRole;
import de.bsvrz.dav.daf.main.ResultData;
import de.bsvrz.dav.daf.main.config.Aspect;
import de.bsvrz.dav.daf.main.config.AttributeGroup;
import de.bsvrz.dav.daf.main.config.SystemObject;
import de.bsvrz.dav.daf.main.config.SystemObjectType;
import de.bsvrz.dua.plformal.plformal.schnittstellen.IPPFVersorger;
import de.bsvrz.dua.plformal.plformal.schnittstellen.IPPFVersorgerListener;
import de.bsvrz.dua.plformal.plformal.typen.PlausibilisierungsMethode;
import de.bsvrz.sys.funclib.bitctrl.daf.DaVKonstanten;
import de.bsvrz.sys.funclib.bitctrl.dua.DUAInitialisierungsException;
import de.bsvrz.sys.funclib.bitctrl.dua.DUAKonstanten;
import de.bsvrz.sys.funclib.bitctrl.dua.DUAUtensilien;
import de.bsvrz.sys.funclib.bitctrl.dua.av.DAVObjektAnmeldung;
import de.bsvrz.sys.funclib.bitctrl.dua.schnittstellen.IVerwaltungMitGuete;
import de.bsvrz.sys.funclib.debug.Debug;

/**
 * Diese Klasse meldet sich auf die Attributgruppe
 * <code>atg.plausibilitätsPrüfungFormal</code> eines Objekts vom Typ
 * <code>typ.plausibilitätsPrüfungFormal</code> an. Es stellt die darin
 * enthaltenen Informationen über die Schnittstelle <code>IPPFVersorger</code>
 * allen angemeldeten Instanzen von <code>IPPFVersorgerListener</code> zur
 * Verfügung. Sie führt die Plausibilisierung von Daten durch
 *
 * @author BitCtrl Systems GmbH, Thierfelder
 */
public final class PPFVersorger implements IPPFVersorger, ClientReceiverInterface {

	private static final Debug LOGGER = Debug.getLogger();

	/**
	 * die statische Instanz dieser Klasse.
	 */
	private static PPFVersorger instanz;

	/**
	 * Systemobjekt einer Ganzen Zahl.
	 */
	private static SystemObjectType typGanzZahl;

	/**
	 * Systemobjekt einer Gleitkommazahl.
	 */
	private static SystemObjectType typKommaZahl;

	/**
	 * die aktuellen Informationen der Attributgruppe
	 * <code>atg.plausibilitätsPrüfungFormal</code>.
	 */
	private PPFPlausibilisierungsBeschreibungen plBeschreibungen;

	/**
	 * Alle Listener dieses Objektes.
	 */
	private final Collection<IPPFVersorgerListener> listenerListe = new HashSet<IPPFVersorgerListener>();

	/**
	 * Verbindung zum Verwaltungsmodul.
	 */
	private final IVerwaltungMitGuete verwaltung;

	/**
	 * Standardkonstruktor.
	 *
	 * @param verwaltung
	 *            Verbindung zum Verwaltungsmodul
	 * @param plausbibilisierungsObjekt
	 *            das Objekt vom Typ
	 *            <code>typ.plausibilitätsPrüfungFormal</code>, auf dessen Daten
	 *            sich angemeldet werden soll
	 * @throws DUAInitialisierungsException
	 *             falls die Datenanmeldung fehl schlägt
	 */
	private PPFVersorger(final IVerwaltungMitGuete verwaltung, final SystemObject plausbibilisierungsObjekt)
			throws DUAInitialisierungsException {
		if (verwaltung == null) {
			throw new DUAInitialisierungsException("Keine Verbindung" + " zum Verwaltungsmodul");
		}
		if (verwaltung.getVerbindung() == null) {
			throw new DUAInitialisierungsException("Keine Verbindung" + " zum Datenverteiler");
		}
		if (plausbibilisierungsObjekt == null) {
			throw new DUAInitialisierungsException("Ungültiges" + " Plausibilisierungsobjekt");
		}
		this.verwaltung = verwaltung;

		PPFVersorger.typGanzZahl = verwaltung.getVerbindung().getDataModel().getType("typ.ganzzahlAttributTyp");
		PPFVersorger.typKommaZahl = verwaltung.getVerbindung().getDataModel().getType("typ.kommazahlAttributTyp");

		try {
			final AttributeGroup atg = verwaltung.getVerbindung().getDataModel().getAttributeGroup(PPFKonstanten.ATG);
			final Aspect asp = verwaltung.getVerbindung().getDataModel().getAspect(DaVKonstanten.ASP_PARAMETER_SOLL);
			final DataDescription dd = new DataDescription(atg, asp);
			verwaltung.getVerbindung().subscribeReceiver(this, plausbibilisierungsObjekt, dd, ReceiveOptions.normal(),
					ReceiverRole.receiver());
		} catch (final Throwable t) {
			throw new DUAInitialisierungsException(
					"Unerwarteter" + " Fehler beim Initialisieren der" + " formalen Plausibilisierung", t);
		}

		PPFVersorger.LOGGER.config("Initialisierung erfolgreich.\n" + "Für die formale Plausibilisierung"
				+ " wird das Objekt " + plausbibilisierungsObjekt + " verwendet.");
	}

	/**
	 * Erfragt die statische Instanz dieser Klasse.<br>
	 * <b>Achtung:</b> Es wird erst innerhalb der dem Verwaltungsmodul
	 * übergebenen Konfigurationsbereiche und dann im Standard-
	 * Konfigurationsbereich nach einem Objekt vom Typ
	 * <code>typ.plausibilitätsPrüfungFormal</code> gesucht.
	 *
	 * @param verwaltung
	 *            Verbindung zum Verwaltungsmodul
	 * @return die statische Instanz dieser Klasse
	 * @throws DUAInitialisierungsException
	 *             falls die Datenanmeldung fehl schlägt
	 */
	public static synchronized PPFVersorger getInstanz(final IVerwaltungMitGuete verwaltung)
			throws DUAInitialisierungsException {
		if (PPFVersorger.instanz == null) {
			/**
			 * Ermittlung des Objektes, das die formale Plausibilisierung
			 * beschreibt
			 */
			final SystemObjectType typPPF = (SystemObjectType) verwaltung.getVerbindung().getDataModel()
					.getObject(PPFKonstanten.TYP);

			SystemObject[] plausibilisierungsObjekte = new SystemObject[0];

			if (typPPF != null) {
				plausibilisierungsObjekte = DUAUtensilien
						.getBasisInstanzen(typPPF, verwaltung.getVerbindung(), verwaltung.getKonfigurationsBereiche())
						.toArray(new SystemObject[0]);
			}

			if (plausibilisierungsObjekte.length == 0) {
				/**
				 * nochmal suchen, ob im Standardkonfigurationsbereich ein
				 * Objekt vom Typ <code>typ.plausibilitätsPrüfungFormal</code>
				 * vorhanden ist
				 */
				plausibilisierungsObjekte = DUAUtensilien.getBasisInstanzen(typPPF, verwaltung.getVerbindung(), null)
						.toArray(new SystemObject[0]);
			}

			if (plausibilisierungsObjekte.length > 0) {
				if (plausibilisierungsObjekte.length > 1) {
					PPFVersorger.LOGGER.warning("Es liegen mehrere Objekte vom Typ " + PPFKonstanten.TYP + " vor");
				}
				PPFVersorger.instanz = new PPFVersorger(verwaltung, plausibilisierungsObjekte[0]);
			} else {
				throw new DUAInitialisierungsException("Es liegt kein Objekt vom Typ " + PPFKonstanten.TYP + " vor");
			}
		}

		return PPFVersorger.instanz;
	}

	@Override
	public void update(final ResultData[] resultate) {
		final PPFPlausibilisierungsBeschreibungen neuePlBeschreibungen = new PPFPlausibilisierungsBeschreibungen(
				verwaltung);
		boolean fehler = false;
		PPFVersorger.LOGGER.info("Neue Parameter empfangen");

		if ((resultate != null) && (resultate.length > 0)) {
			/**
			 * nur ein Objekt wird hier behandelt, d.h. dass nur ein Datensatz
			 * (der letzte) interessiert
			 */
			final ResultData resultat = resultate[resultate.length - 1];

			if ((resultat != null) && resultat.isSourceAvailable() && !resultat.isNoDataAvailable()
					&& resultat.hasData() && (resultat.getData() != null)) {

				try {
					final Data.Array parameterSaetze = resultat.getData().getArray(PPFKonstanten.ATL_PARA_SATZ);

					for (int i = 0; i < parameterSaetze.getLength(); i++) {
						neuePlBeschreibungen.addParameterSatz(parameterSaetze.getItem(i));
						PPFVersorger.LOGGER.fine(parameterSaetze.getItem(i).toString());
					}
				} catch (final PlFormalException e) {
					PPFVersorger.LOGGER.warning("Parameterdatensatz für die formale" + " Plausibilisierung konnte nicht"
							+ " ausgelesen werden", e);
					fehler = true;
				}
			}
		}

		/**
		 * alle informieren
		 */
		if (!fehler) {
			synchronized (this) {
				plBeschreibungen = neuePlBeschreibungen;
				for (final IPPFVersorgerListener listener : listenerListe) {
					listener.aktualisiereParameter(this);
				}
			}
		}

		PPFVersorger.LOGGER.info(this.toString());
	}

	@Override
	public Collection<SystemObject> getBetrachteteObjekte() {
		final Collection<SystemObject> objekte = new HashSet<SystemObject>();

		if (this.plBeschreibungen != null) {
			for (final DAVObjektAnmeldung anmeldung : this.plBeschreibungen.getObjektAnmeldungen()) {
				objekte.add(anmeldung.getObjekt());
			}
		}

		return objekte;
	}

	@Override
	public Collection<DAVObjektAnmeldung> getObjektAnmeldungen() {
		return this.plBeschreibungen == null ? new ArrayList<DAVObjektAnmeldung>()
				: this.plBeschreibungen.getObjektAnmeldungen();
	}

	@Override
	public Data plausibilisiere(final ResultData resultat) {
		Data ergebnis = null;

		if (plBeschreibungen == null) {
			PPFVersorger.LOGGER
			.finest("Es wurden noch keine Parameter" + " für die formale Plausibilisierung empfangen");
		} else if ((resultat != null) && resultat.hasData() && (resultat.getData() != null)) {
			final Collection<PPFAttributSpezifikation> attSpezifikationen = this.plBeschreibungen
					.getAttributSpezifikationenFuer(resultat);

			if (attSpezifikationen.size() > 0) {
				ergebnis = resultat.getData();

				for (final PPFAttributSpezifikation attSpez : attSpezifikationen) {
					final Data dummy = plausibilisiereAttribut(ergebnis, attSpez);
					if (dummy != null) {
						ergebnis = dummy;
					}
				}
			} else {
				PPFVersorger.LOGGER
				.finest("ResultData " + resultat + " ist nicht zur formalen Plausibilisierung vorgesehen.");
			}
		} else {
			PPFVersorger.LOGGER.finest("Das formal zu prüfende Datum" + " enthält keine sinnvollen Daten: "
					+ (resultat == null ? DUAKonstanten.NULL : resultat));
		}

		return ergebnis;
	}

	/**
	 * Führt die formale Plausibilisierung für ein bestimmtes Attribut innerhalb
	 * eines Datensatzes durch und gibt für dieses Attribut plausibilisierten
	 * Datensatz zurück.
	 *
	 * @param datum
	 *            der Datensatz
	 * @param attSpez
	 *            die Spezifikation des Attributs und die Parameter der formalen
	 *            Plausibilisierung desselben
	 * @return für dieses Attribut plausibilisierten Datensatz (<b>so dieser
	 *         verändert werden musste</b>), oder <code>null</code> sonst
	 *
	 */
	private Data plausibilisiereAttribut(final Data datum, final PPFAttributSpezifikation attSpez) {
		Data ergebnis = null;

		if (attSpez != null) {
			final String attPfad = attSpez.getAttributPfad();
			final double min = attSpez.getMin();
			final double max = attSpez.getMax();

			final PlausibilisierungsMethode methode = attSpez.getMethode();
			if ((attPfad != null) && (methode != null)) {
				final Data dummy = datum.createModifiableCopy();

				final Data plausDatum = DUAUtensilien.getAttributDatum(attPfad, dummy);
				if ((plausDatum != null)
						&& (plausDatum.getAttributeType().isOfType(PPFVersorger.typGanzZahl)
								|| plausDatum.getAttributeType().isOfType(PPFVersorger.typKommaZahl))
						&& !plausDatum.asUnscaledValue().isState()) {

					final double alterWert = plausDatum.asUnscaledValue().doubleValue();
					double neuerWert = plausDatum.asUnscaledValue().doubleValue();
					final boolean implausibelMin = alterWert < min;
					final boolean implausibelMax = alterWert > max;

					/**
					 * Plausibilisierung
					 */
					if (methode.equals(PlausibilisierungsMethode.NUR_PRUEFUNG)) {
						if (implausibelMin || implausibelMax) {
							setStatusPlFormalWert(dummy, true, attPfad, PPFKonstanten.ATT_STATUS_IMPLAUSIBEL);
						} else {
							setStatusPlFormalWert(dummy, false, attPfad, PPFKonstanten.ATT_STATUS_IMPLAUSIBEL);
						}
					} else if (methode.equals(PlausibilisierungsMethode.SETZE_MIN_MAX)) {
						if (implausibelMax) {
							neuerWert = max;
							setStatusPlFormalWert(dummy, false, attPfad, PPFKonstanten.ATT_STATUS_MIN);
							setStatusPlFormalWert(dummy, true, attPfad, PPFKonstanten.ATT_STATUS_MAX);
						} else if (implausibelMin) {
							neuerWert = min;
							setStatusPlFormalWert(dummy, true, attPfad, PPFKonstanten.ATT_STATUS_MIN);
							setStatusPlFormalWert(dummy, false, attPfad, PPFKonstanten.ATT_STATUS_MAX);
						}
					} else if (methode.equals(PlausibilisierungsMethode.SETZE_MIN)) {
						if (implausibelMin) {
							neuerWert = min;
							setStatusPlFormalWert(dummy, true, attPfad, PPFKonstanten.ATT_STATUS_MIN);
							setStatusPlFormalWert(dummy, false, attPfad, PPFKonstanten.ATT_STATUS_MAX);
						}
					} else if (methode.equals(PlausibilisierungsMethode.SETZE_MAX)) {
						if (implausibelMax) {
							neuerWert = max;
							setStatusPlFormalWert(dummy, false, attPfad, PPFKonstanten.ATT_STATUS_MIN);
							setStatusPlFormalWert(dummy, true, attPfad, PPFKonstanten.ATT_STATUS_MAX);
						}
					}

					if (neuerWert != alterWert) {
						plausDatum.asUnscaledValue().set((long) neuerWert);
					}

					/**
					 * Veränderung des Rückgabedatums
					 */
					ergebnis = dummy;
				}
			} else {
				PPFVersorger.LOGGER.warning(
						"Für Datum " + datum + " ist die Attributspezifikation " + "unvollständig: " + attSpez);
			}
		}

		return ergebnis;
	}

	/**
	 * Fügt diesem Element einen Listener hinzu.
	 *
	 * @param listener
	 *            der neue Listener
	 */
	public void addListener(final IPPFVersorgerListener listener) {
		if (this.listenerListe.add(listener)) {
			synchronized (this) {
				listener.aktualisiereParameter(this);
			}
		}
	}

	/**
	 * Entfernt einen Listener von diesem Element.
	 *
	 * @param listener
	 *            der zu entfernende Listener
	 */
	public void removeListener(final IPPFVersorgerListener listener) {
		synchronized (this) {
			this.listenerListe.remove(listener);
		}
	}

	@Override
	public String toString() {
		String s = "unbekannt\n";

		synchronized (this) {
			if (this.plBeschreibungen != null) {
				s = "Plausibilisierungsbeschreibung:\n" + this.plBeschreibungen;
			}
		}

		return s;
	}

	/**
	 * Setzt den Wert eines bestimmten Status-Flags (MIN/MAX) für ein bistimmtes
	 * Attribut innerhalb einer bestimmten Attributgruppe. (So dieser vorhanden
	 * ist!)
	 *
	 * @param datum
	 *            der Datensatz, in dem sich der Statuswert befindet
	 * @param wert
	 *            der neue Status-Wert
	 * @param attPfad
	 *            der Attributpfad bis zum dem Wert, der vom bewussten
	 *            Status-Flag flankiert wird.
	 * @param attPfadErsetzung
	 *            der Attributpfad bis zum Status- Flag (vom Wert aus gesehen)
	 */
	private void setStatusPlFormalWert(final Data datum, final boolean wert, final String attPfad,
			final String attPfadErsetzung) {
		if ((datum != null) && (attPfad != null) && (attPfadErsetzung != null)) {
			final String neuerAttPfad = DUAUtensilien.ersetzeLetztesElemInAttPfad(attPfad, attPfadErsetzung);

			if (neuerAttPfad != null) {
				final Data status = DUAUtensilien.getAttributDatum(neuerAttPfad, datum);
				if (status != null) {
					status.asUnscaledValue().set(wert ? 1 : 0);

					if ((this.verwaltung.getGueteFaktor() != 0.0) && wert) { // Ein
						// Statusflag
						// wird
						// eingeschaltet
						final String neuerAttPfadGuete = DUAUtensilien.ersetzeLetztesElemInAttPfad(attPfad,
								PPFKonstanten.ATT_GUETE);
						if (neuerAttPfadGuete != null) {
							final Data guete = DUAUtensilien.getAttributDatum(neuerAttPfadGuete, datum);
							if (guete.asUnscaledValue().longValue() >= 0) {
								guete.asScaledValue()
								.set(guete.asScaledValue().doubleValue() * verwaltung.getGueteFaktor());
							}
						}
					}
				} else {
					PPFVersorger.LOGGER.warning("Statuswert konnte nicht" + " ausgelesen werden:\n" + "Datum: " + datum
							+ "\nAttr.-Pfad: " + attPfad + "\nErsetzung: " + attPfadErsetzung);
				}
			} else {
				throw new RuntimeException("Attributpfad zum Statuswert konnte nicht" + " erstellt werden:\n"
						+ "Datum: " + datum + "\nAttr.-Pfad: " + attPfad + "\nErsetzung: " + attPfadErsetzung);
			}
		}
	}
}
