# -------------------------------------------------------------------------
# Package
#    Jenkins
#
# Dependencies
#    URI
#    XML::XPath
#    LWP::UserAgent
#    ElectricCommander
#    ElectricCommander::PropDB
#
# Purpose
#    Serve several operations to run against a jenkins server
#
# Date
#    10/04/2012
#
# Engineer
#    Carlos Rojas
#
# Copyright (c) 2012 Electric Cloud, Inc.
# All rights reserved
# -------------------------------------------------------------------------
package Jenkins;

use URI;
use strict;
use warnings;
use XML::XPath;
use LWP::UserAgent;
use ElectricCommander;
use ElectricCommander::PropDB;
use ElectricCommander::PropMod;

$|=1;

use constant {
    GET_REQUEST => "get",
    POST_REQUEST => "post",
    SECONDS_IN_A_MINUTE => 60,
    GOOD_RESPONSE => "302 Found",
    MILLISECONDS_IN_A_SECOND => 1000,
    TABLE_ROW => qq{<tr><td>_name_</td><td><a href="_url_" target="_blank">_url_</a></td><td><img src="../../plugins/EC-Jenkins/images/_color_._ext_" alt="_color_" /></td></tr>},
};

sub new {
    my $class = shift;
    #attributes
    my $self = {
        ec                  => shift, #commander instance
        configuration_name  => shift, #configuration name
        server              => shift, #server name/ip
        port                => shift, #server port
        user                => shift, #jenkins user
        password            => shift, #user's password
    };
    bless $self, $class;
    return $self;
}

=head2 getBaseUrl
 
  Title    : getBaseUrl
  Usage    : $self->getBaseUrl("api/xml?arg1=1&arg2=2");
  Function : Returns the jenkins base url formed by (protocol + server + port) + path parameter 
  Returns  : A complete url ready to perform an http request
  Args     : named arguments:
           : -path => additional parameters you may want to add to the base url
           :
=cut
sub getBaseUrl{
    my ($self, $path) = @_;
    my $uri = URI->new;
    $uri->scheme("http");
    $uri->host($self->{server});
    if($self->{port} && $self->{port} ne ""){
        $uri->port($self->{port});
    }else{
        $uri->port("8080");
    }
    if($path && $path ne ""){
        $uri->path_query($path);
    }

    return $uri->as_string;
}

=head2 getBuildStatus
 
  Title    : getBuildStatus
  Usage    : $self->getBuildStatus("testjob", "1");
  Function : Returns the status of the specified job's build in a jenkins server
  Returns  : http response body from jenkins server
  Args     : named arguments:
           : -job_name => name of the job to check
           : -build_number => job's build number (lastBuild if not specified)
           :
=cut
sub getBuildStatus{
    my ($self, $job_name, $build_number, $result_outpp) = @_;
    #load configuration values
    $self->loadConfiguration();
    my $path = $self->getJobAndBuildPath($job_name, $build_number);
    $path .= "api/xml?xpath=/*/result";
    my $url = $self->getBaseUrl($path);
    my $response = $self->request(GET_REQUEST, $url);
    #set property if $result_outpp is set.
    $self->setProperty($result_outpp, $response);
    return $response;
}

=head2 getBuildLog
 
  Title    : getBuildLog
  Usage    : $self->getBuildLog("testjob", "1");
  Function : Returns the console output of the specified job's build in a jenkins server
  Returns  : http response body from jenkins server
  Args     : named arguments:
           : -job_name => name of the job to check
           : -build_number => job's build number (lastBuild if not specified)
           :
=cut
sub getBuildLog{
    my ($self, $job_name, $build_number, $result_outpp) = @_;
    #load configuration values
    $self->loadConfiguration();
    my $path = $self->getJobAndBuildPath($job_name, $build_number);
    $path .= "logText/progressiveText";
    my $url = $self->getBaseUrl($path);
    my $response = $self->request(GET_REQUEST, $url);
    #set property if $result_outpp is set.
    $self->setProperty($result_outpp, $response);
    return $response;
}

