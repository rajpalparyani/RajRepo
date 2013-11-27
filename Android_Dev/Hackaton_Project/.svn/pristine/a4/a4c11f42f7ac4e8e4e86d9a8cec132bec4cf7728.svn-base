#! /bin/sh
ruby jsonvalidate.rb testschema.json good.json >/dev/null
if [ "$?"  -eq "0" ] ; then
echo good json test passed
else
echo good json test failed
fi
ruby jsonvalidate.rb testschema.json bad.json -ne 0] > /dev/null
if [ "$?" -ne "0" ] ; then
echo bad json test passed
else
echo bad json test failed
fi
