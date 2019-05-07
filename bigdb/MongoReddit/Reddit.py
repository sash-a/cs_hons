from pymongo import MongoClient

from Container.Container import Container
from Container.Subreddit import Subreddit

import datetime
from bson.objectid import ObjectId

client = MongoClient()
db = client.Reddit
containers = db.containers

# adding one
# container = Container('user', 'sasha', datetime.datetime.now(), False)
# container.db_insert(containers)
user = Container.from_dict(containers, '5cd0a2810a244d2e255842de')
user.get_id(containers)
print(user._id)

# getting one
# print(Container.from_dict(containers, '5cd0a2810a244d2e255842de'))

sub = Subreddit('subreddit', 'gifs', datetime.datetime.now(), False, rules='No nsfw', mods=[(user._id, user.name)],
                subs=[(user._id, user.name)], num_subs=1, creator=(user._id, user.name))
sub.db_insert(containers)
print(sub.mods[0])