=head2 getBuildDuration
 
  Title    : getBuildDuration
  Usage    : $self->getBuildDuration("testjob", "1");
  Function : Returns the build duration of the specified job's build in a jenkins server
  Returns  : http response body from jenkins server
  Args     : named arguments:
           : -job_name => name of the job to check
           : -build_number => job's build number (lastBuild if not specified)
           :
=cut
sub getBuildDuration{
    my ($self, $job_name, $build_number, $time_unit, $result_outpp) = @_;
    #load configuration values
    $self->loadConfiguration();
    my $path = $self->getJobAndBuildPath($job_name, $build_number);
    $path .= "api/xml?xpath=/*/duration";
    my $url = $self->getBaseUrl($path);
    my $duration = $self->request(GET_REQUEST, $url);
    $duration =~ m/(\d+)/;
    $duration = $1 / MILLISECONDS_IN_A_SECOND;
    $duration = ($time_unit && $time_unit eq "minutes") ? $duration / SECONDS_IN_A_MINUTE : $duration;
    #Format time with 3 decimals 
    $duration = sprintf("%.3f", $duration);
    $duration .= ($time_unit && $time_unit eq "minutes") ? " Minutes" : " Seconds";
    #set property if $result_outpp is set.
    $self->setProperty($result_outpp, $duration);
    return $duration;
}

=head2 serverStatusReport
 
  Title    : serverStatusReport
  Usage    : $self->serverStatusReport("1");
  Function : Creates a report of the jenkins server job's status
  Returns  : http response body from jenkins server
  Args     : named arguments:
           : -job_name => name of the job to check
           : -build_number => job's build number (lastBuild if not specified)
           :
=cut
sub serverStatusReport{
    my ($self, $jobStepId) = @_;
    #load configuration values
    $self->loadConfiguration();
    my $ec = $self->{ec};
    my $path = "api/xml";
    my $url = $self->getBaseUrl($path);
    my $result = $self->request(GET_REQUEST, $url);
    #xml parsing
    my $xpath = XML::XPath->new($result);
    #find available jobs
    my $nodeset = $xpath->find('/hudson/job');
    my $table = "";
    foreach my $node ($nodeset->get_nodelist) {
        my $name = $node->find("name");
        my $url  = $node->find("url");
        my $color = $node->find("color");
        my $current_row = TABLE_ROW;
        $current_row =~ s/_name_/$name/;
        $current_row =~ s/_url_/$url/g;
        #replace disable for grey color
        $color =~ s/disabled/grey/;
        $current_row =~ s/_color_/$color/g;
        my $ext = ($color =~ m/anime/) ? "gif" : "png";
        $current_row =~ s/_ext_/$ext/g;
        $table .= $current_row."\n";
    }
    
    my $template = ($ec->getProperty( "/projects/EC-Jenkins-1.3.0.56335/report_template" ))->findvalue('//value')->string_value;
    #replace template values for the real results
    $template =~ s/_title_/Jenkins Status Report/g;
    $template =~ s/_table_/$table/g;
    my $report_name = "jenkins_status.html";
    #create a report in the job's folder
    open (MYFILE, ">>$report_name");
    print MYFILE "$template";
    close (MYFILE);
    #create the new report url pointing to our jenkins report
    $ec->setProperty("/myJob/artifactsDirectory", '');   
    $ec->setProperty("/myJob/report-urls/EC-Jenkins Server Status","jobSteps/$jobStepId/$report_name");
    return $result;
}

=head2 showBuildProcess
 
  Title    : showBuildProcess
  Usage    : $self->showBuildProcess("testjob", "1");
  Function : Prints the output of a job's build in progress
  Returns  : none
  Args     : named arguments:
           : -job_name => name of the job to check
           : -build_number => job's build number (lastBuild if not specified)
           :
