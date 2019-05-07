from Container.Container import Container
from Container.Subreddit import Subreddit
from Points import Guilds

import datetime
from bson.objectid import ObjectId


# TODO remove type from here and subreddit
class User(Container):
    def __init__(self, container_type, name, date, deleted, content=[], karma=0, guilds=Guilds(), subs=[], mod_subs=[]):
        if container_type != 'user':
            raise Exception('Not of type user')

        super().__init__(container_type, name, date, deleted, content)
        self.karma = karma
        self.guilds = guilds

        self.subs = subs
        self.mod_subs = mod_subs

    def add_sub(self, collection, id, name):
        self.subs.append((id, name))
        collection.update_one({'_id': ObjectId(self.get_id(collection))},
                              {'$push': {'user.subscriptions': {'containerID': id, 'name': name}}})

    # TODO add to db
    def add_mod_sub(self, collection, id, name):
        self.mod_subs.append((id, name))
        collection.update_one({'_id': ObjectId(self.get_id(collection))},
                              {'$push': {'user.mod': {'containerID': id, 'name': name}}})

    def post(self, subreddit):
        pass

    def comment(self, content):
        pass

    def create_sub(self, collection, name, rules):
        if self._id is None:
            self.get_id(collection)

        sub = Subreddit('subreddit', name, datetime.datetime.now(), False, rules=rules,
                        mods=[(self._id, self.name)],
                        subs=[(self._id, self.name)], num_subs=1, creator=(self._id, self.name))
        sub.db_insert(collection)
        sub_id = sub.get_id(collection)

        self.add_sub(collection, sub_id, name)
        self.add_mod_sub(collection, sub_id, name)

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
                            "postLink": content.link,
                            "quickText": content.quick_text,
                            "interactionType": content.interaction_type
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

        return User(user['type'], user['name'], user['dateCreated'], user['deleted'], user['content'],
                    user['user']['karma'],
                    Guilds(user['user']['guilding']['silver'], user['user']['guilding']['gold'],
                           user['user']['guilding']['platinum']),
                    [(sub['containerID'], sub['name']) for sub in user['user']['subscriptions']],
                    [(mod['containerID'], mod['name']) for mod in user['user']['mod']])

    @staticmethod
    def from_db_by_id(collection, id):
        User.from_db(collection, {'_id': ObjectId(id)})
