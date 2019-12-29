properties = null
version_name = null
special_build = null

pipeline {
  agent any
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

 
}//pipeline

