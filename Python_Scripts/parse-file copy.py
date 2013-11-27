import sys
from xml.dom import minidom

mergedlist2 = []
f = file('services_retrieved.json', 'w')


xmldoc = minidom.parse('parse-file.xml')
servicelist = xmldoc.getElementsByTagName('module')
versionlist = xmldoc.getElementsByTagName('revision')

mergedlist2 = ['"' + str(a.attributes['name'].value) + '":"' + str(b.attributes['name'].value) + '"'  for a, b in zip(servicelist, versionlist)]
jsonstr = '{' + '\n' + ',\n'.join(mergedlist2) + ',\n' + '}' 

print >> f, jsonstr

f.close()
