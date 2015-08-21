<?php

namespace App\Interfaces;

interface Story
{
    public function getAllStories();

    public function getAllOpenStories();

    public function getStory($id);

    public function getFilteredStories($parameters);

    public function createStory($xml);

    public function createStorySlot($xml, $workingTitle);

    public function updateStory($xml, $id);

    public function updateStorySlot($xml, $id, $workingTitle);

    public function deleteStory($id);
}