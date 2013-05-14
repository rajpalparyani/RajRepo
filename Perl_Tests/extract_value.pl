#!/usr/bin/perl

  use strict;
  use warnings;

  my $data = 'AdService_20130212150903';
  
if($data =~ m/(.*)_/) {
	print "$1\n";
}

  exit 0;