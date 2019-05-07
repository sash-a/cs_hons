from pymongo import MongoClient

from Container.Container import Container
from Container.Subreddit import Subreddit
from Container.User import User

import pprint
import datetime
from bson.objectid import ObjectId

client = MongoClient()
db = client.Reddit
containers = db.containers


def create_and_add_user(name, subs=[], mods=[]):
    user = User('user', name, datetime.datetime.now(), False, subs=subs, mod_subs=mods)
    user.db_insert(containers)
    user.get_id(containers)
    return user

sasha = create_and_add_user('sasha')
liron = create_and_add_user('liron')

sub = sasha.create_sub(containers, 'gifs', 'be nice')
liron.subscribe(containers, 'gifs')