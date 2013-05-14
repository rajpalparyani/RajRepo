## Perl Code to implement EC2 API Calls
package main;

$::gDebug = 1;
$|        = 1;

use lib $ENV{COMMANDER_PLUGINS} . '/EC-EC2-2.2.2.57360/agent/lib';
use ElectricCommander;
use ElectricCommander::PropDB;
use Data::Dumper;
use MIME::Base64 qw(encode_base64);
$::gMockData     = false;
$::gMockRegistry = q{};

main();
exit 0;

################################################################################

###########################
##
## Pull credential values from a credential
##
###########################
sub getCredential($) {
    my ($credname) = @_;

    my $jobStepId = $ENV{"COMMANDER_JOBSTEPID"};
    my $xPath = $::CmdrAPI->getFullCredential("$credname", { jobStepId => $jobStepId });
    if (!defined $xPath) {
        my $msg = $::CmdrAPI->getError();
        mesg(0, "Error: retrieving credential $msg \n");
        exit(1);
    }

    # Get user and password from Credential
    my $user = $xPath->findvalue('//credential/userName');
    my $pass = $xPath->findvalue('//credential/password');
    return ($user, $pass);
}

#############################################################################
## extract_keyfile
##
## extract keyfile for commands that retun key contents to STDOUT
##
## args:
##      filename - the name of the file to put the key text in
##      pem      - contents of key file
#############################################################################
sub extract_keyfile($$) {
    my ($filename, $pem) = @_;

    open FILE, ">", $filename or die $!;
    print FILE $pem . "\n";
    close FILE;
    chmod(0700, $filename);
}

####
# generate random digit
#   len - number of digits
####
sub getRandKey($) {
    my ($max) = @_;
    my $r = int(rand($max));
    return $r;
}

###########################
##
## Print a message if it meets debugging level
##
###########################
sub mesg {
    my ($level, $msg) = @_;
    if ($level <= $::gDebug) {
        print $msg;
    }
}

###########################
##
## extract a required
## parameter from options
## die if not found
##
###########################
sub getRequiredParam {
    my ($param, $opts) = @_;
    if ("$param" eq "") {
        mesg(0, "Blank parameter name not allowed in getRequiredParam\n");
        exit 1;
    }
    my $value = $opts->{$param};
    if ("$value" eq "") {
        mesg(0, "Required parameter $param not found.\n");
        exit 1;
    }
    return $value;
}

###########################
##
## extract an optional
## parameter from options
##
##
###########################
sub getOptionalParam {
    my ($param, $opts) = @_;
    my $value = "";
    eval { $value = $opts->{$param}; };
    return $value;
}

###########################
##
## Throw EC2 error
##
##
###########################
sub throwEC2Error {
    my ($ex) = @_;

    if (defined($ex)) {
        require Amazon::EC2::Exception;
        if (ref $ex eq "Amazon::EC2::Exception") {
            mesg(1, "Caught Exception: " . $ex->getMessage() . "\n");
            mesg(1, "Response Status Code: " . $ex->getStatusCode() . "\n");
            mesg(1, "Error Code: " . $ex->getErrorCode() . "\n");
            mesg(1, "Error Type: " . $ex->getErrorType() . "\n");
            mesg(1, "Request ID: " . $ex->getRequestId() . "\n");
            mesg(1, "XML: " . $ex->getXML() . "\n");
        }
        else {
            mesg(0, "An error occurred:\n");
            mesg(0, "$ex\n");
        }
        exit 1;
    }
}

###########################
##
## main program
##
###########################
sub main {
    my $opts;

    $::CmdrAPI = new ElectricCommander();
    $::CmdrAPI->abortOnError(0);

    ## load option list from procedure parameters
    my $x       = $::CmdrAPI->getJobDetails($ENV{COMMANDER_JOBID});
    my $nodeset = $x->find('//actualParameter');
    my $desc;
    foreach my $node ($nodeset->get_nodelist) {
        my $parm = $node->findvalue('actualParameterName');
        my $val  = $node->findvalue('value');
        $opts->{$parm} = "$val";
    }

    # check for required params
    if (!defined $opts->{config} || "$opts->{config}" eq "") {
        mesg(0, "config parameter must exist and be non-blank\n");
        exit 1;
    }

    # check to see if a config with this name exists
    my $proj = "EC-EC2-2.2.2.57360";
    if (substr($proj, 0, 1) eq "@") {
        $proj = "EC-EC2-1.0.0.0";
    }

    # set property table to this cfg list
    my $CfgDB = ElectricCommander::PropDB->new($::CmdrAPI, "/projects/$proj/ec2_cfgs");

    # read values from this config
    $opts->{service_url} = $CfgDB->getCol("$opts->{config}", "service_url");
    $opts->{debug}       = $CfgDB->getCol("$opts->{config}", "debug");

    # if mockdata is non blank, hard coded mock data will be
    # used and no actual calls to EC2 will be made
    if ($CfgDB->getCol("$opts->{config}", "mockdata") ne "") {
        print "MOCK DATA MODE=true\n";
        $::gMockData = true;

        # this property sheet will be used to keep track of
        # things created so getters can find them
        $::gMockRegistry = "/myProject/MOCK_REGISTRY/$opts->{config}";
    }

    if ("$opts->{debug}" ne "") {
        $::gDebug = $opts->{debug};
    }

    # generic propdb for writting results
    $opts->{pdb} = ElectricCommander::PropDB->new($::CmdrAPI, "");

    if ("$opts->{service_url}" eq "") {
        mesg(0, "Error: Configuration $opts->{config} does not specify a service_url\n");
        exit 1;
    }

    # credential uses the same name as the configuration
    ($opts->{AWS_ACCESS_KEY_ID}, $opts->{AWS_SECRET_ACCESS_KEY}) = getCredential("$opts->{config}");

    if ("$opts->{AWS_ACCESS_KEY_ID}" eq "") {
        mesg(0, "Access key not found in credential $opts->{config}\n");
        exit 1;
    }
    mesg(5, "Found credential $opts->{AWS_ACCESS_KEY_ID}\n");

    my $config = {
                   ServiceURL       => "$opts->{service_url}",
                   UserAgent        => "Amazon EC2 Perl Library",
                   SignatureVersion => 2,
                   SignatureMethod  => "HmacSHA256",
                   ProxyHost        => undef,
                   ProxyPort        => -1,
                   MaxErrorRetry    => 3
                 };
    require Amazon::EC2::Client;
    my $service = Amazon::EC2::Client->new($opts->{AWS_ACCESS_KEY_ID}, $opts->{AWS_SECRET_ACCESS_KEY}, $config);

    foreach my $op (keys %{$opts}) {
    if($op eq 'AWS_SECRET_ACCESS_KEY')
    { next;
    }
        mesg(5, "\$opts\-\>\{$op\}=$opts->{$op}\n");
    }

    # ---------------------------------------------------------------
    # Dispatch operation
    # ---------------------------------------------------------------
    if ($::gMockData ne false) {
        $opts->{method} = "MOCK_" . $opts->{method};
    }
    $opts->{method}($opts, $service);

    exit 0;
}

########################################
## individual api methods
########################################
sub API_AllocateIP {
    my ($opts, $service) = @_;

    my $ip = "";
    my $propResult = getRequiredParam("propResult", $opts);

    mesg(1, "--Allocating Amazon EC2 Address -------\n");

    use Amazon::EC2::Model::AllocateAddressRequest;

    my $request = new Amazon::EC2::Model::AllocateAddressRequest();
    eval {
        my $response = $service->allocateAddress($request);
        if ($response->isSetAllocateAddressResult()) {
            my $result = $response->getAllocateAddressResult();
            if ($result->isSetPublicIp()) {
                $ip = $result->getPublicIp();
            }
        }
    };
    if ($@) { throwEC2Error($@); }
    if ("$ip" eq "") {
        mesg(1, "Error allocating IP address.\n");
        exit 1;
    }

    mesg(1, "Address $ip allocated\n");

    ## store new key in properties
    if ("$propResult" ne "") {
        $opts->{pdb}->setProp("$propResult/ip", $ip);
    }
    exit 0;
}

