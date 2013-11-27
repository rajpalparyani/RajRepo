#!/usr/bin/python
import time;  # This is required to include time module.
import datetime;

ticks = datetime.datetime.now().strftime('%H:%M:%S_%m-%d')
print "Number of ticks since 12:00am, January 1, 1970:", ticks