Map_Last_Checkin=`svn log http://svn.telenav.com/tn/avengers/Map/trunk/backend/MapService/ -vl1 --quiet | grep "^r" | awk '{print $5}'`
Entity_Last_Checkin=`svn log http://svn.telenav.com/tn/avengers/Entity/trunk/backend/EntityService/ -vl1 --quiet | grep "^r" | awk '{print $5}'`
Ads_Last_Checkin=`svn log http://svn.telenav.com/tn/avengers/Ads/trunk/backend/AdService/ -vl1 --quiet | grep "^r" | awk '{print $5}'`
Speech_Last_Checkin=`svn log http://svn.telenav.com/tn/avengers/Speech/trunk/backend/SpeechService/ -vl1 --quiet | grep "^r" | awk '{print $5}'`
Todays_Date=`date +%Y-%m-%d`

if [ "$Map_Last_Checkin" = "$Todays_Date" ] && [ "$Entity_Last_Checkin" = "$Todays_Date" ] && [ "$Ads_Last_Checkin" = "$Todays_Date" ] && [ "$Speech_Last_Checkin" = "$Todays_Date" ]; then 
    echo "Run Nightly Tests" 
else
    echo "Do Not Run Nightly Tests"  
fi;