sub MOCK_API_AllocateIP {
    my ($opts, $service) = @_;

    my $ip = "";
    my $propResult = getRequiredParam("propResult", $opts);

    mesg(1, "--Allocating Amazon EC2 Address -------\n");

    my $r1 = getRandKey(255);
    my $r2 = getRandKey(255);
    $ip = "192.168.$r1.$2";
    $opts->{pdb}->setProp("$::gMockRegistry/ElasticIPS", $ip);

    mesg(1, "Address $ip allocated\n");

    ## store new key in properties
    if ("$propResult" ne "") {
        $opts->{pdb}->setProp("$propResult/ip", $ip);
    }

    exit 0;
}

sub API_AssociateIP {
    my ($opts, $service) = @_;

    mesg(1, "--Associate Amazon EC2 Address -------\n");
    my $ip       = getRequiredParam("ip",       $opts);
    my $instance = getRequiredParam("instance", $opts);

    require Amazon::EC2::Model::AssociateAddressRequest;

    my $request = new Amazon::EC2::Model::AssociateAddressRequest({ "InstanceId" => "$instance", "PublicIp" => "$ip" });

    # associate address
    eval {
        my $response = $service->associateAddress($request);
        mesg(3, $response->toXML() . "\n");
    };
    if ($@) { throwEC2Error($@); }
    mesg(1, "Address $ip associated with instance $instance\n");
}

sub MOCK_API_AssociateIP {
    my ($opts, $service) = @_;

    mesg(1, "--Associate Amazon EC2 Address -------\n");
    my $ip       = getRequiredParam("ip",       $opts);
    my $instance = getRequiredParam("instance", $opts);

    $opts->{pdb}->setProp("$::gMockRegistry/IPAssociations/$ip", $instance);

    mesg(1, "Address $ip associated with instance $instance\n");
}

sub API_ReleaseIP {
    my ($opts, $service) = @_;

    mesg(1, "--Releasing Amazon EC2 Address -------\n");

    # see if an IP was passed in
    my $ip = getRequiredParam("ip", $opts);

    use Amazon::EC2::Model::ReleaseAddressRequest;

    my $request = new Amazon::EC2::Model::ReleaseAddressRequest({ "PublicIp" => "$ip" });

    eval { my $response = $service->releaseAddress($request); };
    if ($@) { throwEC2Error($@); }

    mesg(1, "Address $ip released\n");

    exit 0;
}

sub MOCK_API_ReleaseIP {
    my ($opts, $service) = @_;

    mesg(1, "--Releasing Amazon EC2 Address -------\n");

    # see if an IP was passed in
    my $ip = getRequiredParam("ip", $opts);

    # if associated, throw error
    my $instance = $opts->{pdb}->setProp("$::gMockRegistry/IPAssociations/$ip");
    if ($instance ne "") {
        mesg(1, "Caught Exception: " . "IP address in use\n");
    }
    else {

        # else remove
        $opts->{pdb}->deleteProp("$::gMockRegistry/ElasticIPS/$ip");
    }

    mesg(1, "Address $ip released\n");

    exit 0;
}

sub API_AttachVolume {
    my ($opts, $service) = @_;

    mesg(1, "-- Attach Volumes -------\n");

    my $instlist1 = getRequiredParam("instances", $opts);
    my $vollist1  = getRequiredParam("volumes",   $opts);
    my $device    = getRequiredParam("device",    $opts);

    my $attachCount = 0;

    my @instlist = split(/;/, $instlist1);
    my @vollist  = split(/;/, $vollist1);

    if (scalar(@vollist) == 0) {
        mesg(1, "--No volumes to attach\n");
        exit 0;
    }

    # pair instances with volumes in order
    my %vol_id_match;
    my $i = 0;
    foreach (@instlist) {
        my $instance_id = $_;
        $vol_id_match{$instance_id} = @vollist[$i];
        $i++;
    }

    my $done = 0;
    my %doneList;
    while (!$done) {
        $done = 1;
        foreach (@instlist) {
            my $instance_id = $_;
            my $vol_id      = $vol_id_match{$instance_id};
            my $status      = "";

            if ("$vol_id" eq "") { next; }

            if (!$doneList{$vol_id}) {

                # at least one more to process
                $done = 0;

                mesg(1, "Attaching $vol_id to instance $instance_id\n");

                # check to make sure volume is in available state
                eval {
                    use Amazon::EC2::Model::DescribeVolumesRequest;
                    my $request = new Amazon::EC2::Model::DescribeVolumesRequest({ "VolumeId" => "$vol_id" });
                    my $response = $service->describeVolumes($request);
                    if ($response->isSetDescribeVolumesResult()) {
                        my $result  = $response->getDescribeVolumesResult();
                        my $volumes = $result->getVolume();

                        # should only be one row in result
                        foreach (@$volumes) {
                            my $vol = $_;
                            $status = $vol->getStatus();
                        }
                    }
                };
                if ($@) { throwEC2Error($@); }

                mesg(1, "Volume $vol_id is in state $status\n");
                if ("$status" eq "available") {

                    ## associate volume to instance
                    use Amazon::EC2::Model::AttachVolumeRequest;
                    mesg(1, "Trying to attach $vol_id to $instance_id\n");
                    eval {
                        my $request = new Amazon::EC2::Model::AttachVolumeRequest(
                                                                                  {
                                                                                    "VolumeId"   => "$vol_id",
                                                                                    "InstanceId" => "$instance_id",
                                                                                    "Device"     => "$device"
                                                                                  }
                                                                                 );
                        my $response = $service->attachVolume($request);
                    };
                    if ($@) { throwEC2Error($@); }

                    mesg(1, "Volume $vol_id attached to instance $instance_id\n");
                    $doneList{$vol_id} = 1;
                    $attachCount++;
                }
            }
            else {
                mesg(1, "Volume $vol_id already attached to $instance_id.\n");
            }
        }
        sleep(10);
    }

    mesg(1, "$attachCount volumes were attached to instances.\n");
    exit 0;
}

sub MOCK_API_AttachVolume {
    my ($opts, $service) = @_;

    mesg(1, "-- Attach Volumes -------\n");

    my $instlist1 = getRequiredParam("instances", $opts);
    my $vollist1  = getRequiredParam("volumes",   $opts);
    my $device    = getRequiredParam("device",    $opts);

    my $attachCount = 0;

    my @instlist = split(/;/, $instlist1);
    my @vollist  = split(/;/, $vollist1);

    if (scalar(@vollist) == 0) {
        mesg(1, "--No volumes to attach\n");
        exit 0;
    }

    # pair instances with volumes in order
    my %vol_id_match;
    my $i = 0;
    foreach (@instlist) {
        my $instance_id = $_;
        $vol_id_match{$instance_id} = @vollist[$i];
        $i++;
    }

    my $done = 0;
    my %doneList;
    while (!$done) {
        $done = 1;
        foreach (@instlist) {
            my $instance_id = $_;
            my $vol_id      = $vol_id_match{$instance_id};
            my $status      = "";

            if ("$vol_id" eq "") { next; }

            if (!$doneList{$vol_id}) {

                # at least one more to process
                $done = 0;

                mesg(1, "Attaching $vol_id to instance $instance_id\n");

                $status = "available";

                mesg(1, "Volume $vol_id is in state $status\n");
                if ("$status" eq "available") {
                    mesg(1, "Attaching $vol_id to instance $instance_id\n");
                    $opts->{pdb}->setProp("$::gMockRegistry/Instances/$instance_id/volume", "$vol_id");
                    $opts->{pdb}->setProp("$::gMockRegistry/Instances/$instance_id/device", "$device");
                    $opts->{pdb}->setProp("$::gMockRegistry/Volumes/$vol_id/instance",      $instance_id);

                    mesg(1, "Volume $vol_id attached to instance $instance_id\n");
                    $doneList{$vol_id} = 1;
                    $attachCount++;
                }
            }
            else {
                mesg(1, "Volume $vol_id already attached to $instance_id.\n");
            }
        }
        sleep(10);
    }

    mesg(1, "$attachCount volumes were attached to instances.\n");
    exit 0;
}

