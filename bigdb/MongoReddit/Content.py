from Points import Votes, Guilds


# {
#     "content": {
#         "type": "post|comment",
#         "dateCreated": "",
#         "creator": "containerID",
#
#         "votes": {
#             "upvotes": "",
#             "downvotes": "",
#             "guilding": {
#                 "gold": "",
#                 "silver": "",
#                 "platinum": ""
#             }
#         },
#         "post": {
#             "title": ""
#         },
#
#         "comments": ["$Content_Objects$"],
#
#         "value": "",
#         "edited": "bool"
#         "deleted" : bool
#     }
# }


class Content:
    def __init__(self, **kwargs):
        if kwargs is None:
            return

        self._id = kwargs['_id']
        self.content_type = kwargs['type']
        self.date_created = kwargs['dateCreated']
        self.creator = (kwargs['creator']['containerID'], kwargs['creator']['name'])
        self.votes = Votes(kwargs['votes']['upvotes'], kwargs['votes']['downvotes'])
        self.guilds = Guilds(kwargs['votes']['guilding']['silver'],
                             kwargs['votes']['guilding']['gold'],
                             kwargs['votes']['guilding']['platinum'])

        if self.ispost():
            self.title = kwargs['post']['title']
        else:
            self.title = None

        self.comments = [Content(**comment) for comment in kwargs['comments']]

        self.value = kwargs['value']
        self.edited = kwargs['edited']
        self.deleted = kwargs['deleted']

    def ispost(self):
        return self.content_type == 'post'

    def add_comment(self):
        pass

    def get_id(self, collection):
        self._id = collection.find_one(self.to_dict())['_id']
        return self._id

    def to_dict(self):
        return {
            "type": self.content_type,
            "dateCreated": self.date_created,
            "creator": {'containerID': self.creator[0], 'name': self.creator[1]},
            "votes": {
                "upvotes": self.votes.up,
                "downvotes": self.votes.down,
                "guilding": {
                    "gold": self.guilds.gold,
                    "silver": self.guilds.silver,
                    "platinum": self.guilds.platinum
                }
            },
            "post": {
                "title": '' if self.title is None else self.title
            },

            "comments": [comment.to_dict() for comment in self.comments],

            "value": self.value,
            "edited": self.edited,
            "deleted": self.deleted
        }
