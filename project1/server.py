from socket import *
import random as r

port = 8080
s_soc = socket(AF_INET, SOCK_STREAM)
s_soc.bind(('localhost', port))
s_soc.listen(1)

ServerName = "Server of Codey Borrelli"
print(ServerName + " is now ONLINE")

while True:
    con, addr = s_soc.accept()

    data = con.recv(1024).decode()
    c_name, c_num = data.split(',')
    c_num = int(c_num)

    print("Client Name: " + c_name)
    print("Server Name: " + ServerName)

    s_num = r.randint(1, 100)
    total_sum = s_num + c_num
    
    s_res = ServerName + "," + str(s_num) + "," + str(total_sum)
    con.send(s_res.encode())

    con.close()