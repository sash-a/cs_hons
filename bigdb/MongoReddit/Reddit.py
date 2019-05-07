from pymongo import MongoClient

from Container.Container import Container
from Container.Subreddit import Subreddit
from Container.User import User

import datetime
from bson.objectid import ObjectId

client = MongoClient()
db = client.Reddit
containers = db.containers


def create_and_add_user(name, subs=[], mods=[]):
    user = User('user', name, datetime.datetime.now(), False, subs=subs, mod_subs=mods)
    user.db_insert(containers)
    return user

# adding one
# container = Container('user', 'sasha', datetime.datetime.now(), False)
# container.db_insert(containers)


# user = User.from_dict(containers, '5cd0a2810a244d2e255842de')
# sub = user.create_sub(containers, 'videos', 'no NSFW')

rgifs = Subreddit.from_db_by_id(containers, '5cd17ac2180556af150a5f07')
shane = create_and_add_user('shane', subs=[(rgifs._id, rgifs.name)])
shane.get_id(containers)
aww = shane.create_sub(containers, 'aww', 'only cute things')
print(aww.creator)

# getting one
# print(Container.from_dict(containers, '5cd0a2810a244d2e255842de'))

# print(sub.name)
