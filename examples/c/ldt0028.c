/*
 * Author: Noel Eck <noel.eck@intel.com>
 * Copyright (c) 2016 Intel Corporation.
 *
 * This program and the accompanying materials are made available under the
 * terms of the The MIT License which is available at
 * https://opensource.org/licenses/MIT.
 *
 * SPDX-License-Identifier: MIT
 */

#include <unistd.h>
#include <signal.h>

#include "ldt0028.h"
#include "upm_utilities.h"

bool shouldRun = true;

void sig_handler(int signo)
{
    if (signo == SIGINT)
        shouldRun = false;
}

int main()
{
    signal(SIGINT, sig_handler);

    //! [Interesting]

    // Instantiate a ldt0028 sensor on analog pin A0
    ldt0028_context sensor = ldt0028_init(0);

    if (!sensor)
    {
        printf("ldt0028_init() failed.\n");
        return -1;
    }

    // Set the aref, scale, and offset
    ldt0028_set_aref(sensor, 5.0);
    ldt0028_set_scale(sensor, 1.0);
    ldt0028_set_offset(sensor, -.1);
    printf("aRef: %0.03f scale: %0.03f offset: %0.03f\n\n",
            ldt0028_get_aref(sensor),
            ldt0028_get_scale(sensor),
            ldt0028_get_offset(sensor));

    float normalized = 0.0;
    float raw_volts = 0.0;
    float volts = 0.0;

    float norm_base = 0.1;

    printf("Using normalized sensor output average == %f\n", norm_base);

    // Every half a second, sample the sensor output
    while (shouldRun)
    {

        ldt0028_get_normalized(sensor, &normalized);
        ldt0028_get_raw_volts(sensor, &raw_volts);
        ldt0028_get_volts(sensor, &volts);

        if (normalized > norm_base)
        {
            printf("Detected vibration!\n");
            printf("Normalized output: %0.03f, raw ldt0028 sensor output: %0.03f v "
                    "adjusted output: %0.03f v\n\n", normalized, raw_volts, volts);
        }

        upm_delay_ms(500);
    }

    //! [Interesting]

    printf("Exiting\n");

    ldt0028_close(sensor);

    return 0;
}