sub API_CreateKeyPair {
    my ($opts, $service) = @_;

    mesg(1, "--Creating Amazon EC2 KeyPair -------\n");

    my $newkeyname = getRequiredParam("keyname",    $opts);
    my $propResult = getRequiredParam("propResult", $opts);
    my $pem;

    use Amazon::EC2::Model::CreateKeyPairRequest;

    mesg(1, "Create request...\n");
    my $request = new Amazon::EC2::Model::CreateKeyPairRequest({ "KeyName" => "$newkeyname" });

    eval {

        #mesg(5, Data::Dumper->Dumper([$request]));
        my $response = $service->createKeyPair($request);
        if ($response->isSetCreateKeyPairResult()) {
            my $result = $response->getCreateKeyPairResult();
            if ($result->isSetKeyPair()) {
                my $pair = $result->getKeyPair();
                $pem = $pair->getKeyMaterial();
            }
        }
    };
    if ($@) { throwEC2Error($@); }

    ## store new key in properties
    if ("$propResult" ne "") {
        $opts->{pdb}->setProp($propResult . "/KeyPairId", $newkeyname);
    }

    ## extract private key from results
    extract_keyfile($newkeyname . ".pem", $pem);
    mesg(1, "KeyPair $newkeyname created\n");
    exit 0;
}

sub MOCK_API_CreateKeyPair {
    my ($opts, $service) = @_;

    mesg(1, "--Creating Amazon EC2 KeyPair -------\n");

    my $newkeyname = getRequiredParam("keyname",    $opts);
    my $propResult = getRequiredParam("propResult", $opts);
    my $pem;

    $pem = "lalalala";
    $opts->{pdb}->setProp($::gMockRegistry . "/Keypairs/$newkeyname", "created");

    ## store new key in properties
    if ("$propResult" ne "") {
        $opts->{pdb}->setProp("$propResult/KeyPairId", $newkeyname);
    }

    ## extract private key from results
    extract_keyfile($newkeyname . ".pem", $pem);
    mesg(1, "KeyPair $newkeyname created\n");
    exit 0;
}

sub API_DeleteKeyPair {
    my ($opts, $service) = @_;

    mesg(1, "--Deleting Amazon EC2 KeyPair -------\n");

    # see if a key was created for this tag
    my $keynames = getRequiredParam("keyname", $opts);
    my @keylist = split(/;/, "$keynames");
    foreach my $keyname (@keylist) {
        use Amazon::EC2::Model::DeleteKeyPairRequest;
        my $request = new Amazon::EC2::Model::DeleteKeyPairRequest({ "KeyName" => "$keyname" });

        eval { my $response = $service->deleteKeyPair($request); };
        if ($@) { throwEC2Error($@); }
        mesg(1, "KeyPair $keyname deleted\n");
    }
    exit 0;
}

sub MOCK_API_DeleteKeyPair {
    my ($opts, $service) = @_;

    mesg(1, "--Deleting Amazon EC2 KeyPair -------\n");

    # see if a key was created for this tag
    my $keynames = getRequiredParam("keyname", $opts);
    my @keylist = split(/;/, "$keynames");
    foreach my $keyname (@keylist) {
        $opts->{pdb}->deleteProp("$::gMockData/Keypairs/$keyname");
        mesg(1, "KeyPair $keyname deleted\n");
    }
    exit 0;
}

sub CreateVolume {
    my ($opts, $service) = @_;

    mesg(1, "-- Create Volume  -------\n");
    my $snap_id = $opts->{snapshot};
    my $propResult = getRequiredParam("propResult", $opts);
    if ("$snap_id" eq "") {
        mesg(0, "No snapshots to process.\n");
        $opts->{pdb}->setProp("$propResult/VolumeList", "");
        exit 0;
    }

    my $propResult = getRequiredParam("propResult", $opts);
    $opts->{pdb}->setProp("$propResult/Snapshot", "$snap_id");

    ## now make a new volume out of the snapshot
    use Amazon::EC2::Model::CreateVolumeRequest;
    use Amazon::EC2::Model::DescribeVolumesRequest;

    # get list of instances
    my $instListProp = $opts->{pdb}->getProp("$propResult/InstanceList");
    my @instList = split(/;/, $instListProp);

    my %volsCreated;
    my $vollist = "";
    foreach (@instList) {
        my $id     = $_;
        my $newvol = "";
        eval {
            my $actualZone = $opts->{pdb}->getProp("$propResult/Instance-$id/Zone");
            mesg(1, "Creating volume from snapshot in zone $actualZone\n");

            my $request = new Amazon::EC2::Model::CreateVolumeRequest(
                                                                      {
                                                                        "SnapshotId"       => "$snap_id",
                                                                        "AvailabilityZone" => "$actualZone"
                                                                      }
                                                                     );
            my $response = $service->createVolume($request);

            # get volume id
            if ($response->isSetCreateVolumeResult()) {
                $result = $response->getCreateVolumeResult();
                my $vol = $result->getVolume();
                $newvol = $vol->getVolumeId();

                mesg(1, "New volume $newvol created from snapshot $snap_id for instance $id\n");
                $opts->{pdb}->setProp("$propResult/Instance-$id/NewVolume", $newvol);
                if ("$vollist" ne "") { $vollist .= ";"; }
                $vollist .= $newvol;
                $volsCreated{$newvol} = 1;
            }
        };
        if ($@) { throwEC2Error($@); }
    }

    ## wait for snapshots to be ready
    my $done   = 0;
    my $status = "pending";
    while (!$done) {
        $done = 1;
        sleep 10;
        eval {
            mesg(1, "Waiting for volume $newvol\n");
            my $request  = new Amazon::EC2::Model::DescribeVolumesRequest();
            my $response = $service->describeVolumes($request);
            if ($response->isSetDescribeVolumesResult()) {
                my $result  = $response->getDescribeVolumesResult();
                my $volumes = $result->getVolume();
                foreach (@$volumes) {
                    my $vol = $_;
                    $status = $vol->getStatus();
                    $id     = $vol->getVolumeId;
                    if ($volsCreated{$id} and $status ne "available") {
                        $done = 0;
                    }
                }
            }
        };
        if ($@) { throwEC2Error($@); }
    }
    $opts->{pdb}->setProp("$propResult/VolumeList", $vollist);
    mesg(1, "Snapshot $snap_id used to create volumes\n");
    exit 0;
}

