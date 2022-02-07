#!/bin/sh
### ====================================================================== ###
##                                                                          ##
##  UC Shutdown Script                                                      ##
##                                                                          ##
### ====================================================================== ###


ps -aef | grep uc-engine | grep -v "grep" |awk '{print $2}' |
while read PID
do
echo "$PID"
kill -9 $PID
done
