#!/bin/bash
# Script to delete branches for all Avengers Services
Avengers_Base_URL="http://svn.telenav.com/tn/avengers"
Delete_Message="AVENGERS-161 Deleted the test branch "
if [ ! -n "$1" ]

 then

echo "Branch Name  not given"
echo "Usage -> sh delete_branches.sh <Branch_Name>"

else

svn del $Avengers_Base_URL/Entity/branches/$1 -m "$Delete_Message $1 for Entity"
svn del $Avengers_Base_URL/Ads/branches/$1 -m "$Delete_Message $1 for Ads"
svn del $Avengers_Base_URL/Map/branches/$1 -m "$Delete_Message $1 for Map"
svn del $Avengers_Base_URL/MobiPlat/branches/$1 -m "$Delete_Message $1 for MobiPlat"
svn del $Avengers_Base_URL/Platform/branches/$1 -m "$Delete_Message $1 for Platform"
svn del $Avengers_Base_URL/Speech/branches/$1 -m "$Delete_Message $1 for Speech"
svn del $Avengers_Base_URL/User/branches/$1 -m "$Delete_Message $1 for User"

fi
