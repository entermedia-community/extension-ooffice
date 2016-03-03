importPackage( Packages.org.openedit.util );
importPackage( Packages.java.util );
importPackage( Packages.java.lang );
importPackage( Packages.java.io );
importPackage( Packages.org.entermediadb.modules.update );


var name = "extension-ooffice";

var war = "http://dev.entermediasoftware.com/jenkins/job/@BRANCH@" + name + "/lastSuccessfulBuild/artifact/deploy/" + name + ".zip";

var root = moduleManager.getBean("root").getAbsolutePath();
var web = root + "/WEB-INF";
var tmp = web + "/tmp";

log.add("1. GET THE LATEST WAR FILE");
var downloader = new Downloader();
downloader.download( war, tmp + "/" + name + ".zip");

log.add("2. UNZIP WAR FILE");
var unziper = new ZipUtil();
unziper.unzip(  tmp + "/" + name + ".zip",  tmp );

log.add("3. REPLACE LIBS");
var files = new FileUtils();


files.deleteMatch( web + "/lib/extension-ooffice*.jar");
files.deleteMatch( web + "/lib/dev_extension-ooffice*.jar");
files.deleteMatch( web + "/lib/em9_extension-ooffice*.jar");
files.deleteAll( root + "/WEB-INF/base/oofice");

files.copyFiles( tmp + "/WEB-INF/base/oofice", root + "/WEB-INF/base/oofice");

files.copyFileByMatch( tmp + "/lib/@BRANCH@extension-ooffice*.jar", web + "/lib/");

log.add("Copy " +  tmp + "/lib/@BRANCH@extension-ooffice*.jar" + " -> "  + web + "/lib/");


log.add("5. CLEAN UP");
files.deleteAll(tmp);

log.add("6. UPGRADE COMPLETED");