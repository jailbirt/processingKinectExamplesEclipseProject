#!/bin/sh
./usbreset /dev/bus/usb/`lsusb | grep 12d1:1003 | awk ‘{ print $2″/”$4}’ | sed -e ‘s/://’`
