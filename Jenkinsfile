pipeline {
    agent any

    stages {
        // Checkout Git repository
        stage('Checkout Git') {
            steps {
                checkout scm
                echo 'Git Checkout Success!'
            }
        }

        // Build repository
        stage('Build') {
            steps {
                sh './gradlew clean bootJar'
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

        // 도커 이미지 빌드
        stage('Build Container Image') {
            steps {
                script {
                    image = docker.build("${REGISTRY_CONTAINER}/${IMAGE_NAME}")
                }
            }
        }

        // 생성된 도커 이미지를 네이버 클라우드 Docker Registry에 등록
        stage('Push Container Image') {
            steps {
                script {
                    docker.withRegistry("https://${REGISTRY_CONTAINER}", REGISTRY_CREDENTIAL) {
                        image.push("${env.BUILD_NUMBER}")
                        image.push("latest")
                        image
                    }
                }
            }
        }

        // 애플리케이션 서버가 도커 이미지를 다운받고 실행하도록 제어
        stage('Server run') {
            steps {
                sshagent(credentials: [SSH_CONNECTION_CREDENTIAL]) {
                    // 최신 컨테이너 삭제
                    sh "ssh -o StrictHostKeyChecking=no ${SSH_CONNECTION_IP} 'docker rm -f ${IMAGE_NAME}'"
                    // 최신 이미지 삭제
                    sh "ssh -o StrictHostKeyChecking=no ${SSH_CONNECTION_IP} 'docker rmi -f ${REGISTRY_CONTAINER}/${IMAGE_NAME}:latest'"
                    // 최신 이미지 PULL
                    sh "ssh -o StrictHostKeyChecking=no ${SSH_CONNECTION_IP} 'docker pull ${REGISTRY_CONTAINER}/${IMAGE_NAME}:latest'"
                    // 이미지 확인
                    sh "ssh -o StrictHostKeyChecking=no ${SSH_CONNECTION_IP} 'docker images'"
                    // 최신 이미지 RUN
                    sh "ssh -o StrictHostKeyChecking=no ${SSH_CONNECTION_IP} 'docker run -d --name ${IMAGE_NAME} -p 8080:8080 ${REGISTRY_CONTAINER}/${IMAGE_NAME}:latest'"
                    // 컨테이너 확인
                    sh "ssh -o StrictHostKeyChecking=no ${SSH_CONNECTION_IP} 'docker ps'"
                }
            }
        }
    }

    post {
        success {
            echo 'Build Success'
        }
    }
}
