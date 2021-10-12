from flask import Flask
from flask import request
app = Flask(__name__)

mes=""

@app.route('/')
def hello_world():
    return "Hello World"

@app.route('/getotp')
def getOTPFunc():
    global mes
    print("mes:",mes)
    return mes

@app.route('/saveotp', methods=['POST'])
def hello_name():
    global mes
    messag=request.get_data()
    mes=str(messag)[2:-1]
    print("\n mes sav:",mes)
    return 'OK'

if __name__ == '__main__':
   app.run(host='0.0.0.0', port=5000)