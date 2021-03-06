/*
* Author: Zion Orent <zorent@ics.com>
* Copyright (c) 2015 Intel Corporation.
*
* This program and the accompanying materials are made available under the
* terms of the The MIT License which is available at
* https://opensource.org/licenses/MIT.
*
* SPDX-License-Identifier: MIT
*/

var upmMQ3 = require("jsupm_gas");

// Attach gas sensor to AIO0
var myMQ3 = new upmMQ3.MQ3(0);

var threshContext = new upmMQ3.thresholdContext;
threshContext.averageReading = 0;
threshContext.runningAverage = 0;
threshContext.averagedOver = 2;

// Infinite loop, ends when script is cancelled
// Repeatedly, take a sample every 2 milliseconds;
// find the average of 128 samples; and
// print a running graph of the averages using a resolution of 5
while(1)
{
    var buffer = new upmMQ3.uint16Array(128);
    var len = myMQ3.getSampledWindow(2, 128, buffer);
    if (len)
    {
        var thresh = myMQ3.findThreshold(threshContext, 30, buffer, len);
        myMQ3.printGraph(threshContext, 5);
        //if (thresh)
        //    console.log("Threshold is " + thresh);
    }
}

// Print message when exiting
process.on('SIGINT', function()
{
	console.log("Exiting...");
	process.exit(0);
});
