
# A little Tips
## How to run the jt-808-server
* Clone the repository
* Use Gradle to reload all projects
* Enable plugin Lombok
* Project-Structure to set the Project SDK Config
* then Run / Debug it 

## Default Test Data
* Use Hex type to send the test data
* c
  —— terminalId = 768901005626
  —— authCode : DEJTNP
* 7E 80 01 00 05 76 89 01 00 56 26 00 01 06 E1 01 02 00 EF 7E 
  —— BuiltInMsgType{msgId=32769(0x8001), desc='平台通用应答'
  
## Modify Config Params
* `private int port = 6808;`
```
jt-808-server-spring-boot-stater/src/main/java/io/github/hylexus/jt808/boot/props/server/Jt808NettyTcpServerProps.java
```
* 