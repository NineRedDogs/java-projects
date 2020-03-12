git status .
git diff nginx/nginx-openid-connect/frontend.conf
git status .
git checkout -- nginx/nginx-openid-connect/frontend.conf
git status .
mv all.sh refresh-all.sh
git status .
rm nginx/nginx-openid-connect/frontend.conf.sav 
ll
git status .
git add refresh-all.sh refresh.sh 
./refresh.sh 
git status .
meld k8s/deployment.yaml 
meld nginx/nginx-openid-connect/frontend.conf
git checkout -- k8s/deployment.yaml nginx/nginx-openid-connect/frontend.conf
git status .
git status
pwd
ll
cd ..
pwd
ll
git status .
cd sca-auth
git status .
cd ..
ll
mv sca-auth-mock-insurance-portal.andy ~/sandbox/
mv sca-auth-mock-loans-portal.andy ~/sandbox/
ll
rm sca-auth.zip
ll
cd sca-auth-mock-insurance-api
git status .
git diff .
git checkout -- k8s/deployment.yaml
git status ..
git status .
git add refresh.sh 
git status .
cd ..
ll
cd sca-auth-mock-insurance-portal
git status .
git diff  k8s/deployment.yaml
git checkout --  k8s/deployment.yaml
git status .
git add refresh.sh 
cat refresh.sh 
cd ..
ll
cd sca-auth-mock-landing-page
ll
git status .
git diff k8s/deployment.yaml
git checkout -- k8s/deployment.yaml
git status .
git add refresh.sh 
ll
git status .
cat refresh.sh 
cd ..
ll
cd sca-auth-mock-loans-api
git status .
git diff k8s/deployment.yaml
git checkout -- k8s/deployment.yaml
git status .
git add refresh.sh 
cat refresh.sh 
ll
git status .
cd ..
ll
cd sca-auth-mock-loans-portal
git status .
git diff k8s/deployment.yaml
git checkout -- k8s/deployment.yaml
cat refresh.sh 
git add refresh.sh 
git status .
cd ..
ll
cd sca-auth
git pull
git status .
git commit -am "added scripts to clean down and rebuild the container on a local machine docker-k8s cluster"
git push
cd ..
ll
cd sca-auth-mock-insurance-api
git pull
git commit -am "added script to clean down and rebuild the container on a local machine docker-k8s cluster"
git push
cd ..
ll
cd sca-auth-mock-insurance-portal
git commit -am "added script to clean down and rebuild the container on a local machine docker-k8s cluster"
git push
git pull
git push
git pull
cd ..
ll
cd sca-auth-mock-landing-page
git pull
git status
git commit -am "added script to clean down and rebuild the container on a local machine docker-k8s cluster"
git push
git pull
git status
git push
cd ..
ll
cd sca-auth-mock-loans-api
git pull
git status .
git commit -am "added script to clean down and rebuild the container on a local machine docker-k8s cluster"
git push
git pull
git status .
cd ..
ll
cd sca-auth-mock-loans-portal
git pull
git status
git commit -am "added script to clean down and rebuild the container on a local machine docker-k8s cluster"
git push
git pull
git status
pwd
ll
cd ..
cd sca-auth
pwd
ll
cp refresh-all.sh 
cp refresh-all.sh check-git.sh
gvim check-git.sh 
./check-git.sh 
gvim check-git.sh 
mv check-git.sh ..
cd ..
ll
./check-git.sh 
pwd
ll
cd sca-auth
pwd
ll
./refresh-all.sh > all.out
gvim all.out 
pwd
ll
rm all.out 
pwd
ll
gvim refresh-all.sh 
./refresh-all.sh > all.out
cd ..
ll
./check-git.sh 
cd ..
cd andy-git-area/
cd java-projects/ajg-java/SoccerForecaster/
git pull
cat ajg-java/SoccerForecaster/logs/predictions.log
cat logs/predictions.log 
git checkout -- logs/predictions.log
git pull
rm  ajg-java/SoccerForecaster/.gradle/3.5/taskHistory/taskHistory.lock
cd ..
git pull
rm  ajg-java/SoccerForecaster/.gradle/3.5/taskHistory/taskHistory.lock
rm -rf  ajg-java/SoccerForecaster/.gradle/3.5/taskHistory/taskHistory.lock
sudo rm -rf  ajg-java/SoccerForecaster/.gradle/3.5/taskHistory/taskHistory.lock
ll  ajg-java/SoccerForecaster/.gradle/3.5/taskHistory/taskHistory.lock
ps -ef | grep grad
git pull
cd ajg-java/SoccerForecaster/
git status .
rm -rf .gradle/
ll
git pull
cd .gradle/
ll
cd 3.5/
ll
cd ..
git remove .gradle/
igt --help
git --help
git rm .gradle/
git rm -rf .gradle/
git commit -am "removed .gradle folder"
git push
pwd
ll
gradle clean build jar
unset https_proxy http_proxy ftp_proxy JAVA_OPTS 
gradle clean build jar
ll build/libs/
java -jar build/libs/SoccerForecaster.jar 
ll  /mnt/c/Users/agrahame/dev/git-repos/andy-git-area/java-projects/ajg-java/SoccerForecaster
mkdir data
find ../.. -name dataUrls.txt
cp ../../ajg-java/andy-footy-predictor/data/dataUrls.txt data/
git add data
git commit -am "added data dir"
git push
git status .
gradle -v
gradle clean build 
gradle
gradle tasks
gradle dependencies
gradle build
meld .
gradle clean build
code .
find . -type f -exec grep -w jupiter {} +
find . -type f -exec grep -w gson {} +
gradle clean build jar
pwd
ll
code . &
pwd
ll
ant clean build
cd dev/git-repos/andy-git-area/java-projects/ajg-java/GameOracle/
cd gatherer/
ll
gradle clean build
gradle clean
find . -name "*.jar"
gradle clean
pwd
ll
rm -rf .vscode/
gradle clean
ll
cls
ll
rm -rf .gradle/
ll
find gradle/
find .gradle/
gradle clean
pwd
ll
git pull
ll
cat settings.gradle 
find gradle
rm -rf gradle/
ll
rm -rf .gradle/
ll
gradle clean
java -version 
ll .settings/
cat .settings/org.eclipse.buildship.core.prefs 
echo $JAVA_HOME
rm -rf .settings/
git pull
gradle clean
unset https_proxy http_proxy ftp_proxy JAVA_OPTS 
gradle clean
gradle clean build
ll -rt
cat settings.gradle 
cat .project 
gradle build
unset https_proxy http_proxy ftp_proxy JAVA_OPTS 
gradle build
gradle clean build
cd dev/
cd git-repos/andy-git-area/java-projects/ajg-java/GameOracle/
cd gatherer/
ll
unset https_proxy http_proxy ftp_proxy JAVA_OPTS 
gradle clean build
cd dev/git-repos/andy-git-area/java-projects/ajg-java/GameOracle/gatherer/
unset https_proxy http_proxy ftp_proxy JAVA_OPTS 
gradle clean build
ll
cd dev
cd git-repos/andy-git-area/java-projects/ajg-java/GameOracle/gatherer/
ll
git status .
git commit -am "updates"
git push
git pull
git status
git status .
gradle clean build
cd dev/
find . -name "*.java" -exec grep -w Logger {} +
find . -name "*.java" -exec grep -w Logger {} + | grep import
find . -name "*.java" -exec grep -w Logger {} + | grep import  | grep WHITES
cd  git-repos/ARD-WHITESANDS/backend/microservice-fnol/
ll
cat pom.xml 
gvim pom.xml 
cls
ll
cd ..
find . -name build.gradle -exec grep logback {} +
cat ./tmp/microservice-frameworks/ktor/ktor-samples/app/chat/build.gradle
cat ./ARD-ADID/neo-backend/build.gradle
cd andy-git-area/java-projects/
pwd
ll
cd ajg-
cd ajg-java/SoccerForecaster/
ll
cat build.gradle 
find . -name "*.java" -exec grep import | grep Logger
find . -name "*.java" -exec grep import {} + | grep Logger
find . -name "*.java" -exec grep import {} + | grep LogManager
cd ..
ll
cd ..
cd
ll
ll -rt
cls
ll
cd dev/git-repos/andy-git-area/
ll
cd java-projects/ajg-java/
cd GameOracle/
ll
find .
cls
cd
ll
find .
ll
cls
ll
cd Downloads
ll -rt
cd nginx-certs/
ll
cd jan2020/
pwd
ll
cd
cd dev/
cd git-repos/
cd azure-repos/
ll
find . -name "*.crt"
cd sca-auth
ll
git status .
unset https_proxy http_proxy ftp_proxy JAVA_OPTS 
git pull
git log nginx-repo.crt
ll
ll docker/
ll k8s/
pwd
ll
find . -type f -exec grep nginx-repo.crt {} +
cat azure-pipelines.yml 
gvim azure-pipelines.yml 
cls
ll
pwd
ll
cls
ll
gvim refresh.sh 
cls
ll
cls
ll
cd git
cd dev
cd ..
ll
kubectl get pods --all-namespaces
./check-git.sh 
pwd
ll
cd sca-auth
ll
git status
rm all.out 
pwd
ll
meld .
git status .
git ?
git --help
git status .
git checkout -- k8s/deployment.yaml nginx/nginx-openid-connect/frontend.conf
git status .
git diff refresh-all.sh
git commit -m "increase sleep to allow deleted resources to settle" refresh-all.sh
git push
git pull
git push
git pull
git status .
cd ..
ll
cls; ./check-git.sh 
pwd
ll
cd sca-auth
ll
gvim refresh-all.sh 
cd ../sca-auth-mock-insurance-api
ll
cat refresh.sh 
cd ..
pwd
ll
./check-git.sh 
cp check-git.sh revert-k8s-deployment-yml.sh
gvim revert-k8s-deployment-yml.sh 
ll
./revert-k8s-deployment-yml.sh 
./check-git.sh 
pwd
ll
cd sca-auth
ll
pwd
ll
./refresh-all.sh 
ll
cd ..
ll
./check-git.sh 
cd sca-auth
pwd
ll
git status .
pwd
ll
pwd
ll
pwd
ll
cd ..
ll
./check-git.sh 
./revert-k8s-deployment-yml.sh 
./check-git.sh 
cd sca-auth
ll
./refresh-all.sh |& tee all.out
unset https_proxy http_proxy ftp_proxy JAVA_OPTS 
cd ..
./check-git.sh 
cd sca-auth
./refresh-all.sh |& tee all.out
git pull
cd ..
cls
git status .
cd sca-auth
ll
git status .
git pull
git status .
cd ..
ll
cls
ll
cls
ll
./check-git.sh 
cd sca-auth
ll
./refresh-all.sh |& tee all.out
gvim all.out 
ll
cat refresh.sh 
cat refresh-all.sh 
kubectl get all
cd ..
./check-git.sh 
meld nginx/nginx-openid-connect/frontend.conf
cd sca-auth
ll
pwd
git status .
meld .
pwd
ll
pwd
find ~/ -name nginx-repo.crt
cd ..
./check-git.sh 
cd sca-auth
grep sac *
pwd
ll
git status .
git checkout -- k8s/deployment.yaml nginx/nginx-openid-connect/frontend.conf
git status .
cd ..
pwd
ll
./check-git.sh 
ll
./revert-k8s-deployment-yml.sh 
cd sca-auth
ll
git pull
cd ..
git pull
cd sca-auth
git pull
cd ..
pwd
ll
cd sca-auth
pwd
ll
find ~/ -name nginx-repo.crt
find ~/Downloads/ -name nginx-repo.crt
git log nginx-repo.crt
git log refresh.sh
find . -type f -exec grep nginx-repo.crt {} +
find . -type f -exec grep nginx-repo.crt {} + | grep -v all.out
cd docker/
ll
gvim Dockerfile 
cd ..
ll
cd sca-auth
pwd
ll
mv nginx-repo.crt nginx-repo.crt.old
mv nginx-repo.key nginx-repo.key.old
ll
./refresh.sh 
ll
cp ~/Downloads/
find ~/Downloads/ -name nginx-repo.crt
cls
ll
cp ~/Downloads/nginx-certs/jan2020/* .
ll
rm *.old
ll
./refresh.sh 
git status .
meld .
git checkout -- nginx/nginx-openid-connect/frontend.conf
git status .
cd ..
cls
ll
./check-git.sh 
./revert-k8s-deployment-yml.sh 
./check-git.sh 
cd sca-auth
ll
./refresh-all.sh |& tee all.out
kubectl get all
kubectl rm pod sca-auth-deployment-d4c54d57d-28nx6
kubectl delete pod sca-auth-deployment-d4c54d57d-28nx6
kubectl get all
kubectl delete pod sca-auth-deployment-d4c54d57d-xmpzd 
kubectl get all
ping sca-poc.nonprod-euiwebservice.co.uk
ping andy.grahame.com
nslookup google.com
nslookup https://dev-w611t8si.eu.auth0.com/
nslookup dev-w611t8si.eu.auth0.com
nslookup sca-poc.nonprod-euiwebservice.co.uk
java -version
exit
cd dev/git-repos/andy-git-area/java-projects/ajg-java/GameOracle/
cd gatherer/
ll
ll -rt
cat gatherer1.iml 
ll .idea/
ll .idea/libraries/
cd .idea/
ll
cat .name 
ll
cat jarRepositories.xml 
cat misc.xml 
cat vcs.xml 
cat workspace.xml 
cd ..
cat build.gradle 
find . | grep apache
find . -type f -exec grep apache {} +
find . -type f -exec grep apache {} + | grep -v ^import
find . -type f -exec grep apache {} + | grep -v import
gradle clean build
ll
cat build
cat build.gradle 
find . -name idea.log
ping sca-poc.nonprod-euiwebservice.co.uk
unset https_proxy http_proxy ftp_proxy JAVA_OPTS 
ping sca-poc.nonprod-euiwebservice.co.uk
pwd
ll
cd dev/
cd git-repos/
ll
cd azure-repos/
ll
./check-git.sh 
cd sca-auth
git checkout -- nginx/nginx-openid-connect/frontend.conf
cd ..
./check-git.sh 
ll
./revert-k8s-deployment-yml.sh 
./check-git.sh 
cd sca-auth
pwd
ll
./refresh-all.sh 
cd ..
unset https_proxy http_proxy ftp_proxy JAVA_OPTS 
j5zhkz6iwb2mfeynm2xqpzofr44hi5m4u6vd7vqurxphcvtt5qsq
./check-git.sh 
cd sca-auth
ll
./refresh-all.sh 
cd ..
./check-git.sh 
ll
gvim confluence-notes.txt 
cls
ll
cat check-git.sh 
cd ..
cd andy-git-area/java-projects/ajg-java/GameOracle/
ll
cd gatherer/
unset https_proxy http_proxy ftp_proxy JAVA_OPTS 
git pull
git status .
git checkout -- .gradle/buildOutputCleanup/buildOutputCleanup.lock
git status .
git pull
ll
gradle clean build
pwd
cls
ll
cd ..
cd azure-repos/
ll
cd sca-auth
ll
cd ..
ll
./check-git.sh 
java -version
ll -rt ~/Downloads
ll -rt ~/Downloads/
sudo dpkg -i ~/Downloads/jdk-13.0.1_linux-x64_bin.deb 
java -version
sudo update-alternatives --config java
cd dev/
pwd
cd git-repos/
pwd
ll
cd azure-repos/
ll
find . -name configure.sh
cd sca-auth
pwd
ll
cd nginx/
ll
ll conf/
cd ..
code .
cd ..
cls
ll
find . -type f -exec grep sca-auth-mock-landing-page-service {} +
rm sca-auth/all.out 
rm confluence-notes.txt 
cls
find . -type f -exec grep sca-auth-mock-landing-page-service {} +
java -version
pwd
cd /opt/
ll
sudo cp ~/Downloads/jdk-13.0.1_linux-x64_bin.tar.gz .
ll
sudo tar xzvf jdk-13.0.1_linux-x64_bin.tar.gz 
ll
cd
sudo update-alternatives --config java
sudo update-alternatives --install /usr/bin/java java /opt/jdk-13.0.1/bin/java 1
sudo update-alternatives --config java
java -version 
cls
ll
java -version
cd dev/git-repos/andy-git-area/java-projects/ajg-java/GameOracle/
ll
find Bluebird/
cd Bluebird/
pwd
ll
cat Bluebird.iml 
cd .idea/
ll
pwd
ll
cd ..
ll
cd Bluebird-gradle/
ll
cat build.gradle 
ll
find . -type f -exec grep -i artifact {} +
find . -type f -exec grep -i group {} +
cd ..
ll
rm -rf Bluebird
rm -rf Bluebird-gradle
ll
mv Bluebird-gradle2/ Bluebird
cd Bluebird/
ll
find . -type f -exec grep -i "Bluebird-gradle" {} +
gvim .idea/workspace.xml 
find . -type f -exec grep -i "Bluebird-gradle" {} +
gvim settings.gradle 
pwd
ll
find . -type f -exec grep -i "Bluebird-gradle" {} +
gradle clean
pwd
ll
gradle clean build
./gradlew clean build
pwd
gradle -version
gradle -v
echo $GRADLE_HOME 
sdk install gradle 6.0.1
unset https_proxy http_proxy ftp_proxy JAVA_OPTS 
sdk update
cd dev/git-repos/andy-git-area/java-projects/ajg-java/GameOracle/gatherer/
ll
ll *.iml
cat gatherer1.iml 
cd .idea/
ll
find libraries/
cat libraries/TheGatherer.xml
cat libraries/TheGatherer1.xml 
cd ..
grep apache build.gradle 
unset https_proxy http_proxy ftp_proxy JAVA_OPTS 
sdk install gradle 6.0.1
unset https_proxy http_proxy ftp_proxy JAVA_OPTS 
sdk install gradle 6.0.1
gradle -v
cd dev/
cd git-repos/andy-git-area/java-projects/ajg-java/
ll
cd GameOracle/
ll
cd Bluebird/
ll
gradle clean build
gradle clean build --warning-mode all
gvim build.gradle 
gradle clean build --warning-mode all
ll
cat settings.gradle 
ll gradle
ll gradle/wrapper/
cat gradle/wrapper/gradle-wrapper.properties 
gvim gradle/wrapper/gradle-wrapper.properties 
cd ..
cd gatherer/
find . -name "*.java"
cat ./src/main/java/dogs/red/nine/oracle/gatherer/GetResults.java
ll
ll src/
ll src/main/
ll src/main/resources/
cat src/main/resources/log4j2.xml 
ll
gradle clean
find src/
pwdll
pwd
ll
cd ..
ll
find data/
cpll
cat gatherer/build.gradle 
cd dev/git-repos/andy-git-area/java-projects/ajg-java/
pwd
ll
mv GameOracle/Bluebird/ .
ll
cp -r GameOracle/gatherer/src/main/java/dogs Bluebird/src/main/java/
find Bluebird/src/main/java/
ll
mkdir Bluebird/data
rmdir Bluebird/data
cp -r GameOracle/data/ Bluebird/
gvim Bluebird/build.gradle 
pwd
ll
meld 
cd Bluebird/
pwd
ll
gradle clean
gradle clean --scan
gradle clean --stacktrace
gradle clean
echo $http_proxy 
pwd
gradle clean
ll
cd ..
git add Bluebird/
git commit -m "New project" Bluebird/
unset https_proxy http_proxy ftp_proxy JAVA_OPTS 
git push
git pull
cd Bluebird/
ll
find build/ -ls
find build/ -ls | grep jar
java -jar build/libs/all-in-one-jar-1.0-SNAPSHOT.jar
gradle clean
git status .
git commit -am "mods"
git push
ll
gvim build/libs/all-in-one-jar-1.0-SNAPSHOT.jar 
git commit -am "mods"
git push
unset https_proxy http_proxy ftp_proxy JAVA_OPTS 
git pull
gradle clean build
gvim build/libs/Bluebird-1.0-SNAPSHOT.jar 
ll build/libs/Bluebird-1.0-SNAPSHOT.jar 
ll build/libs/Bluebird-1.0-SNAPSHOT.jar -h
ll
git rm -r .gradle
git commit . -m "removed .gradle from git"
git push
git pull
ll
rm -rf .gradle/
pwd
ll
pwd
git pull
git status .
git commit -am "removed .gradle from version control"
git push
git pull
ll
find ../.. -name .gitignore
find ../ -name .gitignore
ll .idea/.gitignore 
cat .idea/.gitignore 
find . -name gradle-app.setting
gvim .gitignore
git pull
git status .
git add  .gitignore gradle/wrapper/gradle-wrapper.jar
git status .
git commit -am "better control over waht gets saved to git"
git push
git pull
gradle clean build
pwd
ll
java -version
echo $JAVA_HOME
which java
ll /usr/bin/java
ll /etc/alternatives/java
ll /usr/lib/jvm/java-11-openjdk-amd64/
ll /opt/jdk-13.0.1/
pwd
grep JAVA_HOME ~/.*
gvim ~/.profile 
echo $JAVA_HOME
java -version
cd dev/git-repos/andy-git-area/java-projects/ajg-java/Bluebird/
ll
git pull
git status .
git push
gradle clean build
find . -type f -exec grep 11 {} +
find . -type f -exec grep 11 {} + | grep -v "\/data\/"
env | grep 11
env | grep 13
ll
cat build.gradle 
rm -rf .gradle/
unset https_proxy http_proxy ftp_proxy JAVA_OPTS 
gradle clean build
ll
find .gradle/
find .gradle/ -type f
find .gradle/ -type f -exec grep -i 13 {} +
find .gradle/ -type f -exec grep -i 11 {} +
gradle clean
ll
cat settings.gradle 
cd
cd .gradle/
ll
cat gradle.properties 
echo $JAVA_OPTS
gvim gradle.properties 
echo $JAVA_HOME
gvim gradle.properties 
ll /opt
ll
cd dev/
pwd
ll
cd git-repos/andy-git-area/java-projects/ajg-java/Bluebird/
pwd
ll
gradle clean
gradle clean build
java -jar build/libs/Bluebird-1.0-SNAPSHOT.jar 
git status
git status .
gvim .gitignore 
ll logs/
git rm -rf logs
git status .
git commit -am "more git tidying"
git push
rm -rf logs
ll
git status .
git pull
git push
gradle clean build
java -jar build/libs/Bluebird-1.0-SNAPSHOT.jar 
ll -rt
git pull
git status .
git commit -am "removed temp class"
git push
git status .
ll
find src/
gradle clean build
java -jar build/libs/Bluebird-1.0-SNAPSHOT.jar 
cls
ll
java -jar build/libs/Bluebird-1.0-SNAPSHOT.jar 
cls
java -jar build/libs/Bluebird-1.0-SNAPSHOT.jar 
cls
pwd
ll
git status .
git diff build.gradle
git commit -am "populate full season table - just need to sort it now"
git push
git pull
git status .
git pull
git status .
git push
java -jar build/libs/Bluebird-1.0-SNAPSHOT.jar 
git status .
git commit -am "first stab at sorting tables"
git push
git pull
ll
cls
cd
ll
cd dev/git-repos/andy-git-area/java-projects/ajg-java/Bluebird/
git status .
git pull
gradle clean build
java -jar build/libs/Bluebird-1.0-SNAPSHOT.jar 
cls
git status .
cd
ll
cd
cd dev/git-repos/andy-git-area/java-projects/ajg-java/SoccerForecaster/
ll
code .
pwd
ll -rt
find ~/ -name ".py"
find src/
cat src/main/java/dogs/red/nine/footy/BothTeamsToScore.java
cd dev/git-repos/andy-git-area/java-projects/ajg-java/Bluebird/
ll
ll data/
cd data/
cat fixtures.csv 
private File getResultsDataFileFromRemote(final String datafileUrl) {
final String fname = FilenameUtils.getName(datafileUrl);
final File localFile = new File(AppConstants.DATA_DIR, fname);
try {
logger.debug("getting results from data url : " + datafileUrl);
InputStream in = new URL(datafileUrl).openStream();
Files.copy(in, Paths.get(localFile.toURI()), StandardCopyOption.REPLACE_EXISTING);
logger.debug("set up local file : " + fname);
} catch (Exception ex) {
logger.debug("using existing data file (if one exists), as couldnt get file [" + datafileUrl + "] ex: "
+ ex.getClass().getName() + ", " + ex.getLocalizedMessage());
}
return localFile;
}cls
cls
ll
cd dev/git-repos/andy-git-area/java-projects/ajg-java/Bluebird/
git pull
unset https_proxy http_proxy ftp_proxy JAVA_OPTS 
git pull
gradle clean build
java -jar build/libs/Bluebird-1.0-SNAPSHOT.jar 
cls
ll
cls
ll
find src/
find ../SoccerForecaster/src/ -name GetFixtures.java
cp ../SoccerForecaster/src/main/java/dogs/red/nine/footy/GetFixtures.java src/main/java/dogs/red/nine/oracle/gatherer/
find ../SoccerForecaster/src/ -name FixtureData.java
cp ../SoccerForecaster/src/main/java/dogs/red/nine/footy/FixtureData.java src/main/java/dogs/red/nine/oracle/data/
git status .
git commit -am "added fixture classes"
git push
git pull
pwd
ll
git status .
git revert --abort
git status .
gradle clean build
cat ../SoccerForecaster/src/main/java/dogs/red/nine/footy/Forecaster.java 
ll
cls
java -jar build/libs/Bluebird-1.0-SNAPSHOT.jar 
cls
ll
java -jar build/libs/Bluebird-1.0-SNAPSHOT.jar 
pwd
ll
cd data/
ll
cp ../sample-fixtures.csv ../sample_fixtures_used_for_dev_mode.csv
java -jar build/libs/Bluebird-1.0-SNAPSHOT.jar 
cd ..
java -jar build/libs/Bluebird-1.0-SNAPSHOT.jar 
ll
cat sample-fixtures.csv 
cat sample-fixtures.csv | grep -i city
ll
cp  sample-fixtures.csv data/sample_fixtures_used_for_dev_mode.csv 
java -jar build/libs/Bluebird-1.0-SNAPSHOT.jar 
cls
git status .
rm sample_fixtures_used_for_dev_mode.csv 
git status .
git commit -am "monday mods"
git push
git pull
git status
git status .
pwd
ll -rt
java -jar build/libs/Bluebird-1.0-SNAPSHOT.jar 
find . -type f -exec grep Gatherer
find . -type f -exec grep Gatherer {} +
find . -type f -exec grep Gatherer {} + | grep -v Binary
gradle clean
find . -type f -exec grep Gatherer {} + | grep -v Binary
java -jar build/libs/Bluebird-1.0-SNAPSHOT.jar 
cls
ll
clsll
cls
ll
java -jar build/libs/Bluebird-1.0-SNAPSHOT.jar 
ll
cat build.gradle 
pwd
ll
git status .
git commit -am "started the forecast work"
unset https_proxy http_proxy ftp_proxy JAVA_OPTS 
git push
git pull
git status .
git pull
gradle clean build
java -jar build/libs/Bluebird-1.0-SNAPSHOT.jar 
gradle clean build; java -jar build/libs/Bluebird-1.0-SNAPSHOT.jar 
cls
ll
gvim notes.txt
ll
cd data/
ll
rm E0.csv.1 
git status .
git rm E0.csv.1
git status .
ll
cat dataUrls.txt 
cat getFootyData.sh 
gvim getFootyData.sh 
./getFootyData.sh 
ll -rt
mv E0.csv E0.csv.bak
ll
./getFootyData.sh 
pwd
ll
cat fixtures.csv 
pwd
ll
ll *.csv
git add *.csv
git status .
ll
meld E0.csv E0.csv.bak 
gvim getFootyData.sh 
./getFootyData.sh 
ll
cat E1.csv
ll
rm B1.csv D1.csv E0.csv E1.csv  E2.csv
ll
rm E3.csv EC.csv F1.csv I1.csv N1.csv P1.csv SC0.csv SC1.csv SC2.csv SC3.csv SP1.csv
ll
rm *.csv.1
ll
./getFootyData.sh 
ll
meld E0.csv E0.csv.bak 
ll
git add *.csv
git status .
cd ..
git status .
git commit -am "more mods"
git push
git pull
java -jar build/libs/Bluebird-1.0-SNAPSHOT.jar > x.x; gvim x.x
java -jar build/libs/Bluebird-1.0-SNAPSHOT.jar > x.x
find . -name Team.java
find . -name Teams.java
mv ./src/main/java/dogs/red/nine/oracle/data/Team.java ./src/main/java/dogs/red/nine/oracle/data/Teams.java ~/sandbox/
git status .
gradle clean build
java -jar build/libs/Bluebird-1.0-SNAPSHOT.jar 
cls
find . -name TeamStats.java
mv ./src/main/java/dogs/red/nine/oracle/data/TeamStats.java  ~/sandbox/
gradle clean build
gradle --warning-mode all clean build
java -jar build/libs/Bluebird-1.0-SNAPSHOT.jar 
cls
ll
pwd
find . -name "*.java" -exec grep getFixtureData {} +
find . -name "*.java" -exec grep -l getFixtureData {} +
find . -name "*.java" -exec grep -n getFixtureData {} +
find . -name "*.java" -exec grep -nw process  {} +
find . -name "*.java" -exec grep -nw generateForecastData  {} +
java -jar build/libs/Bluebird-1.0-SNAPSHOT.jar 
find . -name "*.java" -exec grep -n "Adding Match"  {} +
find . -name "*.java" -exec grep -n "storing table"  {} +
java -jar build/libs/Bluebird-1.0-SNAPSHOT.jar 
git status .
rm x.x
rm data/E0.csv.bak data/fixtures.csv.1
git status .
git commit -am "refactored and removed unused classes"
git push
git pull
git status .
gradle clean build
java -jar build/libs/Bluebird-1.0-SNAPSHOT.jar > x.x; gvim x.x
java -jar build/libs/Bluebird-1.0-SNAPSHOT.jar > x.x
git commit -am "mods"
git push
git pull
gradle clean build
java -jar build/libs/Bluebird-1.0-SNAPSHOT.jar 
cls
ll
pwd
java -jar build/libs/Bluebird-1.0-SNAPSHOT.jar 
gradle clean build
java -jar build/libs/Bluebird-1.0-SNAPSHOT.jar 
cls
ll
cls
ll
rm x.x
ll
cat notes.txt 
rm notes.txt 
ll
find . -name sample-fixtures.csv 
cls
ll
cat build.gradle 
cd
cd dev/git-repos/andy-git-area/
cd ..
cd azure-repos/
ll
cd sca-auth
git pull
cd Documents
ll
find . -type f -exec grep "git pull" {} +
cd azure-devops/
ll
find . -type f -exec grep "git pull" {} +
grep git azure-k8s-notes.txt 
cd dev/
cd git-repos/
cd azure-repos/sca-auth
ll
git pull
git status .
env | grep proxy
git status .
cd ..
ll
rm .confluence-notes.txt.un~ 
ll
cd
cd Downloads
ll -rt
cd
find . -type f -exec grep "git pull" {} + > gp.log
gvim gp.log 
rm gp.log 
pwd
ll
git status
cd -
cd
cd dev/git-repos/azure-repos/
ll
cd sca-auth
git pull
unset https_proxy http_proxy ftp_proxy JAVA_OPTS 
git pull
cls
ll
cd dev/git-repos/andy-git-area/java-projects/ajg-java/Bluebird/
ll
cat build.gradle 
pwd
cd
cd dev/git-repos/andy-git-area/java-projects/ajg-java/Bluebird/
gradle clean build
gradle clean
git status
git status .
git commit -am "started the merit table work"
git push
git pull
ngrok
cd
cd .kube
ll
cat config
pwd
ll
cp config config.31012020
gvim config
kubectl config current-context
kubectl config get-contexts
kubectl config use-context rnd-aks
kubectl config current-context
kubectl config get-contexts
kubectl get pods --all-namespaces 
kubectl get
kubectl api-resources 
kubectl get all
env | grep proxy
unset https_proxy http_proxy ftp_proxy JAVA_OPTS 
kubectl get pods
kubectl get pods --all-namespaces 
cd dev/
pwd
ll
cd sandbox/
ll
mkdir twilio
cd twilio/
ll
mkdir day1
cd day1/
pwd
ll
javac -version
echo $JAVA_HOME
which java
ll /opt/
update-alternatives --config java
pwd
java -version
update-alternatives --config javac
sudo update-alternatives --install /usr/bin/javac java /opt/jdk-13.0.1/bin/javac 1
update-alternatives --config javac
sudo update-alternatives --install /usr/bin/javac javac /opt/jdk-13.0.1/bin/javac 1
update-alternatives --config javac
sudo update-alternatives --config javac
javac -version
jar -version
update-alternatives --config jar
sudo update-alternatives --install /usr/bin/jar jar /opt/jdk-13.0.1/bin/jar 1
sudo update-alternatives --config jar
jar
cls
ll
ll -rt ~/Downloads
ll -rt ~/Downloads/
cp ~/Downloads/twilio-7.47.3.jar .
ll
cp ~/Downloads/twilio-7.47.3-jar-with-dependencies.jar .
pwd
ll
gvim build.gradle-ag
cls
ll
gradle init
pwd
ll
gvim build.gradle 
cls
pwd
ll
gvim build.gradle 
gradle clean
gradle clean build
unset https_proxy http_proxy ftp_proxy JAVA_OPTS 
gradle clean build
pwd
ll
find build/
find src/
cat src/main/java/make_voice_call/App.java
cat build.gradle 
gvim build.gradle 
gradle clean build
ll
gradle clean build
pwd
ll
ll build/libs/
java -cp twilio-7.47.3-jar-with-dependencies.jar -jar build/libs/make_voice_call.jar 
mv build.gradle build.gradle.sav
cp /home/agrahame/dev/git-repos/andy-git-area/java-projects/ajg-java/Bluebird/build.gradle .
meld build.gradle build.gradle.sav 
gradle clean build
meld build.gradle build.gradle.sav 
gradle clean build
meld build.gradle /home/agrahame/dev/git-repos/andy-git-area/java-projects/ajg-java/Bluebird/build.gradle
gradle clean build
gradle clean
gradle clean build
gradle clean build --info
gradle clean build --debug
cls
gradle clean build --debug
gradle clean build --stacktrace
echo $GRADLE_HOME 
export GRADLE_USER_HOME .
pwd
export GRADLE_USER_HOME=/home/agrahame/dev/sandbox/twilio/day1
echo $GRADLE_USER_HOME 
gradle clean
gradle clean build --stacktrace
ll
find gradle/
rm -rf gradle
rm -rf .gradle/
gradle clean build --stacktrace
pwd ll
ll
ll build/libs/
java -jar build/libs/make_voice_call-1.0-SNAPSHOT.jar 
gvim build/libs/make_voice_call-1.0-SNAPSHOT.jar
gradle clean build
gvim build/libs/make_voice_call-1.0-SNAPSHOT.jar
java -jar build/libs/make_voice_call-1.0-SNAPSHOT.jar 
cls
echo $PATH
echo $PATH | grep gradle
gradle -v
cd ..
ll
ll day1/
pwd
ll
mkdir bot1
cd bot1
gradle init
ll
gvim build.gradle 
find src/
gvim src/main/java/AutopilotApp/App.java 
gradle run
unset https_proxy http_proxy ftp_proxy JAVA_OPTS 
gradle run
gvim src/main/resources/dynamicsay.json
gvim src/main/java/AutopilotApp/App.java 
gradle run
gvim src/main/java/AutopilotApp/App.java 
gradle run
cat src/main/java/AutopilotApp/App.java 
ll src/main/resources/
cat src/main/resources/dynamicsay.json 
cls
ll
cd ..
ll
mkdir warm-xfer
cd warm-xfer/
pwd
ll
git clone https://github.com/TwilioDevEd/warm-transfer-servlets.git
ll
cd warm-transfer-servlets/
ll
cd ..
mkdir ivr
cd ivr/
git clone https://github.com/TwilioDevEd/ivr-recording-servlets.git
ll
cd ivr-recording-servlets/
ll
find src/
code .
cd ..
ll
cd ../day1/
ll
find src/
cat src/main/java/make_voice_call/MakePhoneCall.java
cd ..
ll
cd bot1/
ll
pwd
ll
cd ..
pwd
mkdir dale-azure
cd dale-azure/
pwd
ll
pwd
ll
cls
ll
az login
kubectl config current-context
pwd
ll
cls
cd ..
ll
cd bot1/
ll
gradle clean
du -sch
ll
cd ..
zip -r bot.zip bot1/
ll
pwd
ll
cp -r bot1 chat
cd chat/
pwd
ll
find . | grep bot
cd ..
zip -r chat.zip chat/
pwd
cd dev/git-repos/andy-git-area/java-projects/
mkdir twilio
cd twilio/
cp -r /home/agrahame/dev/sandbox/twilio/bot1 .
ll
cd ..
pwd
ll
git add twilio/bot1/ilio
git add twilio/
git status .
git commit -m "added twilio project" twilio/
git push
pwd
ll
cd twilio/
ll
cd bot1/
find .
gvim ./src/main/resources/dynamicsay.json
cd ..
ll
mkdir node.js
cd node.js/
ll
node --version
npm install twilio
unset https_proxy http_proxy ftp_proxy JAVA_OPTS 
npm install twilio
unset https_proxy http_proxy ftp_proxy JAVA_OPTS 
env
env | grep 199
env | grep -i proxy
cd dev/git-repos/andy-git-area/java-projects/ajg-java/Bluebird/
ll
unset https_proxy http_proxy ftp_proxy JAVA_OPTS 
git pull
cls
cd
ll
cd .npm/
ll
find . -type f
du -sch
npm cache clean
npm cache verify
sudo npm cache verify
npm config
npm config list
find . -type f -exec grep https-proxy {} +
find . -type f -exec grep -n https-proxy {} +
find . -type f -exec grep -l https-proxy {} +
cd ..
find . -type f -exec grep -l https-proxy {} +
gvim .npmrc 
npm config list
npm config set registry http://registry.npmjs.org/
npm config list
unset https_proxy http_proxy ftp_proxy JAVA_OPTS 
node --version
npm --version
npm install twilio
unset https_proxy http_proxy ftp_proxy JAVA_OPTS 
npm install twilio
cat  /home/agrahame/.npm/_logs/2020-02-04T08_15_47_352Z-debug.log
ll /etc/ssl/certs/
pwd
ll
cat  /home/agrahame/.npm/_logs/2020-02-04T08_15_47_352Z-debug.log
npm install twilio
cd dev/git-repos/andy-git-area/java-projects/
ll
cd node.js/
pwd
ll
npm install twilio
cd..
cd ..
cd twilio/
pwd
mkdir node.js
cd node.js/
gvim send_sms.js
node send_sms.js 
npm install twilio
npm init
ll
npm install twilio
node send_sms.js 
ll -rt
cat package.json 
find node_modules/
npm install express
gvim server.js
npm install -g twilio-cli
man npm install
sudo npm install -g twilio-cli
cls
cd
cd dev/git-repos/andy-git-area/java-projects/ajg-java/Bluebird/
ll
pwd
ll
git pull
git status
git status .
git commit -am "started on merit table"
git push
git pull
git status .
git pull
git push
cd
cd dev/
ll -rt
cd sandbox/
pwd
ll
find kafka
cd dev/git-repos/andy-git-area/java-projects/ajg-java/Bluebird/
pwd
ll
ll -rt
rm x.out fixtures.csv*
ll
git pull
git status .
cat todo.txt 
rm todo.txt 
ll
git status .
pwd
git push
pwd
ll
cls
ll
ll data/
ll logs/
gvim logs/predictions.log 
cls
ll -rt
cat gradle.b
ll -rt
ll -rt ~/sandbox/
ll -rt ~/sandbox/sca-auth-mock-insurance-portal.andy/
cls
ll
find . -name "*.java" -exec grep fixtures.csv {} +
wget http://www.football-data.co.uk/fixtures.csv
unset https_proxy http_proxy ftp_proxy JAVA_OPTS 
wget http://www.football-data.co.uk/fixtures.csv
cat fixtures.csv 
wget http://www.football-data.co.uk/fixtures.csv
cd
cd -
ll
ll -rt
wget http://www.football-data.co.uk/fixtures.csv
ll -rt
cls
ll
cd dev/git-repos/andy-git-area/java-projects/twilio/dialogflow-chat-with-twilio/integrations/dialogflow-integrations/
ll
cd twilio
ll
cat README.md 
cd ..
cat twilio/README.md 
cat twilio-ip/README.md 
cat twilio/README.md 
pwd
ll
cd dev/git-repos/
cd andy-git-area/
ll
cd java-projects/twilio/
ll
git pull
git diff         ajg-java/Bluebird/src/main/java/dogs/red/nine/oracle/AppConstants.java
git diff ../ajg-java/Bluebird/src/main/java/dogs/red/nine/oracle/AppConstants.java
git checkout --         ../ajg-java/Bluebird/src/main/java/dogs/red/nine/oracle/AppConstants.java
git pull
ll
cd dialogflow-chat-with-twilio/
ll
mkdir agents
cd agents/
ll -rt ~/Downloads
ll -rt ~/Downloads/
cp ~/Downloads/Pizza-Agent.zip .
ll
unzip Pizza-Agent.zip 
pwd
ll
mkdir Pizza
ll
mv agent.json entities/ intents/ package.json Pizza
pwd
ll
cd Pizza/
ll
cat package.json 
ll entities/
ll intents/
cd intents/
pwd
ll
cat order.pizza.json 
ll
cd ..
ll
code .
gcloud
gcloud alpha dialogflow agent describe
npm install dialogflow
unset https_proxy http_proxy ftp_proxy JAVA_OPTS 
npm install dialogflow
pwd
ll
cls
ll
cd ..
ll
cd ..
ll
cd dialogflow-integrations/
pwd
ll
gcloud config get-value project
gcloud init
cd ..
ll
cd agents/
ll
mkdir my-Admiral
ll -rt ~/Downloads/
cp ~/Downloads/my-Admiral.zip .
ll
unzip my-Admiral.zip -d insuranceCompany
ll
rmdir my-Admiral/
ll
cd insuranceCompany/
ll
cat agent.json 
ll
cd ..
ll
mkdir service-account-keys
cd service-account-keys/
ll -rt ~/Downloads/*.json
mv ~/Downloads/newFnol-252e12490620.json .
ll
cat newFnol-252e12490620.json 
cd ..
ll
cd google-home
ll
cd ../google-home-projects/
ll
ll newfnol/
ll newfnol/functions/
ll newfnol/functions/index.js 
ll newfnol/functions/surface.js 
cat newfnol/functions/surface.js 
cat newfnol/functions/index.js 
ll
ll fnol/
find fnol/
ll fnol/
ll fnol/functions/
cd ..
cd twilio/
pwd
ll
cd dialogflow-chat-with-twilio/
pwd
ll
mkdir integrations
cd integrations/
pwd
ll
git clone https://github.com/GoogleCloudPlatform/dialogflow-integrations.git
ll
cd dialogflow-integrations/
ll
gvim Dockerfile 
ll ..
ll ../..
ll ../../service-account-keys/
cp ../../service-account-keys/newFnol-252e12490620.json ./twilio-ip/
cd twilio-ip/
ll
gvim server.js 
cd ..
ll
cat Dockerfile 
find . -name Dockerfile
ll
cd ..
ll
cd ..
ll
ll integrations/
ll integrations/dialogflow-integrations/
ll
ll dialogflow-integrations/
pwd
ll
cat integrations/dialogflow-integrations/Dockerfile 
cat integrations/dialogflow-integrations/twilio-ip/server.js 
ll
ll dialogflow-integrations/
rm -rf dialogflow-integrations/
ll
cd integrations/
ll
cd dialogflow-integrations/
ll
cls
cd twilio-ip/
pwd
ll
gvim README.md 
ll
ll images/
cat server.js 
ll
mkdir test-client
ll -rt ~/Downloads/
cp ~/Downloads/twilio-chat-demo-js-master.zip ./test-client/
cd test-client/
pwd
ll
unzip twilio-chat-demo-js-master.zip 
pwd
ll
cd twilio-chat-demo-js-master/
ll
cls
ll
cp credentials.example.json credentials.example.json.sav
ll
mv credentials.example.json credentials.json
ll
gvim credentials.json ../../server.js 
ll
cat credentials.json 
npm install
npm start
find .. -name server.js
ll
cd ..
ll
cd ..
ll
find . -name server.js -ls
cd twilio
pwd
ll
gvim server.js ../twilio-ip/server.js 
cd ..
pwd
ll
gvim Dockerfile 
cat Dockerfile 
pwd
ll
cd twilio
ll
grep twilio-ip *
grep twilio-ip README.md 
grep twilio-ip package.json 
grep twilio-ip server.js 
cat server.js 
find . -type f -exec grep twilio-ip {} +
cd ..
ll
ll twilio-ip/
find . -name newFnol-252e12490620.json
cp ./twilio-ip/newFnol-252e12490620.json ./twilio/
find . -name newFnol-252e12490620.json
ll
cat Dockerfile 
ll
cd twilio
ll
cat server.js 
grep test server.js 
grep -i test server.js 
exit
cd dev/git-repos/andy-git-area/java-projects/twilio/
ll
cd dialogflow-chat-with-twilio/
ll
cd integrations/
ll
cd dialogflow-integrations/
ll
cd twi
cd twilio
ll
cat server.js 
cd ..
mkdir integ1
cd integ1/
pwd
ll
ll -rt ~/Downloads/
mv ~/Downloads/FeedbackBot-master.zip .
ll
unzip FeedbackBot-master.zip 
ll
cd FeedbackBot-master/
pwd
ll
find . -name "*.zip"
cd DialogFlow/
ll
unzip FeedbackBot.zip 
pwd
ll
gvim agent.json 
cd ..
ll
cd FeedbackBotAzureFunctions/
ll
cat StartCall.cs 
ll
grep -i dialog *
grep -i google *
ll
ll Properties/
ll Properties/PublishProfiles/
cat Properties/PublishProfiles/FeedbackBotJP\ -\ Web\ Deploy.pubxml 
ll
gvim *.cs
ll
cd ..
cd
cd sandbox/
ll
sb
pwd
ll
gvim res1.txt
gvim body1.txt
gvim res2.txt
gvim body2.txt
meld res1.txt res2.txt 
cat res1.txt 
cat res2.txt 
mv res2.txt body2.txt.2
mv body2.txt res2.txt
mv body2.txt.2 body2.txt
ll
meld res1.txt res2.txt 
cd
cd dev/git-repos/andy-git-area/java-projects/
mkdir node.js
cd node.js/
ll
mkdir tutorialspoint.com
ll
cd tutorialspoint.com/
pwd
ll
npm --version
node --version
gvim main.js
node main.js 
gvim README
gvim create-server.js
gvim main2.js
node main2.js 
cd ..
pwd
ll
git add tutorialspoint.com/
git commit -m "added node tutorial"
git push
ll
cls
ll
pwd
