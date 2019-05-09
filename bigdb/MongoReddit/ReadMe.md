## Requirements
Python 3 and pymongo

## How to run
Reddit is the driver program. Line 10 is used to flush the DB uncomment this to delete the collection contents each time it is run.

Duplicate users and subreddits are not allowed.

### Class breakdown
Container.py is the base class for subreddit and user. It has general functions like adding to the database and stores common info such as name.

User.py is where most of the functionality is as users create content. This stores all information related to users. A user can be created from the database using ```User.from_db(collection, search_dict)```

Subreddit.py is very similar to User.py it stores all info related to subreddits.

Content.py is a representation of posts and comments

### Index variable
Index is a variable stored within a content object (i.e a post or comment). It indicates the position of all its parent content objects in their content arrays, separated by a period. As an example the fifth post to a subreddit will have index of five, the first comment on that post will have an index of 5.0 and the seventh comment on that comment will have an index of 5.0.7 etc.
