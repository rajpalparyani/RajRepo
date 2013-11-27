cat > output1.sh << EOF

count=`cat links.txt | wc -l`
echo "Count=$count"

#for i in $count
for i in `seq 1 $count`
do
echo "i=$i"	
echo ectool setProperty "/myJob/Issue_List/\"`sed "\`echo $i\`q;d" issue_details.txt`\"" --value "`sed "\`echo $i\`q;d" links.txt`"

done

EOF

cat output1.sh

chmod a+x output1.sh

sh output1.sh
