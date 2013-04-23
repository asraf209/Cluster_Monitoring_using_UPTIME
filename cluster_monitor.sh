#!/bin/sh

#sleep 20m
cd /home/monitor/MonitoringApp

java -jar ClusterMonitoring.jar ClusterMonitor 1.6 > cluster_monitor.out 

