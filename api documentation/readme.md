# API Documentation

##

### Overview
 * List of all stories
 * Story download
 * Media files
   * Get story media files
   * Media file download
   * Delete media file
   * Add media file
 * Story management
   * Create new story
   * Update/edit story
   * Delete story
 * Filter story metadata
 * Temporay route for testing (player)
   * List of all stories
   * Story download
   * Filtering

##

### List of all stories
```
http://api.storytellar.de/story
```
> **IMPORTANT:** We are not able to guarantee for a list of stories that are well formatted and schema validated due to a missing XML catalog. This will be updated in the near future.

 * Method: GET
 * Response format: JSON
 * Data: id, title, description, author, revision, size, size_uom, location, radius, radius_uom, created_at, updated_at
 * Character encoding (header): utf-8

Example response:
```JSON
{
"id": "38",
"title": "Kneipentour",
"description": "Die Legende von Block 4",
"author": "Arno Claus",
"revision": "7",
"size": "40",
"size_uom": "MB",
"location": "48.780332 9.172515",
"radius": "5",
"radius_uom": "km",
"created_at": "2015-05-25 05:22:52",
"updated_at": "2015-05-25 05:22:52"
},
```

### Story download
To download a story XML you have to append the ID.
```
http://api.storytellar.de/story/<id>
```
> **IMPORTANT:** We are not able to guarantee for a list of stories that are well-formatted and schema validated due to a missing XML catalog. This will be updated in the near future.

 * Method: GET
 * Response type: XML
 * Media data: See next paragraph

Example for story ID 38:
```
http://api.storytellar.de/story/38
```

### Media files
**Get story media files**
To get a list of all media files stored for a story, the following URI schema needs to be triggered:
```
http://api.storytellar.de/story/<id>/media
```

 * Method: GET
 * Response format: JSON
 * Data: file, folder, extension, size, created_at
 * Character encoding (header): utf-8

This returns a list of all media files that are currently assigned to that story ID. The size is given in bytes.

Example for story ID 38:
```
http://api.storytellar.de/story/38/media
```

Example response:
```JSON
{
"file": "Piratenzeug.jpg",
"folder": "38",
"extension": "jpg",
"size": 30951,
"created_at": "2015-05-25 05:51:47"
}
```

**Media file download**
Media files are accessible in an media folder, where the subfolder represents the story ID:
```
http://api.storytellar.de/media/<id>/<filename>
```

This returns the file stored for a story. Filenames remain unchanged.

Example for story ID 38 with the file "Piratenzeug.jpg":
```
http://api.storytellar.de/media/38/Piratenzeug.jpg
```

Directory tree:

```
 api.storytellar.de
 | /media
         | /<id>
                 | /<filename>
```

**Delete media file**
To delete a media file for a story, the following route has to be called via delete method:
```
http://api.storytellar.de/story/<id>/media/<filename>
```
This deletes the file for the given story id.

Example for story ID 38 with the file "Piratenzeug.jpg":
```
http://api.storytellar.de/story/38/media/Piratenzeug.jpg
```

**Add media file**
You can add a media file by calling the following route via put method:
```
http://api.storytellar.de/story/<id>/media
```
> **NOTE:** Since uploading files via put is impossible for HTML forms, you can add an extra parameter with the name "_method" and value "PUT" to it for achieving the same result via post method.

This will add an media file to a story for the given id.

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

### Story story
> **Note:** All stories that are not final can be found here:

```
http://api.storytellar.de/story/open
```
The response parameters are the same as for final tagged stories.

**Create new story**
To create a new story that is final the following post route applies:
```
http://api.storytellar.de/story
```

 * Method: POST
 * Parameters: final (optional, read on...), xml
 * Response: Story ID integer

> **Note:** The server will automatically extract the xml metadata from the XML file when the final parameter is given.

If the parameter "final" (content doesn't matter) is set, the XML file/string has to be valid in all ways (media files, well formatted and schema valid). Don't set that parameter without checking!

> **IMPORTANT:** We are unable to check if the XML is well formatted and schema validated at the moment! This has to be done front-end when the "final" parameter is given. This will be updated as soon as the XML catalog is ready. Without "final" parameter there is no need for any validation, you can post whatever you want then.

Let's create a final story for example: The route has to be called via post method and contain a parameter "final" and "xml" with the xml file content as string.

Let's create a story that is not final, which could be called "preparing a story": Only the "xml" parameter has to be filled, it doesn't matter if it's a valid xml string or not. Don't include the parameter "final" within your post. You can now edit this story... (see next chapters)

Valid XML metadata tags have to follow these rules:

 * Title: Required, length: 1-255 characters
 * Description: Required, length: 1-65535 characters
 * Author: Required, length: 1-255 characters
 * Revision: Required, integer
 * Size: Required, integer
 * Size uom: Required, length: 1-255 characters
 * Location: Required with the following regex pattern: /^(-?\d{1,2}\.\d{6}) (-?\d{1,2}\.\d{6})$/
 * Radius: Required, integer
 * Radius uom: Required, length: 1-255 characters

**Update/edit story**
Once a story is stored on the server, you can edit it with the following route via post method:
```
http://api.storytellar.de/story/<id>
```
The rules that apply here are the same as for creating a new story! So be careful with the "final" parameter!

**Delete story**
To delete a story including all it's media files you have to call the following route via delete method:
```
http://api.storytellar.de/story/<id>
```
This will delete the story from the database and remove all files assigned to the media folder.

### Filter story metadata
To filter specific values of a story, the following route needs to be called:
```
http://api.storytellar.de/story?<query parameters>
```

 * Method: GET
 * Query parameters (optional): id, title, description, author, revision, size, size_uom, size_min, size_max, location, radius, radius_uom, created_at, updated_at, creation_date_min, creation_date_max, gps_point, gps_point_radius

> **Note:** If gps_point is given as parameter, then gps_point_radius must be given too! (it would be pointless without...)

The query parameters must be passed encoded and following these rules: http://www.faqs.org/rfcs/rfc3986.html

Nearly every language has this function built in, like urlencode for php or encodeuri for javascript and so on. It doesn't matter if you use '+' or '%20' (without the ' characters) in your query, we accept both.

An example for filtering on the 3 available stories for our temp route would be:
```
http://api.storytellar.de/story?author=lukas
```
This will filter all stories for author's name 'lukas'.

Of course you can add other query parameters as well (all the parameters listed above). An example for making the filtering more specific with a gps point and radius:
```
http://api.storytellar.de/story?author=lukas&gps_point=48.810592+9.237417&gps_point_radius=100
```
This will filter all stories for the following conditions:
- Author of a story must be 'lukas'
- Story location must be in the radius of '100' from the gps point '48.810592 9.237417'

The gps radius unit must be given in km.

We made it possible to exclude detailed timestamps for any dates. So it's possible to just pass the date as YYYY-MM-DD (just like the response format).

 * Response data: id, title, description, author, revision, size, size_uom, location, radius, radius_uom, created_at, updated_at
 * Response format: JSON
 * Character encoding (header): utf-8

### Temporary route for testing (player)
**List of all stories**
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
**Story download**
```
http://api.storytellar.de/temp/<id>
```
> **Note:** Similar to main story download.

 * Method: GET
 * Response type: XML
 * Media data: Filenames included & available

Example for ID 2: http://api.storytellar.de/temp/2

**Filtering**

Filtering for the temp route is available too, it's functionality is analog to the story route. All you have to do is exchange "story" with "temp".
