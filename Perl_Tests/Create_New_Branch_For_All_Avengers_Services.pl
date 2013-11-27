use ElectricCommander;

$| = 1;

my $ec = new ElectricCommander();

my @Service_Name = ();

my $jobId = $ec->getProperty ("/myJob/jobId")->findnodes("//value")->string_value();

my $build_type = $ec->getProperty ("/myJob/Build_Type")->findnodes("//value")->string_value();

my $svn_branch = $ec->getProperty ("/myJob/SVN_Branch")->findnodes("//value")->string_value();

my $project_name = $ec->getProperty ("/myProject/projectName")->findnodes("//value")->string_value();

my $procedure_Name = "Create_New_Branch_for_All_Avengers_Services_$jobId";

my $AdService_Flag = $ec->getProperty ("/myJob/Build_AdService")->findnodes("//value")->string_value();
print "AdService Flag = $AdService_Flag";

my $EntityService_Flag = $ec->getProperty ("/myJob/Build_EntityService")->findnodes("//value")->string_value();
print "\nEntityService Flag = $EntityService_Flag";

my $MapService_Flag = $ec->getProperty ("/myJob/Build_MapService")->findnodes("//value")->string_value();
print "\nMapService Flag = $MapService_Flag";

my $SpeechService_Flag = $ec->getProperty ("/myJob/Build_SpeechService")->findnodes("//value")->string_value();
print "\nSpeechService Flag = $SpeechService_Flag";

my $UserService_Flag = $ec->getProperty ("/myJob/Build_UserService")->findnodes("//value")->string_value();
print "UserService Flag = $UserService_Flag";


if ($AdService_Flag){push(@Service_Names, 'AdService');}
if ($EntityService_Flag){push(@Service_Names, 'EntityService');}
if ($MapService_Flag){push(@Service_Names, 'MapService');}
if ($SpeechService_Flag){push(@Service_Names, 'SpeechService');}
if ($UserService_Flag){push(@Service_Names, 'UserService');}

print "\nCSV Array items are ";
print join( ',', @Service_Names );
my $totalComponents = @Service_Names;
print "\nLength of Array is $totalComponents";

$ec->createProcedure( $project_name, $procedure_Name, { resourceName => "Linux_Build" } );

my $service_type = "";



my $componentVersion = $ec->getProperty ("/myJob/Component_Version")->findnodes("//value")->string_value();

my $componentVersion = $ec->getProperty ("/myJob/Component_Version")->findnodes("//value")->string_value();



for (my $i = 1 ; $i <= $totalComponents ; $i++ ) {

	$service_type = $Service_Names[$i];

	print "\n$i Count Component = $service_type";

	my $dev_mgr_Email_tag =  "/myJob/".$service_type."_DEV_Manager_Email";
	my $qa_mgr_Email_tag =  "/myJob/".$service_type."_QA_Manager_Email";
	my $dev_team_Email_tag =  "/myJob/".$service_type"_Dev_Team_Email";
	my $qa_team_Email_tag =  "/myJob/".$service_type."_QA_Team_Email";
	

	my $dev_mgr_email = $ec->getProperty ($dev_mgr_Email_tag)->findnodes("//value")->string_value();
	my $qa_mgr_email = $ec->getProperty ($qa_mgr_Email_tag)->findnodes("//value")->string_value();
	my $dev_mgr_email = $ec->getProperty ($dev_team_Email_tag)->findnodes("//value")->string_value();
	my $qa_team_email = $ec->getProperty ($qa_team_Email_tag)->findnodes("//value")->string_value();


        $ec->createStep( $project_name, $procedure_Name , "$stepName",
               { subprocedure => "Create New Branch Properties", parallel => 0, 
                     actualParameter => 
                          [ 
                             { actualParameterName => 'Component_Version', value => "$service_type.$build_type" },
                             { actualParameterName => 'Dev_Manager_Email', value => $dev_mgr_email },
                             { actualParameterName => 'Dev_Team_Email', value => $dev_mgr_email },
                             { actualParameterName => 'JIRA_Ticket_Number', value => $svn_branch },
                             { actualParameterName => 'NOC_Manager_Email', value => "$service_type.$build_type" },
                             { actualParameterName => 'NOC_Team_Email', value => "$service_type.$build_type" },
                             { actualParameterName => 'QA_Manager_Email', value => $qa_mgr_email },
                             { actualParameterName => 'QA_Team_Email', value => $qa_team_email },
                             { actualParameterName => 'SVN_Branch_Name', value => $svn_branch },
                          ] 
               } );





}



$ec->setProperty( "/myJob/VarStepProcedureName", $procedure_Name );

