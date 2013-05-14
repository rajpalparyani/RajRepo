#!/bin/bash
# Script to create branches for all Avengers Services
Avengers_Base_URL="http://svn.telenav.com/tn/avengers"
Add_Message="AVENGERS-184 Made a test branch "
if [ ! -n "$1" ]
 then

echo "Branch Name  not given"
echo "Usage -> sh create_branches.sh <Branch_Name>"

else
svn copy $Avengers_Base_URL/Entity/trunk/ $Avengers_Base_URL/Entity/branches/$1 -m "$Add_Message $1 for Entity"
svn copy $Avengers_Base_URL/Ads/trunk/ $Avengers_Base_URL/Ads/branches/$1 -m "$Add_Message $1 for Ads"
svn copy $Avengers_Base_URL/Map/trunk/ $Avengers_Base_URL/Map/branches/$1 -m "$Add_Message $1 for Map"
svn copy $Avengers_Base_URL/MobiPlat/trunk/ $Avengers_Base_URL/MobiPlat/branches/$1 -m "$Add_Message $1 for MobiPlat"
svn copy $Avengers_Base_URL/Platform/trunk/ $Avengers_Base_URL/Platform/branches/$1 -m "$Add_Message $1 for Platform"
svn copy $Avengers_Base_URL/Speech/trunk/ $Avengers_Base_URL/Speech/branches/$1 -m "$Add_Message $1 for Speech"
svn copy $Avengers_Base_URL/User/trunk/ $Avengers_Base_URL/User/branches/$1 -m "$Add_Message $1 for User"
fi

