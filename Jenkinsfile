pipeline {
    agent any

    stages {
        // Stage 1: Checkout code from Git
        stage('Checkout') {
            steps {
                git branch: 'master', url: 'https://github.com/iheblahzami/projetdemo.git'
            }
        }

        // Stage 2: Build Angular frontend
        stage('Build Angular Frontend') {
            steps {
                script {
                    dir('front') {  // Navigate to the Angular frontend directory
                        sh 'npm install' // Install Angular dependencies
                        sh 'npm run build -- --prod' // Build Angular for production
                    }
                }
            }
        }

        // Stage 3: Build Spring Boot backend
        stage('Build Spring Boot Backend') {
            steps {
                script {
                    dir('back') { // Navigate to the Spring Boot backend directory
                        sh './mvnw clean package' // Build Spring Boot application using Maven wrapper
                    }
                }
            }
        }

        // Stage 4: Run unit tests
        stage('Run Unit Tests') {
            steps {
                script {
                    // Run Angular unit tests
                    dir('front') {
                        sh 'npm test -- --watch=false --browsers=ChromeHeadless' // Run Angular unit tests in headless mode
                    }
                    // Run Spring Boot unit tests
                    dir('back') {
                        sh './mvnw test' // Run Spring Boot unit tests using Maven wrapper
                    }
                }
            }
        }

        // Stage 5: Build and run Docker Compose
        stage('Docker Compose Build and Run') {
            steps {
                script {
                    // Ensure Docker Compose is installed
                    sh 'docker-compose --version'

                    // Build and start containers using Docker Compose
                    try {
                        sh 'docker-compose up -d --build'
                    } catch (Exception e) {
                        echo 'Failed to start Docker Compose: ${e.message}'
                        throw e // Fail the pipeline if Docker Compose fails
                    }
                }
            }
        }

        // Stage 6: Run Integration Tests (Optional)
        stage('Run Integration Tests') {
            steps {
                script {
                    // Run integration tests against the running containers
                    sh './run-integration-tests.sh' // Replace with your integration test command
                }
            }
        }

        // Stage 7: Stop Docker Compose
        stage('Stop Docker Compose') {
            steps {
                script {
                    // Stop and remove containers
                    sh 'docker-compose down'
                }
            }
        }
    }

    // Post-build actions
    post {
        success {
            echo 'Pipeline completed successfully!'
        }
        failure {
            echo 'Pipeline failed!'
        }
        always {
            echo 'Cleaning up workspace...'
            cleanWs() // Clean the workspace after the build
        }
    }
}