# bookteria
The bookteria project, a book social network

## Tong hop cau lenh docker

1. MySql

docker pull mysql:8.0.36-debian
docker run --name mysql-8.0.36 -p 3306:3306 -e MYSQL_ROOT_PASSWORD=root -d mysql:8.0.36-debian

2. Mongodb

docker pull bitnami/mongodb:7.0.11
docker run -d --name mongodb-7.0.11 -p 27017:27017 -e MONGODB_ROOT_USER=root -e MONGODB_ROOT_PASSWORD=root bitnami/mongodb:7.0.11

3. Kafka

File: docker-compose.ymal ( chua thong tin setup kafka )
Chay cmd o thu muc: D:\COURSE\SpringBoot\SpringBoot Devteria\bookteria>
-> docker-compose up -d

4. Neo4j

docker pull neo4j:latest
docker run --publish=7474:7474 --publish=7687:7687 -e 'NEO4J_AUTH=neo4j/123456' neo4j:latest

5. Oracle

Link: https://registry.hub.docker.com/r/doctorkirk/oracle-19c
Tao thu muc oracle-19c/oradata trong o D:/
docker pull doctorkirk/oracle-19c
docker run --name oracle-19c -p 1521:1521 -e ORACLE_SID=<nameSid> -e ORACLE_PWD=123456789 -e ORACLE_MEM=2000 -v D:/oracle-19c/oradata/:/opt/oracle/oradata doctorkirk/oracle-19c

6. KeyCloak

docker pull quay.io/keycloak/keycloak:25.0.0
docker run -d --name keycloak-25.0.0 -p 8180:8080 -e KEYCLOAK_ADMIN=admin -e KEYCLOAK_ADMIN_PASSWORD=admin quay.io/keycloak/keycloak:25.0.0 start-dev

## Tao system tren web KeyCloak (http://localhost:8180/)
- Create realm ( tao ten realm)
- Chon realm
    - Client:
        - Create client
            - General settings: (Client type: OpenID Connect, Clien ID: devteria_app, Name: Devteria Webapp) -> Next
            - Capability config: Client authentication: On, Authentication flow: Standard flow, Direct access grants, Implicit flow -> On
            - Login settings:
              - Root URL: http://localhost:3000
              - Home URL: http://localhost:3000
              - Valid redirect URIs: http://localhost:3000/authenticate
              - Valid post logout redirect URIs: http://localhost:3000/logout
              - Web origins: *
              -> Tao ra 1 client id, 1 client secret (trong tab Credentials) de phuc vu send 1 token
            - Service accounts roles (Duoc tao ra khi tich muc Service accounts roles) -> Assign role -> Tim den manage-users (Tich vao) -> Assign (Co role manage-users moi tuong tac duoc api admin)
    - User:
        - Create user: Username: baochau, email:ngobaochau0103@gmail.com, Firstname: Chau, Lastname: Bao
            - Credentials( Create password - 12345678): Temporary: off (Neu de on thi ban dau se bat tao password moi)


- Luu y:
- Duong dan api (discovery Endpoint): http://localhost:8180/realms/devteria/.well-known/openid-configuration (realms/devteria: realms theo ten da dat config tren web KeyCloak)
- Duong dan api (exchange Token): Postman phan Body khai: client_id, client_secret, username, password (user, pass da  dang ky tren web KeyCloak)
- Duong dan https://www.keycloak.org/docs-api/latest/rest-api/openapi.yaml: de xem api tuong tac voi KeyCloak va https://editor.swagger.io/: de giai ma file yaml kia
- Duong dan api (exchange Client Token): Clients -> devteria_app -> Service accounts roles (Tich vao cai nay) (Nhu the moi lay duoc token cua client - no dai dien cho client chu ko phai user)
- Duong dan api (get Users): Dung token exchange Client Token de goi api (Neu loi 403 xem config Service accounts roles)
- Tang thoi gian token (mac dinh 5 phut): Clients -> Advanced -> Access Token Lifespan -> Save
- Bao mat client_ai, client_secret

