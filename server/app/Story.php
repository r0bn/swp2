<?php namespace App;

use Illuminate\Database\Eloquent\Model;

/**
* The Eloquent Model for Story
*/

class Story extends Model {

    /**
     * The table associated with the model.
     *
     * @var string
     */
    protected $table = 'stories';

    /**
     * The attributes that are mass assignable.
     *
     * @var array
     */
    protected $fillable = ['title', 'description', 'author', 'revision', 'size', 'size_uom', 'location', 'radius', 'radius_uom', 'xml_file', 'working_title', 'final'];

}
