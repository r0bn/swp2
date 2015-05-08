# API Documentation

### List of all stories
```
http://api.storytellar.de/story
```
> **Note:** The deprecated route http://api.dev.la/stories will remain active until every group has implemented the new one.
 
 * Method: GET
 * Response format: JSON
 * Data: id, xsd_revision, title, description, author, size, size_uom, location, radius, radius_uom, created_at, updated_at
 * Character encoding (header): utf-8

### Story XML download by ID
```
http://api.storytellar.de/story/<id>
```
> **Note:** The deprecated route http://api.dev.la/story/id will remain active until every group has implemented the new one.
 
 * Method: GET
 * Response type: XML
 * Media data: See next paragraph

Example for ID 2: http://api.storytellar.de/story/2

### Store story on server
```
http://api.storytellar.de/story
```
> **Note:** The deprecated route http://api.dev.la/createstory will remain active until every group has implemented the new one.

 * Method: POST
 * Fields: title, description, author, size, creation_date, location, radius, xml
 * Media: media[]

Media files restrictions:

 * MIME types
   * image/jpeg
   * image/png
   * image/mp4

 * Maximum filesize 5000000 bytes (5 MB)

 * File extensions
   * .jpg
   * .jpeg
   * .png
   * .mp4


Media files are accessible in an media folder, with the story id as sub-folder. Filenames remain unchanged.

Example for story id 201934 and file 'hello.jpg': http://api.storytellar.de/media/201934/hello.jpg

Directory tree:

```
 api.storytellar.de
 | /media
         | /<id>
                 | /<filename>
```

### Filter concept for stories
```
http://api.storytellar.de/story?<query parameters>
```
> **Note:** Not implemented yet!
 
 * Method: GET
 * Query Parameter (optional): id, title, description, author, size_min, size_max, creation_date_min, creation_date_max, location, radius
 * Data: id, xsd_revision, title, description, author, size, size_uom, location, radius, radius_uom, created_at, updated_at 
 * Response format: JSON
 * Character encoding (header): utf-8

Example for author and maximum size filtering: http://api.storytellar.de/story?author=arno+claus&size_max=20
 
### Temporary route for stories
```
http://api.storytellar.de/temp
```
> **Note:** This route is for testing purposes, it will stay excluded from the main route for stories. 3 different stories available! Go get them now!

 * Method: GET
 * Response format: JSON
 * Data: id, title, description, author, revision, size, size_uom, location, radius, radius_uom, created_at, updated_at
 * Character encoding (header): utf-8

Response (pretty):
```JSON
[
    {
        "id": "1",
        "title": "Schneewittchen und die sieben Zwerge",
        "description": "Die halbwüchsigen kehren zurück",
        "author": "Grimm",
        "revision": "7",
        "size": "10",
        "size_uom": "MB",
        "location": "48.783375 9.181187",
        "radius": "2",
        "radius_uom": "km",
        "created_at": "2015-05-01 17:08:31",
        "updated_at": "2015-05-01 18:09:37"
    },
    {
        "id": "2",
        "title": "Aschenputtel",
        "description": "Das arme Ding",
        "author": "Lukas",
        "revision": "7",
        "size": "20",
        "size_uom": "MB",
        "location": "48.799353 9.003754",
        "radius": "2",
        "radius_uom": "km",
        "created_at": "2015-05-02 12:32:58",
        "updated_at": "2015-05-02 14:37:44"
    },
    {
        "id": "3",
        "title": "Schneewittchen",
        "description": "Das arme Ding",
        "author": "Lukas",
        "revision": "7",
        "size": "30",
        "size_uom": "MB",
        "location": "53.552647 10.007829",
        "radius": "2",
        "radius_uom": "km",
        "created_at": "2015-05-03 13:02:19",
        "updated_at": "2015-05-03 16:45:02"
    }
]
```

### Temporary route for story download
```
http://api.storytellar.de/temp/<id>
```
> **Note:** Similar to main story download.

 * Method: GET
 * Response type: XML
 * Media data: Filenames included & available

Example for ID 2: http://api.storytellar.de/temp/2
