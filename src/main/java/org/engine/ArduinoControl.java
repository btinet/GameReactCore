package org.engine;


import com.fazecast.jSerialComm.SerialPort;

import java.util.ArrayList;
import java.util.HashMap;

public class ArduinoControl {

    SerialPort sp;
    Double x = (double) 0;
    HashMap<String,Double> measurementSets = new HashMap<>();
    StringBuilder text = new StringBuilder();
    ArrayList<String> measurementTypes;


    public ArduinoControl(ArrayList<String> measurementTypes) {
        this.measurementTypes = measurementTypes;

        for (SerialPort port : SerialPort.getCommPorts()) {
            System.out.println(port.getDescriptivePortName());
        }

        sp = SerialPort.getCommPort("COM3");
        sp.setComPortParameters(9600, 8, 1, 0);
        sp.setComPortTimeouts(SerialPort.TIMEOUT_NONBLOCKING, 0, 0);

            if (sp.openPort()) {
                System.out.println("Port geöffnet!");
            } else {
                System.out.println("Fehler beim Öffnen des Ports!");
            }

    }

    public HashMap<String, Double> getData() {
        try {
            if (sp != null && sp.bytesAvailable() > 0) {

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
                    //if (serialInput.equals('E'))
                    if (!String.valueOf(serialInput).matches("."))
                    {
                        parseData(text.toString());
                        // parseData sollte besser eine HashMap zurückgeben.
                        text.setLength(0);
                        break;
                    }

                }

            }

        } catch (NumberFormatException | NullPointerException ignored) {

        }
        // besser HashMap zurückgeben (HashMap<String,Double>) String = Schlüssel des Datums / Double = Wert des Datums
        return measurementSets;
    }

    /**
     *
     * @param serialData - String sent by controller
     */
    private void parseData(String serialData)
    {
        for (String measurementType : measurementTypes) {

            if(serialData.contains(measurementType)) {
                Double value = Double.parseDouble(serialData.replaceAll("[^0-9]", ""));
                if(measurementSets.containsKey(measurementType)) {
                    measurementSets.replace(measurementType,value);
                } else {
                    measurementSets.put(measurementType,value);
                }
                break;
            }
        }
    }

    public void closePort() {
        if(sp != null) {
            if (sp.closePort()) {
                System.out.println("Port geschlossen!");
            } else {
                System.out.println("Fehler beim Schließen des Ports!");
            }
        } else {
            System.err.println("Serial Port ist null!");
        }

    }
}
