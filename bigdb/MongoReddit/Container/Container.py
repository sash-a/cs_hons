class Container:
    def __init__(self, container_type, name, date, deleted):
        self.container_type = container_type
        self.name = name
        self.date_created = date
        self.deleted = deleted

        content = []

    def to_dict(self):
        pass