<?php

namespace App\Repositories;

use App\Interfaces\Story as StoryInterface;

use Carbon\Carbon;

class Story implements StoryInterface
{
    public function getAllStories()
    {
        $stories = array();

        foreach (\App\Story::where('final', '=', true)->get() as $entry) {
            $story = array();

            $story['id'] = $entry->id;
            $story['title'] = $entry->title;
            $story['description'] = $entry->description;
            $story['author'] = $entry->author;
            $story['revision'] = $entry->revision;
            $story['size'] = $entry->size;
            $story['size_uom'] = $entry->size_uom;
            $story['location'] = $entry->location;
            $story['radius'] = $entry->radius;
            $story['radius_uom'] = $entry->radius_uom;
            $story['created_at'] = $entry->created_at->toDateTimeString();
            $story['updated_at'] = $entry->updated_at->toDateTimeString();

            $stories[] = $story;
        }

        $headers['Content-Type'] = 'application/json; charset=utf-8';

        return response()->json($stories, 200, $headers, JSON_UNESCAPED_UNICODE);
    }

    public function getUserStories($userId)
    {
        $stories = array();

        foreach (\App\Story::where('user_id', '=', $userId)->get() as $entry) {
            $story = array();

            $story['id'] = $entry->id;
            $story['final'] = $entry->final;
            $story['working_title'] = $entry->working_title;
            $story['title'] = $entry->title;
            $story['description'] = $entry->description;
            $story['author'] = $entry->author;
            $story['revision'] = $entry->revision;
            $story['size'] = $entry->size;
            $story['size_uom'] = $entry->size_uom;
            $story['location'] = $entry->location;
            $story['radius'] = $entry->radius;
            $story['radius_uom'] = $entry->radius_uom;
            $story['created_at'] = $entry->created_at->toDateTimeString();
            $story['updated_at'] = $entry->updated_at->toDateTimeString();

            $stories[] = $story;
        }

        $headers['Content-Type'] = 'application/json; charset=utf-8';

        return response()->json($stories, 200, $headers, JSON_UNESCAPED_UNICODE);
    }


    public function getAllOpenStories()
    {
        $stories = array();

        foreach (\App\Story::where('final', '=', false)->get() as $entry) {
            $story = array();

            $story['id'] = $entry->id;
            $story['working_title'] = $entry->working_title;
            $story['created_at'] = $entry->created_at->toDateTimeString();
            $story['updated_at'] = $entry->updated_at->toDateTimeString();

            $stories[] = $story;
        }

        $headers['Content-Type'] = 'application/json; charset=utf-8';

        return response()->json($stories, 200, $headers, JSON_UNESCAPED_UNICODE);
    }


    public function getStory($id)
    {
        $story = \App\Story::findOrFail($id);

        return $story->xml_file;
    }


    public function createStory($xml)
    {
        $xmlParser = new XmlParser();
        $xmlMetadata = $xmlParser->getXmlMetadata($xml);

        $story = new \App\Story;

        $story->title = $xmlMetadata['title'];
        $story->description = $xmlMetadata['description'];
        $story->author = $xmlMetadata['author'];
        $story->revision = $xmlMetadata['revision'];
        $story->size = $xmlMetadata['size'];
        $story->size_uom = $xmlMetadata['size_uom'];
        $story->location = $xmlMetadata['location'];
        $story->radius = $xmlMetadata['radius'];
        $story->radius_uom = $xmlMetadata['radius_uom'];

        $story->xml_file = $xml;

        $story->final = true;

        $story->save();

        return $story->id;
    }

    public function createStorySlot($xml, $workingTitle, $userId)
    {
        $story = new \App\Story;

        $story->user_id = $userId;

        $story->working_title = $workingTitle;

        $story->xml_file = $xml;

        $story->final = false;

        $story->save();

        return $story->id;
    }

    public function updateStory($xml, $id)
    {
        $xmlParser = new XmlParser();
        $xmlMetadata = $xmlParser->getXmlMetadata($xml);

        $story = \App\Story::find($id);

        $story->title = $xmlMetadata['title'];
        $story->description = $xmlMetadata['description'];
        $story->author = $xmlMetadata['author'];
        $story->revision = $xmlMetadata['revision'];
        $story->size = $xmlMetadata['size'];
        $story->size_uom = $xmlMetadata['size_uom'];
        $story->location = $xmlMetadata['location'];
        $story->radius = $xmlMetadata['radius'];
        $story->radius_uom = $xmlMetadata['radius_uom'];

        $story->updated_at = new \DateTime;

        $story->xml_file = $xml;

        $story->final = true;

        $story->save();
    }

    public function updateStorySlot($xml, $id, $workingTitle)
    {
        $story = \App\Story::find($id);

        $story->working_title = $workingTitle;

        $story->updated_at = new \DateTime;

        $story->xml_file = $xml;

        $story->final = false;

        $story->save();
    }


