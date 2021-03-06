/*
 * Author: Jon Trulson <jtrulson@ics.com>
 * Copyright (c) 2016 Intel Corporation.
 *
 * This program and the accompanying materials are made available under the
 * terms of the The MIT License which is available at
 * https://opensource.org/licenses/MIT.
 *
 * SPDX-License-Identifier: MIT
 */

import upm_t8100.T8100;

public class T8100_Example
{
    private static String defaultDev = "/dev/ttyUSB0";

    public static void main(String[] args) throws InterruptedException
    {
// ! [Interesting]

        // You will need to edit this example to conform to your site
        // and your devices, specifically the Device Object Instance
        // ID passed to the constructor, and the arguments to
        // initMaster() that are appropriate for your BACnet network.

        if (args.length > 0)
            defaultDev = args[0];
        System.out.println("Using device " + defaultDev);
        System.out.println("Initializing...");

        // Instantiate an T8100 object for an T8100 device that has
        // 568000 as it's unique Device Object Instance ID.  NOTE: You
        // will certainly want to change this to the correct value for
        // your device(s).
        T8100 sensor = new T8100(568000);

        // Initialize our BACnet master, if it has not already been
        // initialized, with the device and baudrate, choosing 1000001
        // as our unique Device Object Instance ID, 2 as our MAC
        // address and using default values for maxMaster and
        // maxInfoFrames
        sensor.initMaster(defaultDev, 38400, 1000001, 2);

        // Uncomment to enable debugging output
        // sensor.setDebug(true);

        System.out.println();
        System.out.println("Device Description: "
                           + sensor.getDeviceDescription());
        System.out.println("Device Location: " + sensor.getDeviceLocation());
        System.out.println();

        // update and print a few values every 5 seconds
        while (true)
            {
                // update our values
                sensor.update();

                System.out.println("CO2 Concentration: "
                                   + sensor.getCO2()
                                   + " ppm");

                // we show both C and F for temperature
                System.out.println("Temperature: "
                                   + sensor.getTemperature()
                                   + " C / "
                                   + sensor.getTemperature(true)
                                   + " F");

                System.out.println("Humidity: "
                                   + sensor.getHumidity()
                                   + " %RH");

                System.out.println("Relay State: "
                                   + sensor.getRelayState());

                System.out.println();
                Thread.sleep(5000);
            }

// ! [Interesting]
    }
}
