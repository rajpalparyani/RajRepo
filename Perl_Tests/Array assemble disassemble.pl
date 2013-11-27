
push(@Service_Names, 'EntityService');
push(@Service_Names, 'AdService');
push(@Service_Names, 'SpeechService');
push(@Service_Names, 'UserService');
push(@Service_Names, 'MapService');

print "CSV Array items are ";
print join( ',', @Service_Names );
$Service_Names = @Service_Names;
print "Length of Array is $Service_Names";

#print "Array items are @Service_Names";


 