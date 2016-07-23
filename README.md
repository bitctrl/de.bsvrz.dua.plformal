[![Build Status](https://travis-ci.org/bitctrl/de.bsvrz.dua.plformal.svg?branch=master)](https://travis-ci.org/bitctrl/de.bsvrz.dua.plformal)
[![Build Status](https://api.bintray.com/packages/bitctrl/maven/de.bsvrz.dua.plformal/images/download.svg)](https://bintray.com/bitctrl/maven/de.bsvrz.dua.plformal)


#  Segment 4 Datenübernahme und Aufbereitung (DUA), SWE 4.1 Pl-Prüfung formal  

Version: ${version}

## Übersicht

Aufgabe der SWE Pl-Prüfung formal ist es, die Werte aller parametrierten
Attribute nach formalen Kriterien zu überprüfen. Je Attribut müssen dazu
numerische Grenzwerte in der Parametrierung hinterlegt sein. Prinzipiell
ermöglicht die SWE Pl-Prüfung formal, alle terminalen numerischen Attribute
zu überprüfen, die innerhalb einer Datenidentifikation vorkommen können.


## Versionsgeschichte

### 2.0.0

Release-Datum: 31.05.2016

#### Neue Abhängigkeiten
Die SWE benötigt nun das Distributionspaket de.bsvrz.sys.funclib.bitctrl.dua in Mindestversion 1.5.0 und de.bsvrz.sys.funclib.bitctrl in Mindestversion 1.4.0.

#### Änderungen
Folgende Änderungen gegenüber vorhergehenden Versionen wurden durchgeführt:

- Die formale Plausibilitätsprüfung (Anforderung DUA–11) wurde gestrichen, 
  wodurch die SWE Pl-Prüfung Formal keine Prüfungen mehr durchführen muss, da die formelle Prüfung 
  der Wertebereiche bereits implizit von der Datenverteilersoftware durchgeführt wird. 
  Die SWE wurde daher so modifiziert, dass sie als Modul (bzw. Bearbeitungsknoten im Sinne des Softwareentwurfs)
  alle übergebenen Daten unverändert an den nächsten Bearbeitungsknoten weiterleitet.
- Der Parameterdatensatz atg.plausibilitätsPrüfungFormal wird nicht mehr ausgewertet.
- Die SWE kann nicht mehr eigenständig gestartet werden und dient nur noch als Bibliothek.
  
### 1.5.0
- Umstellung auf Java 8 und UTF-8

### 1.4.1
- Kompatibilität zu DuA-2.0 hergestellt

### 1.4.0
- Umstellung auf Funclib-BitCtrl-Dua

### 1.3.0
- Umstellung auf Maven-Build

### 1.2.3
  - Sämtliche Konstruktoren DataDescription(atg, asp, sim)
    ersetzt durch DataDescription(atg, asp)

### 1.2.2
  - Güteanpassung wird jetzt durchgeführt, wenn ein Gütefaktor via -gueteFaktor angegeben wurde

### 1.2.1
- Bash-Startfile hinzu
  
### 1.2.0
  - Anpassung an neue Kernsoftware

### 1.1.0
  - Umpacketierung

### 1.0.0
  - Erste Auslieferung

## Bemerkungen

- Tests:

	Die automatischen Tests, die in Zusammenhang mit der Prüfspezifikation durchgeführt
	werden, befinden sich innerhalb des Packages de.bsvrz.dua.plformal.test und
	sind als JUnit-Tests ausführbar. Alle anderen Tests sind analog der Package-Struktur
	der SWE selbst definiert. 
	

- Logging-Hierarchie (Wann wird welche Art von Logging-Meldung produziert?):

	ERROR:
	- DUAInitialisierungsException --> Beendigung der Applikation
	- Fehler beim An- oder Abmelden von Daten beim Datenverteiler
	- Interne unerwartete Fehler
	
	WARNING:
	- Fehler, die die Funktionalität grundsätzlich nicht
	  beeinträchtigen, aber zum Datenverlust führen können
	- Nicht identifizierbare Konfigurationsbereiche
	- Probleme beim Explorieren von Attributpfaden 
	  (von Plausibilisierungsbeschreibungen)
	- Wenn mehrere Objekte eines Typs vorliegen, von dem
	  nur eine Instanz erwartet wird
	- Wenn Parameter nicht korrekt ausgelesen werden konnten
	  bzw. nicht interpretierbar sind
	- Wenn inkompatible Parameter übergeben wurden
	- Wenn Parameter unvollständig sind
	- Wenn der Statuswert bei der formalen Pl-Prüfung formal
	  nicht gesetzt werden konnte
	
	INFO:
	- Wenn neue Parameter empfangen wurden
	
	CONFIG:
	- Allgemeine Ausgaben, welche die Konfiguration betreffen
	- Benutzte Konfigurationsbereiche der Applikation bzw.
	  einzelner Funktionen innerhalb der Applikation
	- Benutzte Objekte für Parametersteuerung von Applikationen
	  (z.B. die Instanz der Datenflusssteuerung, die verwendet wird)
	- An- und Abmeldungen von Daten beim Datenverteiler
	
	FINE:
	- Wenn Daten empfangen wurden, die nicht weiterverarbeitet 
	  (plausibilisiert) werden können (weil keine Parameter vorliegen)
	- Informationen, die nur zum Debugging interessant sind 


## Disclaimer

Segment 4 Datenübernahme und Aufbereitung (DUA), SWE 4.1 Pl-Prüfung formal
Copyright (C) 2007 BitCtrl Systems GmbH 

This program is free software; you can redistribute it and/or modify it under
the terms of the GNU General Public License as published by the Free Software
Foundation; either version 2 of the License, or (at your option) any later
version.

This program is distributed in the hope that it will be useful, but WITHOUT
ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
details.

You should have received a copy of the GNU General Public License along with
this program; if not, write to the Free Software Foundation, Inc., 51
Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA.


## Kontakt

BitCtrl Systems GmbH
Weißenfelser Straße 67
04229 Leipzig
Phone: +49 341-490670
mailto: info@bitctrl.de
