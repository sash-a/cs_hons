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

    def add_sub(self, collection, id, name):
        self.subs.append((id, name))
        self.nsubs += 1

        collection.update_one({'_id': ObjectId(self.get_id(collection))},
                              {'$push': {'subreddit.subscribers': {'containerID': id, 'name': name}}})
        collection.update_one({'_id': ObjectId(self.get_id(collection))},
                              {'$set': {'subreddit.numberSubscribers': self.nsubs}})


    def add_mod(self, id, name):
        self.mods.append((id, name))

    # TODO
    def add_post(self, content, collection):
        self.content.append(content)
        collection.update_one({'name': self.name},
                              {'content': {
                                  '$push': {
                                      'subredditContent': {
                                          'post': content.to_dict
                                      }
                                  }
                              }})

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
    def from_db(collection, search_dict):
        db_sub = collection.find_one(search_dict)

        sub = Subreddit(db_sub['type'], db_sub['name'], db_sub['dateCreated'], db_sub['deleted'],
                        [Content(**content['subredditContent']['post']) for content in db_sub['content']],
                        db_sub['subreddit']['rules'],
                        db_sub['subreddit']['mods'],
                        db_sub['subreddit']['subscribers'],
                        db_sub['subreddit']['numberSubscribers'],
                        # think will gen as ((id, name))
                        ((creator[0], creator[1]) for creator in db_sub['subreddit']['creator']))
        sub._id = db_sub['_id']
        return sub

    @staticmethod
    def from_db_by_id(collection, id):
        return Subreddit.from_db(collection, {'_id': ObjectId(id)})
