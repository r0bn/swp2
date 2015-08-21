<?php

namespace App\Interfaces;

interface TempStory
{
    public function getAllStories();

    public function getStory($id);

    public function getFilteredStories($parameters);
}