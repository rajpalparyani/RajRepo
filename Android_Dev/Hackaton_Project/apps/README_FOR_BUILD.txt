1. How to build dev enviroment. 
ant build_lib #if you have build this before, and there's no update in lib after taht, can skip this step.
ant build_dev -propertyfile scout_us_brand.properties -propertyfile android.properties -Dversion=$version$

$version$ should be a number.

2. How to make a release build.
ant build_lib #if you have build this before, and there's no update in lib after taht, can skip this step.
ant publish-all -Dversion=$version$

$version$ should be a number.

3. Run unit_test only
ant build_lib #if you have build this before, and there's no update in lib after taht, can skip this step.
ant unittest_dev -DlocalCache=true

4. Run findbugs only
ant build_lib #if you have build this before, and there's no update in lib after taht, can skip this step.
ant findbugs_dev -DlocalCache=true

4. Run both unit_test and findbugs
ant build_lib #if you have build this before, and there's no update in lib after taht, can skip this step.
ant unit_test -DlocalCache=true