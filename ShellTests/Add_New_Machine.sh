sudo hostname ec2d-resource-01
sudo perl -pi.bak -e "s/HOSTNAME=ec2-avengerscdn-01/HOSTNAME=ec2d-resource-01/g" /etc/sysconfig/network
sudo /etc/init.d/syslog restart

~/Documents/ddns/ddns-add-2 ec2d-resource-01.dyn.mypna.com. A 10.190.2.60