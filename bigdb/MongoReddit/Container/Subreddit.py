from Container.Container import Container
from Content import Content
from Points import Guilds, Votes
from bson.objectid import ObjectId


# "containers": {
#    "type": "user|subreddit",
#    "containerID": "",
#    "name": "",
#    "dateCreated": "",
#    "deleted": "bool",
#
#    "content":[
#          {
#            "userContent": {
#              "postLink": "contentID #only held by users",
#              "quickText": "",
#              "interactionType": "create|upvotes|downvotes|guild|saved|hidden"
#            },
#            "subredditContent": {
#              "post": "$Content_Object$ #only held by subreddits"
#            }
#          }
#    ],
#
#    "user": {
#      "karma": "",
#      "guilding": {
#        "gold": "",
#        "silver": "",
#        "bronze": ""
#      },
#      "subscriptions": [{
#        "containerID": "",
#        "name": ""
#      }],
#
#      "mod": {
#        "subreddits": ["containerID"]
#      }
#    },
#
#    "subreddit": {
#      "rules": "",
#      "mods": ["containerID"],
#      "subscribers": ["containerID"],
#      "numberSubscribers": "",
#      "creator": "containerID"
#    }
#  }


class Subreddit(Container):
    def __init__(self, container_type, name, date, deleted, content=[], rules='', mods=[], subs=[], num_subs=0,
                 creator=None):
        if container_type != 'user':
            raise Exception('Not of type user')

        super().__init__(container_type, name, date, deleted, content)
        self.rules = rules

        self.subs = subs
        self.mods = mods

        self.nsubs = num_subs
        self.creator = creator

    def add_sub(self, id, name):
        self.subs.append((id, name))

    def add_mod(self, id, name):
        self.mods.append((id, name))

    def to_dict(self):
        return \
            {
                "type": super().container_type,
                "name": super().name,
                "dateCreated": super().date_created,
                "deleted": super().deleted,

                "content": [
                    {
                        "subredditContent": {
                            "post": post
                        }
                    } for post in super().content],
                "subreddit":{
                    "rules": self.rules,
                    "mods": [{"contentID": id, "name": name} for id, name in self.mods],
                    "subscribers": [{"contentID": id, "name": name} for id, name in self.subs],
                    "numberSubscribers": self.nsubs,
                    "creator": {"id": self.creator[0], "name": self.creator[1]}
                }
            }

    # TODO
    @staticmethod
    def from_dict(collection, id):
        sub = collection.find_one({'_id': ObjectId(id)})
        return Subreddit(sub['type'], sub['name'], sub['dateCreated'], sub['deleted'], sub['content'],
                         sub['user']['karma'],
                         Guilds(sub['user']['silver'], sub['user']['gold'], sub['user']['platinum']),
                         [(id, name) for (id, name) in sub['user']['subscriptions']],
                         [(id, name) for (id, name) in sub['user']['mod']])