=cut
sub showBuildProcess{
    my ($self, $job_name, $build_number, $result_outpp) = @_;
    #load configuration values
    $self->loadConfiguration();
    my $path = "";
    my $url = "";
    my $response = undef;
    my $start = 0;
    do{
        $path = $self->getJobAndBuildPath($job_name, $build_number);
        $path .= "logText/progressiveText?start=$start";
        $url = $self->getBaseUrl($path);
        $response = $self->requestReponse(GET_REQUEST, $url);
        # check the outcome  
        if ($response->is_success) {     
            print $response->decoded_content;     
            $start += $response->header("X-Text-Size");     
            sleep(5);  
        }
        else 
        {     
            print "Error: " . $response->status_line . "\n";  
        }
    } while ($response->header("X-More-Data") && $response->header("X-More-Data") eq "true");
    
    #set property if $result_outpp is set.
    $self->setProperty($result_outpp, $response->content());
}

=head2 jobAction
 
  Title    : jobAction
  Usage    : $self->jobAction("stop", "testjob", "1");
           : $self->jobAction("disable", "testjob");
           : $self->jobAction("enable", "testjob");
           : $self->jobAction("build", "testjob");
           : $self->jobAction("delete", "testjob");
           : $self->jobAction("rename", "testjob", "", "newName");
           : $self->jobAction("copy", "testjob", "", "testjobCopy");
  Function : Perform a job operation against a jenkins job
  Returns  : http response body from jenkins server
  Args     : named arguments:
           : -action => type of action (build, stop, enable, disable, delete, rename and copy)
           : -job_name => Name of the job
           : -build_number => job's build number (lastBuild if not specified)
           : -new_name => new name of the job (only for copy and rename actions)
           :
=cut
sub jobAction{
    my ($self, $action, $job_name, $build_number, $new_name, $parameters) = @_;
    #load configuration values
    $self->loadConfiguration();
    my $url = $self->createActionUrl($action, $job_name, $build_number, $new_name, $parameters);
    my $response = $self->requestReponse(POST_REQUEST, $url, "1");
    if($response->status_line && $response->status_line ne GOOD_RESPONSE){
        return "Error: ".$response->status_line."\n";
    }
    return $response->content();
}

=head2 createActionUrl
 
  Title    : createActionUrl
  Usage    : $self->createActionUrl("stop", "testjob", "1");
           : $self->createActionUrl("disable", "testjob");
           : $self->createActionUrl("enable", "testjob");
           : $self->createActionUrl("build", "testjob");
           : $self->createActionUrl("delete", "testjob");
           : $self->createActionUrl("rename", "testjob", "", "newName");
           : $self->createActionUrl("copy", "testjob", "", "testjobCopy");
  Function : Returns the required url to perform a jenkins server request according to the $action parameter
  Returns  : an url ready to be sent to the jenkins server
  Args     : named arguments:
           : -action => type of action (build, stop, enable, disable, delete, rename and copy)
           : -job_name => Name of the job
           : -build_number => job's build number (lastBuild if not specified)
           : -new_name => new name of the job (only for copy and rename actions)
           :
=cut
sub createActionUrl{
    my ($self, $action, $job_name, $build_number, $new_name, $parameters) = @_;
    my $path = "";
    #build, stop, enable, disable, delete, rename and copy
    if($action && $action eq "stop"){
        $path = $self->getJobAndBuildPath($job_name, $build_number);
        $path .= "stop";
    }elsif($action && $action eq "rename"){
        $path = "job/$job_name/doRename?newName=$new_name";
    }
    elsif($action && $action ne ""){
        # validate if the build needs to be called with parameters
        if($action eq "build" && $parameters && $parameters ne ""){
            $path = "job/$job_name/buildWithParameters?$parameters";
        }else{
            $path = "job/$job_name/$action";
        }
    }else{
        print "Error: Unknown action: $action\n";
    }

    my $url = $self->getBaseUrl($path);
    return $url;
}

=head2 getJobAndBuildPath
 
  Title    : getJobAndBuildPath
  Usage    : $self->getJobAndBuildPath("testjob", "1");
  Function : create the build and job part of the jenkins server api
  Returns  : a string with information about the job and build number. e.g "job/testjob/1"
  Args     : named arguments:
           : -job_name => name of the job to check
           : -build_number => job's build number (lastBuild if not specified)
           :
