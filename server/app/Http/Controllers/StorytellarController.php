<?php namespace App\Http\Controllers;

use App\Http\Requests;
use App\Http\Controllers\Controller;

use Illuminate\Http\Request;

use App\Story;

use App\PostStory;

class StorytellarController extends Controller {

    /**
     * Get all stories.
     *
     * @return json
     */
    public function getStories()
    {
        $stories = array();

        foreach(Story::all() as $entry) {
            $story = array();

            $story['id'] = $entry->id;
            $story['title'] = $entry->title;
            $story['description'] = $entry->description;
            $story['author'] = $entry->author;
            $story['size'] = $entry->size;
            $story['creation_date'] = $entry->creation_date;
            $story['location'] = $entry->location;
            $story['radius'] = $entry->radius;

            $stories[] = $story;
        }

        $headers['Content-Type'] = 'application/json; charset=utf-8';

        return response()->json($stories, 200, $headers, JSON_UNESCAPED_UNICODE);
    }


    /**
     * Get a specific story by id.
     *
     * @param  Story  $id
     * @return xml
     */
    public function getStory($id)
    {
        $story = Story::findOrFail($id);

        return $story->xml_file;
    }


    /**
     * Create a new story.
     *
     * @param  Request  $request
     * @return string
     */
    public function createStory(Request $request)
    {

        $story = new PostStory;

        $story->title = $request->input('title');
        $story->description = $request->input('description');
        $story->author = $request->input('author');
        $story->size = $request->input('size');
        $story->creation_date = $request->input('creation_date');
        $story->location = $request->input('location');
        $story->radius = $request->input('radius');

        $story->xml_file = $request->input('xml');

        $story->save();

        $storyId = $story->id;

        $files = $request->file('media');

        if (count($files) != 0) {
            foreach($files as $file) {
                $mediaPath = 'media/' . $storyId;
                $filename = $file->getClientOriginalName();
                $upload = $file->move($mediaPath, $filename);
            }
        }

        return "check http://www.storytellar.de/media/" . $storyId . "/<filename> if the uploaded file is available!";

    }

}