7. OAuth2

# Các bước cài để lấy client id
### Truy cập: https://console.cloud.google.com/welcome?_ga=2.131647823.1729457257.1722487233-718716992.1722487127&project=booming-premise-337208 (Nơi tạo project làm việc với Google)
### Tạo 1 project có tên Devteria và vào mục APIs & Services
### Chọn Credentials ( Đây là nơi làm việc với OAuth 2.0)
### Chọn CREATE CREDENTIALS và chọn tiếp OAuth client ID ( Tạo client Id)
### Chọn Configure consent screen -> Tích
   External -> Creat
### Điền mục App name, User support email, Developer contact information -> SAVE AND CONTINUE liên tục
### OAuth consent screen -> PUBLISH APP
### Quay lại mục Credentials ( có thể tạo được 1 Credentials mới )
### Credentials -> CREATE CREDENTIALS -> OAuth client ID
   -> Application type (chọn Web application), Name (Devteria web-app)
   -> Authorized JavaScript origins (http://localhost:3000) # Điền url ban đầu và có thể thêm nhiều url ở mục này
   -> Authorized redirect URIs (http://localhost:3000/authenticate) # Nó sẽ redirect khi login được, cũng có thể khai nhiều url mục này
### Chọn Create để tạo, n sẽ sinh ra Dialog OAuth client created có thông tin Client ID, Client secret ( 2 thông tin này quan trọng không để public)

# Xem thong tin json
### Chon Project Devteria da tao -> Credentials -> OAuth 2.0 Client IDs (Chon name project) -> Click icon cham than !
   {
   "web":{
   "client_id":"...................apps.googleusercontent.com", // Xác định application là ai
   "project_id":"devteria-431204",
   "auth_uri":"https://accounts.google.com/o/oauth2/auth", // địa chỉ chuẩn để lấy thông tin
   "token_uri":"https://oauth2.googleapis.com/token",
   "auth_provider_x509_cert_url":"https://www.googleapis.com/oauth2/v1/certs", // verify token
   "client_secret":"...................", // không được lộ
   "redirect_uris":["http://localhost:3000/authenticate"],
   "javascript_origins":["http://localhost:3000"]
   }
   }

8. Build Docker image

### Build project
docker build -t <account>/identity-service:0.0.1 . (tên account trên docker destop - namngoc231)
### Run project
docker run -d identity-service:0.0.1

## Docker guideline
### Create network:
`docker network create devteria-network` (co the doi ten khac namngoc-network - tạo 1 network để kết nối đến docker)
### Start MySQL in devteria-network ( run Sql docker với network đã tạo)
`docker run --network devteria-network --name mysql -p 3306:3306 -e MYSQL_ROOT_PASSWORD=root -d mysql:8.0.36-debian`
### Run your application in devteria-network
`docker run --name identity-service --network devteria-network -p 8080:8080 -e DBMS_CONNECTION=jdbc:mysql://mysql:3306/identity_service identity-service:0.0.1`

# Publish Docker image lên Docker Hub
Đăng ký tài khoản trên docker hub rồi login trên docker desktop
Chạy lệnh để Build Docker image (docker build -t <account>/identity-service:0.0.1 .)
Chạy: docker image push <account>/identity-service:0.0.1 (de push len docker hub)
Kiem tra sau khi push:
Vao web https://hub.docker.com/ -> Repositories (Xem image da push len)
Chon Public View -> Docker Pull Command de tai (docker pull namngoc231/identity-service)
-> Chay cmd: docker pull namngoc231/identity-service:0.0.1 (kem them version)
Chay lenh run: docker run --name identity-service --network devteria-network -p 8080:8080 -e DBMS_CONNECTION=jdbc:mysql://mysql:3306/identity_service namngoc231/identity-service:0.0.1


## Các lệnh docker
docker image ls (xem image dang quan ly)
docker image rm identity-service:0.0.1 (Xoa REPOSITORY:TAG)
