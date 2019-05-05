from Container import Container
from Points import Guilds


class User(Container):
    def __init__(self, container_type, name, date, deleted):
        if container_type != 'user':
            raise Exception('Not of type user')

        super().__init__(container_type, name, date, deleted)
        self.karma = 0
        self.guilds = Guilds()

        subs = []
        mod_subs = []

    def add_subs(self, id, name):
        self.subs.append((id, name))

    def add_mod_sub(self, id, name):
        self.mod_subs.append((id, name))

    def to_dict(self):
        return \
            {
                "type": super().container_type,
                "name": super().name,
                "dateCreated": super().date,
                "deleted": super().date,

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
                    "mod": [{"containerID": sub[0], "name": sub[1]} for sub in self.subs]
                }
            }

    def from_dict(self):
        pass
