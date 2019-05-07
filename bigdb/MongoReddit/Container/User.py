from Container import Container
from Points import Guilds

from bson.objectid import ObjectId


class User(Container):
    def __init__(self, container_type, name, date, deleted, content=[], karma=0, guilds=Guilds(), subs=[], mod_subs=[]):
        if container_type != 'user':
            raise Exception('Not of type user')

        super().__init__(container_type, name, date, deleted, content)
        self.karma = karma
        self.guilds = guilds

        self.subs = subs
        self.mod_subs = mod_subs

    def add_sub(self, id, name):
        self.subs.append((id, name))

    def add_mod_sub(self, id, name):
        self.mod_subs.append((id, name))

    def post(self, subreddit):
        pass

    def comment(self, content):
        pass

    def to_dict(self):
        return \
            {
                "type": super().container_type,
                "name": super().name,
                "dateCreated": super().date_created,
                "deleted": super().deleted,

                "content": [
                    {
                        "userContent": {
                            "postLink": content.link,
                            "quickText": content.quick_text,
                            "interactionType": content.interaction_type
                        }
                    } for content in super().content],
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
    def from_dict(collection, id):
        user = collection.find_one({'_id': ObjectId(id)})
        return User(user['type'], user['name'], user['dateCreated'], user['deleted'], user['content'], user['user']['karma'],
                    Guilds(user['user']['silver'], user['user']['gold'], user['user']['platinum']),
                    [(id, name) for (id, name) in user['user']['subscriptions']],
                    [(id, name) for (id, name) in user['user']['mod']])
