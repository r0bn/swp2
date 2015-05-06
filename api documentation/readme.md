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
 
