pipeline {
   agent  { label 'ubuntu' }
   options {
      buildDiscarder(logRotator(numToKeepStr: '2'))
      disableConcurrentBuilds() 
   }
   triggers {
      pollSCM('H/5 * * * * ')
   }
   tools {
      maven 'Maven 3.3.9'
      jdk 'JDK 1.8 (latest)'
   }
   stages {
      stage('Informations') {
          steps {
              echo "Branche we are building is : refs/heads/release90"
          }
      }
      stage('SCM operation') {
          steps {
              echo 'clean up netbeans sources'
              sh 'rm -rf netbeanssources'
              echo 'Get NetBeans sources'
              checkout([$class: 'GitSCM', branches: [[name: 'refs/heads/release90']], doGenerateSubmoduleConfigurations: false, extensions: [[$class: 'CloneOption', noTags: false, reference: '', shallow: true], [$class: 'MessageExclusion', excludedMessage: 'Automated site publishing.*'], [$class: 'RelativeTargetDirectory', relativeTargetDir: 'netbeanssources']], submoduleCfg: [], userRemoteConfigs: [[refspec: '+refs/tags/*:refs/remotes/origin/tags/*' , url: 'https://github.com/apache/incubator-netbeans/']]])
          }
      }
      stage('NetBeans Builds') {
          steps {
              dir ('netbeanssources'){
                  withAnt(installation: 'Ant (latest)') {
                      sh 'ant'
                      sh "ant build-javadoc -Djavadoc.web.root='http://bits.netbeans.org/9.0/javadoc' -Dmodules-javadoc-date='29 Jul 2018' -Datom-date='2018-07-29T12:00:00Z' -Djavadoc.web.zip=${env.WORKSPACE}/WEBZIP.zip"
                  }
              }
              archiveArtifacts 'WEBZIP.zip'
            }
      }
   }
}