use strict;
use ElectricCommander;

$| = 1;
my $ec = new ElectricCommander();

  my $count=0;	



	
  foreach my $val (@values) {
    $count++;
    $ec->setProperty("/myWorkflow/BuildComp$count", $val);
  }

    $ec->setProperty("/myWorkflow/build_total_components_count", $count);

