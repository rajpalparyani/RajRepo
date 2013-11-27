import sys,os
from xml.dom import minidom

mergedlist2=[]
xmldoc = minidom.parse('./modules/ios-sdk6.0/tmp/ivy-cache/telenav-TelenavNavigator-default.xml')
servicelist = xmldoc.getElementsByTagName('module')
versionlist = xmldoc.getElementsByTagName('revision')
print "Length of service List= " + str(len(servicelist)) + "And Length of Version List = " + str(len(versionlist))
#add try and catch to make sure the lists are same size
print "Service List Values now -:\n"
for a in servicelist:
 print str(a.attributes['name'].value)

print "Version List Values now -:\n"
for a in versionlist:
    print str(a.attributes['name'].value)


#for a, b in zip(servicelist, versionlist):
# print str(a.attributes['name'].value) + '":"' + str(b.attributes['name'].value)

    #if (len(servicelist) == len(versionlist)):
    #    mergedlist2 = ['"' + str(a.attributes['name'].value) + '":"' + str(b.attributes['name'].value) + '"'  for a, b in zip(servicelist, versionlist)]
    # jsonstr = '{' + '\n' + ',\n'.join(mergedlist2) + ',\n' + '}'
    #    print "Now creating a file for services retrieved"
    #    with open('services_retrieved.json', 'w') as f:
    #       f.write(jsonstr)
      
    #    f.close()
    #    print ("This is the String that was pasted" + jsonstr)
    #    os.system( 'mv ./services_retrieved.json ./Res/config/')
    # else:
#    print 'Length of Services and Version List are not the same'

