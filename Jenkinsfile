pipeline {
    agent any

    environment {
        ENV_FILE = "${WORKSPACE}/.env"
    }

    stages {
        stage('Git Checkout') {
            steps {
                echo "✅ GitHub에서 최신 코드 가져오는 중..."
                checkout scm
            }
        }

        stage('Load .env from SSM') {
            steps {
                echo "🔐 AWS SSM에서 .env 다운로드 중..."
                sh '''
                aws ssm get-parameter \
                  --name "/ottshare/env" \
                  --with-decryption \
                  --query "Parameter.Value" \
                  --output text > $WORKSPACE/.env

                set -o allexport
                source $WORKSPACE/.env
                set +o allexport
                '''
            }
        }

        stage('Docker Compose Build & Deploy') {
            steps {
                echo "🐳 Docker Compose 다운 → 빌드 → 재기동..."
                sh '''
                docker-compose down || true
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