sub MOCK_CreateVolume {
    my ($opts, $service) = @_;

    mesg(1, "-- Create Volume  -------\n");
    my $snap_id = $opts->{snapshot};
    my $propResult = getRequiredParam("propResult", $opts);
    if ("$snap_id" eq "") {
        mesg(0, "No snapshots to process.\n");
        $opts->{pdb}->setProp("$propResult/VolumeList", "");
        exit 0;
    }

    my $propResult = getRequiredParam("propResult", $opts);
    $opts->{pdb}->setProp("$propResult/Snapshot", "$snap_id");

    # get list of instances
    my $instListProp = $opts->{pdb}->getProp("$propResult/InstanceList");
    my @instList = split(/;/, $instListProp);

    my %volsCreated;
    my $vollist = "";
    foreach (@instList) {
        my $id     = $_;
        my $v      = getRandKey(9999999);
        my $newvol = "vol-$v";
        mesg(1, "New volume $newvol created from snapshot $snap_id for instance $id\n");
        $opts->{pdb}->setProp("$propResult/Instance-$id/NewVolume", $newvol);

        $opts->{pdb}->setProp("$::gMockRegistry/Volumes/$newvol/state", "created");
        if ("$vollist" ne "") { $vollist .= ";"; }
        $vollist .= $newvol;
    }
    $opts->{pdb}->setProp("$propResult/VolumeList", $vollist);
    mesg(1, "Snapshot $snap_id used to create volumes\n");
    exit 0;
}

sub SnapVolume {
    my ($opts, $service) = @_;

    mesg(1, "-- Snapping Volume -------\n");

    my $vol        = getRequiredParam("volume",     $opts);
    my $instance   = getRequiredParam("instance",   $opts);
    my $propResult = getRequiredParam("propResult", $opts);
    my $snap_id    = "";

    if ("$vol" eq "") {
        mesg(0, "Volume parameter is blank.\n");
        exit 0;
    }
    if ("$instance" eq "") {
        mesg(0, "Instance parameter is blank.\n");
        exit 0;
    }

    ## double check that the volume is attached to the instance
    eval {
        use Amazon::EC2::Model::DescribeVolumesRequest;
        my $request = new Amazon::EC2::Model::DescribeVolumesRequest({ "VolumeId" => "$vol" });
        my $response = $service->describeVolumes($request);
        if ($response->isSetDescribeVolumesResult()) {
            my $result  = $response->getDescribeVolumesResult();
            my $volumes = $result->getVolume();

            # should only be one row in result
            foreach (@$volumes) {
                my $volume      = $_;
                my $attachments = $volume->getAttachment();
                foreach (@$attachments) {
                    my $attach = $_;
                    my $id     = $attach->getInstanceId();
                    if ("$id" ne "$instance") {
                        mesg(0, "Volume $vol was not attached to instance $instance ($id).\n");
                        exit 1;
                    }
                }
            }
        }
    };
    if ($@) { throwEC2Error($@); }

    ## create a snapshot from volume
    eval {
        use Amazon::EC2::Model::CreateSnapshotRequest;
        my $request = new Amazon::EC2::Model::CreateSnapshotRequest({ "VolumeId" => "$vol" });
        my $response = $service->createSnapshot($request);
        if ($response->isSetCreateSnapshotResult()) {
            my $result   = $response->getCreateSnapshotResult();
            my $snapshot = $result->getSnapshot();
            $snap_id = $snapshot->getSnapshotId();
        }
    };
    if ($@) { throwEC2Error($@); }
    mesg(1, "Created new snapshot $snap_id\n");

    # return new snapid
    $opts->{pdb}->setProp("$propResult/NewSnapshot", $snap_id);
    exit 0;
}

sub MOCK_SnapVolume {
    my ($opts, $service) = @_;

    mesg(1, "-- Snapping Volume -------\n");

    my $vol        = getRequiredParam("volume",     $opts);
    my $instance   = getRequiredParam("instance",   $opts);
    my $propResult = getRequiredParam("propResult", $opts);
    my $snap_id    = "";

    if ("$vol" eq "") {
        mesg(0, "Volume parameter is blank.\n");
        exit 0;
    }
    if ("$instance" eq "") {
        mesg(0, "Instance parameter is blank.\n");
        exit 0;
    }

    my $r = getRandKey(9999999);
    $snap_id = "snap-$r";
    $opts->{pdb}->setProp("$::gMockRegistry/Snapshots/$snap_id", "created");
    mesg(1, "Created new snapshot $snap_id\n");

    # return new snapid
    $opts->{pdb}->setProp("$propResult/NewSnapshot", $snap_id);
    exit 0;
}

sub API_DescribeInstances {
    my ($opts, $service) = @_;

    mesg(1, "-- Describe Instances -------\n");

    # possible states
    #   pending
    #   running
    #   shutting-down
    #   terminated
    #   stopping
    #   stopped
    #
    # answer will be in form $resultHash->{instance} = state;
    my $resultHash;

    # instances can be of 2 forms
    # 1-  a single instance i-1232
    # 2 - a list of instances i-1232;i-4566
    my $reservation = getRequiredParam("instances",  $opts);
    my $propResult  = getRequiredParam("propResult", $opts);

    @instances = split(/;/, $reservation);
    mesg(1, " found " . scalar(@instances) . " in instance list $reservation\n");

    foreach my $instanceName (@instances) {
        mesg(2, " describing $instanceName\n");

        eval {
            my $request = new Amazon::EC2::Model::DescribeInstancesRequest({ "InstanceId" => "$instanceName" });
            my $response = $service->describeInstances($request);
            if ($response->isSetDescribeInstancesResult()) {
                my $result  = $response->getDescribeInstancesResult();
                my $resList = $result->getReservation();
                foreach (@$resList) {
                    my $res   = $_;
                    my $resId = $res->getReservationId();
                    $instanceList = $res->getRunningInstance();
                    foreach (@$instanceList) {
                        my $instance = $_;
                        my $id       = $instance->getInstanceId();
                        my $stateObj = $instance->getInstanceState();
                        $resultHash->{$instanceName}{state}  = $stateObj->getName();
                        $resultHash->{$instanceName}{image}  = $instance->getImageId();
                        $resultHash->{$instanceName}{prvdns} = $instance->getPrivateDnsName();
                        $resultHash->{$instanceName}{pubdns} = $instance->getPublicDnsName();
                        $resultHash->{$instanceName}{key}    = $instance->getKeyName();
                        $resultHash->{$instanceName}{type}   = $instance->getInstanceType();
                        $resultHash->{$instanceName}{launch} = $instance->getLaunchTime();
                        my $placement = $instance->getPlacement();
                        $resultHash->{$instanceName}{zone} = $placement->getAvailabilityZone();
                    }
                }
            }
        };
        # dont die on error...
        if ($@) { 
            require Amazon::EC2::Exception;
            if (ref $@ eq "Amazon::EC2::Exception") {
                mesg(1, "Caught Exception: " . $@->getMessage() . "\n");
                mesg(1, "Response Status Code: " . $@->getStatusCode() . "\n");
                mesg(1, "Error Code: " . $@->getErrorCode() . "\n");
                mesg(1, "Error Type: " . $@->getErrorType() . "\n");
                mesg(1, "Request ID: " . $@->getRequestId() . "\n");
                mesg(1, "XML: " . $@->getXML() . "\n");
            }
            else {
                mesg(0, "An error occurred:\n");
                mesg(0, "$@\n");
            }
            # send back results that it is stopped
            $resultHash->{$instanceName}{state}  = "stopped";
        }
    }
    my $xml = "<DescribeResponse>";
    foreach my $i (keys %{$resultHash}) {
        $xml .= "  <instance>\n";
        $xml .= "    <id>$i</id>\n";
        foreach my $p (keys %{ $resultHash->{$i} }) {
            $xml .= "    <$p>" . $resultHash->{$i}{$p} . "</$p>\n";
        }
        $xml .= "  </instance>\n";
    }
    $xml .= "</DescribeResponse>\n";
    $opts->{pdb}->setProp("$propResult/describe", $xml);
}

