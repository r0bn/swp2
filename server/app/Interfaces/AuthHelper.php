<?php

namespace App\Interfaces;

interface AuthHelper
{
    public function checkUserValidation($storyId);

    public function getStoryUserStatus($story);

    public function validateUser($storyUserId, $tokenUserId);
}