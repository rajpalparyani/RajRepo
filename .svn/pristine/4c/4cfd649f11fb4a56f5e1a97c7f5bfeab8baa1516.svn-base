#!/usr/bin/perl
#use strict;

my $vtab="|";
my $pass;
my $fail;
my $total;
my $passRate;

search_dir($ARGV[0]);


sub search_dir{
        my ($entries)=@_;
    @dirlist = listdir($entries);
        my %testcaseMap =();
        foreach my $file (@dirlist) {
                if (-d $entries."/".$file){ #is a dir or not
                    if(-e $entries."/"."overview-summary.html"){
                        $count = length($file);
                        if($count gt 1){
                             getStatusFromHtml($entries."/"."overview-summary.html");                             
                        }
                    }
                }
        }
        
        if($total > 0 ){
        	$passRate = sprintf("%.2f", (($pass/$total)*100)); 
        }        
                
        print "Pass ".$pass."\n";
        print "Fail ".$fail."\n";
        print "Error ".$error."\n";
        print "Total ".$total."\n";
        print "Passrate ".$passRate."\n";

        system "ectool setProperty /myJob/numPass $pass";
        system "ectool setProperty /myJob/numFail $fail";
        system "ectool setProperty /myJob/numError $error";
        system "ectool setProperty /myJob/numTotal $total";
        system "ectool setProperty /myJob/passRate $passRate";
}

sub listdir{
        my ($dirname) = @_;

        opendir(DIR_HANDLE, $dirname);
        while(my $file = readdir(DIR_HANDLE)){
           push @dirlist, $file;
        }
        closedir(DIR_HANDLE);
        return @dirlist;
}


sub getStatusFromHtml{
        my ($entry)=@_;
        open(IN,$entry);
    my %testcaseMap=();
        while(<IN>)
        {
                chomp;
                my $line=$_;
                if($line =~ m{<td><a title=".*?all tests" href="all.*?.html">(.*?)</a>.*?href=".*?fails.html">(.*?)</a>.*?href=".*?errors.html">(.*?)</a>.*?</td>}){                	
                        #print $line."\n";
                        $total = $1;
                        $fail = $2;
                        $error = $3;
                        $pass = ($total - ($error + $fail));                        
                }
        }
        close(IN);
}
