from socket import *

c_soc = socket(AF_INET, SOCK_STREAM)
c_soc.connect(('localhost', 8080))

ClientName = "Client of Codey Borrelli"
ClientNumber = int(input("Input a number between 1 and 100: "))

data_to_send = f"{ClientName},{ClientNumber}"
c_soc.send(data_to_send.encode())

s_res = c_soc.recv(1024).decode()

s_name, s_num, s_sum = s_res.split(',')
s_num = int(s_num)
s_sum = int(s_sum)

print("Server Name: " + s_name)
print("Server Number: " + str(s_num))
print("Server Sum: " + str(s_sum))

c_soc.close()