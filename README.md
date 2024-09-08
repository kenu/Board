# Board

## Redis
```sh
docker run -d --name redis-stack -p 6379:6379 -p 8001:8001 redis/redis-stack:latest
```
- http://localhost:8001

## Dev
1. copy `src/main/resources/application.properties.example` to `src/main/resources/application.properties`
2. run `./gradlew bootRun` or `gradlew.bat bootRun` (win)
- http://localhost:8080

## Optional
- google api key: https://console.cloud.google.com/apis/credentials
- naver api key: https://developers.naver.com/apps/#/list
- kakao api key: https://developers.kakao.com/console/app
