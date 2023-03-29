# Zio-dog-file

## 1、打包
```shell
sbt 
universal:packageBin
```

## 2、运行
```shell
nohup ./bin/file-http -Xms512M > zio-dogfile.log 2>&1 &
```