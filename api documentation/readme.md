# API Documentation

### List of all stories
```
http://api.storytellar.de/story
```
> **Note:** The deprecated route http://api.dev.la/stories will remain active until every group has implemented the new one.
 
 * Method: GET
 * Response format: JSON
 * Data: id, title, description, author, size, creation_date, location, radius
 * Character encoding (header): utf-8

### Story XML download by ID
```
http://api.storytellar.de/story/<id>
```
> **Note:** The deprecated route http://api.dev.la/story/id will remain active until every group has implemented the new one.
 
 * Method: GET
 * Response type: XML

Example for ID 2: http://api.storytellar.de/story/2

### Store story on server
```
http://api.storytellar.de/story
```
> **Note:** The deprecated route http://api.dev.la/createstory will remain active until every group has implemented the new one.

 * Method: POST
 * Fields: title, description, author, size, creation_date, location, radius, xml
 * Media: media[]