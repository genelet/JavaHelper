# GeneletJavaHelper

Run this program under a command line and generate a skeleton of your initial Genelet Java project.

<sub>*program -dbname="" -dbuser="" -dbpass="" [-root=""] [-project=""] [-script=""] [-force] table1 table2 ...*</sub>

Defaults:  
* -dbname, database name, mandatory
* -dbuser, database user name
* -dbpass, password of the database user
* -root, top directory where the project files located, default to current directoy "." 
* -project, name of package, default to "myproject", 
* -script, script name in URL, default to "myscript"
* -force, if set, force to override the existing files

The following files will be generated:

<pre>
> root
     > web
         > WEB-INF                          
                 > web.xml                  # system configuration file
                 > myproject.json           # Genelet configuration file
         > admin                            # JSP files for role "admin"
                 > login.html               # admin's login
                 > table1                   # admin's JSP files to component "table1"
                        > dashboard.html
                        > topics.html
                        > edit.html
                        > startnew.html
                        > insert.html
                        > update.html
                        > delete.html
                 > table2                   # admin's JSP files to component "table2"
                        > ...
     > www                                  # AngularJS 
         > index.html                       # the single-URL index file  
         > init.js                          # global initial files for genelet.js
         > (genelet.html)                   # [download here](http://gitbub.com/tianzhen99/GeneletAngularJS/)
         > admin                            # partial files for role "admin"
               > login.html                 # admin's login
               > header.html                # admin's general header 
               > footer.html                # admin's general footer
               > table1                     # admin's partial files to component "table1"
                      > dashboard.html
                      > topics.html
                      > edit.html
                      > startnew.html
               > table2
                      > ...
         > public                           # the default used in index.html is public/table1/startnew.html 
                > header.html
                > footer.html
                > table1
                       > startnew.html
     > src
         > java
              > myproject                         # generated Java source files
                        > MyprojectFilter.java    # system Filter class
                        > MyprojectModel.java     # system Model class
                        > MyprojectServlet.java   # project's servlet 
                        > MyprojectListener.java  # project's listener class
                        > table1                  # package for component "table1"
                               > Filter.java      # component's Filter class
                               > Model.java       # component's Model class
                        > table2
                               > ...
</pre>


