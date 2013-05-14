# Set the regular expression pattern of the version that you are looking for.
# Currently we are assuming trunk is of the format '0.0.0.NNNN'.
VERSION_PATTERN="FB-Avengers.0\.0\.[0-9][0-9]"
TAR_REPO="http://tar1.telenav.com:8080/repository/telenav/htmlpoi/"


# Retrieve the latest version number.  We are assuming that the tar
# repository lists the versions from oldest to newest.
LATEST_VERSION=`wget -q -O - $TAR_REPO | grep "$VERSION_PATTERN" | tail -1 | grep -o -m 1 "$VERSION_PATTERN" | tail -1`
echo "Latest version found to be $LATEST_VERSION"

ARTIFACT_NAME="htmlpoi-$LATEST_VERSION-deploy.us.war"

# Retrieve the appropriate version.
echo "Retrieving $ARTIFACT_NAME"
wget $TAR_REPO/$LATEST_VERSION/$ARTIFACT_NAME