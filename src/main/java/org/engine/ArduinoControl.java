package org.engine;


import com.fazecast.jSerialComm.SerialPort;

import java.util.ArrayList;

public class ArduinoControl {

    SerialPort sp;
    Double x = (double) 0;
    StringBuilder text = new StringBuilder();

    ArrayList<String> measurementTypes;


    public ArduinoControl(ArrayList<String> measurementTypes) {
        this.measurementTypes = measurementTypes;

        SerialPort[] sps = SerialPort.getCommPorts();
        sp = sps[0];
        sp.setComPortParameters(9600, 8, 1, 0);
        sp.setComPortTimeouts(SerialPort.TIMEOUT_WRITE_BLOCKING, 0, 0);

        if (sp.openPort()) {
            System.out.println("Port geöffnet!");
        } else {
            System.out.println("Fehler beim Öffnen des Ports!");
        }

    }

    public Double getData() {
        try {
            if (sp.bytesAvailable() > 0) {

                // Bytes auslesen
                byte[] newData = new byte[16];
                sp.readBytes(newData, newData.length);

                for (byte newDatum : newData)
                {
                    // Byte als Char casten und dem String hinzufügen
                    Character serialInput = (char) newDatum;
                    if(Character.isLetterOrDigit(serialInput))
                    {
                        text.append(serialInput);
                    }
                    // E kennzeichnet Ende eines Datensatzes
                    if (serialInput.equals('E'))
                    {
                        parseData(text.toString());
                        // parseData sollte besser eine HashMap zurückgeben.
                        text.setLength(0);
                        break;
                    }
                }

            }
        } catch (NumberFormatException ignored) {

        }
        // besser HashMap zurückgeben (HashMap<String,Double>) String = Schlüssel des Datums / Double = Wert des Datums
        return x;
    }

    /**
     *
     * @param serialData - String sent by controller
     */
    private void parseData(String serialData)
    {
        for (String measurementType : measurementTypes) {
            if(serialData.contains(measurementType)) {
                x = Double.parseDouble(serialData.replaceAll("[^0-9]", ""));
                break;
            }
        }
    }

    public void closePort() {
        if (sp.closePort()) {
            System.out.println("Port geschlossen!");
        } else {
            System.out.println("Fehler beim Schließen des Ports!");
        }
    }
}
