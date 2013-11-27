use strict;
use warnings;
use XML::XPath;

my $file = "xpath.xml";
my $path = XML::XPath->new(filename => $file);

my $workflowName = $path->find('/responses/response/workflow/workflowName');
print "Workflow Name = $workflowName\n";
