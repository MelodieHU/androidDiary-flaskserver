# coding=utf-8
import json
from flask import Flask
from flask import request
# from flask_script import Manager
import os
from flask_sqlalchemy import SQLAlchemy
from sqlalchemy import MetaData
from sqlalchemy import create_engine
from sqlalchemy.orm import *
import sys
import importlib


app = Flask(__name__)
basedir = os.path.abspath(os.path.dirname(__file__))
app.config['SQLALCHEMY_DATABASE_URI'] = 'sqlite:///' + os.path.join(basedir, 'userConfigBase.sqlite')
app.config['SQLALCHEMY_COMMIT_ON_TEARDOWN'] = True
app.config['SQLALCHEMY_TRACK_MODIFICATIONS'] = True

#echo标识用于设置通过python标准日志模块完成的SQLAlchemy日志系统，当开启日志功能，我们将能看到所有的SQL生成代码
#address 数据库://用户名:密码（没有密码则为空）@主机名：端口/数据库名（第一个参数）
engine = create_engine('sqlite:///' + os.path.join(basedir, 'userConfigBase.sqlite'), echo=True)

#设置metadata并将其绑定到数据库引擎
metadata = MetaData(engine)
userdb = SQLAlchemy(app)
# manager=Manager(app)
userName = ''
# 初始化数据库连接:
engine = create_engine('sqlite:///' + os.path.join(basedir, 'userConfigBase.sqlite'))
# 创建DBSession类型:
DBSession = sessionmaker(bind=engine)


@app.route('/')
def test():
    return 'success'


class userInfoTable(userdb.Model):
    __tablename__ = 'userInfo'
    id = userdb.Column(userdb.Integer, primary_key=True)
    username = userdb.Column(userdb.String, unique=True)
    password = userdb.Column(userdb.String)

    def __repr__(self):
        return 'table name is ' + self.username


# 此方法处理用户登录 返回码为0无注册 返回码为1密码错误



# 检查用户登陆
@app.route('/user', methods=['POST'])
def check_user():
    userName = request.form['username']
    haveregisted = userInfoTable.query.filter_by(username=request.form['username']).all()
    if haveregisted.__len__() is not 0:  # 判断是否已被注册
        passwordRight = userInfoTable.query.filter_by(username=request.form['username'],
                                                      password=request.form['password']).all()
        if passwordRight.__len__() is not 0:
            print (str(userName) + " log success")
            return '登录成功'
        else:
            return '1'
    else:
        print (str(userName) + " log fail")
        return '0'


# 此方法处理用户注册
@app.route('/register', methods=['POST'])
def register():
    print('first stepkk')
    userName = request.form['username']
    userdb.create_all()
    haveregisted = userInfoTable.query.filter_by(username=request.form['username']).all()
    if haveregisted.__len__() is not 0:  # 判断是否已被注册
        return '0'
    userInfo = userInfoTable(username=request.form['username'], password=request.form['password'])
    userdb.session.add(userInfo)
    userdb.session.commit()
    print (str(userName) + " register succes")
    return '注册成功'



# 客户端将数据传上服务器，更新服务器上的数据
@app.route('/postdiary', methods=['POST'])
def postDiary():
    userName = request.form['username']

    class userTable(userdb.Model):
        if userName is not '':
            __tablename__ = userName
            __table_args__ = {"useexisting": True}
            id = userdb.Column(userdb.Integer, primary_key=True)
            date = userdb.Column(userdb.String)
            title = userdb.Column(userdb.Integer)
            content = userdb.Column(userdb.String)
            tag = userdb.Column(userdb.String)
            review = userdb.Column(userdb.String)
            extend_existing = True
        else:
            print ('tableName is NUll')

    userdb.create_all()
    haveExisted = userTable.query.filter_by(content=request.form['content']).all()  # 判断数据库中是否有相同的数据了
    if len(haveExisted) is not 0:
        print ('already exist' + str(len(haveExisted)))
        return '0'
    cDate = request.form['date']
    cTitle = request.form['title']
    cContent = request.form['content']
    cTag = request.form['tag']
    cReview = request.form['review']

    try:
        userData = userTable(date=cDate, title=cTitle, content=cContent, tag=cTag, review = cReview)
        userdb.session.add(userData)
        userdb.session.commit()
    except:
        print ('云端上传日记出问题了')
    return '1'




# 客户端获取服务器上的bilBean
@app.route('/getdiary', methods=['GET', 'POST'])
def getDiary():
    userName = request.form['username']

    class userTable(userdb.Model):
        if userName is not '':
            __tablename__ = userName
            __table_args__ = {"useexisting": True}
            id = userdb.Column(userdb.Integer, primary_key=True)
            date = userdb.Column(userdb.String)
            title = userdb.Column(userdb.Integer)
            content = userdb.Column(userdb.String)
            tag = userdb.Column(userdb.String)
            review = userdb.Column(userdb.String)
            extend_existing = True
        else:
            print ('tableName is NUll')

    userdb.create_all()
    session = DBSession()
    data = dict()
    billBeanList = session.query(userTable).all()
    if billBeanList.__len__() is 0:
        return '0'
    for i in range(billBeanList.__len__()):
        bean = dict()
        bean['date'] = billBeanList.__getitem__(i).date
        bean['title'] = billBeanList.__getitem__(i).title
        bean['content'] = billBeanList.__getitem__(i).content
        bean['tag'] = billBeanList.__getitem__(i).tag
        bean['review'] = billBeanList.__getitem__(i).review
        print (str(i) + ' ' + billBeanList.__getitem__(i).date)
        data[str(i + 1)] = bean.copy()
    return json.dumps(data, ensure_ascii=False)


if __name__ == '__main__':
    # manager.run()
    app.run(host='0.0.0.0', port=8000,threaded=True)


    # python simpleCountServer.py runserver --host 192.168.253.1
