db = {
    'user'     : 'root',		# 1)
    'password' : '1234',		# 2)
    'host'     : '49.50.172.150',	# 3)
    'port'     : 3306,			# 4)
    'database' : 'mydb'		# 5)
}

DB_URL = f"mysql+mysqlconnector://{db['user']}:{db['password']}@{db['host']}:{db['port']}/{db['database']}?charset=utf8"