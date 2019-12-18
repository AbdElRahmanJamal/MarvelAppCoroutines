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

    stage ("Build: Clean") {
      steps {
        sh "y | $ANDROID_HOME/tools/bin/sdkmanager --licenses"
        sh "chmod a+x ./gradlew"
        sh "./gradlew clean --info"
      }
    }

    stage ("Build: Compile") {
      steps {
        sh "./gradlew build"
      }
    }


}

def sendmail(){
emailext attachLog: true, body: '''Dears,
HIIIII status for HIII - Build # 100:
Check console output at $BUILD_URL to view the results.
If you don't have access to Jenkins, Please find the Logs attached in the mail.

Best Regards,
TSS-DeploymentCoE''', subject: '$PROJECT_NAME - Build # $BUILD_NUMBER - $BUILD_STATUS!', to: 'abdelrahmangamal31@gmail.com'
}
