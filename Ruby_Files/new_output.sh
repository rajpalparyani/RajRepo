#!/bin/bash
i=0
count=`cat links.txt | wc -l`

echo i=$i
echo count=$count
while [ $i -lt $count ]
do
    echo "output: $i"
    true $(( i++ ))
    echo ectool setProperty "/myJob/Issue_List/\"`sed "\`echo $i\`q;d" issue_details.txt`\"" --value "`sed "\`echo $i\`q;d" links.txt`"
e