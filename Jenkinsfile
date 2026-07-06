def executarComando(String comando) {
    if (isUnix()) {
        sh comando
    } else {
        bat comando
    }
}

pipeline {
    agent any

    environment {
        APP_NAME = 'approval-flow-api'
        DOCKER_IMAGE = 'approval-flow-api'
    }

    stages {
        stage('Checkout') {
            steps {
                echo 'Baixando código do repositório...'
                checkout scm
            }
        }

        stage('Preparar ambiente') {
            steps {
                echo 'Preparando ambiente de build...'

                script {
                    if (isUnix()) {
                        sh 'chmod +x mvnw'
                    }
                }

                executarComando('java -version')
            }
        }

        stage('Testes') {
            steps {
                echo 'Executando testes unitários...'

                script {
                    if (isUnix()) {
                        sh './mvnw test'
                    } else {
                        bat 'mvnw.cmd test'
                    }
                }
            }

            post {
                always {
                    junit allowEmptyResults: true, testResults: 'target/surefire-reports/*.xml'
                }
            }
        }

        stage('Build Maven') {
            steps {
                echo 'Gerando build da aplicação...'

                script {
                    if (isUnix()) {
                        sh './mvnw clean package -DskipTests'
                    } else {
                        bat 'mvnw.cmd clean package -DskipTests'
                    }
                }
            }

            post {
                success {
                    archiveArtifacts artifacts: 'target/*.jar', fingerprint: true
                }
            }
        }

        stage('Docker Build') {
            steps {
                echo 'Construindo imagem Docker da API...'

                script {
                    executarComando("docker build -t ${DOCKER_IMAGE}:latest .")
                }
            }
        }

        stage('Deploy local com Docker Compose') {
            steps {
                echo 'Subindo aplicação e RabbitMQ com Docker Compose...'

                script {
                    executarComando('docker compose down')
                    executarComando('docker compose up -d --build')
                    executarComando('docker compose ps')
                }
            }
        }

        stage('Health Check') {
            steps {
                echo 'Validando se a API subiu corretamente...'

                script {
                    if (isUnix()) {
                        sh 'sleep 20'
                        sh 'curl -f http://localhost:8080/actuator/health'
                    } else {
                        bat 'powershell -Command "Start-Sleep -Seconds 20; Invoke-WebRequest -Uri http://localhost:8080/actuator/health -UseBasicParsing"'
                    }
                }
            }
        }
    }

    post {
        success {
            echo 'Pipeline finalizado com sucesso.'
        }

        failure {
            echo 'Pipeline falhou. Verifique os logs do Jenkins.'
        }

        always {
            echo 'Finalizando execução do pipeline.'
        }
    }
}