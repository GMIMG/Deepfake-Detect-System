

환경설정

- jdk 13
- gradle 6.2.2
- spring boot



## jar 배포파일 만들기

### gradle 있을때

```shell
# ./spring-boot/application
gradle bootjar
```



### gradle 없을때

windows

```powershell
# cmd
# start.sh
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



