# Server software specs
 * PHP: 5.4.16
 * MySQL: 5.6.19
 * Apache: 2.4

# Database
 * Type: InnoDB
 * Encoding: (utf8)
 * Collation: utf8_unicode_ci

### Database Blueprint
  - id (DBMS Auto-Increment Value)
  - title (VARCHAR (255))
  - description (TEXT (65,535))
  - author (VARCHAR (255))
  - size (INT)
  - size_uom (VARCHAR (255))
  - location (VARCHAR (255))
  - radius (INT)
  - radius_uom (VARCHAR (255))
  - xml_file (BLOB)
  - created_at (timestamp - framework related)
  - updated_at (timestamp - framework related)

### Create syntax (DEPRECATED)
```
CREATE TABLE `stories` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `title` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `description` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `author` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `size` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `creation_date` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `location` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `radius` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `media` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `xml_file` blob NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `updated_at` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
```
