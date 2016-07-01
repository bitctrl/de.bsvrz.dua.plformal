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

package de.bsvrz.dua.plformal.vew;

import de.bsvrz.dav.daf.main.ReceiveOptions;
import de.bsvrz.dav.daf.main.ReceiverRole;
import de.bsvrz.dav.daf.main.ResultData;
import de.bsvrz.dua.plformal.plformal.PPFVersorger;
import de.bsvrz.dua.plformal.plformal.PlPruefungFormal;
import de.bsvrz.dua.plformal.plformal.schnittstellen.IPPFVersorger;
import de.bsvrz.dua.plformal.plformal.schnittstellen.IPPFVersorgerListener;
import de.bsvrz.sys.funclib.application.StandardApplicationRunner;
import de.bsvrz.sys.funclib.bitctrl.dua.DUAInitialisierungsException;
import de.bsvrz.sys.funclib.bitctrl.dua.adapter.AbstraktVerwaltungsAdapterMitGuete;
import de.bsvrz.sys.funclib.bitctrl.dua.av.DAVEmpfangsAnmeldungsVerwaltung;
import de.bsvrz.sys.funclib.bitctrl.dua.dfs.typen.SWETyp;

/**
 * Implementierung des Moduls Verwaltung der SWE PL-Prüfung formal. Dieses Modul
 * erfragt die zu überprüfenden Daten aus der Parametrierung und initialisiert
 * damit das Modul PL-Prüfung formal, das dann die eigentliche Prüfung
 * durchführt.
 *
 * @author BitCtrl Systems GmbH, Thierfelder
 */
public class VerwaltungPlPruefungFormal extends AbstraktVerwaltungsAdapterMitGuete implements IPPFVersorgerListener {

	/**
	 * Instanz des Moduls PL-Prüfung formal.
	 */
	private PlPruefungFormal plPruefungFormal;

	/**
	 * Verwaltung für alle Empfangsanmeldungen dieses Moduls.
	 */
	private DAVEmpfangsAnmeldungsVerwaltung empfangsVerwaltung;

	@Override
	public SWETyp getSWETyp() {
		return SWETyp.PL_PRUEFUNG_FORMAL;
	}

	@Override
	public void aktualisiereParameter(final IPPFVersorger parameter) {
		/**
		 * Die Menge der für diese SWE betrachteten Objekte ändert sich
		 * dynamisch
		 */
		setSystemObjekte(parameter.getBetrachteteObjekte());

		this.empfangsVerwaltung.modifiziereObjektAnmeldung(parameter.getObjektAnmeldungen());
	}

	@Override
	protected void initialisiere() throws DUAInitialisierungsException {
		super.initialisiere();

		this.empfangsVerwaltung = new DAVEmpfangsAnmeldungsVerwaltung(getVerbindung(), ReceiverRole.receiver(),
				ReceiveOptions.delayed(), this);

		this.plPruefungFormal = new PlPruefungFormal(new PPFStandardAspekteVersorger(this).getStandardPubInfos());
		this.plPruefungFormal.setPublikation(true);
		this.plPruefungFormal.initialisiere(this);

		/**
		 * An dieser Stelle werden die Parameter der formalen Plausibilisierung
		 * auch ausgewertet, da sich hier aus Gründen der Systemarchitektur auf
		 * die Daten angemeldet werden muss, die innerhalb der Untermodule
		 * plausibilisiert werden sollen.
		 */
		PPFVersorger.getInstanz(this).addListener(this);
	}

	@Override
	public void update(final ResultData[] resultate) {
		this.plPruefungFormal.aktualisiereDaten(resultate);
	}

	/**
	 * Startet diese Applikation.
	 *
	 * @param argumente
	 *            Argumente der Kommandozeile
	 */
	public static void main(final String[] argumente) {
		StandardApplicationRunner.run(new VerwaltungPlPruefungFormal(), argumente);
	}

	/**
	 * Standard-Gütefaktor für Ersetzungen (90%).<br>
	 * Wenn das Modul Pl-Prüfung logisch LVE einen Messwert ersetzt (eigentlich
	 * nur bei Wertebereichsprüfung) so vermindert sich die Güte des
	 * Ausgangswertes um diesen Faktor (wenn kein anderer Wert über die
	 * Kommandozeile übergeben wurde)
	 */
	@Override
	public double getStandardGueteFaktor() {
		return 0.0;
	}
}
