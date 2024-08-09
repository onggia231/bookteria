# Api Gateway

1. Khai dependency spring-cloud-starter-gateway
2. Cau hinh application: 
   - Thay vi goi den cac port 8081, 8082... thi dung chung 1 port 8888
   - Khai cac uri la cac port cua service khac, khi aau chi can dung predicates de call api
   -> Tac dung: Config de gom ve 1 port duy nhat khi goi api

# Tong ket
1. Tat ca cac request tu ben ngoai vao phai thong qua Api gateway
2. Authen, autho he thong nay dang xay dung Api gateway dam nhiem authen xem cho phep ai duoc hay ko duoc truy cap
3. Con Autho thi do services dam nhiem sau khi nhan duoc token tu api gateway gui xuong, no se xem role va permission cua user de autho cho request do
4. Autho giua cac service (Vd: AuthenticationRequestInterceptor)
5. Viec de nhiem vu Autho cho tung service de maintain, Api gateway ko can nam het nghiep vu cua tung services
