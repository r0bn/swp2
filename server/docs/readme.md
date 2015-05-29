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
  - title (string (255))
  - description (string (255))
  - author (string (255))
  - size (string (255))
  - **not final:** size_uom (string (255)) may be added
  - creation_date (string (255))
  - location (string (255))
  - radius (string (255))
  - **not final:** radius_uom (string (255)) may be added
  - xml_file (blob)
  - created_at (timestamp - framework related)
  - updated_at (timestamp - framework related)

### Create syntax
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