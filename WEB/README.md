# 웹서버 실행방법(dev)

환경설정

- Docker desktop 2.2.0.3
- Nginx
- jdk 13
- gradle 6.2.2
- spring boot 2.2.6
- python 3.6
- flask



## jar 배포파일 만들기

### gradle 있을때

```shell
# /WEB/spring-boot/application
gradle bootjar
```



### gradle 없을때

windows

```powershell
# cmd
docker run --rm -u gradle -v %cd%:/home/gradle/project -w /home/gradle/project gradle:6.2.2-jdk13 gradle build

# powershell
docker run --rm -u gradle -v ${pwd}:/home/gradle/project -w /home/gradle/project gradle:6.2.2-jdk13 gradle build
```

linux

```bash
# ubuntu
docker run --rm -u gradle -v $(pwd):/home/gradle/project -w /home/gradle/project gradle:6.2.2-jdk13 gradle build
```



## 실행

```shell
docker-compose up --build
```



