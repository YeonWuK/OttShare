pipeline {
    agent any

    stages {
        stage('Git Checkout') {
            steps {
                echo "✅ GitHub에서 최신 코드 가져오는 중..."
                checkout scm
            }
        }

        stage('Docker Compose Build & Deploy') {
            steps {
                echo "🐳 Docker Compose 다운 → 빌드 → 재기동..."
                sh '''
                docker-compose down -v || true
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
            echo "❌ 배포 실패. Jenkins 콘솔 로그를 확인하세요."
        }
    }
}