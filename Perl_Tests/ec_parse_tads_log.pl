use strict;
use ElectricCommander;

$| = 1;
my $ec = new ElectricCommander();

my $prev_jobStepId = $ec->getProperty ("/myjob/prev_jobStepId")->findnodes("//value")->string_value();

my $file_name = "Deploy_Component" . $prev_jobStepId . ".log"; 

open (MYFILE, $file_name);
while (<MYFILE>) {
 	chomp;
 	
	if($_ =~ m/tads.client.msg=(.*)successfully/) {
		print "$1\n";
    $ec->setProperty("/myJob/outcome", "success");

	} else {
	
    $ec->setProperty("/myJob/outcome", "error");
	
	}

}
 close (MYFILE); 