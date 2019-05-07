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


class Container:
    def __init__(self, container_type, name, date, deleted, _id=None, content=[]):
        self._id = _id
        self.container_type = container_type
        self.name = name
        self.date_created = date
        self.deleted = deleted

        self.content = content

    def to_dict(self):
        return {
            "type": self.container_type,
            "name": self.name,
            "dateCreated": self.date_created,
            "deleted": self.deleted,
        }

    @staticmethod
    def from_dict(collection, id):
        container = collection.find_one({"_id": ObjectId(id)})

        return Container(container['type'], container['name'], container['dateCreated'], container['deleted'])

    # TODO gets ID from db or adds to db and gets id
    def get_id(self, collection):
        self._id = collection.find_one(self.to_dict()).get('_id')
        return self._id

    def db_insert(self, collection):
        collection.insert_one(self.to_dict())
