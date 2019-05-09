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
    def __init__(self, db_collection, container_type, name, date, deleted, content=None, rules='', mods=[], subs=[],
                 num_subs=0,
                 creator=None):
        if container_type != 'subreddit':
            raise Exception('Not of type subreddit')

        super().__init__(db_collection, container_type, name, date, deleted, content)
        self.rules = rules

        self.subs = subs
        self.mods = mods

        self.nsubs = num_subs
        self.creator = creator

    def add_sub(self, id, name):
        """Adds a subreddit to the db"""
        self.subs.append((id, name))
        self.nsubs += 1

        self.db_collection.update_one({'_id': ObjectId(self.get_id())},
                                      {'$push': {'subreddit.subscribers': {'containerID': id, 'name': name}}})
        self.db_collection.update_one({'_id': ObjectId(self.get_id())},
                                      {'$set': {'subreddit.numberSubscribers': self.nsubs}})

    def add_mod(self, id, name):
        """Adds a moderator to the db"""
        self.mods.append((id, name))
        self.db_collection.update_one({'_id': ObjectId(self.get_id())},
                                      {'$push': {'subreddit.mods': {'containerID': id, 'name': name}}})

    def post(self, content):
        """Adds a post to the db"""
        self.content.append(content)
        print(content.to_dict())
        self.db_collection.update_one({'name': self.name, 'type': self.container_type},
                                      {'$push': {
                                          'content': content.to_dict()

                                      }})

    def comment(self, content):
        """Adds a comment to the db"""
        # using the index field to determine which position in the parent arrays all the parent comments/posts are
        post_pos = content.index[0:2]
        pos = '.'.join(['comments.' + str(i) for i in content.index[2:-2].split('.') if i != ''])
        pos += 'comments' if pos == '' else '.comments'

        self.db_collection.update_one({'name': self.name, 'type': self.container_type},
                                      {'$push': {
                                          'content.' + str(post_pos) + str(pos): content.to_dict()
                                      }})

    def to_dict(self):
        """returns this object as a dictionary"""
        return \
            {
                "type": self.container_type,
                "name": self.name,
                "dateCreated": self.date_created,
                "deleted": self.deleted,
                "subreddit": {
                    "rules": self.rules,
                    "mods": [{"contentID": mod[0], "name": mod[1]} for mod in self.mods],
                    "subscribers": [{"contentID": sub[0], "name": sub[1]} for sub in self.subs],
                    "numberSubscribers": self.nsubs,
                    "creator": {"contentID": self.creator[0], "name": self.creator[1]}
                },
                "content": [
                    {
                        "subredditContent": {
                            "post": post
                        }
                    } for post in self.content]
            }

    @staticmethod
    def from_db(collection, search_dict):
        db_sub = collection.find_one(search_dict)

        sub = Subreddit(collection, db_sub['type'], db_sub['name'], db_sub['dateCreated'], db_sub['deleted'],
                        [Content(**content) for content in db_sub['content']],
                        db_sub['subreddit']['rules'],
                        db_sub['subreddit']['mods'],
                        db_sub['subreddit']['subscribers'],
                        db_sub['subreddit']['numberSubscribers'],
                        ((creator[0], creator[1]) for creator in db_sub['subreddit']['creator']))
        sub._id = db_sub['_id']
        return sub

    @staticmethod
    def from_db_by_id(collection, id):
        return Subreddit.from_db(collection, {'_id': ObjectId(id)})
