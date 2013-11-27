import sys,os,re,logging, HTMLParser, urlparse
from urllib2 import Request, urlopen, URLError, HTTPError

BASE_REVISION="1.3"
REPO_URL= os.environ.get( 'TELENAV_IVY_REPO_URL', 
   "http://tar1.telenav.com:8080/repository/telenav/client/" )

# Parse options
logLevel = logging.INFO
skipUpdate = False
help = False
for arg in sys.argv[1:]:
   if arg == '--skip-update':
      skipUpdate = True
   elif arg == '--help' or arg == '-h':
      help = True
   elif arg == '--quiet' or arg == '-q':
      logLevel = logging.ERROR
   elif arg == '--mute' or arg == '-Q':
      logLevel = logging.CRITICAL
   elif arg == '--verbose' or arg == '-v':
      logLevel = logging.DEBUG

# Configure logger
logging.basicConfig( level=logLevel, format=sys.argv[0] + ': %(message)s' ) 

# Set up paths
platform = 'win32' if os.sep == '\\' else 'unix'
modulesDir = os.path.join( os.path.join( os.path.dirname(__file__), '..', 'modules' )) 
pythonLibDir = os.path.join( modulesDir, platform, 'lib', 'python' )
sys.path.append( pythonLibDir )
binDir = os.path.join( modulesDir, platform, 'bin' )
tmpDir = os.path.join( modulesDir, platform, 'tmp' )
os.environ['PATH'] = binDir + os.pathsep + os.environ.get('PATH','')

# Update tools
if not skipUpdate and not (help and os.path.exists(pythonLibDir)):
   logging.info( "Checking for build tools updates" )
   try:

      def mkdir( path ):
         if not os.path.exists( path ):
            os.makedirs( path )

      def download( url, saveto = None ):
          req = Request(url)
          f = urlopen(req)
          if not saveto:
             return f.read()
          else:
             if os.path.exists( saveto ):
                return False
             else:
                mkdir(os.path.dirname(saveto))
                local_file = open(saveto, "wb")
                local_file.write(f.read())
                local_file.close()
                return True

      def findUrls( text ):
          class URLFinder(HTMLParser.HTMLParser):
             urls=[]
             def handle_starttag(self, tag, attrs):
                 if tag == 'a':
                    self.urls.append( dict(attrs).get('href','') )
          f = URLFinder()
          f.feed( text )
          return f.urls

      pattern = r"BuildTools-%s-%s\.(\d+)(-\w+)?\.(zip|tgz|cab)" % (platform, BASE_REVISION)

      latestPackage = None
      for branch in ['release', 'beta', 'trunk']:
         repoUrl = REPO_URL + branch + "/BuildTools"
         logging.debug( "Checking in %s", repoUrl )
         try:
            index = download( repoUrl )
         except Exception as err:
            logging.debug( "Failed to read %s: %s", repoUrl, err )
            repoUrl = repoUrl + '/index.html'
            logging.debug( "Checking in %s", repoUrl )
            try:
               index = download( repoUrl )
            except Exception as err:
               logging.debug( "Failed to read %s: %s", repoUrl, err )
               continue

         latestBuild = None

         for url in [urlparse.urljoin( repoUrl, url ) for url in findUrls( index )]:
            m = re.search( pattern, url )
            if m:
               build = int(m.group(1))
               if not latestBuild or build > latestBuild:
                  latestBuild = build
                  latestPackageUrl = url
                  latestPackage = m.group(0)
                  latestBranch = branch
         if latestPackage:
            break

      if not latestPackage:
         raise RuntimeError(  "Could not find BuildTools-%s-%s.* in %s" % (platform, BASE_REVISION, REPO_URL) )

      toolsZip = os.path.join( tmpDir, latestPackage )
      if os.path.exists( toolsZip ):
         logging.info( "No updates, using BuildTools v%s.%s (%s;%s)", BASE_REVISION, latestBuild, latestBranch, platform )
      else:
         logging.debug( "Downloading %s", latestPackage )
         download( latestPackageUrl, toolsZip )

         logging.debug( "Installing %s", latestPackage)
         if platform != 'win32':
            errlevel = os.system( 'unzip -oq "%s" -d "%s"' % (toolsZip, modulesDir))
         else:
            unzipExe = os.path.join( binDir, 'unzip.exe' )
            download( "%s/release/BuildTools/unzip.exe" % REPO_URL, unzipExe )
            errlevel = os.system( 'unzip -oq "%s" -x win32/bin/unzip.exe -d "%s"' % (toolsZip, modulesDir))

         if errlevel:
            raise RuntimeError(  "Could not extract %s, unzip failed", toolsZip )

         logging.info( "Updated to BuildTools v%s.%s (%s;%s)", BASE_REVISION, latestBuild, latestBranch, platform )
   
   except (HTTPError,URLError,RuntimeError) as err:
      logging.error( "[FAILED] %s", err)
      exit(-1)

from tnbuildtools import *
