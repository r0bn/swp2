<?php namespace App\Http\Controllers;

use Illuminate\Http\Request;
use Illuminate\Routing\Controller;


class RegisterController extends Controller
{

    public function register(Request $request)
    {
        $credentials = $request->only('username', 'password');

        $validator = \Validator::make($credentials, [
            'username' => 'required|min:1|max:255|unique:users',
            'password' => 'required|min:5|max:60',
        ]);

        if ($validator->fails()) {
            return response()->json(['error' => 'validation_error'], 400, [], JSON_UNESCAPED_UNICODE);
        } else {
            $user = new \App\User;

            $user->username = $credentials['username'];
            $user->password = \Hash::make($credentials['password']);

            $user->save();
        }

    }

}
