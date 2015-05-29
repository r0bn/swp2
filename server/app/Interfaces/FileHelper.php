<?php

namespace App\Interfaces;

interface FileHelper
{
    public function deleteStoryMediaFile($id, $filename);

    public function getStoryMediaFiles($id);

    public function storeStoryMediaFile($id, $file);

    public function validateFile($file);
}