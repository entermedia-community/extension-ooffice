importPackage( Packages.com.openedit.util );
importPackage( Packages.java.util );
importPackage( Packages.java.lang );
importPackage( Packages.com.openedit.modules.update );

var war = "http://dev.entermediasoftware.com/jenkins/job/entermedia-ooffice/lastSuccessfulBuild/artifact/deploy/ROOT.war";

var root = moduleManager.getBean("root").getAbsolutePath();
var web = root + "/WEB-INF";
var tmp = web + "/tmp";

log.add("1. GET THE LATEST WAR FILE");
var downloader = new Downloader();
downloader.download( war, tmp + "/ROOT.war");

log.add("2. UNZIP WAR FILE");
var unziper = new ZipUtil();
unziper.unzip(  tmp + "/ROOT.war",  tmp );

log.add("3. REPLACE LIBS");
var files = new FileUtils();
files.deleteMatch( web + "/lib/entermedia-ooffice*.jar");
files.deleteMatch( web + "/lib/json-*.jar");
files.deleteMatch( web + "/lib/commond-cli-*.jar");
files.deleteMatch( web + "/lib/jodconverter-*.jar");
files.deleteMatch( web + "/lib/juh-*.jar");
files.deleteMatch( web + "/lib/jurt-*.jar");
files.deleteMatch( web + "/lib/ridl-*.jar");
files.deleteMatch( web + "/lib/unoil-*.jar");

files.copyFileByMatch( tmp + "/WEB-INF/lib/entermedia-ooffice*.jar", web + "/lib/");
files.copyFileByMatch( tmp + "/WEB-INF/lib/common-cli*.jar", web + "/lib/");
files.copyFileByMatch( tmp + "/WEB-INF/lib/jodconverter-*.jar", web + "/lib/");
files.copyFileByMatch( tmp + "/WEB-INF/lib/juh-*.jar", web + "/lib/");
files.copyFileByMatch( tmp + "/WEB-INF/lib/jurt-*.jar", web + "/lib/");
files.copyFileByMatch( tmp + "/WEB-INF/lib/ridl-*.jar", web + "/lib/");
files.copyFileByMatch( tmp + "/WEB-INF/lib/unoil-*.jar", web + "/lib/");

log.add("5. CLEAN UP");
//files.deleteAll(tmp);

log.add("6. UPGRADE COMPLETED");