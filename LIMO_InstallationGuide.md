# LIMO Installation Guide
## 1 IDE Setup
1. Install Eclipse (<https://www.eclipse.org/downloads/>). Any version should work
2. Install Google Web Toolkit
    1. Click Help – Install New Software
    2. In the ‘Work With’ textbox
        1. Paste: <http://storage.googleapis.com/gwt-eclipse-plugin/v3/release>
        2. 'Select All'
        3. Click 'Next' and 'Install'
3. Install PyDev
    1. Click Help - Install New Software
    2. In the 'Work With' textbox
        1. Paste: <http://www.pydev.org/updates>
        2. 'PyDev for Eclipse'
        3. Click 'Next' and 'Install'

## 2 Importing Source Code
1. Clone the repository via CLI
    1. SSH: git@github.com:purduedb/LIMO.git
    2. HTML: https://github.com/purduedb/LIMO.git
2. Right click in Package Explorer -> Import -> Import project in Eclipse -> Import as Local Git -> location of cloned repo -> import as general project
3. Right click name of project –> properties –> project facets –> click on “Convert to faceted form” –> Set java version to 8 –> Apply –> OK
    1. If desired, most recent java version should work
4. Build path settings
    1. Right click name of project, select ‘build path’ –> ‘configure build path’
    2. In the Libraries tab, click Add Jars –> navigate to the project folder/war/WEB-INF/lib –> shift click and select all the jars in that folder –> Apply
    3. In the Source tab, Set default output folder to “\<name of project\>/war/WEB-INF/classes” –> Apply -> OK
    4. Right click name of project –> GWT –> Settings –> Check Use GWT –> Use default SDK –> OK

## 3 Running/Compiling Code
1. To run the code in local machine
    1. Right click name of project
    2. Click "Run as..." -> GWT Development Mode with Jetty –> Select GWTMaps.html –> OK
    3. You will see a URL for the web app on the right hand side, double click to open it on the browser
2. To compile source codes for deployment
    1. Right click name of project
    2. Select GWT – Compile
    3. Compiled files will be located under the war directory
    4. Push to repo

## 4 Installing testing and linting tools
1. Download and install Postman found here <https://www.getpostman.com/downloads/>
    1. Optional - install Newman, the Postman CLI. Found here: <https://www.npmjs.com/package/newman> or by running `npm install -g newman`
    2. Import the Environment and LIMO test configurations found in the `tools` directory
2. Download and install pip found here <https://pip.pypa.io/en/stable/installing/>
    1. Installing with the `--user` command is recommended to avoid permissions based issues
    2. Install yapf via `pip install yapf --user`
    3. Install pylint via `pip install pylint --user`

## 5. Deploying to the server (Purdue Ibnkhaldun Specific)
1. Right click on the project directory, select GWT -> Compile -> Compile
2. Transfer all files under the ‘war’ directory into ibnkhaldun server (using ftp/scp and your purdue account)
3. ssh to the ibnkhaldun server using purdue id, switch to limo-sudo
    1. $ sudo -u limo-sudo –s
    2. (password)
    3. $ cd
4. The web app files need to be copied from your ibnkhaldun account to shared limo-sudo account: `/homes/limo-sudo/apache/tomcat/webapps/limo`
    1. $ cd /homes/limo-sudo/apache/tomcat/webapps/limo
    2. $ scp -r <yourPurdueId>@ibnkhaldun.cs.purdue.edu:/home/<yourPurdueId>/somefolder/* .
    3. (password)
