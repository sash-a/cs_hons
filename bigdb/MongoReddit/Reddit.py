from pymongo import MongoClient
from Container.Container import Container
import datetime


client = MongoClient()
db = client.Reddit
containers = db.containers

# adding one
# container = Container('user', 'sasha', datetime.datetime.now(), False)
# container.db_insert(containers)
# print(containers.find_one({'_id': ObjectId('5cd0a2810a244d2e255842de')}))

# getting one
# print(Container.from_dict(containers, '5cd0a2810a244d2e255842de'))
