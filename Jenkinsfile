pipeline {
    agent any

    tools {
        maven 'Maven-3'      // configure this name in Jenkins global tools
        jdk 'JDK-17'         // configure this name in Jenkins global tools
    }

    environment {
        DOCKER_IMAGE = "university/gate-entry"
        DOCKER_TAG = "latest"
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build with Maven') {
            steps {
                sh 'mvn -q -DskipTests clean package'
            }
        }

        stage('Build Docker Image') {
            steps {
                sh 'docker build -t ${DOCKER_IMAGE}:${DOCKER_TAG} .'
            }
        }

        stage('Push Docker Image') {
            when {
                expression { return env.DOCKER_REGISTRY_CREDENTIALS != null }
            }
            steps {
                script {
                    docker.withRegistry('', env.DOCKER_REGISTRY_CREDENTIALS) {
                        sh 'docker push ${DOCKER_IMAGE}:${DOCKER_TAG}'
                    }
                }
            }
        }

        stage('Deploy to Kubernetes') {
            when {
                expression { return fileExists('k8s/deployment.yaml') }
            }
            steps {
                sh 'kubectl apply -f k8s/deployment.yaml'
                sh 'kubectl apply -f k8s/service.yaml'
            }
        }
    }
}


