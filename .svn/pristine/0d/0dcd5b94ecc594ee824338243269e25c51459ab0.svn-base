#!/usr/bin/perl -w

 open (MYFILE, 'Single_Success.log');
 while (<MYFILE>) {
 	chomp;
 	
 	if($_ =~ m/tads.client.msg=(.*)successfully/) {
	print "$1\n";
	print "Success in first\n";

	 	if($1 =~ m/(.*)successfully/) {
	print "Success Raj\n";

	}


}
 	
 	
 }


 close (MYFILE); 