from Container.Container import Container
from Container.Subreddit import Subreddit
from Content import Content
from Points import Guilds, Votes

import datetime
from bson.objectid import ObjectId


class User(Container):
    def __init__(self, db_collection, container_type, name, date, deleted, content=[], karma=0, guilds=Guilds(),
                 subs=[], mod_subs=[]):
        if container_type != 'user':
            raise Exception('Not of type user')

        super().__init__(db_collection, container_type, name, date, deleted, content)
        self.karma = karma
        self.guilds = guilds

        self.subs = subs
        self.mod_subs = mod_subs

    def subscribe(self, name):
        try:
            sub = Subreddit.from_db(self.db_collection, {'name': name, 'type': 'subreddit'})
            sub_id = sub.get_id()
            self.add_sub(sub_id, name)
            sub.add_sub(sub_id, self.name)
        except Exception:
            raise Exception('Subreddit does not exist')

    def add_sub(self, id, name):
        self.subs.append((id, name))
        self.db_collection.update_one({'_id': ObjectId(self.get_id())},
                                      {'$push': {'user.subscriptions': {'containerID': id, 'name': name}}})

    def add_mod_sub(self, id, name):
        self.mod_subs.append((id, name))
        self.db_collection.update_one({'_id': ObjectId(self.get_id())},
                                      {'$push': {'user.mod': {'containerID': id, 'name': name}}})

    def post(self, sub_name, title, text):
        post = Content(_id=None, title='post', dateCreated=datetime.datetime.now(), type='subreddit',
                       creator={'containerID': self.get_id(), 'name': self.name},
                       votes={'upvotes': 0, 'downvotes': 0, 'guilding': {'silver': 0, 'gold': 0, 'platinum': 0}},
                       value=text, edited=False, deleted=False, comments=[], post={'title': title})
        sub = Subreddit.from_db(self.db_collection, {'name': sub_name, 'type': 'subreddit'})
        sub.post(post)
        self.db_collection.update_one({'name': self.name, 'type': 'user'},
                                      {'$push': {'content': {'quickText': title, 'interactionType': 'post'}}})

    def comment(self, content):
        pass

    def create_sub(self, name, rules):
        if self._id is None:
            self.get_id()

        sub = Subreddit(self.db_collection, 'subreddit', name, datetime.datetime.now(), False, rules=rules,
                        mods=[(self._id, self.name)], subs=[(self._id, self.name)], num_subs=1,
                        creator=(self._id, self.name))
        sub.db_insert()
        sub_id = sub.get_id()

        self.add_sub(sub_id, name)
        self.add_mod_sub(sub_id, name)

        return sub

    def to_dict(self):
        return \
            {
                "type": self.container_type,
                "name": self.name,
                "dateCreated": self.date_created,
                "deleted": self.deleted,

                "content": [
                    {
                        "userContent": {
                            "quickText": content[0],
                            "interactionType": content[1]
                        }
                    } for content in self.content],
                "user": {
                    "karma": self.karma,
                    "guilding": {
                        "gold": self.guilds.gold,
                        "silver": self.guilds.silver,
                        "platinum": self.guilds.platinum
                    },
                    "subscriptions": [{"containerID": sub[0], "name": sub[1]} for sub in self.subs],
                    "mod": [{"containerID": mod[0], "name": mod[1]} for mod in self.mod_subs]
                }
            }

    @staticmethod
    def from_db(collection, search_dict):
        user = collection.find_one(search_dict)
        if user is None:
            raise Exception('User not found')

        return User(collection, user['type'], user['name'], user['dateCreated'], user['deleted'],
                    [(content['quicktext'], content['interactionType']) for content in user['content']],
                    user['user']['karma'],
                    Guilds(user['user']['guilding']['silver'], user['user']['guilding']['gold'],
                           user['user']['guilding']['platinum']),
                    [(sub['containerID'], sub['name']) for sub in user['user']['subscriptions']],
                    [(mod['containerID'], mod['name']) for mod in user['user']['mod']])

    @staticmethod
    def from_db_by_id(collection, id):
        User.from_db(collection, {'_id': ObjectId(id)})
