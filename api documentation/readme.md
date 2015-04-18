# API Documentation

### List of all stories

http://api.dev.la/stories

Method: GET
Response type: JSON

Data: id, title, description, author, size, creation_date, location, radius

Character encoding (header): utf-8


### Story XML by ID

http://api.dev.la/story/<ID>

Example for ID 2: http://api.dev.la/story/2

Method: GET
Response type: XML

Notes: Media data with absolute URI included.