package org.engine;


import com.fazecast.jSerialComm.SerialPort;

import java.io.IOException;

public class ArduinoControl {

    SerialPort sp;
    char lastChar = '?';
    StringBuilder text = new StringBuilder();

    public ArduinoControl() {

        SerialPort[] sps = SerialPort.getCommPorts(); // device name TODO: must be changed
        sp = sps[0];
        sp.setComPortParameters(9600, 8, 1, 0); // default connection settings for Arduino
        sp.setComPortTimeouts(SerialPort.TIMEOUT_WRITE_BLOCKING, 0, 0); // block until bytes can be written

        if (sp.openPort()) {
            System.out.println("Port is open :)");
        } else {
            System.out.println("Failed to open port :(");
        }

    }

    public Double getData() {
        //return sp.getInputStream().read();

        try {
            if (sp.bytesAvailable() > 0) {
                byte[] data = new byte[10];
                sp.readBytes(data, 1);

                if ((char) data[0] == ':') lastChar = ':';
                if ((char) data[0] == ',') lastChar = ',';


                if (lastChar == ',') {
                    //System.out.println(text.toString());
                    Double x = Double.parseDouble(text.toString());
                    text.setLength(0);
                    lastChar = '?';
                    return x;
                } else {
                    if((char)data[0] >= ' ' && lastChar == ':' && (char)data[0] != ':')
                        if(Character.isDigit((char)data[0])) {
                            text.append((char)data[0]);
                        }

                }


            }
        } catch (NumberFormatException ignored) {

        }
        return null;
    }

    public void closePort() {
        if (sp.closePort()) {
            System.out.println("Port is closed :)");
        } else {
            System.out.println("Failed to close port :(");
        }
    }
}
