# GeneletJavaHelper

Run this program under a command line and generate a skeleton of your initial Genelet Java project.

program -dbname="DATABASE_NAME" -dbuser="DATABASE_USER" -dbpass="DATABASE_PASSWORD" [-root="ROOT_DIRECTORY] [-project="PROJECT_NAME"] [-script="SCRIPT_NAME"] [-force] table1 table2 ...

Defaults:  -root => ".", current directoy; -project => "myproject", -script => "myscript"

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
                   table2
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