sub MOCK_API_DescribeInstances {
    my ($opts, $service) = @_;

    mesg(1, "-- Describe Instances -------\n");

    # possible states
    #   pending
    #   running
    #   shutting-down
    #   terminated
    #   stopping
    #   stopped
    #
    # answer will be in form $resultHash->{instance} = state;
    my $resultHash;

    # instances can be of 2 forms
    # 1-  a single instance i-1232
    # 2 - a list of instances i-1232;i-4566
    my $reservation = getRequiredParam("instances",  $opts);
    my $propResult  = getRequiredParam("propResult", $opts);

    @instances = split(/;/, $reservation);
    mesg(1, " found " . scalar(@instances) . " in instance list $reservation\n");

    foreach my $instanceName (@instances) {
        mesg(2, " describing $instanceName\n");
        $resultHash->{$instanceName}{state} = $opts->{pdb}->getProp("$::gMockRegistry/Instances/$instanceName/state");
        if ($resultHash->{$instanceName}{state} eq "") {
            $resultHash->{$instanceName}{state} = "terminated";
        }
        $resultHash->{$instanceName}{image}  = $opts->{pdb}->getProp("$::gMockRegistry/Instances/$instanceName/image");
        $resultHash->{$instanceName}{pvrdns} = $opts->{pdb}->getProp("$::gMockRegistry/Instances/$instanceName/pvrdns");
        $resultHash->{$instanceName}{pubdns} = $opts->{pdb}->getProp("$::gMockRegistry/Instances/$instanceName/pubdns");
        $resultHash->{$instanceName}{key}    = $opts->{pdb}->getProp("$::gMockRegistry/Instances/$instanceName/key");
        $resultHash->{$instanceName}{type}   = $opts->{pdb}->getProp("$::gMockRegistry/Instances/$instanceName/type");
        $resultHash->{$instanceName}{launch} = $opts->{pdb}->getProp("$::gMockRegistry/Instances/$instanceName/launch");
        $resultHash->{$instanceName}{zone}   = $opts->{pdb}->getProp("$::gMockRegistry/Instances/$instanceName/zone");
        $resultHash->{$instanceName}{volue}  = $opts->{pdb}->getProp("$::gMockRegistry/Instances/$instanceName/volume");
    }
    my $xml = "<DescribeResponse>";
    foreach my $i (keys %{$resultHash}) {
        $xml .= "  <instance>\n";
        $xml .= "    <id>$i</id>\n";
        foreach my $p (keys %{ $resultHash->{$i} }) {
            $xml .= "    <$p>" . $resultHash->{$i}{$p} . "</$p>\n";
        }
        $xml .= "  </instance>\n";
    }
    $xml .= "</DescribeResponse>\n";
    $opts->{pdb}->setProp("$propResult/describe", $xml);
}

sub API_DeleteVol {
    my ($opts, $service) = @_;

    mesg(1, "-- Delete Dynamic Volume -------\n");

    my $volumes = $opts->{volumes};
    my @volumeList = split(/;/, "$volumes");
    if (@volumeList == 0) {
        mesg(1, "No volumes to delete.\n");
        exit 0;
    }

    my $detachOnly = getRequiredParam("detachOnly", $opts);

    use Amazon::EC2::Model::DescribeVolumesRequest;
    use Amazon::EC2::Model::DeleteVolumeRequest;
    use Amazon::EC2::Model::DetachVolumeRequest;

    my $delCount = 0;
    my $detCount = 0;

    foreach (@volumeList) {
        my $vol_id = $_;

        mesg(1, "Deleting Volume $vol_id\n");

        # loop until volume available
        # either it completes or the step times out...
        my $status = "";
        while (1) {
            eval {
                my $request = new Amazon::EC2::Model::DescribeVolumesRequest({ "VolumeId" => "$vol_id" });
                my $response = $service->describeVolumes($request);
                if ($response->isSetDescribeVolumesResult()) {
                    my $result  = $response->getDescribeVolumesResult();
                    my $volumes = $result->getVolume();

                    # should only be one row in result
                    foreach (@$volumes) {
                        my $vol = $_;
                        $status = $vol->getStatus();
                    }
                }
            };
            if ($@) { throwEC2Error($@); }
            mesg(1, "Found status=[$status]\n");
            if ($status eq "available") {
                last;
            }
            if ($status eq "terminated" or $status eq "deleting") {
                mesg(1, "Error detaching volume $vol_id\n");
                exit 1;
            }
            if ($status eq "in-use") {
                mesg(1, "Trying to detach $vol_id\n");
                eval {
                    my $request = new Amazon::EC2::Model::DetachVolumeRequest({ "VolumeId" => "$vol_id" });
                    my $response = $service->detachVolume($request);
                };
                if ($@) {
                    throwEC2Error($@);
                    $status = "busy";
                }
                $detCount++;
                mesg(1, "Volume $vol_id detached\n");
            }
            mesg(1, "Waiting for volume $vol_id to be in available state\n");
            sleep 10;
        }
        if (!$detachOnly) {
            ## delete volume
            eval {
                mesg(1, "Deleting volume $vol_id\n");
                my $request = new Amazon::EC2::Model::DeleteVolumeRequest({ "VolumeId" => "$vol_id" });
                my $response = $service->deleteVolume($request);
            };
            if ($@) { throwEC2Error($@); }
            mesg(1, "Volume $vol_id deleted\n");
            $delCount++;
        }

    }

    if (!$detachOnly) {
        mesg(1, "$delCount volumes deleted.\n");
    }
    else {
        mesg(1, "$detCount volumes detached.\n");
    }
    exit 0;
}

sub MOCK_API_DeleteVol {
    my ($opts, $service) = @_;

    mesg(1, "-- Delete Dynamic Volume -------\n");

    my $volumes = $opts->{volumes};
    my @volumeList = split(/;/, "$volumes");
    if (@volumeList == 0) {
        mesg(1, "No volumes to delete.\n");
        exit 0;
    }

    my $detachOnly = getRequiredParam("detachOnly", $opts);

    use Amazon::EC2::Model::DescribeVolumesRequest;
    use Amazon::EC2::Model::DeleteVolumeRequest;
    use Amazon::EC2::Model::DetachVolumeRequest;

    my $delCount = 0;
    my $detCount = 0;

    foreach (@volumeList) {
        my $vol_id = $_;

        mesg(1, "Deleting Volume $vol_id\n");

        # if volume attached
        my $instance = $opts->{pdb}->getProp("$::gMockRegistry/Volumes/$vol_id/instance");

        if ($instance ne "") {
            mesg(1, "Trying to detach $vol_id\n");
            $opts->{pdb}->setProp("$::gMockRegistry/Volumes/$vol_id/instance",   "");
            $opts->{pdb}->setProp("$::gMockRegistry/Instances/$instance/volume", "");
            $detCount++;
            mesg(1, "Volume $vol_id detached\n");
        }

        if (!$detachOnly) {
            ## delete volume
            $opts->{pdb}->delRow("$::gMockRegistry/Volumes/$vol_id");
            mesg(1, "Volume $vol_id deleted\n");
            $delCount++;
        }

    }

    if (!$detachOnly) {
        mesg(1, "$delCount volumes deleted.\n");
    }
    else {
        mesg(1, "$detCount volumes detached.\n");
    }
    exit 0;
}

sub API_Start {
    my ($opts, $service) = @_;

    mesg(1, "--Start Amazon EC2 Instance -------\n");
    use ElectricCommander;

    my $instance = getRequiredParam("instance", $opts);

    ## start EBS instance
    use Amazon::EC2::Model::StartInstancesRequest;
    use Amazon::EC2::Model::StartInstancesResponse;
    use Amazon::EC2::Model::StartInstancesResult;

    mesg(1, "Starting instance\n");

    eval {

        my $request = new Amazon::EC2::Model::StartInstancesRequest({ "InstanceId" => "$instance", });
        my $response = $service->startInstances($request);
    };
    if ($@) { throwEC2Error($@); }
    exit 0;
}

