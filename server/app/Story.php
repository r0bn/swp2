<?php namespace App;

use Illuminate\Database\Eloquent\Model;

class Story extends Model {

    protected $table = 'stories';

    protected $fillable = ['title', 'description', 'author', 'size', 'creation_date', 'location', 'radius', 'media', 'xml_file'];

}
