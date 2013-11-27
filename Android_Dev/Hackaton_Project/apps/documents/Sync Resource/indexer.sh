#tn70-android-sprint-i18n-ota-7011099.zip\SN_NAV\7.0.01\ANDROID

echo "Inderxer Processing..."

rm tn70-android-$1-i18n-ota-$3.zip
wget http://tar1.telenav.com:8080/repository/telenav/tnclient/TNStudio/release/mobile/apps/branches/$2/telenav-global/android/$3/tn70-android-$1-i18n-ota-$3.zip


if [ $1 = "att" ];then
	CarrierFolder=ATT_NAV
elif [ $1 = "sprint" ];then
	CarrierFolder=SN_NAV
elif [ $1 = "vivo" ];then
	CarrierFolder=VIVO_NAV
elif [ $1 = "tmobileuk" ];then
	CarrierFolder=TMO_NAV
fi


if [ $2 = "7.0.0" ];then
	CSvrVersionFolder="7.0.00"
elif [ $2 = "7.0.1" ];then
	CSvrVersionFolder="7.0.01"
fi


if [ ! $CarrierFolder ]; then
	DisplayInfo="Fail!!! cannot match $1, please select one in (att, sprint, vivo, tmobile)"
elif [ ! $CSvrVersionFolder ]; then
	DisplayInfo="Fail!!! cannot match $2, please select one in (7.0.0, 7.0.1)"
fi


if [ ! $CarrierFolder ] || [ ! $CSvrVersionFolder ]; then
	ERROR="true"
elif [ -f tn70-android-$1-i18n-ota-$3.zip ]; then 
	echo "Inderxer Processing $CSvrVersionFolder $CarrierFolder ..."

	if [ ! -d $CarrierFolder ]; then
		mkdir $CarrierFolder
	fi
	
	rm -r -f tmp_$CSvrVersionFolder
	unzip tn70-android-$1-i18n-ota-$3.zip -d tmp_$CSvrVersionFolder
	rm -r -f $CarrierFolder/$CSvrVersionFolder
	cp -r tmp_$CSvrVersionFolder/$CarrierFolder/* $CarrierFolder
	rm -r -f tmp_$CSvrVersionFolder
	rm tn70-android-$1-i18n-ota-$3.zip
	DisplayInfo="Success!!!"
else
	DisplayInfo="Fail!!! cannot find http://tar1.telenav.com:8080/repository/telenav/tnclient/TNStudio/release/mobile/apps/branches/$CSvrVersionFolder/telenav-global/android/$3/tn70-android-$1-i18n-ota-$3.zip"
	ERROR="true"
fi


echo
echo
echo "$DisplayInfo"
echo

if [ $ERROR ]; then
	exit 1
fi

#sh indexer.sh 1:(carrier which in att,sprint,vivo and tmobile) 2:(app version) 3:(build number)
#sh indexer.sh att 7.0.1 7011095

#wget http://tar1.telenav.com:8080/repository/telenav/tnclient/TNStudio/release/mobile/apps/branches/7.0.1/telenav-global/android/7011099/tn70-android-att-i18n-ota-7011099.zip