sub MOCK_API_Start {
    my ($opts, $service) = @_;

    mesg(1, "--Start Amazon EC2 Instance -------\n");
    use ElectricCommander;

    my $instance = getRequiredParam("instance", $opts);

    ## start EBS instance
    use Amazon::EC2::Model::StartInstancesRequest;
    use Amazon::EC2::Model::StartInstancesResponse;
    use Amazon::EC2::Model::StartInstancesResult;

    mesg(1, "Starting instance\n");

    $opts->{pdb}->setProp("$::gMockRegistry/Instances/$instance/state", "running");
    exit 0;
}

sub API_Stop {
    my ($opts, $service) = @_;

    mesg(1, "--Stop Amazon EC2 Instance -------\n");

    my $instance = getRequiredParam("instance", $opts);

    ## stop EBS instance
    use Amazon::EC2::Model::StopInstancesRequest;
    use Amazon::EC2::Model::StopInstancesResponse;
    use Amazon::EC2::Model::StopInstancesResult;

    mesg(1, "Stopping instance\n");

    eval {

        my $request = new Amazon::EC2::Model::StopInstancesRequest({ "InstanceId" => "$instance", });
        my $response = $service->stopInstances($request);
    };
    if ($@) { throwEC2Error($@); }
    exit 0;

}

sub MOCK_API_Stop {
    my ($opts, $service) = @_;

    mesg(1, "--Stop Amazon EC2 Instance -------\n");

    my $instance = getRequiredParam("instance", $opts);

    ## stop EBS instance
    use Amazon::EC2::Model::StopInstancesRequest;
    use Amazon::EC2::Model::StopInstancesResponse;
    use Amazon::EC2::Model::StopInstancesResult;

    mesg(1, "Stopping instance\n");

    $opts->{pdb}->setProp("$::gMockRegistry/Instances/$instance/state", "stopped");
    exit 0;
}

sub API_TerminateInstances {
    my ($opts, $service) = @_;

    mesg(1, "--Terminate Amazon EC2 Instance -------\n");
    my $id = getRequiredParam("id", $opts);
    my $resources = getOptionalParam("resources", $opts);

    ## terminate instance
    use Amazon::EC2::Model::TerminateInstancesRequest;
    use Amazon::EC2::Model::DescribeInstancesRequest;
    my $termCount = 0;
    my @list = getInstanceList($id, $service);
    foreach (@list) {
        my $id = $_;

        mesg(1, "Terminating instance $id\n");
        eval {
            my $request = new Amazon::EC2::Model::TerminateInstancesRequest({ "InstanceId" => "$id" });
            my $response = $service->terminateInstances($request);
        };
        if ($@) { throwEC2Error($@); }
        $termCount++;
    }
    mesg(1, "$termCount instances terminated.\n");

    mesg(1, "Deleting resources.\n");
    my @rlist = split(/;/, $resources);
    foreach my $r (@rlist) {
        deleteResource($opts, $r);
    }
    exit 0;
}

sub MOCK_API_TerminateInstances {
    my ($opts, $service) = @_;

    mesg(1, "--Terminate Amazon EC2 Instance -------\n");
    my $id = getRequiredParam("id", $opts);
    my $resources = getOptionalParam("resources", $opts);

    ## terminate instance
    my $termCount = 0;
    my @list = getInstanceList($id, $service);
    foreach (@list) {
        my $id = $_;
        mesg(1, "Terminating instance $id\n");
        $opts->{pdb}->setProp("$::gMockRegistry/Instances/$id/state", "terminated");
        $termCount++;
    }
    mesg(1, "$termCount instances terminated.\n");

    mesg(1, "Deleting resources.\n");
    my @rlist = split(/;/, $resources);
    foreach my $r (@rlist) {
        deleteResource($opts, $r);
    }
    exit 0;
}

#
# Instance list can be in one of three forms:
#    reservation of the form r-xxxxxx
#    instance of the form i-xxxxx
#    instance list of the form i-xxxx;i-xxxxx;i-xxxxxx
#
sub getInstanceList($$) {
    my ($resIn, $service) = @_;

    my @list;

    # if first letter of id is "r" then we want to terminate all instances of reservation.
    # If the first letter is "i" then we only want to terminate a specific instance or list of instances
    if ($resIn =~ m/i-/) {
        @list = split(/;/, $resIn);
        return @list;
    }

    # otherwise make a list of each instance in the reservation
    eval {
        my $request = new Amazon::EC2::Model::DescribeInstancesRequest({ "ReservationId" => "$reservation" });
        my $response = $service->describeInstances($request);

        if ($response->isSetDescribeInstancesResult()) {
            my $result  = $response->getDescribeInstancesResult();
            my $resList = $result->getReservation();
            foreach (@$resList) {
                my $res   = $_;
                my $resId = $res->getReservationId();

                # if instance not in reservation
                if ("$resId" ne "$resIn") { next; }
                $instanceList = $res->getRunningInstance();
                foreach (@$instanceList) {
                    my $instance = $_;
                    my $id       = $instance->getInstanceId();
                    push(@list, $id);
                }
            }
        }
    };
    if ($@) { throwEC2Error($@); }
    return @list;
}

#
# Instance list can be in one of three forms:
#    reservation of the form r-xxxxxx
#    instance of the form i-xxxxx
#    instance list of the form i-xxxx;i-xxxxx;i-xxxxxx
#
sub MOCK_getInstanceList($$) {
    my ($resIn, $service) = @_;

    my @list;

    # if first letter of id is "r" then we want to terminate all instances of reservation.
    # If the first letter is "i" then we only want to terminate a specific instance or list of instances
    if ($resIn =~ m/i-/) {
        @list = split(/;/, $resIn);
        return @list;
    }

    print "Mock Data mode does not support reservations...\n";
    exit 1;
}

sub DeregisterInstance {
    my ($opts, $service) = @_;

    mesg(1, "--Deregister Amazon EC2 Windows Instance -------\n");

    use Amazon::EC2::Model::DeregisterImageRequest;

    my $ami = getRequiredParam("ami", $opts);

    my $request = new Amazon::EC2::Model::DeregisterImageRequest({ "ImageId" => "$ami" });

    eval { my $response = $service->deregisterImage($request); };
    if ($@) { throwEC2Error($@); }
    mesg(1, "AMI $ami deregistered.\n");
    exit 0;
}

sub MOCK_DeregisterInstance {
    my ($opts, $service) = @_;

    mesg(1, "--Deregister Amazon EC2 Windows Instance -------\n");

    use Amazon::EC2::Model::DeregisterImageRequest;

    my $ami = getRequiredParam("ami", $opts);
    $opts->{pdb}->delRow("$::gMockRegistry/Images/$ami");

    mesg(1, "AMI $ami deregistered.\n");
    exit 0;
}

