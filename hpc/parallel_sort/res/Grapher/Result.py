class Result:
    def __init__(self, data):
        data = data.split(', ')
        self.nv = int(data[0])
        self.threads = int(data[1])
        self.sorted = bool(data[2])
        self.time = float(data[3])

    def __repr__(self):
        return 'nv: ' + str(self.nv) + '\nTime: ' + str(self.time)
