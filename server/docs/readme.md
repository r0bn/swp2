## Server software specs

PHP: 5.4.16

MySQL: 5.6.19

Apache: 2.4


### Database Blueprint
  - id (DBMS Auto-Increment Value)
  - title (string)
  - description (string)
  - author (string)
  - size (string)
> not final: size_uom may be added
  - creation_date (string)
  - location (string)
  - radius (string)
> not final: radius_uom may be added
  - xml_file (blob)
  - created_at (string - framework related)
  - updated_at (string - framework related)