sub CreateImage {
    my ($opts, $service) = @_;

    mesg(1, "--Create EBS Image from existing EBS image -------\n");

    use Amazon::EC2::Model::CreateImageRequest;

    my $instance   = getRequiredParam("instance",   $opts);
    my $name       = getRequiredParam("name",       $opts);
    my $desc       = getRequiredParam("desc",       $opts);
    my $noreboot   = getRequiredParam("noreboot",   $opts);
    my $propResult = getRequiredParam("propResult", $opts);
    my $newami     = "";

    my $request = new Amazon::EC2::Model::CreateImageRequest(
                                                             {
                                                               "InstanceId"  => "$instance",
                                                               "Name"        => "$name",
                                                               "Description" => "$desc",
                                                               "NoReboot"    => "$noreboot"
                                                             }
                                                            );

    eval {
        my $response = $service->createImage($request);
        if ($response->isSetCreateImageResult()) {
            my $result = $response->getCreateImageResult();
            $newami = $result->getImageId();
        }
    };
    if ($@) { throwEC2Error($@); }
    mesg(1, "CreateImage returned new AMI=$newami\n");

    # loop until new instance has been created
    require Amazon::EC2::Model::DescribeImagesRequest;
    require Amazon::EC2::Model::DescribeImagesResponse;
    require Amazon::EC2::Model::DescribeImagesResult;

    my $state = "";
    while ("$state" eq "pending" || "$state" eq "") {
        if ("$state" ne "") {
            sleep(30);
        }
        eval {
            my $request = new Amazon::EC2::Model::DescribeImagesRequest({ "ImageId" => "$newami" });
            my $response = $service->describeImages($request);

            if ($response->isSetResponseMetadata()) {
                my $responseMetadata = $response->getResponseMetadata();
            }
            if ($response->isSetDescribeImagesResult()) {
                my $describeImagesResult = $response->getDescribeImagesResult();
                my $imageList            = $describeImagesResult->getImage();
                foreach (@$imageList) {
                    my $image = $_;
                    $state = $image->getImageState();
                }
            }
        };
        if ($@) { throwEC2Error($@); }
        mesg(1, "AMI $newami state is $state\n");
    }
    mesg(1, "Image $newami created.\n");
    $opts->{pdb}->setProp("$propResult/NewAMI",  $newami);
    $opts->{pdb}->setProp("$propResult/NewName", $name);
    exit 0;
}

sub MOCK_CreateImage {
    my ($opts, $service) = @_;

    mesg(1, "--Create EBS Image from existing EBS image -------\n");

    my $instance   = getRequiredParam("instance",   $opts);
    my $name       = getRequiredParam("name",       $opts);
    my $desc       = getRequiredParam("desc",       $opts);
    my $noreboot   = getRequiredParam("noreboot",   $opts);
    my $propResult = getRequiredParam("propResult", $opts);
    my $newami     = "";

    my $r = getRandKey(999999);
    $newami = "ami-$r";
    $opts->{pdb}->setProp("$::gMockRegistry/Images/$newami", "created");
    mesg(1, "Image $newami created.\n");
    $opts->{pdb}->setProp("$propResult/NewAMI",  $newami);
    $opts->{pdb}->setProp("$propResult/NewName", $name);
    exit 0;
}

sub API_RunInstance {
    my ($opts, $service) = @_;

    mesg(1, "--Run Amazon EC2 Instances -------\n");

    my $ami          = getRequiredParam("image",        $opts);
    my $key          = getRequiredParam("keyname",      $opts);
    my $instanceType = getRequiredParam("instanceType", $opts);
    my $group        = getRequiredParam("group",        $opts);
    my $zone         = getRequiredParam("zone",         $opts);
    my $count        = getRequiredParam("count",        $opts);
    my $propResult   = getRequiredParam("propResult",   $opts);

    my $poolName  = getOptionalParam("res_poolName",  $opts);
    my $workspace = getOptionalParam("res_workspace", $opts);
    my $port      = getOptionalParam("res_port",      $opts);

    my $userData = getOptionalParam("userData", $opts);
    if ("$userData" eq "") {
        $userData = MIME::Base64::encode_base64("none");
    }
    else {
        $userData = MIME::Base64::encode_base64("$userData");
    }

    mesg(1, "Running $count instance(s) of $ami in zone $zone as type $instanceType with group $group\n");

    ## run new instance
    use Amazon::EC2::Model::RunInstancesRequest;
    use Amazon::EC2::Model::DescribeInstancesRequest;
    use Amazon::EC2::Model::Placement;

    my $reservation = "";

    eval {
        my $placement = new Amazon::EC2::Model::Placement();
        $placement->setAvailabilityZone("$zone");

        my $request = new Amazon::EC2::Model::RunInstancesRequest(
                                                                  {
                                                                    "ImageId"       => "$ami",
                                                                    "Placement"     => $placement,
                                                                    "MinCount"      => "$count",
                                                                    "MaxCount"      => "$count",
                                                                    "KeyName"       => "$key",
                                                                    "SecurityGroup" => "$group",
                                                                    "InstanceType"  => "$instanceType",
                                                                    "UserData"      => "$userData",
                                                                  }
                                                                 );
        $request->setPlacement($placement);

        my $response = $service->runInstances($request);

        # get reservation
        if ($response->isSetRunInstancesResult()) {
            $result = $response->getRunInstancesResult();
            my $res = $result->getReservation();
            $reservation = $res->getReservationId();
            mesg(1, "Run instance returned reservation id $reservation\n");
        }
    };
    if ($@) { throwEC2Error($@); }

    # loop until all instances in reservation are running
    # either it completes or the step times out...
    while ("$reservation" ne "") {
        my $running = 0;         # number ready
        my $total   = $count;    # number in reservation
        eval {
            my $request = new Amazon::EC2::Model::DescribeInstancesRequest({ "ReservationId" => "$reservation" });
            my $response = $service->describeInstances($request);

            # examine all instances in reservation and
            # exit if ALL instances are running
            # (this ignores the original count, only
            #  looks at instances that Amazon thinks are
            #  part of the reservation)
            if ($response->isSetDescribeInstancesResult()) {
                my $result  = $response->getDescribeInstancesResult();
                my $resList = $result->getReservation();
                foreach (@$resList) {
                    my $res   = $_;
                    my $resId = $res->getReservationId();

                    # if instance not in reservation
                    if ("$resId" ne "$reservation") { next; }
                    $total        = 0;
                    $instanceList = $res->getRunningInstance();
                    foreach (@$instanceList) {
                        my $instance = $_;
                        my $id       = $instance->getInstanceId();
                        my $stateObj = $instance->getInstanceState();
                        my $state    = $stateObj->getName();
                        mesg(1, "Evaluating instance $id in state $state\n");
                        $total += 1;

                        # if it is running or something went wrong, either way consider this
                        # task complete
                        if ("$state" eq "running" || "$state" eq "shutting-down" || "$state" eq "terminated") {
                            $running += 1;
                        }
                    }
                }
            }
        };
        if ($@) { throwEC2Error($@); }
        mesg(1, "$running of $total instances ready\n");
        if ("$running" eq "$total") { last; }
        sleep 10;
    }

    if ("$reservation" eq "") {
        mesg(1, "Error running instances. No reservation created.\n");
        exit 1;
    }

    my $instlist = "";

    # Now describe them one more time to capture the attributes
    eval {
        my $request = new Amazon::EC2::Model::DescribeInstancesRequest({ "ReservationId" => "$reservation" });
        my $response = $service->describeInstances($request);

        if ($response->isSetDescribeInstancesResult()) {
            my $result  = $response->getDescribeInstancesResult();
            my $resList = $result->getReservation();
            foreach (@$resList) {
                my $res   = $_;
                my $resId = $res->getReservationId();

                # if instance not in reservation
                if ("$resId" ne "$reservation") { next; }
                $instanceList = $res->getRunningInstance();
                foreach (@$instanceList) {
                    my $instance = $_;
                    my $id       = $instance->getInstanceId();
                    my $image    = $instance->getImageId();
                    my $rtype    = $instance->getRootDeviceType();

                    # if we get back something other than a string, default
                    if (ref($rtype) ne "") { $rtype = "instance-store"; }

                    use Data::Dumper;

                    #print Data::Dumper->Dumper([$rtype]);

                    my $publicIP  = $instance->getPublicDnsName();
                    my $privateIP = $instance->getPrivateDnsName();

                    my $placement  = $instance->getPlacement();
                    my $actualZone = $placement->getAvailabilityZone();
                    mesg(1, "Instance $id: IP=$publicIP  AMI=$image ZONE=$actualZone\n");

                    my $resource = "";
                    if ("$poolName" ne "") {
                        $resource = makeNewResource($opts, $publicIP, $poolName, $workspace, $port);
                    }

                    if ("$propResult" ne "") {
                        $opts->{pdb}->setProp("$propResult/Instance-$id/AMI",      "$image");
                        $opts->{pdb}->setProp("$propResult/Instance-$id/RootType", "$rtype");
                        $opts->{pdb}->setProp("$propResult/Instance-$id/Address",  "$publicIP");
                        $opts->{pdb}->setProp("$propResult/Instance-$id/Private",  "$privateIP");
                        $opts->{pdb}->setProp("$propResult/Instance-$id/Zone",     "$actualZone");
                        $opts->{pdb}->setProp("$propResult/Instance-$id/Resource", "$resource");
                    }
                    if ("$instlist" ne "") { $instlist .= ";"; }
                    $instlist .= "$id";
                    mesg(1, "Adding $id to instance list\n");
                }
            }
        }
    };
    if ($@) { throwEC2Error($@); }

    if ("$propResult" ne "") {
        mesg(1, "Saving instance list $instlist\n");
        $opts->{pdb}->setProp("$propResult/InstanceList", $instlist);
        $opts->{pdb}->setProp("$propResult/Reservation",  $reservation);
        $opts->{pdb}->setProp("$propResult/Count",        $count);
    }

    exit 0;
}

