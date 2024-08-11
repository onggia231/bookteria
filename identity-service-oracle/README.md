# Profile service oracle

## Prerequisites

Link: https://registry.hub.docker.com/r/doctorkirk/oracle-19c
Tao thu muc oracle-19c/oradata trong o D:/
docker pull doctorkirk/oracle-19c
docker run --name oracle-19c -p 1521:1521 -e ORACLE_SID=<nameSid> -e ORACLE_PWD=123456789 -e ORACLE_MEM=2000 -v D:/oracle-19c/oradata/:/opt/oracle/oradata doctorkirk/oracle-19c
