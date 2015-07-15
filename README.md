# GeneletJavaHelper

Run this program under a command line and generate a skeleton of your initial Genelet Java project.

<sub>*program -dbname="Database_Name" -dbuser="Database_User" -dbpass="Database_Password" [-root="ROOT_DIRECTORY"] [-project="PROJECT_NAME"] [-script="SCRIPT_NAME"] [-force] table1 table2 ...*</sub>

Defaults:  
* -root    = "." , current directoy 
* -project = "myproject", 
* -script  = "myscript"

The files generated will follow the structure:

<pre>
> root
     > web
         > WEB-INF
                 > web.xml
                 > myproject.json
         > admin
                 > login.html
                 > table1
                        > dashboard.html
                        > topics.html
                        > edit.html
                        > startnew.html
                        > insert.html
                        > update.html
                        > delete.html
                 > table2
                        > ...
     > www
         > index.html
         > init.js
         > admin
               > login.html
               > header.html
               > footer.html
               > table1
                      > dashboard.html
                      > topics.html
                      > edit.html
                      > startnew.html
               > table2
                      > ...
         > public
                > header.html
                > footer.html
                > table1
                       > startnew.html
     > src
         > java
              > myproject
                        > MyprojectFilter.java
                        > MyprojectModel.java
                        > MyprojectServlet.java
                        > MyprojectListener.java
                        > table1
                               > Filter.java
                               > Model.java
                        > table2
                               > ...
</pre>