sub MOCK_API_RunInstance {
    my ($opts, $service) = @_;

    mesg(1, "--Run Amazon EC2 Instances -------\n");

    my $ami          = getRequiredParam("image",        $opts);
    my $key          = getRequiredParam("keyname",      $opts);
    my $instanceType = getRequiredParam("instanceType", $opts);
    my $group        = getRequiredParam("group",        $opts);
    my $zone         = getRequiredParam("zone",         $opts);
    my $count        = getRequiredParam("count",        $opts);
    my $propResult   = getRequiredParam("propResult",   $opts);

    my $poolName  = getOptionalParam("res_poolName",  $opts);
    my $workspace = getOptionalParam("res_workspace", $opts);
    my $port      = getOptionalParam("res_port",      $opts);

    my $userData = getOptionalParam("userData", $opts);
    if ("$userData" eq "") {
        $userData = MIME::Base64::encode_base64("none");
    }
    else {
        $userData = MIME::Base64::encode_base64("$userData");
    }

    mesg(1, "Running $count instance(s) of $ami in zone $zone as type $instanceType with group $group\n");

    ## run new instance
    use Amazon::EC2::Model::RunInstancesRequest;
    use Amazon::EC2::Model::DescribeInstancesRequest;
    use Amazon::EC2::Model::Placement;

    my $reservation = "";

    for (my $num = 0; $num < $count; $num++) {
        my $r  = getRandKey(9999999);
        my $id = "i-$r";
        $opts->{pdb}->setProp("$::gMockRegistry/Instances/$id/state",    "running");
        $opts->{pdb}->setProp("$::gMockRegistry/Instances/$id/key",      "$key");
        $opts->{pdb}->setProp("$::gMockRegistry/Instances/$id/group",    "$group");
        $opts->{pdb}->setProp("$::gMockRegistry/Instances/$id/zone",     "$zone");
        $opts->{pdb}->setProp("$::gMockRegistry/Instances/$id/userData", "$userData");
        $opts->{pdb}->setProp("$::gMockRegistry/Instances/$id/ami",      "$ami");
        $opts->{pdb}->setProp("$::gMockRegistry/Instances/$id/root",     "ebs");

        my $publicIP = $opts->{pdb}->getProp("/myProject/publicIP");
        if ($publicIP eq "") {
            $publicIP = "192.168." . getRandKey(255) . "." . getRandKey(255);
        }
        my $privateIP = "192.168." . getRandKey(255) . "." . getRandKey(255);
        $opts->{pdb}->setProp("$::gMockRegistry/Instances/$id/prvdns", "$privateIP");
        $opts->{pdb}->setProp("$::gMockRegistry/Instances/$id/pubdns", "$publicIP");

        my $resource = "";
        if ("$poolName" ne "") {
            $resource = makeNewResource($opts, $publicIP, $poolName, $workspace, $port);
        }

        if ("$propResult" ne "") {
            $opts->{pdb}->setProp("$propResult/Instance-$id/AMI",      "$ami");
            $opts->{pdb}->setProp("$propResult/Instance-$id/RootType", "ebs");
            $opts->{pdb}->setProp("$propResult/Instance-$id/Address",  "$publicIP");
            $opts->{pdb}->setProp("$propResult/Instance-$id/Private",  "$privateIP");
            $opts->{pdb}->setProp("$propResult/Instance-$id/Zone",     "$zone");
            $opts->{pdb}->setProp("$propResult/Instance-$id/Resource", "$resource");
        }
        if ("$instlist" ne "") { $instlist .= ";"; }
        $instlist .= "$id";
        mesg(1, "Adding $id to instance list\n");
    }

    if ("$propResult" ne "") {
        mesg(1, "Saving instance list $instlist\n");
        $opts->{pdb}->setProp("$propResult/InstanceList", $instlist);
        $opts->{pdb}->setProp("$propResult/Reservation",  $reservation);
        $opts->{pdb}->setProp("$propResult/Count",        $count);
    }

    exit 0;
}

sub makeNewResource() {
    my ($opts, $host, $pool, $workspace, $port) = @_;

    # host must be present
    if ("$host" eq "") {
        mesg(1, "No host provded to makeNewResource.\n");
        return "";
    }

    # workspace and port can be blank

    mesg(1, "Creating resource for machine $host in pool $pool\n");

    my $resName = "$pool-$now_$seq";

    #-------------------------------------
    # Create the resource
    #-------------------------------------
    for (my $seq = 1; $seq < 9999; $seq++) {
        my $now = time();
        $resName = "$pool" . "-" . $now . "_" . $seq;
        my $cmdrresult = $opts->{pdb}->getCmdr()->createResource(
                                                                 $resName,
                                                                 {
                                                                    description   => "EC2 provisioned resource (dynamic)",
                                                                    workspaceName => "$workspace",
                                                                    port          => "$port",
                                                                    hostName      => "$host",
                                                                    pools         => "$pool"
                                                                 }
                                                                );

        # Check for error return
        my $errMsg = $opts->{pdb}->getCmdr()->checkAllErrors($cmdrresult);
        if ($errMsg ne "") {
            if ($errMsg =~ /DuplicateResourceName/) {
                mesg(4, "resource $resName exists\n");
                next;
            }
            else {
                mesg(1, "Error: $errMsg\n");
                return "";
            }
        }
        mesg(1, "Resource Name:$resName\n");
        return $resName;
    }

    return "";

}

sub deleteResource() {
    my ($opts, $resource) = @_;

    # host must be present
    if ("$resource" eq "") {
        mesg(1, "No resource provded to deleteResource.\n");
        return;
    }

    mesg(1, "Deleting resource $resource\n");

    #-------------------------------------
    # Delete the resource
    #-------------------------------------
    my $cmdrresult = $opts->{pdb}->getCmdr()->deleteResource($resource);

    # Check for error return
    my $errMsg = $opts->{pdb}->getCmdr()->checkAllErrors($cmdrresult);
    if ($errMsg ne "") {
        mesg(1, "Error: $errMsg\n");
    }
    return;
}

