# Just save this file to the working folder
# that contains your code file.
# Then just run it ...
# It will create two subfolders called build and dist.
# The dist folder contains your .exe file, MSVCR71.dll and w9xpopen.exe
# Your .exe file contains your code, all needed modules + the Python
# interpreter as a Pythonxx.dll.
# MSVCR71.dll can be distributed, but is often already in the system32
# folder. The build folder can be deleted.

from distutils.core import setup
import py2exe
import sys

# if run without args, build executables in quiet mode
if len(sys.argv) == 1:
    sys.argv.append("py2exe")
    sys.argv.append("-q")


class Target:
    def __init__(self, **kw):
        self.__dict__.update(kw)
        # for the versioninfo resources
        self.version = "0.01.1"
        self.company_name = "telenav.com"
        self.copyright = "Copyright (c) 2011 telenav, Inc."
        self.name = "Image Tools"

# create an instance of class Target
# and give it additional needed info
target = Target(
    description = "TN7.0 ImageTools, include scale/compare/cut image",
    # this is your code file
    script = "imageTools.py",
    # this will form convertapp.exe
    dest_base = "ImageTools")

#support install Chinese directory
includes = ["encodings", "encodings.*"]

setup(
    options = {"py2exe": {"compressed": 1,
                          "optimize": 2,
                          "ascii": 1,
                          "includes": includes,
                          "packages" : ["encodings"],
                          "bundle_files": 1}},
    zipfile = None,
    windows = [target]
    )

