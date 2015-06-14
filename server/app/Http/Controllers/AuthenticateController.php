<?php namespace App\Http\Controllers;

use Illuminate\Http\Request;
use Illuminate\Routing\Controller;

use JWTAuth;
use Tymon\JWTAuth\Exceptions\JWTException;
use Tymon\JWTAuth\Providers\Auth\AuthInterface;

class AuthenticateController extends Controller
{
    public function authenticate(Request $request)
    {
        $credentials = array(
            'username' => $request->input('username'),
            'password' => $request->input('password')
        );

        try {
            if (! $token = JWTAuth::attempt($credentials)) {
                return response()->json(['error' => 'invalid_credentials'], 401, [], JSON_UNESCAPED_UNICODE);
            }
        } catch (JWTException $e) {
            return response()->json(['error' => 'could_not_create_token'], 500, [], JSON_UNESCAPED_UNICODE);
        }

        // all good so return the token
        return response()->json(compact('token'), 200, [], JSON_UNESCAPED_UNICODE);
    }

    public function reauthenticate()
    {

    }
}
