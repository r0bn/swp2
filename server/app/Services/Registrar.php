<?php namespace App\Services;

use App\User;
use Validator;
use Illuminate\Http\Request;
use Illuminate\Contracts\Auth\Registrar as RegistrarContract;

class Registrar implements RegistrarContract {

	/**
	 * Get a validator for an incoming registration request.
	 *
	 * @param  array  $data
	 * @return \Illuminate\Contracts\Validation\Validator
	 */
	public function validator(array $data)
	{
		return Validator::make($data, [
			'email' => 'required|email|max:255|unique:users',
			'password' => 'required|confirmed|min:6',
		]);
	}

	/**
	 * Create a new user instance after a valid registration.
	 *
	 * @param  array  $data
	 * @return User
	 */
	public function create(array $data)
	{
        $verifiedToken = bin2hex(openssl_random_pseudo_bytes(16)) . uniqid();

		User::create([
			'email' => $data['email'],
			'password' => bcrypt($data['password']),
            'verified_token' => $verifiedToken,
		]);

        $this->sendVerificationEmail($data['email'], $verifiedToken);

        return true;
	}


    public function sendVerificationEmail($email, $token)
    {
        // $host = $request->getHost();
        $host = 'http://local.dev:8888/';

        $verificationLink = $host . 'register/verify/' . $token;

        \Mail::send(array('text' => 'emails.verifyemail'), ['verificationLink' => $verificationLink], function($message) use ($email)
        {
            $message->to($email)->subject('Storytellar e-mail verification link');
        });

    }

}
