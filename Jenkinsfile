pipeline {
    agent any

    environment {
        PATH = "/usr/local/bin:$PATH"
    }

    stages {
        stage('Git Pull') {
            steps {
                echo "✅ GitHub에서 최신 코드 가져오는 중..."
                checkout scm
            }
        }

        stage('Gradle Build') {
            steps {
                echo "🛠️ Gradle로 빌드 중 (test 생략)..."
                sh './gradlew clean build -x test'
            }
        }

        stage('Docker Compose Rebuild & Deploy') {
            steps {
                echo "🐳 Docker Compose 다운 → 빌드 → 재기동..."
                sh '''
                docker-compose down -v
                docker-compose build
                docker-compose up -d
                '''
            }
        }
    }

    post {
        success {
            echo "🚀 배포 완료! 컨테이너가 성공적으로 기동되었습니다."
        }
        failure {
            echo "❌ 배포 실패. 로그를 확인하세요."
        }
    }
}