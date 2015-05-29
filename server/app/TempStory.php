<?php namespace App;

use Illuminate\Database\Eloquent\Model;

class TempStory extends Model {

    protected $table = 'temp_stories';

    protected $fillable = ['title', 'description', 'author', 'revision', 'size', 'size_uom', 'location', 'radius', 'radius_uom', 'xml_file', 'final'];

}
