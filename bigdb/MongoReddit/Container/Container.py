from bson.objectid import ObjectId

class Container:
    def __init__(self, container_type, name, date, deleted, content=[]):
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

    def db_insert(self, collection):
        collection.insert_one(self.to_dict())

        # {
        #     "type": super().container_type,
        #     "name": super().name,
        #     "dateCreated": super().date,
        #     "deleted": super().date,
        #
        #     "content": [
        #         {
        #             "userContent": {
        #                 "postLink": content.link,
        #                 "quickText": content.quick_text,
        #                 "interactionType": content.interaction_type
        #             }
        #         } for content in super().content],
        #     "user": {
        #         "karma": self.karma,
        #         "guilding": {
        #             "gold": self.guilds.gold,
        #             "silver": self.guilds.silver,
        #             "platinum": self.guilds.platinum
        #         },
        #         "subscriptions": [{"containerID": sub[0], "name": sub[1]} for sub in self.subs],
        #         "mod": [{"containerID": mod[0], "name": mod[1]} for mod in self.mod_subs]
        #     }
        # }
