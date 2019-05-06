from pymongo import MongoClient

client = MongoClient()
db = client.test
print(db.list_collection_names())
col = db.people
# col.insert_one({'date' : "1/2/2001"})
print(col.find_one({'date': "1/2/2001"}))