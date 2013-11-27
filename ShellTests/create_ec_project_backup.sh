#!/bin/bash
#Script to Create backups for different Projects in EC

#Local Variables for the script
SVN_ROOT=http://svn.telenav.com/tn/BuildMgt/EC_Backups

if [ ! -n "$1" ]
then

        echo "Project Name Not Given"
        echo "Usage -> sh create_ec_project_backup.sh <EC_Project_Name> <Backup_File_Name>"

else

        /bin/mkdir `date +%Y%m%d`
        cd `date +%Y%m%d`
        /opt/electriccloud/electriccommander/bin/ectool export /tmp/$2.xml --path "/projects/$1" --excludeJobs true --relocatable true  --withAcls true --withNotifiers true
        sudo /bin/mv /tmp/$2.xml .
        #/bin/echo "import $SVN_ROOT/$2/ -m REL-3378 Create EC backup job for $2 Project on `date +%Y%m%d` $2.xml"
        /usr/bin/svn import $SVN_ROOT/$2/ -m "REL-3378 Create EC backup job for $2 Project on `date +%Y%m%d`" 
        cd ..
        rm -fr `date +%Y%m%d`
fi
