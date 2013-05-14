MESSAGE="Test Message"
if [ ! -n "$1" ] 
 then
echo "First parameter $MESSAGE not given"
else
echo "First Parameter $MESSAGE = $1"
fi
