

#mkdir -p /EC/Build/Avengers/$[jobId]
#cd /EC/Build/Avengers/$[jobId]/
HTMLPOI_Release_Name=htmlpoi_`date +%Y%m%d%H%M%S`
ectool setProperty /myjob/HTMLPOI_Release_Name --value $HTMLPOI_Release_Name
# Set the regular expression pattern of the version that you are looking for.
# Currently we are assuming trunk is of the format '0.0.0.NNNN'.
VERSION_PATTERN="[0-9][0-9]/"
TAR_REPO="http://tar2/repository/telenav/BSVR/trunk"


# Retrieve the latest version number.  We are assuming that the tar
# repository lists the versions from oldest to newest.
LATEST_VERSION=`wget -q -O - $TAR_REPO | grep "$VERSION_PATTERN" | tail -1 | grep -o -m 1 "$VERSION_PATTERN" | tail -1`
echo "Latest version found to be $LATEST_VERSION"

ARTIFACT_NAME="htmlpoi.war"

# Retrieve the appropriate version.
echo "Retrieving $ARTIFACT_NAME"
wget $TAR_REPO/$LATEST_VERSION/$ARTIFACT_NAME

#java -jar /home/buildmaster01/tools/tads-cli-2.0.jar upload htmlpoi $HTMLPOI_Release_Name htmlpoi.war /EC/Build/Avengers/$[jobId]/$ARTIFACT_NAME
