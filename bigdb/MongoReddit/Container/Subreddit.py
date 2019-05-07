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
    # does this empty content obj work?
    def __init__(self, container_type, name, date, deleted, content=None, rules='', mods=[], subs=[], num_subs=0,
                 creator=None):
        if container_type != 'subreddit':
            raise Exception('Not of type subreddit')

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

    # TODO
    def add_post(self, content, collection):
        super().content.append(content)
        collection.update_one({'name': self.name},
                              {'content': {
                                  '$push': {
                                      'subredditContent': {
                                          'post': content.to_dict
                                      }
                                  }
                              }})

    def db_insert(self, collection):
        collection.insert_one(self.to_dict())

    def to_dict(self):
        return \
            {
                "type": self.container_type,
                "name": self.name,
                "dateCreated": self.date_created,
                "deleted": self.deleted,

                "content": [
                    {
                        "subredditContent": {
                            "post": post
                        }
                    } for post in self.content],
                "subreddit": {
                    "rules": self.rules,
                    "mods": [{"contentID": mod[0], "name": mod[1]} for mod in self.mods],
                    "subscribers": [{"contentID": sub[0], "name": sub[1]} for sub in self.subs],
                    "numberSubscribers": self.nsubs,
                    "creator": {"contentID": self.creator[0], "name": self.creator[1]}
                }
            }

    @staticmethod
    def from_dict(collection, id):
        sub = collection.find_one({'_id': ObjectId(id)})
        return Subreddit(sub['type'], sub['name'], sub['dateCreated'], sub['deleted'],
                         Content(**sub['content']['subredditContent']['post']),
                         sub['subreddit']['rules'],
                         sub['subreddit']['mods'],
                         sub['subreddit']['subscribers'],
                         sub['subreddit']['numberSubscribers'],
                         ((id, name) for id, name in sub['subreddit']['creator']))  # think the will gen as ((id, name))
