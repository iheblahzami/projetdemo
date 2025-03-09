pipeline {
    agent any

    parameters {
        string(name: 'GIT_REPO', defaultValue: 'https://github.com/iheblahzami/projetdemo.git', description: 'Git Repository URL')
    }

    environment {
        FRONTEND_DIR = 'front'
        BACKEND_DIR = 'back'
        CHROME_BIN = '/usr/bin/chromium-browser'
        DOCKER_BUILDKIT = '1'
        SLACK_CHANNEL = '#testdemo' // e.g., #build-notifications
        SLACK_CREDENTIALS_ID = 'slack-webhook-credentials' // Name of your Slack credential in Jenkins
    }

    stages {
        stage('Checkout') {
            steps {
                script {
                    try {
                        git branch: 'master', url: params.GIT_REPO
                    } catch (Exception e) {
                        error "Git checkout failed: ${e.message}"
                    }
                }
            }
        }
        // Start MySQL Database
        stage('Start MySQL Database') {
            steps {
                script {
                    try {
                        sh 'docker-compose up -d mysql' // Start the MySQL container
                        sh 'sleep 10' // Wait for the database to initialize
                    } catch (Exception e) {
                        error "Failed to start MySQL database: ${e.message}"
                    }
                }
            }
        }

        stage('Build Angular Frontend') {
            steps {
                script {
                    dir(FRONTEND_DIR) {
                        try {
                            sh 'npm install --cache .npm' // Caching dependencies
                            sh 'npm run build -- --configuration production'
                        } catch (Exception e) {
                            error "Frontend build failed: ${e.message}"
                        }
                    }
                }
            }
        }

        stage('Build Spring Boot Backend') {
            steps {
                script {
                    dir(BACKEND_DIR) {
                        try {
                            sh 'chmod +x ./mvnw'
                            sh './mvnw clean package'
                        } catch (Exception e) {
                            error "Backend build failed: ${e.message}"
                        }
                    }
                }
            }
        }

stage('Run Unit Tests') {
    environment {
        CHROME_BIN = '/usr/bin/chromium-browser' // Explicitly set Chrome binary for Jenkins
    }
    steps {
        script {
            try {
                sh 'echo "Using Chrome binary at: $CHROME_BIN"'
                dir(FRONTEND_DIR) {
                    sh 'npm test -- --watch=false --browsers=ChromeHeadless'
                }
                dir(BACKEND_DIR) {
                    sh './mvnw test'
                }
            } catch (Exception e) {
                error "Unit tests failed: ${e.message}"
            }
        }
    }
}



        stage('Docker Compose Build and Run') {
            steps {
                script {
                    try {
                        sh 'docker-compose --version'
                        sh 'docker-compose up -d --build'
                    } catch (Exception e) {
                        error "Docker Compose failed: ${e.message}"
                    }
                }
            }
        }



        stage('Stop Docker Compose') {
            steps {
                script {
                    try {
                        sh 'docker-compose down'
                    } catch (Exception e) {
                        error "Failed to stop Docker Compose: ${e.message}"
                    }
                }
            }
        }
    }

    post {
        success {
            slackSend(
                channel: env.SLACK_CHANNEL,
                color: 'good',
                message: "✅ SUCCESS: Job ${env.JOB_NAME} - Build ${env.BUILD_NUMBER}\nURL: ${env.BUILD_URL}"
            )
        }
        failure {
            slackSend(
                channel: env.SLACK_CHANNEL,
                color: 'danger',
                message: "❌ FAILURE: Job ${env.JOB_NAME} - Build ${env.BUILD_NUMBER}\nURL: ${env.BUILD_URL}"
            )
        }
        always {
            cleanWs()
        }
    }
}
