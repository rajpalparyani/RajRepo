use ElectricCommander;

$| = 1;
my $ec = new ElectricCommander();

my $jobId = $ec->getProperty ("/myJob/jobId")->findnodes("//value")->string_value();

my $build_type = $ec->getProperty ("/myJob/Build_Type")->findnodes("//value")->string_value();

my $svn_branch = $ec->getProperty ("/myJob/SVN_Branch")->findnodes("//value")->string_value();

my $job_type = $ec->getProperty ("/myJob/Job_Type")->findnodes("//value")->string_value();


my $propcount = 0;

my $temp_property = "";

my $nested_sheet_name = "";

my $test_hostname = "";

my $test_port = "";

my $serverName= "";

my $number_of_threads = "";

my $cluster_name = "";

my $deployed_release_name = "";

my $project_name = $ec->getProperty ("/myProject/projectName")->findnodes("//value")->string_value();

my $procedure_Name = "Parallel_Jobs_for_$job_type_$jobId";

print "Project Name = $project_name\n";

if ($job_type eq "Build") {

$ec->createProcedure( $project_name, $procedure_Name, { resourceName => "Linux_Build" } );

$nested_sheet_name = $ec->getProperty ("/myJob/Build_Sheet_Name")->findnodes("//value")->string_value();

}

if ($job_type eq "Func_Test") {

$ec->createProcedure( $project_name, $procedure_Name, { resourceName => "QA-Test-Pool" } );

$nested_sheet_name = "/myJob/Func_Test_List";

$test_hostname = $ec->getProperty ("/myJob/Test_Hostname")->findnodes("//value")->string_value();

$test_port = $ec->getProperty ("/myJob/Test_Port")->findnodes("//value")->string_value();

}

if ($job_type eq "Load_Test") {

$ec->createProcedure( $project_name, $procedure_Name, { resourceName => "QA-Test-Pool" } );

$nested_sheet_name = "/myJob/Load_Test_List";

$serverName = $ec->getProperty ("/myJob/Server_Name")->findnodes("//value")->string_value();

$number_of_threads = $ec->getProperty ("/myJob/Number_Of_Threads")->findnodes("//value")->string_value();

}

if ($job_type eq "Deploy") {

$ec->createProcedure( $project_name, $procedure_Name, { resourceName => "Linux_Build" } );

$nested_sheet_name = "/myJob/Deploy_List";

$cluster_name = $ec->getProperty ("/myJob/Cluster_Name")->findnodes("//value")->string_value();

}



$ec->setProperty( "/myJob/VarStepProcedureName", $procedure_Name );


print "Nested Sheet Name = $nested_sheet_name\n";
		
my $propId = $ec->getProperty ($nested_sheet_name)->findnodes("//propertySheetId")->string_value();

my $newxpath = $ec->getProperties({propertySheetId => $propId});

foreach my $temp ($newxpath->findnodes("//propertyId")) {
$propcount++;
        }

my $totalComponents = $propcount;

print "Total Components = $totalComponents\n";

if ($job_type eq "Deploy") {

$ec->setProperty( "/myWorkflow/Total_Deploy_Count", $totalComponents );

}


for (my $i = 1 ; $i <= $totalComponents ; $i++ ) {


#my $temp_property = $nested_sheet_name . "/" . $i . "/Component_Name";


if ($job_type eq "Build") {

$temp_property = $nested_sheet_name . "/" . $i . "/Component_Name";

}


if ($job_type eq "Func_Test") {

$temp_property = $nested_sheet_name . "/Comp" . $i;
}


if ($job_type eq "Load_Test") {

$temp_property = $nested_sheet_name . "/Comp" . $i;
}


if ($job_type eq "Deploy") {

$temp_property = $nested_sheet_name . "/Comp" . $i;
}



print "temp property = $temp_property\n";

my $temp_component_Name = $ec->getProperty ($temp_property)->findnodes("//value")->string_value();

print "temp Component name = $temp_component_Name\n";
	
	
	my $stepName = "Step for $job_type for $temp_component_Name";


if ($job_type eq "Deploy") {

$deployed_release_name = $ec->getProperty ("/myWorkflow/Deployed_Release_Name/$temp_component_Name Release Name")->findnodes("//value")->string_value();

print "Deployed Release Name = $deployed_release_name\n";
	

}



if ($job_type eq "Build") {
	
        $ec->createStep( $project_name, $procedure_Name , "$stepName",
               { subprocedure => "Build_and_Upload_Avengers_Components", parallel => 1, 
                     actualParameter => 
                          [ 
                             { actualParameterName => 'Build_Type', value => $build_type },
                             { actualParameterName => 'Cluster_Name', value => "Dev1" },
                             { actualParameterName => 'Component_Name', value => $temp_component_Name },
                             { actualParameterName => 'SVN_Branch', value => $svn_branch },
                          ] 
               } );

}	

if ($job_type eq "Func_Test") {
	
        $ec->createStep( $project_name, $procedure_Name , "$stepName",
               { subprocedure => "Run_Avengers_Functional_Tests", parallel => 1, 
                     actualParameter => 
                          [ 
                             { actualParameterName => 'Build_Type', value => $build_type },
                             { actualParameterName => 'Component_Name', value => $temp_component_Name },
                             { actualParameterName => 'Test_Hostname', value => $test_hostname },
                             { actualParameterName => 'Test_Port', value => $test_port },
                             { actualParameterName => 'SVN_Branch', value => $svn_branch },
                          ] 
               } );

}	


if ($job_type eq "Load_Test") {
	
        $ec->createStep( $project_name, $procedure_Name , "$stepName",
               { subprocedure => "Run_Load_Tests", parallel => 1, 
                     actualParameter => 
                          [ 
                             { actualParameterName => 'Build_Type', value => $build_type },
                             { actualParameterName => 'Component_Name', value => $temp_component_Name },
                             { actualParameterName => 'Server_Name', value => $serverName },
                             { actualParameterName => 'Number_Of_Threads', value => $number_of_threads },
                             { actualParameterName => 'SVN_Branch', value => $svn_branch },
                          ] 
               } );

}	


if ($job_type eq "Deploy") {
	
        $ec->createStep( $project_name, $procedure_Name , "$stepName",
               { subprocedure => "Deploy_Components", parallel => 1, 
                     actualParameter => 
                          [ 
                             { actualParameterName => 'Cluster_Name', value => $cluster_name },
                             { actualParameterName => 'Component_Name', value => $temp_component_Name },
                             { actualParameterName => 'Deployed_Release_Name', value => $deployed_release_name },
                          ] 
               } );

}	



	
	}