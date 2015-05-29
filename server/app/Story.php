<?php namespace App;

use Illuminate\Database\Eloquent\Model;

class Story extends Model {

    protected $table = 'stories';

    protected $fillable = ['title', 'description', 'author', 'revision', 'size', 'size_uom', 'location', 'radius', 'radius_uom', 'xml_file', 'working_title', 'final'];

}
