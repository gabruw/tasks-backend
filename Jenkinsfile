pipeline {
    agent any
    stages {
        stage ('Build Backend') {
            steps {
				dir ('backend') {
                    git credentialsId: 'github_credentials', url: 'https://github.com/gabruw/tasks-backend.git'
                    bat 'mvn clean package -DskipTests=true'
                    deploy adapters: [tomcat8(credentialsId: 'tomcat_credentials', path: '', url: 'http://localhost:8080/')], contextPath: 'tasks', war: 'target/tasks.war'
                }
            }
        }
        stage ('Unit Tests') {
            steps {
				dir ('backend') {
					bat 'mvn test'
				}
            }
        }
        stage ('Sonar Analysis') {
            environment {
                scannerHome = tool 'SONAR_SCANNER'
            }
            steps {
                withSonarQubeEnv('SONAR_LOCAL'){
                    bat "${scannerHome}/bin/sonar-scanner -e -Dsonar.projectKey=TasksBackend -Dsonar.host.url=http://localhost:9000 -Dsonar.login=567d51bdb046f71ac47fd73317393eb46ef35b1c -Dsonar.java.binaries=target -Dsonar.coverage.exclusions=**/.mvn/**,**/src/test/**,**/model/**,**Application.java"
                }
            }
        }
        stage ('Quality Gate') {
            steps {
                sleep(10)
                timeout(time: 1, unit: 'MINUTES') {
                    waitForQualityGate abortPipeline: true
                }     
            }
        }
        stage ('Deploy Backend') {
            steps {
				dir ('backend') {
					deploy adapters: [tomcat8(credentialsId: 'tomcat_credentials', path: '', url: 'http://localhost:8080/')], contextPath: 'tasks-backend', war: 'target/tasks-backend.war'
				}
            }
        }
        stage ('API Test') {
            steps {
                dir ('api-test') {
                    git credentialsId: 'github_credentials', url: 'https://github.com/gabruw/tasks-api-test.git'
                    bat 'mvn test'
                }
            }
        }
        stage ('Deploy Frontend') {
            steps {
                dir ('frontend') {
                    git credentialsId: 'github_credentials', url: 'https://github.com/gabruw/tasks-frontend.git'
                    bat 'mvn clean package -DskipTests=true'
                    deploy adapters: [tomcat8(credentialsId: 'tomcat_credentials', path: '', url: 'http://localhost:8080/')], contextPath: 'tasks', war: 'target/tasks.war'
                }
            }
        }
        stage ('Functional Test') {
            steps {
                dir ('functional-test') {
                    git credentialsId: 'github_credentials', url: 'https://github.com/gabruw/tasks-functional-test.git'
                    bat 'mvn test'
                }
            }
        }
        stage ('Health Check') {
            steps {
                dir ('functional-test') {
                    bat 'mvn verify -Dskip.surefire.tests'
                }
            }
        }
    }
    post {
        always {
            junit allowEmptyResults: true, testResults: 'target/surefire-reports/*.xml, api-test/target/surefire-reports/*.xml, functional-test/target/surefire-reports/*.xml, functional-test/target/failsafe-reports/*.xml'
            archiveArtifacts 'target/tasks-backend.war, frontend/target/tasks.war'
        }
        unsuccessful {
            emailext attachLog: true, body: 'See the attached log below', subject: 'Build $BUILD_NUMBER has failed', to: 'gabrielmarques.mg@gmail.com'
        }
        fixed {
            emailext attachLog: true, body: 'See the attached log below', subject: 'Build $BUILD_NUMBER is fine', to: 'gabrielmarques.mg@gmail.com'
        }
    }
}

