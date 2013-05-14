#!/usr/bin/perl

  use strict;
  use warnings;

  my $data = 'Becky Alcorn,25,female,Melbourne';
  my $count=0;	
  my @values = split(',', $data);
	
  foreach my $val (@values) {
    $count++;
  }

  foreach my $val (@values) {
    print "$val\n";
  }

	print "Count: $count\n";

  exit 0;