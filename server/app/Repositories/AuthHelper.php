<?php

namespace App\Repositories;

use App\Interfaces\AuthHelper as AuthHelperInterface;
use Tymon\JWTAuth\Exceptions\JWTException;
use App\User;

class AuthHelper implements AuthHelperInterface
{
    /*
    * Looks up the story in the database
    * Calls function to check whether an user is bind to the given story or not
    * If its bind to an user a function is called to validate the user
    * Returns whether the user is valid or not
    */
    public function checkUserValidation($storyId)
    {
        $retVal = null;

        $story = \App\Story::findOrFail($storyId);

        if ($this->getStoryUserStatus($story)) {
            $token = \JWTAuth::getToken();
            $tokenUser = \JWTAuth::toUser($token);
            $storyUserId = $story->user_id;

            $retVal = $this->validateUser($storyUserId, $tokenUser->id);
        } else {
            $retVal = true;
        }

        return $retVal;
    }

    /*
    * Checks whether the story is bind to an user or not
    */
    public function getStoryUserStatus($story)
    {
        $retVal = null;

        if ($story->user_id) {
            $retVal = true;
        } else {
            $retVal = false;
        }

        return $retVal;
    }

    /*
    * Looks up the user in the database
    * Calls function to get the user varification status
    * Returns whether the user is valid or not
    */
    public function validateUser($storyUserId, $tokenUserId)
    {
        $retVal = null;

        $user = User::findOrFail($tokenUserId);

        if (($storyUserId == $tokenUserId) && $this->getUserVerificationStatus($user)) {
            $retVal = true;
        } else {
            throw new JWTException('User not valid', 400);
        }

        return $retVal;
    }

    /*
    * Checks the verification status of the given user
    */
    public function getUserVerificationStatus($user)
    {
        $retVal = null;

        if ($user->verified) {
            $retVal = true;
        } else {
            $retVal = false;
        }

        return $retVal;
    }

}
