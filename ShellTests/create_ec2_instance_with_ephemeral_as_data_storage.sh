#!/bin/bash
/bin/touch /var/tmp/start
if [ -b /dev/sdg ]; then
        FSTEST=`file -sL /dev/sdg2 | grep "sdg2: ERROR" | wc -l`
        if [ "$FSTEST" = "1" ]; then
                /bin/touch /var/tmp/inside
                #Create Partitions in sdg
                /bin/echo -e "n\np\n1\n\n+10240M\nw\n" | fdisk /dev/sdg
                /bin/echo -e "t\n82\nw\n" | fdisk /dev/sdg
                /bin/echo -e "n\np\n2\n\n\nw\n" | fdisk /dev/sdg

                #Format Patitions
                /sbin/mkswap /dev/sdg1
                /sbin/mkfs.ext3 /dev/sdg2

                #Mount Swap and Data Partitions
                /bin/mkdir -p /data/01
                /sbin/swapon /dev/sdg1
                /bin/echo '/dev/sdg2           /data/01  ext3    defaults            0  0' >> /etc/fstab
                /bin/mount -a

                #Move directories to new disks
                /bin/mv /home /data/01/home
                /bin/ln -s /data/01/home /home
                /bin/mv /usr/local /data/01/local
                /bin/ln -s /data/01/local /usr/local
                /bin/mv /opt /data/01/opt
                /bin/ln -s /data/01/opt /opt
                /bin/mv /var/log /data/01/log
                /bin/ln -s /data/01/log /var/log


        else
                /bin/echo "Secondary disk already has a filesystem"
        fi
else
        /bin/echo "Device has already been added"
fi

/bin/touch /var/tmp/done
