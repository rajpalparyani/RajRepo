#!/usr/bin/perl

  use strict;
  use warnings;

my $count = 0;

while($count++ < 3) {
  chomp(my $line = <STDIN>);
  last if $line =~ /quit/;
  next if $line =~ /while|until/i;
  redo;
}

# end of the loop
print "Count = ", --$count, "\n";

