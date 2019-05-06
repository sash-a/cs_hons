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
        self.content_type = kwargs['type']
        self.date_created = kwargs['dateCreated']
        self.creator = kwargs['creator']
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
