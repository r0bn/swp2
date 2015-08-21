# Server software specs
 * PHP: 5.4.16
 * MySQL: 5.6.19
 * Apache: 2.4

# Database
 * Type: InnoDB
 * Encoding: (utf8)
 * Collation: utf8_unicode_ci

### Database Blueprint
  - id (DBMS Auto-Increment value, primary key)
  - title (string (255))
  - description (string (text) (65535))
  - author (string (255))
  - revision (integer)
  - size (integer)
  - size_uom (string (255))
  - location (string (255))
  - radius (integer)
  - radius_uom (string (255))
  - xml_file (blob)
  - working_title (string (255))
  - final (bool)
  - created_at (timestamp - framework related)
  - updated_at (timestamp - framework related)

### Create syntax
```
CREATE TABLE `stories` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `title` varchar(255) COLLATE utf8_unicode_ci DEFAULT '',
  `description` text COLLATE utf8_unicode_ci,
  `author` varchar(255) COLLATE utf8_unicode_ci DEFAULT '',
  `revision` int(11) DEFAULT NULL,
  `size` int(11) DEFAULT NULL,
  `size_uom` varchar(255) COLLATE utf8_unicode_ci DEFAULT '',
  `location` varchar(255) COLLATE utf8_unicode_ci DEFAULT '',
  `radius` int(11) DEFAULT NULL,
  `radius_uom` varchar(255) COLLATE utf8_unicode_ci DEFAULT '',
  `xml_file` blob,
  `working_title` varchar(255) COLLATE utf8_unicode_ci DEFAULT '',
  `final` tinyint(1) DEFAULT '0',
  `created_at` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `updated_at` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
```