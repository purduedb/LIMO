### Install Instruction
1. Install Eclipse (https://www.eclipse.org/downloads/) *any version should work
2. Install Google Web Toolkit
    1. Click Help – Install New Software
    2. In the ‘Work With’ textbox
        1. Paste: http://storage.googleapis.com/gwt-eclipse-plugin/v3/release
        2. Select All
        3. Click Next and Install

### Importing Source Code
1. Clone the repo
2. Import project in Eclipse -> location of cloned repo
3. Right Click name of project – properties – project facets – click on “Convert to
faceted form” – Set java version to 1.8 – Apply – OK *can also most recent java
version
4. Build path settings
    1. Right click name of project, select ‘build path’ – ‘configure build path’
    2. In the Libraries tab, click Add Jars – navigate to the project
folder/war/WEB-INF/lib – shift click and select all the jars in that folder – Apply
    3. In the Source tab, Set Default output folder to “name of
project/war/WEB-INF/ classes” – Apply, OK
    4. Right Click name of project – GWT – Settings – Check Use GWT – Use
default SDK – OK

### Running/Compiling Code
1. To run the code in local machine
    1. Right Click name of project
    2. Click Run as… -- GWT Development Mode with Jetty – Select
GWTMaps.html – OK
    3. You will see a URL for the web app on the right hand side, double click to
open it on the browser
2. To compile source codes for deployment
    1. Right click name of project
    2. Select GWT – Compile
    3. Compiled files will be located under the war directory
    4. Push to repo