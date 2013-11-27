This document is show the process to generate properties and excel.

To user ant pro_to_excel, you don't need to add any parameter.
The default carrier is "cingular,sprint,uscc,t-mobile-us,t-mobile-uk,vivo,cmcc,ttx,mmi"
If you want to add any carrier,please modify build.properties, please follow my style.

propertiesPath:  mobile\apps\branches\7.1.1\telenav-global\res\android\carrier\i18n
outputPath:  \mobile\apps\branches\7.1.1\telenav-global\documents\i18n2excel
1. transform properites into excel
   run "ant pro_to_excel"
   example: my work dir is E:\TN70\mobile\apps\branches\7.1.1\telenav-global
   after run "ant pro_to_excel", The new excel is generated to E:\TN70\mobile\apps\branches\7.1.1\telenav-global\documents\i18n2excel
   
2. transform Excel into propertie
   run "ant excel_to_pro"
   example: my work dir is E:\TN70\mobile\apps\branches\7.1.1\telenav-global
   after run "ant excel_to_pro", The new properties is generate dto E:\TN70\mobile\apps\branches\7.1.1\telenav-global\res\android\carrier\i18n
   
3. merge
   run "ant merge"
   example:
   The merged excel will generated to E:\TN70\mobile\apps\branches\7.1.1\telenav-global\documents\i18n2excel
   
user case: I want to add some key or modify properites
required:
1. modify excel firstly.
2. "run excel_to_pro" to generate properties
3. find the properites, use diff before you check in all the properties.
4. check in

Optional :
If you want to release a build.
1. update the i18 index files 
   run "ant" in "mobile\apps\branches\7.1.1\telenav-global\tools\IndexFile"
   you can find index file in "mobile\apps\branches\7.1.1\telenav-global\res\android\carrier\i18n"
   check in all the index file before you release a build.
   
Note:please don't modify properites directly.