=cut
sub getJobAndBuildPath{
    my ($self, $job_name, $build_number) = @_;
    my $path = "job/";
    if($job_name && $job_name ne ""){
        $path .= "$job_name/";
    }
    if($build_number && $build_number ne ""){
        $path .= "$build_number/";
    }else{
        $path .= "lastBuild/";
    }
    return $path;
}

=head2 request
 
  Title    : request
  Usage    : $self->request("http://jenkins/job/911/215/api/xml");
  Function : perform a generic http get request
  Returns  : http response content
  Args     : named arguments:
           : -url => target to the url we want to request
           :
=cut
sub request{
    my ($self, $type, $url) = @_;
    my $request = ($type && $type eq GET_REQUEST) ? HTTP::Request->new(GET => $url): HTTP::Request->new(POST => $url);
    if($self->{user} && $self->{password}){
        $request->authorization_basic($self->{user}, $self->{password});
    }
    print "> Request:\n".$request->as_string;
    my $ua  = LWP::UserAgent->new;
    my $response = $ua->request($request);
    
    # check the outcome
    if ($response->is_success) {  
        return $response->content();
    } else {  
        return "Error: " . $response->status_line . "\n";
    }
}

=head2 requestReponse
 
  Title    : requestReponse
  Usage    : $self->requestReponse("http://jenkins/job/911/215/api/xml");
  Function : perform a generic http get request
  Returns  : http response
  Args     : named arguments:
           : -type => type of request to perform GET_REQUEST or POST_REQUEST
           : -url => target to the url we want to request
           : -print_request => should this method print the request contest? 1/0
=cut
sub requestReponse{
    my ($self, $type, $url, $print_request) = @_;
    my $request = ($type && $type eq GET_REQUEST) ? HTTP::Request->new(GET => $url): HTTP::Request->new(POST => $url);
    if($self->{user} && $self->{password}){
        $request->authorization_basic($self->{user}, $self->{password});
    }
    if($print_request && $print_request eq "1"){
        print "Request:\n".$request->as_string;
    }
    
    my $ua  = LWP::UserAgent->new;
    #return http response
    return $ua->request($request);
}

=head2 setProperty
 
  Title    : setProperty
  Usage    : $self->setProperty("/myjob/result","value");
  Function : sets a commander property if $prop is set
  Returns  : none
  Args     : named arguments:
           : -prop => Property path
           : -value => Value to write in the property
           :
=cut
sub setProperty{
    my ($self, $prop, $value) = @_;
    if($prop && $prop ne ""){
        $self->{ec}->setProperty($prop, $value);
    }
}

=head2 loadConfiguration
 
  Title    : loadConfiguration
  Usage    : $self->loadConfiguration();
  Function : Retrieves configuration content and set class atributes
  Returns  : none
  Args     : named arguments:
           : none
           :
=cut
sub loadConfiguration{
    my ($self) = @_;
  
    my $ec = $self->{ec};    
    my $config_name = $self->{configuration_name};
    
    my %configuration;
  
    my $pluginConfigs = new ElectricCommander::PropDB($ec,"/projects/EC-Jenkins-1.3.0.56335/Jenkins_cfgs");
  
    my %config_row = $pluginConfigs->getRow($config_name);
  
    # Check if configuration exists
    unless(keys(%config_row)) {
        print "Unable to find the credential: $config_name";
        exit 1;
    }
  
    # Get user/password out of credential
    my $xpath = $ec->getFullCredential($config_row{credential});
    if($xpath){
        $configuration{'user'} = $xpath->findvalue("//userName");
        $configuration{'password'} = $xpath->findvalue("//password");
    }
  
    foreach my $c (keys %config_row) {
        #getting all values except the credential that was read previously
        if($c ne "credential"){
            $configuration{$c} = $config_row{$c};
        }
    }
    #set class atributes
    if(%configuration){
        #store the configuration content in class atributes
        $self->{server}     = $configuration{'server'};
        $self->{port}       = $configuration{'port'};
        $self->{user}       = $configuration{'user'};
        $self->{password}   = $configuration{'password'};
    }
}

1;
