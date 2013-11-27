#!/usr/bin/python
import os,sys,shutil
from tools import *

# Use this example if your component publishes a single 
# static library.
#
# The name of the class is the name of your component (unless you
# override it with name = 'xxx' attribute). Names should be unique 
# within TeneNav, start with capital letter, use CamelCase, and should 
# not contain any punctuation.
#
# The (StaticLibraryComponent) part means you're declaring a component
# that builds a single static library. 
#
# Your project directory structure should be:
#
# include/<LibraryName>.h
# include/<LibraryName>/ (optional)
# src/ 
# tests/ (optional) 
# 
# Build script will build the library from the sources located in src 
# directory.
#
# Public header files should go in the include directory. Each component must
# provide a main header file, its name should match component's name and
# it should be the only file that users of your library have to include. 
# If your library interface is too big to put in one file, place additional 
# include files in a directory under include. The directory name should match
# your component name. Then include files in that directory from your main 
# include file.
#
# Test files should go in an optional tests directory. If it is provided, 
# build tools will build a <LibraryName>_test executable. It is linked 
# with all library dependencies and the library itself.
#
class GetOpenGLEngine(Component):
   # Base revision consists of major and minor versions. You increment
   # minor version whenever you make a backwards-incompartible change 
   # in your component. You increment major version whenever you make 
   # a major change (like a complete rewrite of the API, etc.)  
   #
   # The actual version of your published component will consist of
   # baseRevision build number appended, e.g. 1.2.3456. We use SVN
   # revision as build number. 
   baseRevision = '1.3'
   
   #artifacts = [
    #   FileArtifact( 'include', 'include' ),]
   # This example does not specify any dependencies. See ExecutableComponent
   # for a working example. In general, you specify dependencies like this:
   dependencies = [ 
	      StaticLibraryDependency( componentName = 'OpenGLMapEngine', baseRevision = '1.6.92489'),
       ]
       
if __name__ == '__main__':
    main()

