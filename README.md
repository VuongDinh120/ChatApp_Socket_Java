# Socket_Java
##### Project type: Java Ant

##### Description: Xây dựng hệ thống chat, có kèm tính năng gửi file
##### System Description: Xây dựng hệ thống gồm Client - FileServer - MasterServer
 - MasterServer:
    + Có địa chỉ IP:port cố định. Chứa thông tin về các file được chia sẻ bởi file server, thông tin IP:port của file server quản lý tương ứng.
    + Cung cấp service (#1) để ghi nhận thông tin mà file server gửi lên gồm: danh sách các file, IP:port của file server. Service này sử dụng giao thức TCP tại tầng Transport.
    + Cung cấp service (#2) để client có thể lấy thông tin danh sách các file được chia sẻ, kèm theo IP:port của file server quản lý file. Service này sử dụng giao thức TCP tại tầng Transport.
    + Cho phép nhiều client, nhiều file server kết nối tới cùng một thời điểm.
 - FileServer:
    + Chứa các files có thể chia sẻ được với client.
    + Khi file server khởi động, nó kết nối đến master server và gọi service (1) để gửi thông tin của chính nó lên file server gồm: danh sách file có thể chia sẻ, địa chỉ IP, port mà client có thể kết nối tới để tải file.
    + Cung cấp service (#3) để client tải file với input là tên file cần tải. Service này sử dụng
giao thức UDP tại tầng Transport.
    + Cho phép nhiều client kết nối đến cùng một thời điểm.
    + Khi file server ngừng hoạt động, master server cần loại bỏ danh sách file của file server
tương ứng.
 - Client: 
    + chat, upload file, download file
    + Có thể tải nhiều file cùng một thời điểm, từ nhiều file server khác nhau.
