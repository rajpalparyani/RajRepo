import re

lines = [line.strip() for line in open('./modules/ios-sdk6.0/tmp/ivy-cache/telenav-TelenavNavigator-default.xml')]
jsonstr = '{\n'
print '{'
for line in lines:
 m = re.search('location=\"http://tar1.telenav.com/repository/telenav/client//trunk/(.*)-trunk.zip\"\/>', line)
 if m:
  found = m.group(1)
  n = re.search('(.*)/(.*)-ios-sdk6.0-(.*)', found)
  if n:
   newfound1=n.group(2)
   newfound2=n.group(3)
   jsonstr += '"' + newfound1 + '"' + ":" + '"' + newfound2 + '"' +',\n'

jsonstr +="}"

with open('services_retrieved.json', 'w') as f:
 f.write(jsonstr)

f.close() 
 
os.system( 'mv ./services_retrieved.json ./Res/config/')
