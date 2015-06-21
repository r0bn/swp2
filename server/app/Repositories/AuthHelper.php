<?php

namespace App\Repositories;

use App\Interfaces\AuthHelper as AuthHelperInterface;
use Tymon\JWTAuth\Exceptions\JWTException;
use App\User;

class AuthHelper implements AuthHelperInterface
{

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
