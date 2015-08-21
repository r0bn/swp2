<?php namespace App\Http\Controllers;

use Illuminate\Http\Request;
//use Illuminate\Routing\Controller;

use JWTAuth;
use App\User;
use Tymon\JWTAuth\Exceptions\JWTException;
use Tymon\JWTAuth\Providers\Auth\AuthInterface;

class AuthenticateController extends Controller
{
    /*
    * This function returns an authentication token when the user credentials are valid
    */
    public function authenticate(Request $request)
    {
        $credentials = array(
            'email' => $request->input('email'),
            'password' => $request->input('password')
        );

        try {
            if (!$token = JWTAuth::attempt($credentials)) {
                return response()->json(['error' => 'invalid_credentials'], 401, [], JSON_UNESCAPED_UNICODE);
            }
        } catch (JWTException $e) {
            return response()->json(['error' => 'could_not_create_token'], 500, [], JSON_UNESCAPED_UNICODE);
        }

        $retVal = null;

        $user = JWTAuth::toUser($token);

        if ($this->getUserEmailVerificationStatus($user)) {
            $retVal = response()->json(compact('token'), 200, [], JSON_UNESCAPED_UNICODE);
        } else {
            $retVal = response()->json(['error' => 'email_not_verified'], 400, [], JSON_UNESCAPED_UNICODE);
        }

        return $retVal;
    }


    public function reauthenticate()
    {

    }

    /*
    * Checks whether a User has validated his email
    */
    public function getUserEmailVerificationStatus($user)
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
