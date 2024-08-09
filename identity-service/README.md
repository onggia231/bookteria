# Identity service
This microservice is responsible for:
* Onboarding users
* Roles and permissions
* Authentication

## Tech stack
* Build tool: maven >= 3.9.5
* Java: 21
* Framework: Spring boot 3.2.x
* DBMS: MySQL

## Prerequisites
* Java SDK 21
* A MySQL server

## Start application
`mvn spring-boot:run`

## Build application
`mvn clean package`

## Luu y sd OpenFeign
1. Khai cloud de dung openfeign. Moi version cloud se tuong ung voi version spring
2. Khi tao 1 User no se dong thoi dung openfeign de goi den api tao Profile (set id cua user vao Profile)