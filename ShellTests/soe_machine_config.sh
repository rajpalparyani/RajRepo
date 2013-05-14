                mkdir -p /data/01
                mv /home /data/01/home
                ln -s /data/01/home /home
                mv /usr/local /data/01/local
                ln -s /data/01/local /usr/local
                cd /usr/local/
                echo "- Pulling installersv4.tgz"
                curl http://10.224.0.68/installersv4/installersv4.tgz > installersv4.tgz
                echo "- Unpacking installersv4.tgz"
                tar xfz installersv4.tgz
                echo "- Setting up java"
                mkdir -p /usr/java
                mv /usr/local/jdk* /usr/java
                #mv jdk1.6.0_30 /usr/local/jdk1.6.0_30
                cd /bin
                ln -s /usr/java/jdk1.6.0_30/bin/java java
                rm -f rm /usr/local/installersv4.tgz
                yum -y install coreutils
                /etc/init.d/syslog start
                chkconfig syslog on

