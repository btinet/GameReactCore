package org.engine;


import com.fazecast.jSerialComm.SerialPort;

import java.io.IOException;

public class ArduinoControl {

    SerialPort sp;
    char lastChar = '?';

    Double x = (double) 0;
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
                // new

                /* Read serial bytes from communication port */
                byte[] newData = new byte[16];
                sp.readBytes(newData, newData.length);

                for (byte newDatum : newData)
                {
                    /* convert byte to char */
                    Character serialInput = (char) newDatum;
                    if(Character.isLetterOrDigit(serialInput))
                    {
                        text.append(serialInput);
                    }
                    /* Indicates the end of a value */
                    if (serialInput.equals('E'))
                    {
                        parseData(text.toString());
                        text.setLength(0);
                        break;
                    }
                }


                /*

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
                 */



            }
        } catch (NumberFormatException ignored) {

        }
        return x;
    }

    /**
     *
     * @param serialData - String sent by controller
     */
    private void parseData(String serialData)
    {

        if(serialData.contains("voltage"))
        {
            x = Double.parseDouble(serialData.replaceAll("[^0-9]", ""));

        }

        else if (serialData.contains("current"))
        {

        }

        else if (serialData.contains("Widerstand"))
        {


        }
        //System.out.println(serialData);
    }

    public void closePort() {
        if (sp.closePort()) {
            System.out.println("Port is closed :)");
        } else {
            System.out.println("Failed to close port :(");
        }
    }
}