    public function deleteStory($id)
    {
        $story = \App\Story::findOrFail($id);

        $story->delete();

        \Storage::delete(\Storage::allFiles($id));
        \Storage::deleteDirectory($id);
    }


    public function getFilteredStories($parameters)
    {
        // get & set the input parameters
        $id = $parameters->input('id');
        $title = $parameters->input('title');
        $description = $parameters->input('description');
        $author = $parameters->input('author');
        $revision = $parameters->input('revision');
        $size = $parameters->input('size');
        $sizeUom = $parameters->input('size_uom');
        $sizeMin = $parameters->input('size_min');
        $sizeMax = $parameters->input('size_max');
        $location = $parameters->input('location');
        $radius = $parameters->input('radius');
        $radiusUom = $parameters->input('radius_uom');
        $createdAt = $parameters->input('created_at');
        $updatedAt = $parameters->input('updated_at');
        $createdAtMin = $parameters->input('creation_date_min');
        $createdAtMax = $parameters->input('creation_date_max');
        $gpsPoint = $parameters->input('gps_point');
        $gpsPointRadius = $parameters->input('gps_point_radius');


        // build the query string for meta data
        $query = \DB::table('stories');

        if ($id) $query->where('id', '=', $id);
        if ($title) $query->where('title', '=', $title);
        if ($description) $query->where('description', '=', $description);
        if ($author) $query->where('author', '=', $author);
        if ($revision) $query->where('revision', '=', $revision);

        if ($size) $query->where('size', '=', $size);
        if ($sizeUom) $query->where('size_uom', '=', $sizeUom);

        if ($sizeMin) $query->where('size', '>=', $sizeMin);
        if ($sizeMax) $query->where('size', '<=', $sizeMax);

        if ($location) $query->where('location', '=', $location);
        if ($radius) $query->where('radius', '=', $radius);
        if ($radiusUom) $query->where('radius_uom', '=', $radiusUom);

        if ($createdAt) $query->where('created_at', '=', $createdAt);
        if ($updatedAt) $query->where('updated_at', '=', $updatedAt);

        if ($createdAtMin) $query->where('created_at', '>=', $this->setEarliestTimestamp($createdAtMin));
        if ($createdAtMax) $query->where('created_at', '<=', $this->setLatestTimestamp($createdAtMax));

        $searchResultsMeta = $query->where('final', '=', true)->get();


        $searchResults = array();

        // refine results if gps search is given
        if ($gpsPoint && $gpsPointRadius) {
            foreach ($searchResultsMeta as $result) {
                $storyGpsPoint = explode(' ', $result->location);
                $queryGpsPoint = explode(' ', $gpsPoint);

                if ($this->getGpsDistance($storyGpsPoint[0], $storyGpsPoint[1], $queryGpsPoint[0], $queryGpsPoint[1]) <= $gpsPointRadius) {
                    $searchResults[] = $result;
                }
            }
        } else {
            $searchResults = $searchResultsMeta;
        }


        // build the results array
        $stories = array();

        foreach ($searchResults as $result) {
            $story = array();

            $story['id'] = $result->id;
            $story['title'] = $result->title;
            $story['description'] = $result->description;
            $story['author'] = $result->author;
            $story['revision'] = $result->revision;
            $story['size'] = $result->size;
            $story['size_uom'] = $result->size_uom;
            $story['location'] = $result->location;
            $story['radius'] = $result->radius;
            $story['radius_uom'] = $result->radius_uom;
            $story['created_at'] = $result->created_at;
            $story['updated_at'] = $result->updated_at;

            $stories[] = $story;
        }

        $headers['Content-Type'] = 'application/json; charset=utf-8';

        return response()->json($stories, 200, $headers, JSON_UNESCAPED_UNICODE);
    }


    public function getGpsDistance($latitude1, $longitude1, $latitude2, $longitude2)
    {
        $earth_radius = 6371;

        $dLat = deg2rad($latitude2 - $latitude1);
        $dLon = deg2rad($longitude2 - $longitude1);

        $a = sin($dLat / 2) * sin($dLat / 2) + cos(deg2rad($latitude1)) * cos(deg2rad($latitude2)) * sin($dLon / 2) * sin($dLon / 2);
        $c = 2 * asin(sqrt($a));
        $distance = $earth_radius * $c;

        return $distance;
    }


    public function setLatestTimestamp($date)
    {
        $retVal = null;

        try {
            $retVal = Carbon::createFromFormat('Y-m-d', $date)->setTime(23, 59, 59);
        } catch (\InvalidArgumentException $e) {
            $retVal = '';
        }

        return $retVal;
    }


    public function setEarliestTimestamp($date)
    {
        $retVal = null;

        try {
            $retVal = Carbon::createFromFormat('Y-m-d', $date)->setTime(00, 00, 00);
        } catch (\InvalidArgumentException $e) {
            $retVal = '';
        }

        return $retVal;
    }


    public function changeFinalStatus($id, $status)
    {
        $story = \App\Story::findOrFail($id);

        $story->final = $status;

        $story->save();
    }


}