from ec import ElectricCommander
cmdr = ElectricCommander('hq-ecloud-01')

try:
    xml = cmdr.login('rajpalp@telenav.com', 'R@aajpal12232')
except Exception as inst:
    print(inst)



xml_output = cmdr.getProperty(dict(propertyName='/users/admin/fullUserName'))
print (xml_output)

"""
print(cmdr.findObjects(dict(
    objectType = 'project',
    filter = dict(
        operator = 'or',
        filter = [
            dict(
                propertyName = 'projectName',
                operator = 'like',
                operand1 = 'Ave%'),
            dict(
                propertyName = 'projectName',
                operator = 'like',
                operand1 = 'Server%')]))))
                
"""
                
"""                
print(cmdr.httpPost(cmdr.makeEnvelope(
        cmdr.createRequest('getProperty', 
                           dict(propertyName='/server/myprop'))
        + cmdr.createRequest('getServerStatus'),
       'parallel')))
"""
