#!/bin/bash
count=`cat links.txt | wc -l`
#echo "Count=$count"

#for i in $count
for i in `seq 1 \`cat links.txt | wc -l\``
do
   #echo "Welcome $i times"
   #echo "`cat links.txt | head -$i`"
#   cat links.txt|head -"$i"
#   cat text.txt|head -"$i"
	#echo "i=$i"
	echo \"`sed "\`echo $i\`q;d" text.txt`\"
	#sed "`echo $i`q;d" links.txt
   #echo "`sed '$iq;d' text.txt` + Links : `sed '$iq;d' links.txt`"

done

i=0
count=`cat links.txt | wc -l`
while [ $i -lt $max ]
do
    echo "output: $i"
    true $(( i++ ))
done