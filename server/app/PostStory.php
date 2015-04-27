<?php namespace App;

use Illuminate\Database\Eloquent\Model;

class PostStory extends Model {

    protected $table = 'post_stories';

    protected $fillable = ['title', 'description', 'author', 'size', 'creation_date', 'location', 'radius', 'xml_file'];

}
