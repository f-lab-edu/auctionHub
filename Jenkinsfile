pipeline {
    agent any

    stages {
        // Checkout Git repository
        stage('Checkout Git') {
            steps {
//                 checkout scm
                echo 'Git Checkout Success!'
            }
        }

        // Build repository
        stage('Build') {
            steps {
//                 sh './gradlew clean bootJar'
                echo 'Build repository Success!'
            }
        }

        // Test
        stage('Test') {
            steps {
//                 sh 'gradle test'
                echo 'Test Success!'
            }
        }
    }

    post {
        success {
            echo 'Build Success'
        }
    }
}
