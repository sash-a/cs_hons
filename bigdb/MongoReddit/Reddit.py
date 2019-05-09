from pymongo import MongoClient
from Container.User import User
import datetime

client = MongoClient()
db = client.Reddit
containers = db.containers

# this deletes the previous DB so that no duplicated users get in the way of the program
# containers.delete_many({})


def create_and_add_user(name, subs=[], mods=[]):
    user = User(containers, 'user', name, datetime.datetime.now(), False, subs=subs, mod_subs=mods)
    user.db_insert()
    user.get_id()
    return user


# creating two users
sasha = create_and_add_user('sasha')
liron = create_and_add_user('liron')

# sasha creates r/gifs
sub = sasha.create_sub('gifs', 'be nice')
print(sub)
# liron subscribes to r/gifs
liron.subscribe('gifs')

# Posting and commenting
sasha_post_0 = sasha.post('gifs', 'This is a cat', 'imgur.com/cat')
print(sasha_post_0)
liron_comment_0 = liron.comment('gifs', sasha_post_0, 'lol nice cat')
print(liron_comment_0)
liron_post_0 = liron.post('gifs', 'This is a dog', 'imgur.com.dog')
print(liron_post_0)
sasha_comment_0 = sasha.comment('gifs', liron_comment_0, 'thanks!')
print(sasha_comment_0)
