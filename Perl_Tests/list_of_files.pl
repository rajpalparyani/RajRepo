#!/usr/bin/perl -w

my $dir = "/usr/local/apache-tomcat-7.0.32/lib/*";

#my @files = readdir $dir;

my @files = <$dir>;

print @files;