pipeline {
    agent any

    environment {
        IMAGE_NAME = "pavelglinskiy/springtodo"
        IMAGE_TAG  = "latest"
    }

    stages {

        stage('Checkout') {
            steps {
                checkout scm
                script {
                    // Получаем текущую ветку через Jenkins переменную
                    currentBranch = env.GIT_BRANCH?.replaceAll(/^origin\//, '') ?: 'unknown'
                    echo "Current branch: ${currentBranch}"
                }
            }
        }

        stage('Build & Test (Maven)') {
            steps {
                bat 'mvn clean install'
            }
        }

        stage('Docker Build') {
            when {
                expression {
                    return currentBranch == 'main'
                }
            }
            steps {
                bat 'docker build -t %IMAGE_NAME%:%IMAGE_TAG% .'
            }
        }

        stage('Docker Login & Push') {
            when {
                expression {
                    return currentBranch == 'main'
                }
            }
            steps {
                withCredentials([
                    usernamePassword(
                        credentialsId: 'docker-hub-credentials',
                        usernameVariable: 'DOCKER_USER',
                        passwordVariable: 'DOCKER_PASS'
                    )
                ]) {
                    bat '''
                    docker login -u %DOCKER_USER% -p %DOCKER_PASS%
                    docker push %IMAGE_NAME%:%IMAGE_TAG%
                    '''
                }
            }
        }
    }

    post {
        success {
            echo 'Pipeline SUCCESS'
        }
        failure {
            echo 'Pipeline FAILED'
        }
    }
}
