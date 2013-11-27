#!/bin/bash
#Script to run backups for different Projects in EC

#Local Variables for the script
SVN_ROOT=http://svn.telenav.com/tn/BuildMgt/EC_Backups/

if [ ! -n "$1" ]
then

	echo "Project Name Not Given"
	echo "Usage -> sh backup_ec_projects.sh <EC_Project_Name> <Backup_File_Name>"

else

	/bin/mkdir `date +%Y%m%d`
	cd `date +%Y%m%d`
	/usr/bin/svn co $SVN_ROOT/$2 .
	/usr/bin/md5sum OSMDataPipeline.xml > $2.xml.md5
	/opt/electriccloud/electriccommander/bin/ectool export /tmp/$2.xml --path "/projects/$1" --excludeJobs true --relocatable true  --withAcls true --withNotifiers true
	/bin/mv /tmp/$2.xml .
	/usr/bin/md5sum --status -c $2.xml.md5
	status=$?
	if [ $status -ne 0 ]; then
    		/bin/echo "Files are different .. Run svn commit"
		/usr/bin/svn ci -m "REL-3378 Runnning EC Backup Jobs on `date +%Y%m%d` for $2 Project" $2.xml
		#echo "REL-3378 Runnning EC Backup Jobs on `date +%Y%m%d` for $2 Project" $2.xml
	else
		/bin/echo  "Not checking in .. Files are identical"
	fi
	cd ..	
	rm -fr `date +%Y%m%d` 
fi
