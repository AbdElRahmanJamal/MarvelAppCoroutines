properties = null
version_name = null
special_build = null

pipeline {
  agent {label 'Android'}
  environment {
        SLACK_COLOR_DANGER  = '#E01515'
        SLACK_COLOR_INFO    = '#439FE0'
        SLACK_COLOR_WARNING = '#FFC300'
        SLACK_COLOR_GOOD    = '#1B9600'
  }
  stages {
      stage ('prepare') {
            steps {
                script {
                    properties = readProperties file: 'app/config.properties'
                    echo ">>>>>>>>\n${properties}\n<<<<<<<"
                    version_name = properties.VERSION_NAME.replace(".","-")
                    special_build = properties.SPECIAL_BUILD
                }
            }
        }
    stage ("Notifications: Build Started") {
      steps {
//        sendslack("Started","${env.SLACK_COLOR_INFO}")
//       sendmail()
            echo "PipeLine Started"
            echo "Build Flavor ${properties.JENKINS_BUILD_FLAVOR}"
            echo "Build Name   ${properties.VERSION_NAME}"
            echo "Build code   ${properties.VERSION_CODE}"
            echo "Jenkins Build version ${version_name}"
      }
    }
    stage ("Build: Clean") {
      steps {
        sh "y | $ANDROID_HOME/tools/bin/sdkmanager --licenses"
        sh "chmod a+x ./gradlew"
        sh "./gradlew clean --info"
      }
    }

//    stage ("Build: Compile") {
//      steps {
//        sh "./gradlew build"
//      }
//    }
   stage ("test: UT, CoCo") {
         steps {
           sh "./gradlew jacocoTestReport"
         }
      }

    stage ("test: Sonar Static Analysis") {
      steps {
        sh "./gradlew sonarqube"
      }
   }

    stage ("Build: Assemble the APK ") {
    when {
        expression {special_build ==~ /(true)/ || (env.GIT_BRANCH ==~ /(Develop\/${properties.VERSION_NAME})/ && !env.CHANGE_ID)}
        }
      steps {
        sh "./gradlew assemble${properties.JENKINS_BUILD_FLAVOR}Release"
      }
    }

//    stage ("Sign APK") {
//      steps {
//        signAndroidApks (
//          keyStoreId: "TO be updated later!",
//          keyAlias: "vfhudatadevelopment",
//          archiveSignedApks : false,
//          archiveUnsignedApks : false,
//          apksToSign: "app/build/outputs/apk/${properties.JENKINS_BUILD_FLAVOR}/release/MAH-app-${properties.JENKINS_BUILD_FLAVOR}-release-${properties.VERSION_NAME}-${properties.VERSION_CODE}.apk",
//        )
//      }
//    }

    stage ("App Upload to Nexus") {
      when {
        expression {special_build ==~ /(true)/ || (env.GIT_BRANCH ==~ /(Develop\/${properties.VERSION_NAME})/ && !env.CHANGE_ID)}
             }
      steps {
      nexusArtifactUploader(
          credentialsId: 'Nexus_K8t',
          nexusUrl: 'nexus-vfhu.skytap-tss.vodafone.com',
          nexusVersion: 'nexus3',
          protocol: 'https',
          repository: 'VFHU-Android-Releases',
          groupId: 'tsse.vfhu.myvodafoneapp',
          version: "${properties.VERSION_NAME}",
          artifacts: [
            [artifactId: "${properties.JENKINS_BUILD_FLAVOR}",
            classifier: "${properties.VERSION_CODE}",
            file: "app/build/outputs/apk/${properties.JENKINS_BUILD_FLAVOR}/release/MAH-app-${properties.JENKINS_BUILD_FLAVOR}-release-${version_name}-${properties.VERSION_CODE}.apk",
            type: 'apk']
          ]
        )
      }
    }

   stage ("Upload to Fabric Beta") {
   when {
        expression {special_build ==~ /(true)/ || (env.GIT_BRANCH ==~ /(Develop\/${properties.VERSION_NAME})/ && !env.CHANGE_ID)}
       }
      steps {
      sh "./gradlew crashlyticsUploadDistribution${properties.JENKINS_BUILD_FLAVOR}Release"
      }
    }
          
//   stage ("Stage Archive") {
//     steps {
//     tell Jenkins to archive the apks
//     archiveArtifacts artifacts: '**/*.apk', fingerprint: true
//     }
//   }
//   stage("Sign"){
//     steps {
//       signAndroidApks (
//           keyStoreId: "testSign",
//           keyAlias: "key0",
//           apksToSign: "**/*-unsigned.apk",
//       )
//     }
//   }
  } //stages
  post {
    aborted {
      echo "Sending ABORT message to Slack"
//      sendslack("ABORTED","${env.SLACK_COLOR_WARNING}")
//      sendMail()
    } // aborted

    failure {
      echo "Sending FAILURE message to Slack"
      //sendslack("FAILURE","${env.SLACK_COLOR_DANGER}")
//      sendmail()
    } // failure

    success {
      echo "Sending SUCCESS message to Slack"
      //sendslack("SUCCESS","${env.SLACK_COLOR_GOOD}")
//      sendmail()
    } // success

  } // post
}//pipeline

def sendslack(STATUS,COLOR){
slackSend baseUrl: 'https://vfhungary.slack.com/services/hooks/jenkins-ci/', 
channel: 'cicd-notifications', 
color: "$COLOR", 
message: "Build $STATUS - JOB: ${env.JOB_NAME}, BUILD #: ${env.BUILD_NUMBER}, (<${env.BUILD_URL}|Open>)", 
tokenCredentialId: 'jenkins-slack-integration'
}

